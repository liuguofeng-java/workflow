<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false">
    <el-button @click="drawer = false">关闭</el-button>
    <NConfigProvider abstract :componentOptions="{ DynamicInput: { buttonSize: 'small' } }" :hljs="hljs">
      <NDialogProvider>
        <div :class="{ computedClasses }" id="designer-container">
          <NMessageProvider>
            <Toolbar></Toolbar>
            <div class="main-content">
              <Designer v-model:xml="processXml"></Designer>
              <Panel></Panel>
            </div>
            <Setting v-model:settings="editorSettings"></Setting>
            <ContextMenu></ContextMenu>
          </NMessageProvider>
        </div>
      </NDialogProvider>
    </NConfigProvider>
  </el-drawer>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from "vue";
import Toolbar from "@/components/bpmnJs/Toolbar";
import Designer from "@/components/bpmnJs/Designer";
import Panel from "@/components/bpmnJs/Panel";
import Setting from "@/components/bpmnJs/Setting";
import ContextMenu from "@/components/bpmnJs/ContextMenu/index.vue";
import { EditorSettings } from "@/components/bpmnJs/types/editor/settings";
import { defaultSettings } from "@/components/bpmnJs/config";

import hljs from "highlight.js/lib/core";
import xml from "highlight.js/lib/languages/xml";
import json from "highlight.js/lib/languages/json";
import { NConfigProvider, NDialogProvider, NMessageProvider } from "naive-ui";

hljs.registerLanguage("xml", xml);
hljs.registerLanguage("json", json);

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 初始化表单
const open = (formId: string) => {
  drawer.value = true;
};

const editorSettings = ref<EditorSettings>({ ...defaultSettings });

const processXml = ref<string | undefined>(undefined);

const customPalette = computed<boolean>(() => editorSettings.value.paletteMode === "custom");
const customPenal = computed<boolean>(() => editorSettings.value.penalMode === "custom");

const computedClasses = computed<string>(() => {
  const baseClass = ["designer-container"];
  customPalette.value && baseClass.push("designer-with-palette");
  customPenal.value && baseClass.push("designer-with-penal");
  editorSettings.value.bg === "grid-image" && baseClass.push("designer-with-bg");
  editorSettings.value.bg === "image" && baseClass.push("designer-with-image");

  return baseClass.join(" ");
});

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
