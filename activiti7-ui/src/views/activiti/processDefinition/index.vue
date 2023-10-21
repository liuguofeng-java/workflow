<template>
  <div>
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <!-- <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button> -->
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="流程id" align="center" prop="id"/>
      <el-table-column label="流程名称" align="center" prop="name" />
      <el-table-column label="流程key" align="center" prop="key" />
      <el-table-column label="版本" align="center" prop="version" />
      <el-table-column>
        <template #default="scope">
          <!-- <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.userId)">修改</el-button> -->
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize"
      v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, nextTick, toRef } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import baseService from "@/service/baseService";

// 查询参数
const queryForm = reactive({
  definitionName: '',
  definitionKey: '',
  pageNo: 1,
  pageSize: 10
})
// 列表内容数量
const total = ref(0);
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);

// 查询列表
const getList = () => {
  loading.value = true;
  baseService
    .get("/processDefinition/list", queryForm)
    .then((res) => {
      loading.value = false;
      if (res.code === 200) {
        list.value = res.rows
        total.value = res.total;
      } else {
        list.value = []
      }
    })
    .catch(() => {
      loading.value = false;
    });
}

// 搜索按钮操作
function handleQuery() {
  queryForm.pageNo = 1
  getList();
}


// 删除按钮操作
function handleDelete(id: any) {
  ElMessageBox.confirm('确认要删除当前项吗?', '提示')
    .then(() => {
      baseService
        .delete(`/userInfo/delete`, id)
        .then((res) => {
          if (res.code === 200) {
            ElMessage.success(res.msg);
            getList()
          } else {
            ElMessage.error(res.msg);
          }
        })
    })
}

getList()
</script>

<style scoped></style>
