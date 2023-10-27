<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" class="custom-drawer">
    <el-form :inline="true" :model="form" ref="formRef" :rules="rules" class="inline">
      <el-form-item label="表单名称" prop="formName">
        <el-input v-model="form.formName" placeholder="表单名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="drawer = false">关闭</el-button>
      </el-form-item>
    </el-form>

    <v-form-designer ref="vfdRef"></v-form-designer>
  </el-drawer>
</template>

<script setup lang="ts">
import { nextTick, ref, toRef, reactive } from "vue";
import VFormDesigner from "@/components/form-designer/index.vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

// vform实例
const vfdRef = ref();
// 表单实例
const formRef = ref();
// 是否加载抽屉
let drawer = ref<boolean>(false);
// 提交表单实例
let form = toRef(
  reactive({
    formId: "",
    formName: "",
    formData: ""
  })
);

// 表单验证
const rules = ref({
  formName: [{ required: true, message: "表单名称不能为空！", trigger: "blur" }]
});

// 初始化表单
const open = (formId: string) => {
  drawer.value = true;
  nextTick(() => {
    vfdRef.value.clearDesigner();
  });

  // 重置表单
  form.value = {
    formId: "",
    formName: "",
    formData: ""
  };
  nextTick(() => {
    formRef.value?.resetFields();
  });

  if (formId) {
    baseService.get(`/sysForm/info/${formId}`).then((res) => {
      if (res.code === 200) {
        form.value = res.data;
        vfdRef.value.setFormJson(JSON.parse(form.value.formData));
      }
    });
  }
};

// 提交
const submit = () => {
  formRef.value.validate((valid: boolean) => {
    if (!valid) return;
    const json = vfdRef.value.getFormJson();
    form.value.formData = JSON.stringify(json);
    baseService.post(`/sysForm/save`, form.value).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        drawer.value = false;
        okClick();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};
defineExpose({
  open
});

const emit = defineEmits<{
  (event: "ok"): void;
}>();
const okClick = () => {
  emit("ok");
};
</script>

<style>
.inline {
  display: flex;
  justify-content: flex-end;
}

::v-deep(.el-drawer__body) {
  overflow: hidden !important;
}
</style>
