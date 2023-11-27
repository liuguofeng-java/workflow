<template>
  <!-- 用户填写的主表单 -->
  <VFormRender ref="preFormRef" :form-json="formJson" :form-data="formData" :preview-state="true" v-if="Object.keys(formJson).length !== 0" />
  <div v-else class="msg">用户没有填写主表单</div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import VFormRender from "@/components/FormDesigner/form-render/index.vue";
import { nextTick } from "vue";
import { watch } from "vue";
const pops = defineProps({
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

watch(
  () => pops.formJson,
  async () => {
    nextTick(() => {
      preFormRef.value?.disableForm();
    });
  },
  { deep: true, immediate: true }
);
</script>

<style scoped lang="scss">
.msg {
  font-size: 20px;
  font-weight: 600;
  text-align: center;
}
</style>
