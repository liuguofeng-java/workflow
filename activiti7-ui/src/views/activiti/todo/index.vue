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

    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="流程名称" align="center" prop="definitionName" />
      <el-table-column label="流程key" align="center" prop="definitionKey" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="当前节点" align="center" prop="taskName" />
      <el-table-column label="当前节点key" align="center" prop="taskDefinitionKey" />
      <el-table-column label="发起人" align="center" prop="startUserName" />
      <el-table-column>
        <template #default="scope">
          <el-button link type="primary" icon="Pointer" @click="handleApproval(scope.row)">审批</el-button>
          <el-button link type="primary" icon="Pointer" @click="handleHistoryRecord(scope.row.processInstanceId)">审批记录</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 审批 -->
    <Approval ref="approvalRef" @ok="getList" />

    <!-- 审批记录 -->
    <HistoryRecord ref="historyRecordRef" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";
import Approval from "./model/Approval.vue";
import HistoryRecord from "@/components/HistoryRecord/index.vue";

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
const approvalRef = ref();
// 审批记录弹出框
const historyRecordRef = ref();

/**
 * 查询列表
 */
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

/**
 * 搜索按钮操作
 */
function handleQuery() {
  queryForm.pageNo = 1;
  getList();
}

/**
 * 审批流程实例
 * @param row 当前行数据
 */
function handleApproval(row: any) {
  approvalRef.value.handleOpen(row.processInstanceId, row.taskId, row.taskDefinitionKey);
}

/**
 * 审批记录
 * @param instanceId 流程实例id
 */
function handleHistoryRecord(instanceId: string) {
  historyRecordRef.value.open(instanceId);
}

getList();
</script>

<style scoped></style>
