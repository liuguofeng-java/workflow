<!-- 多选用户组件 -->
<template>
  <div class="select-container">
    <div class="tag-input">
      <el-tag v-for="(tag, index) in selectUserList" :key="tag.userId" closable @close="handleDel(index)">
        {{ tag.username }}
      </el-tag>
    </div>
    <el-button @click="handleOpen" type="primary">
      <el-icon> <Edit /> </el-icon>选择
    </el-button>
  </div>
  <el-dialog v-model="open" title="选择人" width="1200px" append-to-body :before-close="submit">
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

    <el-table v-loading="loading" :data="list" @select="handleSelectionChange" @select-all="handleSelectionChange" ref="tableRef">
      <el-table-column type="selection" width="55" />
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="账号名称" align="center" prop="account" />
      <el-table-column label="部门" align="center" prop="deptName" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
    </el-table>
    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="submit">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import baseService from "@/service/baseService";

// 表格实例
const tableRef = ref();

// 表格选择的数据
const tableSelectionList = ref<any[]>([]);

// 选择的用户信息
const selectUserList = ref<any[]>([]);

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
        addSelectUserList();
        list.value = res.rows;
        total.value = res.total;
        reloadTableSelect();
      } else {
        list.value = [];
      }
    })
    .catch(() => {
      loading.value = false;
    });
};

/**
 * 添加到selectUserList
 */
const addSelectUserList = () => {
  tableSelectionList.value.forEach((item) => {
    const selectIndex = selectUserList.value.findIndex((t) => t.userId === item.userId);
    if (selectIndex === -1) {
      selectUserList.value.push({
        userId: item.userId,
        username: item.username
      });
    }
  });
};

/**
 * 加载选择的table项
 */
const reloadTableSelect = () => {
  setTimeout(() => {
    selectUserList.value.forEach((item) => {
      const index = list.value.findIndex((t) => t.userId === item.userId);
      if (index !== -1) {
        tableRef.value.toggleRowSelection(list.value[index], undefined);
      }
    });
  }, 100);
};

/**
 * 选择table数据
 * @param selectData 选择的数据
 */
const handleSelectionChange = (selectData: any[]) => {
  tableSelectionList.value = selectData;
  // 删除取消的
  list.value.forEach((item) => {
    const tableIndex = tableSelectionList.value.findIndex((t) => t.userId === item.userId);
    if (tableIndex === -1) {
      const selectIndex = selectUserList.value.findIndex((t) => t.userId === item.userId);
      if (selectIndex !== -1) selectUserList.value.splice(selectIndex, 1);
    }
  });

  emit("ok", selectUserList.value);
};

/**
 * 搜索按钮操作
 */
function handleQuery() {
  queryForm.pageNo = 1;
  getList();
}

/**
 * 删除
 * @param index 下标
 */
function handleDel(index: number) {
  const row: any = selectUserList.value.splice(index, 1);
  let tableIndex = tableSelectionList.value.findIndex((t) => t.userId === row[0].userId);
  if (tableIndex !== -1) tableSelectionList.value.splice(tableIndex, 1);
  emit("ok", selectUserList.value);
}

/**
 * 提交数据
 */
function submit() {
  addSelectUserList();
  emit("ok", selectUserList.value);
  open.value = false;
}

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", list: any[]): void;
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
  min-height: 30px;
  width: 100%;
  border-radius: 5px;
  padding: 2px 10px;
  box-shadow: 0 0 0 1px var(--el-input-border-color, var(--el-border-color)) inset;
}
.tag-input > span {
  margin-right: 5px;
}
</style>
