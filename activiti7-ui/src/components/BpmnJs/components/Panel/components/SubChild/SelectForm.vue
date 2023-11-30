<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" append-to-body>
    <div class="container">
      <div class="header">
        <el-button type="primary" @click="submit">保存</el-button>
      </div>
      <VFormDesigner v-if="drawer" ref="vfdRef" />
    </div>
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
  emit("ok", JSON.parse(JSON.stringify(formJson)));
  drawer.value = false;
};

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", formJson: any): void;
}>();
</script>

<style scoped lang="scss">
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
  :deep() .full-height {
    overflow-y: hidden;
    display: block;
  }
  .header {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
