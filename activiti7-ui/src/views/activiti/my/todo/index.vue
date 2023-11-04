<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="流程名称">
        <el-input v-model="queryForm.definitionName" placeholder="流程名称(不是模糊查询)" clearable />
      </el-form-item>
      <el-form-item label="流程key">
        <el-input v-model="queryForm.definitionKey" placeholder="流程key(不是模糊查询)" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="流程名称" align="center" prop="definitionName" />
      <el-table-column label="key" align="center" prop="definitionKey" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="当前节点" align="center" prop="taskName" />
      <el-table-column label="当前节点key" align="center" prop="taskDefinitionKey" />
      <el-table-column label="发起人" align="center" prop="startUserName" />
      <el-table-column>
        <template #default="scope">
          <!-- <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />
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

// 发起流程弹出框
const startProcessRef = ref();

// 查询列表
const getList = () => {
  loading.value = true;
  baseService
    .get("/processTodo/list", queryForm)
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

// 搜索按钮操作
function handleQuery() {
  queryForm.pageNo = 1;
  getList();
}

// 发起流程
function handleAdd() {
  startProcessRef.value.init();
}

// 删除按钮操作
function handleDelete(id: any) {
  ElMessageBox.confirm("确认要删除当前项吗?", "提示").then(() => {
    baseService.delete(`/processTodo/delete`, id).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        getList();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
}

getList();
</script>

<style scoped></style>
