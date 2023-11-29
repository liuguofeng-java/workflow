<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" append-to-body>
    <div>
      <el-button @click="drawer = false">关闭</el-button>
      <el-button @click="submit">确定</el-button>
    </div>
    <VFormDesigner v-if="drawer" ref="vfdRef" />
  </el-drawer>
</template>
<script setup lang="ts">
import { ref } from "vue";
import VFormDesigner from "@/components/FormDesigner/index.vue";
import { nextTick } from "vue";

// 是否打开弹出框
const drawer = ref(false);

// vform实例
const vfdRef = ref();

const pops = defineProps({
  formJson: {
    type: Object,
    default: () => {
      return {};
    }
  }
});

/**
 * 打开选择器
 */
const handleOpen = () => {
  drawer.value = true;
  nextTick(() => {
    vfdRef.value.setFormJson(pops.formJson);
  });
};

/**
 * 提交
 */
const submit = () => {
  const formJson = vfdRef.value.getFormJson();
  console.log(formJson);
  emit("ok", formJson);
  drawer.value = false;
};

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", formJson: any): void;
}>();
</script>

<style scoped>
.inline {
  display: flex;
  justify-content: flex-end;
}

::v-deep(.el-drawer__body) {
  overflow: hidden !important;
}
</style>
