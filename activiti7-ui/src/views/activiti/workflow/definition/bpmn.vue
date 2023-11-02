<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false">
    <div id="designer-container">
      <div class="close">
        <el-icon @click="drawer = false" size="20">
          <CloseBold />
        </el-icon>
      </div>
      <Toolbar></Toolbar>
      <div class="main-content">
        <Designer v-model:xml="processXml"></Designer>
        <Panel></Panel>
      </div>
      <ContextMenu></ContextMenu>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Toolbar from "@/components/bpmnJs/Toolbar";
import Designer from "@/components/bpmnJs/Designer";
import Panel from "@/components/bpmnJs/Panel";
import ContextMenu from "@/components/bpmnJs/ContextMenu/index.vue";

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 初始化表单
const open = (formId: string) => {
  drawer.value = true;
};

const processXml = ref<string | undefined>(undefined);

onMounted(() => {
  document.body.addEventListener("contextmenu", function (ev) {
    ev.preventDefault();
  });
});

defineExpose({
  open
});
</script>

<style scoped lang="scss">
.close {
  display: flex;
  justify-content: flex-end;
  margin: 5px;
  i {
    cursor: pointer;
  }
}
#designer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>
