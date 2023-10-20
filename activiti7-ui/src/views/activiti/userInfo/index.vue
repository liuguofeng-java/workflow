<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="用户名称">
        <el-input v-model="queryForm.userName" placeholder="用户名称" clearable />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="queryForm.mobile" placeholder="手机号" clearable />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="queryForm.email" placeholder="邮箱" clearable />
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker v-model="daterange" type="daterange" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
          range-separator="到" start-placeholder="开始时间" end-placeholder="开始时间" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="账号名称" align="center" prop="account" />
      <el-table-column label="密码" align="center" prop="password" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize"
      v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";

// 查询参数
const queryForm = reactive({
  userName: '',
  mobile: '',
  email: '',
  startTime: '',
  endTime: '',
  pageNo: 1,
  pageSize: 10
})
// 列表内容数量
const total = ref(0);
const daterange = ref([])
// 列表是否加载
const loading = ref(true);
// 列表返回值
const list = ref<any[]>([]);


// 查询列表
const getList = () => {
  loading.value = true;
  console.log(daterange.value);

  if (daterange.value != null && daterange.value.length > 0) {
    queryForm.startTime = daterange.value[0]
    queryForm.endTime = daterange.value[1]
  } else {
    queryForm.startTime = ''
    queryForm.endTime = ''
  }
  baseService
    .get("/userInfo/list", queryForm)
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

getList()
</script>

<style scoped></style>
