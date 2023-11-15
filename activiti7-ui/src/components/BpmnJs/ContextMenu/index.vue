<template>
  <div v-if="showPopover" :show="showPopover" :x="x" :y="y">
    <div class="bpmn-context-menu">
      <div class="context-menu_header">{{ contextMenuTitle }}</div>
      <div class="context-menu_body">
        <div v-for="item in currentReplaceOptions" :key="item.actionName" class="context-menu_item">
          <i :class="`context-menu_item_icon ${item.className}`"></i>
          <span @click="triggerAction(item, $event)">{{ translateCh(item.label) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
/**
 * @direction 自定义右键菜单
 * @author MiyueFE
 * @date 2022/7/11
 */
import { onBeforeUnmount, onMounted, ref } from "vue";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import { Element } from "diagram-js/lib/model/Types";
import { customTranslate } from "@/components/BpmnJs/additional-modules/Translate";
import BpmnReplaceOptions from "@/components/BpmnJs/utils/BpmnReplaceOptions";
import { isAppendAction } from "@/components/BpmnJs/utils/BpmnDesignerUtils";
import contextMenuActions from "@/components/BpmnJs/ContextMenu/contextMenuActions";

const translateCh = customTranslate;

const showPopover = ref(false);
const x = ref(0);
const y = ref(0);

const currentReplaceOptions = ref<any[]>([]);

let currentElement: Element | null = null;
const isAppend = ref<boolean>(false);
const contextMenuTitle = ref<string>("创建元素");

const { appendAction, replaceAction } = contextMenuActions();

const triggerAction = (entry, event) => {
  try {
    isAppend.value ? appendAction(entry.target, event) : replaceAction(entry.target, currentElement);
    showPopover.value = false;
  } catch (e) {
    console.error(e);
  }
};

const initEventCallback = (event: MouseEvent, element?: Element) => {
  x.value = event.clientX;
  y.value = event.clientY;
  currentElement = element || null;
  isAppend.value = isAppendAction(element);
  currentReplaceOptions.value = BpmnReplaceOptions(element);
  contextMenuTitle.value = isAppend.value ? "创建元素" : "更改元素";
  showPopover.value = true;
};

const closePopover = () => (showPopover.value = false);

onMounted(() => {
  EventBus.on("show-contextmenu", initEventCallback);
  document.body.addEventListener("click", closePopover);
});

onBeforeUnmount(() => {
  EventBus.off("show-contextmenu", initEventCallback);
  document.body.removeEventListener("click", closePopover);
});
</script>
