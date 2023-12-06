<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="监听器名称">
        <el-input v-model="queryForm.listenerName" placeholder="监听器名称" clearable />
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
      <el-table-column label="序号" type="index" width="70" />
      <el-table-column label="监听器名称" align="center" prop="listenerName" />
      <el-table-column label="事件类型" align="center" prop="event">
        <template #default="scoped">
          <template v-for="(item, index) in events" :key="index">
            <span v-if="item.value === scoped.row.event">{{ item.label }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="java类" align="center" prop="javaClass" show-overflow-tooltip />
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column>
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row.listenerId)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.listenerId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next" v-model:page-size="queryForm.pageSize" v-model:current-page="queryForm.pageNo" :total="total" @current-change="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog v-model="open" :title="title" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="名称" prop="listenerName">
              <el-input v-model="form.listenerName" placeholder="请输入监听器名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件类型" prop="event">
              <el-select v-model="form.event" clearable placeholder="请输入事件类型">
                <el-option v-for="item in events" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="java类" prop="javaClass">
          <el-input v-model="form.javaClass" placeholder="请输入java类" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" maxlength="200" placeholder="备注" show-word-limit type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, nextTick, toRef } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";

// 查询参数
const queryForm = reactive({
  listenerName: "",
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
const formRef = ref();
// 是否打开弹出框
const open = ref(false);
// 弹出框标题
const title = ref("");
// 提交表单数据
let form = toRef(
  reactive({
    listenerName: "",
    event: "",
    javaClass: "",
    remark: ""
  })
);
// 表单验证
const rules = ref({
  listenerName: [{ required: true, message: "监听器名称不能为空", trigger: "blur" }],
  event: [{ required: true, message: "事件类型不能为空", trigger: "blur" }],
  javaClass: [{ required: true, message: "java类不能为空", trigger: "blur" }]
});

// 事件类型
const events = [
  { label: "连接线", value: "take" },
  { label: "开始", value: "start" },
  { label: "结束", value: "end" }
];

/**
 * 查询列表
 */
const getList = () => {
  loading.value = true;
  baseService
    .get("/listener/list", queryForm)
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
 * 表单重置
 */
function reset() {
  form.value = {
    listenerName: "",
    event: "",
    javaClass: "",
    remark: ""
  };

  nextTick(() => {
    formRef.value?.resetFields();
  });
}

/**
 * 新增按钮操作
 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加";
}

/**
 * 修改按钮操作
 * @param id 主键
 */
function handleUpdate(id: string) {
  open.value = true;
  title.value = "修改";
  reset();
  baseService.get(`/listener/info/${id}`).then((res) => {
    if (res.code === 200) {
      form.value = res.data;
    }
  });
}

/**
 * 提交按钮
 */
function submitForm() {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    baseService.post(`/listener/save`, form.value).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        open.value = false;
        getList();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
}

/**
 * 删除按钮操作
 * @param id 主键
 */
function handleDelete(id: any) {
  ElMessageBox.confirm("确认要删除当前项吗?", "提示").then(() => {
    baseService.delete(`/listener/delete`, id).then((res) => {
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
