<template>
  <div class="select-container">
    <div class="tag-input">
      <el-tag v-for="(tag, index) in selectDeptList" :key="tag.deptId" closable @close="handleDel(index)">
        {{ tag.deptName }}
      </el-tag>
    </div>
    <el-button @click="handleOpen" type="primary">
      <el-icon> <Edit /> </el-icon>选择
    </el-button>
  </div>
  <el-dialog v-model="open" title="选择部门" width="1200px" append-to-body :before-close="submit">
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="部门名称">
        <el-input v-model="queryForm.deptName" placeholder="部门名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" @select="handleSelectionChange" @select-all="handleSelectionChange" ref="tableRef">
      <el-table-column type="selection" width="55" />
      <el-table-column label="序号" type="index" width="100" />
      <el-table-column label="部门名称" align="center" prop="deptName" />
      <el-table-column label="负责人" align="center" prop="leader" />
      <el-table-column label="手机号" align="center" prop="phone" />
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
import { ref, watch, reactive } from "vue";
import baseService from "@/service/baseService";

const props = defineProps({
  list: {
    type: Array,
    default: () => []
  }
});

// 表格实例
const tableRef = ref();

// 表格选择的数据
const tableSelectionList = ref<any[]>([]);

// 选择的用户信息
const selectDeptList = ref<any[]>([]);

watch(
  () => props.list,
  async () => {
    selectDeptList.value = props.list;
  },
  { deep: true, immediate: true }
);

// 查询参数
const queryForm = reactive({
  deptName: "",
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
    .get("/sysDept/list", queryForm)
    .then((res) => {
      loading.value = false;
      if (res.code === 200) {
        addselectDeptList();
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
 * 添加到selectDeptList
 */
const addselectDeptList = () => {
  tableSelectionList.value.forEach((item) => {
    const selectIndex = selectDeptList.value.findIndex((t) => t.deptId === item.deptId);
    if (selectIndex === -1) {
      selectDeptList.value.push({
        deptId: item.deptId,
        deptName: item.deptName
      });
    }
  });
};

/**
 * 加载选择的table项
 */
const reloadTableSelect = () => {
  setTimeout(() => {
    selectDeptList.value.forEach((item) => {
      const index = list.value.findIndex((t) => t.deptId === item.deptId);
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
    const tableIndex = tableSelectionList.value.findIndex((t) => t.deptId === item.deptId);
    if (tableIndex === -1) {
      const selectIndex = selectDeptList.value.findIndex((t) => t.deptId === item.deptId);
      if (selectIndex !== -1) selectDeptList.value.splice(selectIndex, 1);
    }
  });

  emit("ok", selectDeptList.value);
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
  const row: any = selectDeptList.value.splice(index, 1);
  let tableIndex = tableSelectionList.value.findIndex((t) => t.deptId === row[0].deptId);
  if (tableIndex !== -1) tableSelectionList.value.splice(tableIndex, 1);
  emit("ok", selectDeptList.value);
}

/**
 * 提交数据
 */
function submit() {
  addselectDeptList();
  emit("ok", selectDeptList.value);
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
  align-items: center;
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
