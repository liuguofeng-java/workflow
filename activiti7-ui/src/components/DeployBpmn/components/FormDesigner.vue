<template>
  <VFormDesigner ref="vfdRef" />
</template>

<script setup lang="ts">
import { nextTick, ref, onMounted } from "vue";
import VFormDesigner from "@/components/FormDesigner/index.vue";

// vform实例
const vfdRef = ref();
// 表单实例
const formRef = ref();

/**
 * 获取表单结构
 */
const getFormJson = () => {
  return vfdRef.value.getFormJson();
};

defineExpose({
  getFormJson
});

onMounted(() => {
  // 初始化表单
  nextTick(() => {
    vfdRef.value.clearDesigner();
    formRef.value?.resetFields();
  });
});
</script>

<style scoped lang="scss"></style>
