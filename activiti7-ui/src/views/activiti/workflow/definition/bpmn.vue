<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false">
    <div id="designer-container">
      <el-button @click="drawer = false">关闭</el-button>

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
import { computed, ref, onMounted } from "vue";
import Toolbar from "@/components/bpmnJs/Toolbar";
import Designer from "@/components/bpmnJs/Designer";
import Panel from "@/components/bpmnJs/Panel";
import ContextMenu from "@/components/bpmnJs/ContextMenu/index.vue";
import { EditorSettings } from "@/components/bpmnJs/types/editor/settings";
import { defaultSettings } from "@/components/bpmnJs/config";

import hljs from "highlight.js/lib/core";
import xml from "highlight.js/lib/languages/xml";
import json from "highlight.js/lib/languages/json";

hljs.registerLanguage("xml", xml);
hljs.registerLanguage("json", json);

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
#designer-container {
  width: 100%;
  height: 100%;
}
.main-content {
  height: 100%;
}
</style>
