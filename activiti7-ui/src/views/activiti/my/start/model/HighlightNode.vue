<template>
  <div v-if="Object.keys(highlightNode).length !== 0" class="root">
    <DesignerDetails :xml="highlightNode.xml" id="highlightNode" v-if="highlightNode.xml" />
    <el-card class="box-card" id="nodeInfo">
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

let popper: Popper;
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
    // 因为渲染流程图需要时间,所以加延时
    setTimeout(() => {
      if (Object.keys(pops.highlightNode).length === 0) return;
      var svg = document.getElementById("highlightNode");

      pops.highlightNode.nodeInfo.forEach((item) => {
        var node = svg?.querySelector(`[data-element-id='${item.activityId}']`);
        node?.classList.add(item.status == 1 ? "executed" : "unfinished");
      });
    }, 10);
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
  popper = new Popper(element, nodeInfo, {
    placement: "right"
  });
};

/**
 * 获取bpmn事件
 */
EventBus.on("modeler-init", (modeler) => {
  // 移入移出节点
  let elementId: string;
  modeler.on("element.hover", (event) => {
    // 移出时摧毁上一个
    if (elementId && event.element.id != elementId) {
      popper?.destroy();
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
