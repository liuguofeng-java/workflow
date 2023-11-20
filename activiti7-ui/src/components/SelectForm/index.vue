<template>
  <el-dialog v-model="open" title="选择表单" width="1200px" append-to-body>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="表单名称">
        <el-input v-model="queryForm.formName" placeholder="表单名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" ref="tableRef">
      <el-table-column label="表单名称" align="center" prop="formName" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button link type="primary" @click="handleCurrentChange(scope.row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="open = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";

// 表格实例
const tableRef = ref();

// 选择的行
const currentRow = ref<any>();

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
// 是否打开弹出框
const open = ref(false);

/**
 * 打开选择器
 */
const handleOpen = () => {
  getList();
  open.value = true;
};

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
 * 选择行事件
 * @param val 选择的行数据
 */
function handleCurrentChange(val: any) {
  currentRow.value = val;
  emit("ok", { formId: val.formId, formName: val.formName });
  open.value = false;
}

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", currentRow: any): void;
}>();
</script>

<style scoped></style>
