<template>
  <!-- 主表单结构 -->
  <el-popover :width="600" trigger="hover" v-if="Object.keys(formJson).length !== 0" @show="vFormRenderShow">
    <template #reference>
      <el-link type="primary">主表单</el-link>
    </template>
    <!-- 节点动态表单 -->
    <VFormRender ref="preFormRef" />
  </el-popover>
</template>
<script setup lang="ts">
import { ref } from "vue";
import VFormRender from "@/components/FormDesigner/form-render/index.vue";
import { nextTick } from "vue";
const pops = defineProps({
  formJson: {
    type: Object,
    default: () => {
      return {};
    }
  }
});

// 动态表单实例
const preFormRef = ref();

/**
 * 打开vFormRender
 */
const vFormRenderShow = () => {
  nextTick(async () => {
    await preFormRef.value.setFormJson(pops.formJson);
    await preFormRef.value.disableForm();
  });
};
</script>

<style>
.el-popper {
  overflow: hidden;
}
</style>
