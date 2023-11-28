<template>
  <div id="designer-container">
    <!-- 操作按钮 -->
    <div class="toolbar">
      <Scales class="room"></Scales>
      <Commands class="room"></Commands>
      <Previews class="room"></Previews>
    </div>
    <div class="main-content">
      <Designer :xml="xml"></Designer>
      <Panel></Panel>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onBeforeUnmount } from "vue";
import Previews from "@/components/BpmnJs/components/Toolbar/components/Previews";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";

import Designer from "src/components/BpmnJs/components/Designer";
import Panel from "src/components/BpmnJs/components/Panel";
import EventBus from "@/utils/EventBus";
import Modeler from "bpmn-js/lib/Modeler";

// 初始化的xml
const xml = ref("");

// bpmn.js 实例
let bpmnModel: Modeler;

/**
 * 获取xml
 */
const getXml = async () => {
  const { xml } = await bpmnModel.saveXML({ format: true, preamble: true });
  return xml;
};

/**
 * 获取bpmn事件
 */
EventBus.on("modeler-init", (modeler: Modeler) => {
  bpmnModel = modeler;
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("modeler-init");
});

defineExpose({
  getXml
});
</script>

<style scoped lang="scss">
#designer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>
