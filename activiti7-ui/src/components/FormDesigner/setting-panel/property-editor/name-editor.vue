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
    <el-input type="text" v-model="optionModel.name" @change="updateWidgetNameAndRef"></el-input>
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
import modelerStore from "@/components/BpmnJs/store/modeler";
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
     * 如果label名称改变也要更新数据
     */
    "selectedWidget.options.label": {
      immediate: true,
      handler() {
        if (this.checkField) {
          const modeler = modelerStore();
          modeler.setNodeColumn({
            columnName: this.optionModel.name,
            columnComment: this.optionModel.label
          });
        }
      }
    },
    /**
     * 如果取消勾选要重新分配组件唯一名称
     */
    checkField: {
      immediate: true,
      handler(value) {
        if (!value) {
          console.log("selectedWidget>>>", this.selectedWidget);
          const newName = this.selectedWidget.type + generateId();
          this.optionModel.name = newName;
          this.updateWidgetNameAndRef(newName);
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
      let oldName = this.designer.selectedWidgetName;
      if (isEmptyStr(newName)) {
        this.selectedWidget.options.name = oldName;
        this.$message.info(this.i18nt("designer.hint.nameRequired"));
        return;
      }

      const regex = /^[a-zA-Z][a-zA-Z0-9_]*$/;
      var pattern = new RegExp(regex);
      if (!pattern.test(newName)) {
        this.selectedWidget.options.name = oldName;
        this.$message.info("表字段不符合规则");
        return;
      }

      if (this.designer.formWidget) {
        let foundRef = this.designer.formWidget.getWidgetRef(newName); // 检查newName是否已存在！！
        if (foundRef) {
          this.selectedWidget.options.name = oldName;
          this.$message.info(this.i18nt("designer.hint.duplicateName") + newName);
          return;
        }

        let widgetInDesign = this.designer.formWidget.getWidgetRef(oldName);
        if (!!widgetInDesign && !!widgetInDesign.registerToRefList) {
          widgetInDesign.registerToRefList(oldName); //注册组件新的ref名称并删除老的ref！！
          let newLabel = this.getLabelByFieldName(newName);
          this.designer.updateSelectedWidgetNameAndLabel(this.selectedWidget, newName, newLabel);
        }
        if (this.checkField) {
          const modeler = modelerStore();
          modeler.setNodeColumn({
            columnName: this.optionModel.name,
            columnComment: this.optionModel.label
          });
        }
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
     * 获取表信息
     */
    geTableInfo() {
      this.checkField = false;
      const modeler = modelerStore();
      this.tableInfo = modeler.getTableInfo || {};
      console.log("getWidgetType", modeler.getWidgetType);
      this.widgetType = modeler.getWidgetType.widgetType[this.selectedWidget.type];
      if (modeler.getTableInfo?.type === "ready") {
        // 没有找到对应组件的控件
        if (!this.widgetType) return;
        // 查询的数据库相应的字段
        const fieldList = [];
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
