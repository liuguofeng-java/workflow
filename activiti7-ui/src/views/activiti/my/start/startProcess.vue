<template>
  <div>
    <el-dialog v-model="open" title="发起流程" width="1200px" append-to-body>
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="流程名称">
          <el-input v-model="queryForm.definitionName" placeholder="流程名称" clearable />
        </el-form-item>
        <el-form-item label="流程key">
          <el-input v-model="queryForm.definitionKey" placeholder="流程key" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="list">
        <el-table-column label="流程id" align="center" prop="id" width="300" />
        <el-table-column label="流程名称" align="center" prop="name" />
        <el-table-column label="流程key" align="center" prop="key" />
        <el-table-column label="版本" align="center" prop="version" />
        <el-table-column>
          <template #default="scope">
            <el-button link type="primary" @click="handleSelect(scope.row.id)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";

// 查询参数
const queryForm = reactive({
  definitionName: "",
  definitionKey: "",
  pageNo: 1,
  pageSize: 10
});
// 列表内容数量
const total = ref(0);
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);

// 是否打开弹出框
const open = ref(false);

// 初始化
const init = () => {
  getList();
  open.value = true;
};

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/processDefinition/list", queryForm)
    .then((res) => {
      loading.value = false;
      if (res.code === 200) {
        list.value = res.rows;
        total.value = res.total;
      } else {
        list.value = [];
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

/**
 * 搜索按钮操作
 */
function handleQuery() {
  queryForm.pageNo = 1;
  getList();
}

/**
 * 选择流程
 * @param definitionId 流程定义id
 */
function handleSelect(definitionId: any) {
  ElMessageBox.confirm("是否要发起流程?", "提示").then(() => {
    baseService.get(`/processStart/start?definitionId=${definitionId}`).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        open.value = false;
        emit("ok");
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
}

const emit = defineEmits<{
  (event: "ok"): void;
}>();

defineExpose({
  init
});
</script>

<style scoped></style>
