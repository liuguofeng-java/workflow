<template>
  <div v-if="Object.keys(highlightNode).length !== 0" class="root">
    <DesignerDetails :xml="highlightNode.xml" id="highlightNode" />
    <el-card class="box-card" id="nodeInfo" v-show="open">
      <div v-for="(item, index) in nodeInfoItem" :key="index">
        <HistoryNodeInfo :nodeItem="item" />
      </div>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, onBeforeUnmount } from "vue";
import DesignerDetails from "@/components/BpmnJs/components/Designer/details";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import Popper from "popper.js";
import HistoryNodeInfo from "./HistoryNodeInfo.vue";
import type Modeler from "bpmn-js/lib/Modeler";
import Canvas from "diagram-js/lib/core/Canvas";
import { nextTick } from "vue";

// 弹框实例
let bpmnModeler: Modeler;
// 弹框实例
let popper: Popper;
// 是否显示
let open = ref<boolean>(false);
// 选择的历史审批记录
const nodeInfoItem = ref<any[]>([]);

const pops = defineProps({
  // 流程图高亮信息
  highlightNode: {
    type: Object,
    default: () => {
      return {};
    }
  }
});

watch(
  () => pops.highlightNode,
  async () => {
    // 高亮流程图
    setTimeout(() => {
      const canvas: Canvas = bpmnModeler?.get("canvas");
      if (Object.keys(pops.highlightNode).length === 0) return;
      nextTick(() => {
        pops.highlightNode.nodeInfo.forEach((item) => {
          canvas.addMarker(item.activityId, item.status == 1 ? "executed" : "unfinished");
        });
      });
    }, 50);
  },
  { deep: true, immediate: true }
);

/**
 * 弹出节点信息
 * @param elementId 节点id
 */
const showNodeInfo = (elementId: string) => {
  nodeInfoItem.value = pops.highlightNode.nodeInfo.find((t: any) => t.activityId === elementId)?.historyRecordVo;
  if (!nodeInfoItem.value) return;

  const element = document.querySelector(`[data-element-id='${elementId}']`) as HTMLElement;
  const nodeInfo = document.querySelector(`#nodeInfo`) as HTMLElement;
  open.value = true;
  popper = new Popper(element, nodeInfo, {
    placement: "right"
  });
};

/**
 * 获取bpmn事件
 */
EventBus.on("modeler-init", (modeler) => {
  bpmnModeler = modeler;
  // 移入移出节点
  let elementId: string;
  modeler.on("element.hover", (event) => {
    // 移出时摧毁上一个
    if (elementId && event.element.id != elementId) {
      popper?.destroy();
      open.value = false;
    }
    // 移入的如果是用户节点，就弹出框
    if (event.element.type === "bpmn:UserTask") {
      showNodeInfo(event.element.id);
    }
    elementId = event.element.id;
  });
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("modeler-init");
});
</script>

<style scoped>
.root {
  height: 100%;
}
#highlightNode {
  height: 100%;
}
</style>
