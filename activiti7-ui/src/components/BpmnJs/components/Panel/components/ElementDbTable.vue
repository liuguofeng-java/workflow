<template>
  <el-card shadow="never" class="container">
    <el-divider content-position="left">数据库配置</el-divider>

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
          <el-input v-model="form.comment" placeholder="无" readonly="readonly" />
        </el-form-item>
      </template>

      <template v-if="form.type === 'create'">
        <el-form-item label="表名称" prop="tableName">
          <el-input v-model="form.tableName" placeholder="表名称" clearable />
        </el-form-item>
        <el-form-item label="表备注">
          <el-input v-model="form.comment" placeholder="表备注" clearable />
        </el-form-item>
      </template>

      <el-form-item>
        <el-button type="primary" @click="submitForm">更新</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import modelerStore from "@/components/BpmnJs/store/modeler";
import EventBus from "@/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import { type Element } from "bpmn-js/lib/util/ModelUtil";
import baseService from "@/service/baseService";
import { ElMessageBox, ElMessage } from "element-plus";

const modeler = modelerStore();

// 当前节点信息
let scopedElement: Element;

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
  comment: "",
  columns: []
});

// 表单验证
const rules = ref({
  tableName: [{ required: true, message: "表名称必填项", trigger: "blur" }]
});

watch(
  () => form.value.tableName,
  () => {
    // 更新表备注
    if (form.value.type !== "ready") return;
    const item = list.value.find((t) => t.tableName === form.value.tableName);
    if (item) form.value.comment = item.tableComment;
  },
  { deep: true, immediate: true }
);

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
  form.value.comment = "";
};

/**
 * 提交
 */
const submitForm = () => {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    ElMessageBox.confirm("确认要做更新操作吗?更新将删除所有已配置的字段", "提示").then(() => {
      form.value.columns = [];
      modeler.setTableInfo(form.value);
      ElMessage.success("更新成功!");
    });
  });
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    if (modeler.getTableInfo) {
      form.value = JSON.parse(JSON.stringify(modeler.getTableInfo));
    }
  });
});
getList();
</script>

<style scoped lang="scss">
.container {
  :deep(.el-select) {
    width: 100%;
  }
}
</style>
