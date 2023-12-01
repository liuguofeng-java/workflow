<template>
  <!-- 用户节点填写的表单 -->
  <el-popover :width="600" trigger="hover" v-if="formJson && formJson.widgetList && formJson.widgetList.length !== 0" @show="vFormRenderShow">
    <template #reference>
      <el-link type="primary">表单详情</el-link>
    </template>
    <!-- 节点动态表单 -->
    <VFormRender ref="preFormRef" :form-json="formJson" :form-data="formData" :preview-state="true"> </VFormRender>
  </el-popover>
</template>
<script setup lang="ts">
import { ref } from "vue";
import VFormRender from "@/components/FormDesigner/form-render/index.vue";
import { nextTick } from "vue";
defineProps({
  formJson: {
    type: Object,
    default: () => {
      return {};
    }
  },
  formData: {
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
  nextTick(() => {
    preFormRef.value.disableForm();
  });
};
</script>

<style scoped></style>
