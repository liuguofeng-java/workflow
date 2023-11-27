<!-- 多选用户组件 -->
<template>
  <div class="select-container">
    <div class="tag-input">
      <el-tag v-if="currentRow?.username">{{ currentRow?.username }}</el-tag>
    </div>
    <el-button @click="handleOpen" type="primary">
      <el-icon> <Edit /> </el-icon>选择
    </el-button>
  </div>
  <el-dialog v-model="open" title="选择人" width="1200px" append-to-body>
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
        <el-date-picker v-model="daterange" type="daterange" format="YYYY-MM-DD" value-format="YYYY-MM-DD" range-separator="到" start-placeholder="开始时间" end-placeholder="开始时间" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" ref="tableRef">
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="账号名称" align="center" prop="account" />
      <el-table-column label="部门" align="center" prop="deptName" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column label="邮箱" align="center" prop="email" />
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
import { ref, watch, reactive } from "vue";
import baseService from "@/service/baseService";

const props = defineProps({
  user: {
    type: Object,
    default: () => {
      return {};
    }
  }
});

// 表格实例
const tableRef = ref();

// 选择的行
const currentRow = ref<any>();

watch(
  () => props.user,
  async () => {
    currentRow.value = props.user;
  },
  { deep: true, immediate: true }
);

// 查询参数
const queryForm = reactive({
  userName: "",
  mobile: "",
  email: "",
  startTime: "",
  endTime: "",
  pageNo: 1,
  pageSize: 10
});
// 列表内容数量
const total = ref(0);
const daterange = ref([]);
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
  // 处理时间
  if (daterange.value != null && daterange.value.length > 0) {
    queryForm.startTime = daterange.value[0];
    queryForm.endTime = daterange.value[1];
  } else {
    queryForm.startTime = "";
    queryForm.endTime = "";
  }
  baseService
    .get("/sysUser/list", queryForm)
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
  emit("ok", { userId: val.userId, username: val.username });
  open.value = false;
}

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", currentRow: any): void;
}>();
</script>

<style scoped>
.select-container {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
}

.tag-input {
  margin-right: 10px;
  min-height: 32px;
  width: 100%;
  border-radius: 5px;
  padding: 2px 10px;
  box-shadow: 0 0 0 1px var(--el-input-border-color, var(--el-border-color)) inset;
}

.tag-input > span {
  margin-right: 5px;
}
</style>
