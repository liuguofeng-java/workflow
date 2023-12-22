<template>
  <el-card shadow="never" class="container">
    <el-divider content-position="left">数据库配置</el-divider>

    <div class="but">
      <el-button type="primary" link @click="open = true">更新</el-button>
    </div>

    <!-- 绑定状态 -->
    <el-descriptions :column="1" border v-if="detail.type !== ''">
      <el-descriptions-item label="配置状态">
        <span v-if="detail.type === 'ready'">已有数据库表</span>
        <span v-if="detail.type === 'create'">创建数据库表</span>
      </el-descriptions-item>
      <el-descriptions-item label="表名">
        <span>{{ detail.tableName }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="表备注">
        <span>{{ detail.tableComment }}</span>
      </el-descriptions-item>
    </el-descriptions>
    <el-empty v-else />

    <!-- 展示已绑定字段 -->
    <div v-if="columnList.length !== 0">
      <el-divider content-position="left">已绑定字段</el-divider>
      <div v-for="(item, index) in columnList" :key="index" class="binding-container">
        <div class="node-name">
          <span>节点名称: </span>
          <span>{{ item.activityName || "无" }}</span>
        </div>
        <div>
          <el-table :data="item.columns" style="width: 100%">
            <el-table-column prop="columnName" label="表字段" />
            <el-table-column prop="widgetIcon" label="控件类型">
              <template #default="scope">
                <svg-icon v-if="scope.row.widgetIcon" :icon-class="scope.row.widgetIcon" class-name="color-svg-icon" />
                {{ i18n.methods.i18n2t(`designer.widgetLabel.${scope.row.widgetType}`, `extension.widgetLabel.${scope.row.widgetType}`) }}
              </template>
            </el-table-column>
            <el-table-column prop="columnComment" label="控件名称" />
          </el-table>
        </div>
      </div>
    </div>

    <!-- 更新数据库配置 -->
    <el-dialog v-model="open" title="数据库配置" width="500px">
      <el-form label-width="80px" :model="form" ref="formRef" :rules="rules">
        <el-form-item label="类型">
          <el-radio-group v-model="form.type" @change="resetFields()">
            <el-radio-button label="">无</el-radio-button>
            <el-radio-button label="ready">已有数据库表</el-radio-button>
            <el-radio-button label="create">创建数据库表</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <template v-if="form.type === 'ready'">
          <el-form-item label="表名称" prop="tableName">
            <el-select v-model="form.tableName" filterable remote reserve-keyword placeholder="绑定数据库表" :loading="loading">
              <el-option v-for="item in list" :key="item.value" :value="item.tableName">
                <span>{{ item.tableName }}</span>
                <span v-if="item.tableComment"> - {{ item.tableComment }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="表备注">
            <el-input v-model="form.tableComment" placeholder="无" readonly="readonly" />
          </el-form-item>
        </template>

        <template v-if="form.type === 'create'">
          <el-form-item label="表名称" prop="tableName">
            <el-input v-model="form.tableName" placeholder="表名称" clearable />
          </el-form-item>
          <el-form-item label="表备注">
            <el-input v-model="form.tableComment" placeholder="表备注" clearable />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancel">关闭</el-button>
          <el-button type="primary" @click="submitForm">更新</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import modelerStore from "@/store/modeler";
import EventBus from "@/utils/EventBus";
import baseService from "@/service/baseService";
import { ElMessageBox, ElMessage } from "element-plus";
import i18n from "@/components/FormDesigner/utils/i18n";
import SvgIcon from "@/components/FormDesigner/svg-icon/index.vue";

const modeler = modelerStore();

let _elements: any[] = [];

// 是否打开更新
const open = ref<boolean>(false);

// 绑定的字段列表
let columnList = ref<NodeColumnInfo[]>([]);

// 表单实例
const formRef = ref();

// 搜索加载
const loading = ref<boolean>(false);

const list = ref<any[]>([]);

// 绑定数据
// eslint-disable-next-line no-undef
const form = ref<TableInfo>({
  tableName: "",
  type: "",
  tableComment: "",
  columns: []
});

/**
 * 详情
 */
// eslint-disable-next-line no-undef
const detail = ref<TableInfo>({
  tableName: "",
  type: "",
  tableComment: "",
  columns: []
});

// 表单验证
const rules = ref({
  tableName: [
    { required: true, message: "表名称必填项", trigger: "blur" },
    {
      type: "string",
      required: true,
      message: "表不符合规则！",
      validator: (rule, value) => {
        if (!value) return true;
        const regex = /^[a-zA-Z][a-zA-Z0-9_]*$/;
        var pattern = new RegExp(regex);
        return pattern.test(value);
      }
    },
    {
      type: "string",
      required: true,
      message: "不能包含'sys_'、'act_'、'sql_'",
      validator: (rule, value) => {
        return value.indexOf("sys_") === -1 && value.indexOf("sql_") === -1 && value.indexOf("act_") === -1;
      }
    },
    {
      type: "string",
      required: true,
      message: "不能创建数据库已有的表",
      validator: (rule, value) => {
        if (form.value.type === "ready") return true;
        return list.value.findIndex((t) => t.tableName === value) === -1;
      }
    }
  ]
});

watch(
  () => form.value.tableName,
  () => {
    // 更新表备注
    if (form.value.type !== "ready") return;
    const item = list.value.find((t) => t.tableName === form.value.tableName);
    if (item) form.value.tableComment = item.tableComment;
  },
  { deep: true, immediate: true }
);

type NodeColumnInfoItem = {
  columnName: string; // 表字段名称
  columnComment: string; // 备注
  widgetType: string; // 控件类型
  widgetIcon: string; // 控件类型
};

type NodeColumnInfo = {
  activityName: string;
  columns: NodeColumnInfoItem[];
};

/**
 * 用于获取绑定的节点信息
 */
const getNodeColumns = () => {
  const nodeColumnInfos: NodeColumnInfo[] = [];
  modeler.getNodeColumns.forEach((nodeItem) => {
    const columns: NodeColumnInfoItem[] = [];
    const formJson = modeler.getFormJsonList.find((t) => t.activityId === nodeItem.activityId);
    nodeItem.columns.forEach((columnItem) => {
      const widgetItem = formJson?.formJson.widgetList.find((t) => t.options.name === columnItem.columnName);
      columns.push({
        columnName: columnItem.columnName,
        columnComment: columnItem.columnComment,
        widgetType: widgetItem?.type || "",
        widgetIcon: widgetItem?.icon || ""
      });
    });
    nodeColumnInfos.push({
      activityName: _elements[nodeItem.activityId]?.element.businessObject?.name,
      columns: columns
    });
  });
  columnList.value = nodeColumnInfos;
};

/**
 * 获取数据
 */
const getList = () => {
  loading.value = true;
  baseService
    .get(`/table/list`)
    .then((res) => {
      loading.value = false;
      if (res.code === 200) {
        list.value = res.data;
      } else {
        list.value = [];
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

/**
 * 重置字段
 */
const resetFields = () => {
  form.value.tableName = "";
  form.value.tableComment = "";
};

/**
 * 提交
 */
const submitForm = () => {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    ElMessageBox.confirm("确认要做更新操作吗?更新将删除所有已配置的字段", "提示").then(async () => {
      form.value.columns = [];
      if (form.value.type === "ready") {
        let res = await baseService.get(`/table/tableColumns?tableName=${form.value.tableName}`);
        form.value.columns = res.data;
      }
      // 只有选项是创建表的时候,更新数据才不会置空节点绑定的字段
      if (!(modeler.getTableInfo?.type === "create" && form.value.type === "create")) {
        modeler.setNodeColumns([]);
      }
      modeler.setTableInfo(form.value);
      ElMessage.success("更新成功!");
      open.value = false;
      getElementData();
      getNodeColumns();
    });
  });
};

/**
 * 取消
 */
const cancel = () => {
  open.value = false;
  getElementData();
};

/**
 * 取消更新
 */
const getElementData = () => {
  if (modeler.getTableInfo) {
    form.value = JSON.parse(JSON.stringify(modeler.getTableInfo));
    detail.value = JSON.parse(JSON.stringify(modeler.getTableInfo));
  }
};

// 点击用户节点，初始化用
EventBus.on("element-init", function (_modeler) {
  _elements = _modeler.get("elementRegistry")._elements;
  getNodeColumns();
  getElementData();
});
getList();
</script>

<style scoped lang="scss">
.container {
  :deep(.el-select) {
    width: 100%;
  }
}
.but {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.binding-container {
  padding: 10px;
  border-radius: var(--el-card-border-radius);
  border: 1px solid var(--el-card-border-color);
  background-color: var(--el-card-bg-color);
  overflow: hidden;
  color: var(--el-text-color-primary);
  margin-bottom: 5px;
}
</style>
@/store/modeler
