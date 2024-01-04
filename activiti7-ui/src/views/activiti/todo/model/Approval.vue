<template>
  <div>
    <el-dialog v-model="open" title="审批" width="1200px">
      <!-- 节点动态表单 -->
      <VFormRender ref="preForm" :form-json="formJson" :preview-state="true" />

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="submit">审批</el-button>
          <el-button @click="open = false">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, toRef, nextTick } from "vue";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import VFormRender from "@/components/FormDesigner/form-render/index.vue";

// 是否打开弹出框
const open = ref(false);
// 提交表单数据
let form = toRef(
  reactive({
    processInstanceId: "",
    variables: {}
  })
);

// 动态表单实例
const preForm = ref();
// 动态表单结构数据
const formJson = ref<object>({});

// 当前节点id
let activityId = "";

/**
 * 初始化
 * @param instanceId 流程实例id
 * @param taskId 任务id
 * @param taskDefinitionKey 节点id
 */
const handleOpen = (instanceId: string, taskId: string, taskDefinitionKey: string) => {
  form.value.processInstanceId = instanceId;
  activityId = taskDefinitionKey;
  // 获取动态表单
  baseService.get(`/processTodo/getNodeForm/${taskId}`).then((res) => {
    if (res.code === 200 && res.data !== "") {
      formJson.value = res.data;
      open.value = true;
      nextTick(() => {
        if (Object.keys(res.data).length !== 0) {
          preForm.value?.resetForm();
          preForm.value?.setFormJson(res.data);
        }
      });
    }
  });
};

/**
 * 提交
 */
async function submit() {
  // 获取动态表单数据
  const formData = await preForm.value.getFormData();

  ElMessageBox.confirm("是否要提交?", "提示").then(() => {
    // 在流程节点局部变量设置值, 可以方便使用 `${}` 直接设置流程变量
    var variables = JSON.parse(JSON.stringify(formData));

    // 在流程节点局部变量设置表单的结构和值方便以后回显使用
    variables[`${activityId}_formJson`] = formJson;
    variables[`${activityId}_formData`] = JSON.parse(JSON.stringify(formData));
    form.value.variables = variables;

    baseService.post(`/processTodo/complete`, form.value).then((res) => {
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
  handleOpen
});
</script>

<style scoped></style>
