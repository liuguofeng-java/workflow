<template>
  <div>
    <el-dialog v-model="open" title="审批" width="1200px" append-to-body>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
        <el-form-item label="处理意见">
          <el-input v-model="form.comment" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">审批</el-button>
          <el-button @click="open = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, toRef } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";

// 是否打开弹出框
const open = ref(false);
// 表单实例
const formRef = ref();
// 提交表单数据
let form = toRef(
  reactive({
    processInstanceId: "",
    comment: ""
  })
);
// 表单验证
const rules = ref({});

/**
 * 初始化
 * @param instanceId 流程实例id
 */
const init = (instanceId: string) => {
  form.value.processInstanceId = instanceId;
  open.value = true;
};

/**
 * 提交
 */
function submit() {
  ElMessageBox.confirm("是否要提交?", "提示").then(() => {
    baseService.post(`/processTodo/approval`, form.value).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        open.value = false;
        emit("ok");
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
}

const emit = defineEmits<{
  (event: "ok"): void;
}>();

defineExpose({
  init
});
</script>

<style scoped></style>
