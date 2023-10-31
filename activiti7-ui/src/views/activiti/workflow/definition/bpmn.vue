<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false">
    <el-button @click="drawer = false">关闭</el-button>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 初始化表单
const open = (formId: string) => {
  drawer.value = true;
};

defineExpose({
  open
});
</script>

<style scoped lang="scss">
.canvas {
  width: 100%;
  height: 100%;
}
</style>
