<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="表单名称">
        <el-input v-model="queryForm.formName" placeholder="表单名称" clearable />
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
      <el-table-column label="表单名称" align="center" prop="formName" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column>
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.formId)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.formId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 表单弹出框 -->
    <Form ref="formRef" @ok="getList" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import Form from "./form.vue";

// 查询参数
const queryForm = reactive({
  formName: "",
  pageNo: 1,
  pageSize: 10
});
// 列表内容数量
const total = ref(0);
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);

// 表单实例
const formRef = ref<any>(null);

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/sysForm/list", queryForm)
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
 * 新增按钮操作
 */
function handleAdd() {
  formRef.value.open();
}

/**
 * 修改按钮操作
 * @param id
 */
function handleUpdate(id: string) {
  formRef.value.open(id);
}

/**
 * 删除按钮操作
 * @param id 表单id
 */
function handleDelete(id: any) {
  ElMessageBox.confirm("确认要删除当前项吗?", "提示").then(() => {
    baseService.delete(`/sysForm/delete`, id).then((res) => {
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
