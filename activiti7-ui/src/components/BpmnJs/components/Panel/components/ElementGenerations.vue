<template>
  <el-card shadow="never">
    <el-divider content-position="left">常规信息</el-divider>
    <el-form label-width="80px">
      <el-form-item label="ID">
        <el-input v-model="elementId" disabled maxlength="32" @change="updateElementId" />
      </el-form-item>
      <el-form-item label="流程名称">
        <el-input v-model="elementName" maxlength="20" @change="updateElementName" />
      </el-form-item>
      <el-form-item label="介绍">
        <el-input v-model="elementDoc" type="textarea" @change="updateElementDoc" />
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import modelerStore from "@/store/modeler";
import { Element } from "bpmn-js/lib/model/Types";
import { getNameValue, setNameValue } from "@/components/BpmnJs/bo-utils/nameUtil";
import { setIdValue } from "@/components/BpmnJs/bo-utils/idUtil";
import { getDocumentValue, setDocumentValue } from "@/components/BpmnJs/bo-utils/documentationUtil";
import EventBus from "@/utils/EventBus";

const modeler = modelerStore();

// 当前节点信息
let scopedElement: Element;
// 节点id
const elementId = ref<string>("");
// 节点名称
const elementName = ref<string>("");
// 节点文档
const elementDoc = ref<string>("");

// 默认节点名称
type DefaultElement = {
  elementType: string; // 节点类型
  defaultName: string; // 默认节点名称
};

// 默认节点名称
const list: DefaultElement[] = [
  { elementType: "bpmn:StartEvent", defaultName: "开始事件" },
  { elementType: "bpmn:UserTask", defaultName: "用户任务" },
  { elementType: "bpmn:ServiceTask", defaultName: "服务任务" },
  { elementType: "bpmn:ExclusiveGateway", defaultName: "互斥网关" },
  { elementType: "bpmn:ParallelGateway", defaultName: "并行网关" },
  { elementType: "bpmn:EndEvent", defaultName: "结束节点" }
];

/**
 * 获取节点数据
 */
const getElementData = () => {
  elementId.value = modeler.getActiveId as string;
  elementName.value = getNameValue(scopedElement) || "";
  elementDoc.value = getDocumentValue(scopedElement) || "";
};

/**
 * 更新节点id
 * @param value 节点id
 */
const updateElementId = (value: string) => {
  setIdValue(scopedElement, value);
};

/**
 * 更新节点名称
 * @param value 节点名称
 */
const updateElementName = (value: string) => {
  setNameValue(scopedElement, value);
};

/**
 * 更新节点文档
 * @param value 节点文档
 */
const updateElementDoc = (value: string) => {
  setDocumentValue(scopedElement, value);
};

/**
 * 初始化节点名称,如果节点名称等于空预赋值
 */
const initElementName = () => {
  if (elementName.value) return;
  const item = list.find((t) => t.elementType === modeler.getActive.type);
  if (item) updateElementName(item.defaultName);
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  scopedElement = modeler.getActive;
  getElementData();
  initElementName();
});
</script>
