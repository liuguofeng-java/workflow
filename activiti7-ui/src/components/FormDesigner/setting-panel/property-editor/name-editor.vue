<!-- eslint-disable vue/no-mutating-props -->
<template>
  <el-form-item label="数据库" v-if="isShowCheckField">
    <el-checkbox v-model="checkField" label="绑定表字段" />
  </el-form-item>

  <el-alert
    :description="`数据库表类型必须是${widgetType}才能选择,
    而数据库表中没有符合类型,所以不能绑定表字段`"
    type="warning"
    :closable="false"
    v-if="tableInfo.type === 'ready' && fieldList.length === 0 && widgetType"
  />

  <!-- 绑定新创建表字段 -->
  <el-form-item prop="name" :rules="nameRequiredRule" label="表字段" v-if="isShowCreate">
    <el-input type="text" v-model="optionModel.name" @change="updateWidgetNameAndRef" maxlength="15"></el-input>
  </el-form-item>

  <!-- 是否显示绑定已有的表字段 -->
  <el-form-item prop="name" :rules="nameRequiredRule" label="表字段" v-else-if="isShowReady">
    <el-select v-model="optionModel.name" filterable @change="updateWidgetNameAndRef" :title="i18nt('designer.setting.editNameHelp')">
      <el-option v-for="(item, index) in fieldList" :key="index" :value="item.columnName">
        <span v-if="!item.columnComment">{{ item.columnName }}</span>
        <span v-if="item.columnComment">{{ item.columnComment }}</span>
      </el-option>
    </el-select>
  </el-form-item>

  <el-form-item prop="name" :rules="nameRequiredRule" label="唯一名称" v-else>
    <el-input type="text" v-model="optionModel.name" disabled @change="updateWidgetNameAndRef"></el-input>
  </el-form-item>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import { isEmptyStr } from "@/components/FormDesigner/utils/util";
import modelerStore from "@/store/modeler";
import { generateId } from "@/components/FormDesigner/utils/util";

export default {
  name: "name-editor",
  mixins: [i18n],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  inject: ["serverFieldList", "getDesignerConfig"],
  data() {
    return {
      nameRequiredRule: [
        { required: true, message: "必填项" },
        { min: 3, max: 15, message: "长度应为 3 到 15" },
        {
          type: "string",
          required: true,
          message: "表不符合规则！",
          validator: (rule, value) => {
            const regex = /^[a-zA-Z][a-zA-Z0-9_]*$/;
            var pattern = new RegExp(regex);
            return pattern.test(value);
          }
        }
      ],
      checkField: false, // 是否绑定数据库字段
      tableInfo: {}, //表结构
      fieldList: [], // 数据库字段
      widgetType: [] // 控件类型
    };
  },
  watch: {
    /**
     * 组件id改变时要初始化数据
     */
    "selectedWidget.id": {
      immediate: true,
      handler() {
        this.geTableInfo();
      }
    },
    /**
     * 如果取消勾选要重新分配组件唯一名称
     */
    checkField: {
      immediate: true,
      handler(value) {
        const modeler = modelerStore();
        if (!value) {
          const newName = this.selectedWidget.type + generateId();
          // eslint-disable-next-line vue/no-mutating-props
          this.optionModel.name = newName;
          this.updateWidgetNameAndRef(newName);
        } else if (modeler.getTableInfo?.type === "create") {
          this.setTableColumn();
        }
      }
    },
    /**
     * 如果label名称改变也要更新数据
     */
    "selectedWidget.options.label": {
      immediate: true,
      handler() {
        this.setTableColumn();
      }
    },
    /**
     * 如果name名称改变,并且是create和选中状态
     */
    "selectedWidget.options.name": {
      immediate: true,
      handler() {
        const modeler = modelerStore();
        if (modeler.getTableInfo?.type === "create" && this.checkField) {
          this.setTableColumn();
        }
      }
    }
  },
  computed: {
    /**
     * 是否绑定新创建表字段
     */
    isShowCreate() {
      return this.tableInfo.type === "create" && this.checkField;
    },
    /**
     * 是否显示绑定已有的表字段
     */
    isShowReady() {
      return this.tableInfo.type === "ready" && this.fieldList.length !== 0 && this.checkField;
    },
    /**
     * 是否显示绑定表字段
     */
    isShowCheckField() {
      if (this.tableInfo.type === "create") {
        return true;
      } else if (this.tableInfo.type === "ready" && this.fieldList.length !== 0 && this.widgetType) {
        return true;
      }
      return false;
    },
    widgetNameReadonly() {
      return !!this.getDesignerConfig().widgetNameReadonly;
    }
  },
  methods: {
    updateWidgetNameAndRef(newName) {
      // 把-全部替换_
      newName = newName.replace(/-/g, "_");
      // 空验证
      let oldName = this.designer.selectedWidgetName;
      if (isEmptyStr(newName)) {
        // eslint-disable-next-line vue/no-mutating-props
        this.selectedWidget.options.name = oldName;
        this.$message.info(this.i18nt("designer.hint.nameRequired"));
        return;
      }

      // 正则验证
      const regex = /^[a-zA-Z][a-zA-Z0-9_]*$/;
      var pattern = new RegExp(regex);
      if (!pattern.test(newName)) {
        // eslint-disable-next-line vue/no-mutating-props
        this.selectedWidget.options.name = oldName;
        this.$message.info("表字段不符合规则");
        return;
      }

      // 验证唯一名称是否重复
      if (this.designer.formWidget) {
        let foundRef = this.designer.formWidget.getWidgetRef(newName); // 检查newName是否已存在！！
        if (foundRef) {
          // eslint-disable-next-line vue/no-mutating-props
          this.selectedWidget.options.name = oldName;
          this.$message.info(`当前表字段${newName}已绑定表单`);
          return;
        }

        let widgetInDesign = this.designer.formWidget.getWidgetRef(oldName);
        if (!!widgetInDesign && !!widgetInDesign.registerToRefList) {
          widgetInDesign.registerToRefList(oldName); //注册组件新的ref名称并删除老的ref！！
          let newLabel = this.getLabelByFieldName(newName);
          this.designer.updateSelectedWidgetNameAndLabel(this.selectedWidget, newName, newLabel);
        }
        this.setTableColumn();
      }
    },

    getLabelByFieldName(fieldName) {
      for (let i = 0; i < this.serverFieldList.length; i++) {
        if (this.serverFieldList[i].name === fieldName) {
          return this.serverFieldList[i].label;
        }
      }
      return null;
    },
    /**
     * 设置节点字段
     */
    setTableColumn() {
      const modeler = modelerStore();
      // 如果现有字段没有被选择
      const fieldIndex = this.fieldList.findIndex((t) => t.columnName === this.optionModel.name);
      if (modeler.getTableInfo?.type === "ready" && fieldIndex === -1) return;

      // 如果选择数据库字段就更新节点数据
      if (this.checkField) {
        modeler.setNodeColumn({
          columnName: this.optionModel.name,
          columnComment: this.optionModel.label
        });
        // 如果是创建表要在结构添加
        if (modeler.getTableInfo?.type === "create") {
          const widgetDefaultDataType = modeler.getWidgetDataType.widgetDefaultDataType;
          let dataTypeValue = widgetDefaultDataType[this.selectedWidget.type];
          let dataType = Reflect.ownKeys(dataTypeValue);
          modeler.setTableColumn({
            columnName: this.optionModel.name,
            columnComment: this.optionModel.label,
            dataType: dataType[0],
            columnLength: dataTypeValue[dataType[0]],
            columnKey: ""
          });
        }
      }
      // eslint-disable-next-line vue/no-mutating-props
      this.optionModel.checkField = this.checkField;
    },
    /**
     * 获取表信息
     */
    geTableInfo() {
      this.checkField = false;
      const modeler = modelerStore();
      const fieldList = [];
      this.tableInfo = modeler.getTableInfo || {};
      console.log("getWidgetDataType", modeler.getWidgetDataType);
      this.widgetType = modeler.getWidgetDataType.widgetDataType[this.selectedWidget.type];
      if (modeler.getTableInfo?.type === "ready") {
        // 没有找到对应组件的控件
        if (!this.widgetType) return;
        // 查询的数据库相应的字段
        for (let i = 0; i < this.widgetType.length; i++) {
          const dataType = this.widgetType[i];
          const columns = modeler.getTableInfo.columns.filter((t) => t.dataType === dataType);
          columns.forEach((item) => fieldList.push(item));
        }
        this.fieldList = fieldList;
      }
      // 回显
      const column = modeler.getNodeColumn?.find((t) => t.columnName == this.optionModel.name);
      if (column) {
        this.checkField = true;
      }
    }
  }
};
</script>

<style lang="scss" scoped></style>
@/store/modeler
