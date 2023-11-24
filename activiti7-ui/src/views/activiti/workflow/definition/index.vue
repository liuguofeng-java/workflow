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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="流程id" align="center" prop="id" />
      <el-table-column label="流程名称" align="center" prop="name" />
      <el-table-column label="流程key" align="center" prop="key" />
      <el-table-column label="版本" align="center" prop="version" />
      <el-table-column>
        <template #default="scope">
          <el-button link type="primary" icon="Crop" @click="handleDesign(scope.row.deploymentId)">设计</el-button>
          <el-button link type="primary" icon="Pointer" @click="handleDetails(scope.row.deploymentId)">查看</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.deploymentId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 部署bpmn弹出框 -->
    <DeployBpmn ref="deployBpmn" @ok="getList" />

    <!-- 流程详情 -->
    <BpmnDetails ref="bpmnDetails" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import baseService from "@/service/baseService";
import DeployBpmn from "./model/DeployBpmn.vue";
import BpmnDetails from "./model/BpmnDetails.vue";

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
// 部署bpmn
const deployBpmn = ref();
//流程详情
const bpmnDetails = ref();

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
 * 详情
 * @param deploymentId 流程部署id
 */
function handleDetails(deploymentId: string) {
  bpmnDetails.value.open(deploymentId);
}

/**
 * 新增
 */
function handleAdd() {
  deployBpmn.value.open();
}

/**
 * 设计
 * @param deploymentId 流程部署id
 */
function handleDesign(deploymentId: string) {
  // 获取到上一个版本的流程图xml
  baseService.get("/processDefinition/getDefinitionXml", { deploymentId }).then((res) => {
    if (res.code === 200) {
      deployBpmn.value.open(res.data);
    }
  });
}

/**
 * 删除按钮操作
 * @param deploymentId 流程部署id
 */
function handleDelete(deploymentId: any) {
  ElMessageBox.confirm("确认要删除当前项吗? 流程实例启动的也将被删除,谨慎删除", "提示").then(() => {
    baseService.delete(`/processDefinition/delete`, deploymentId).then((res) => {
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
