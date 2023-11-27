<template>
  <div>
    <el-divider content-position="left">条件设置</el-divider>
    <el-form label-width="80px">
      <template v-if="varVisible">
        <el-form-item label="变量名称">
          <el-input v-model="variableName" maxlength="32" @change="setElementVariableName" />
        </el-form-item>
        <el-form-item label="变量事件" v-if="varEventVisible">
          <el-input v-model="variableEvents" maxlength="32" @change="setElementVariableEvents" />
        </el-form-item>
      </template>

      <el-form-item label="条件类型">
        <el-select v-model="conditionData.conditionType" @change="setElementConditionType">
          <el-option v-for="item in conditionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="条件内容" v-if="conditionData.conditionType && conditionData.conditionType === 'expression'">
        <el-input v-model="conditionData.expression" maxlength="100" @change="setConditionExpression" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, onBeforeUnmount } from "vue";
import { Element } from "diagram-js/lib/model/Types";
import * as CU from "@/components/BpmnJs/bo-utils/conditionUtil";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";

// element The element.
let scopedElement: Element;

// 变量配置部分
const varVisible = ref<boolean>(false);
const variableName = ref<string | undefined>(undefined);
const varEventVisible = ref<boolean>(false);
const variableEvents = ref<string | undefined>(undefined);

// 条件类型配置部分
const conditionTypeOptions = ref<Record<string, string>[]>([]);
const conditionData = ref<any>({});

/**
 * 回显表单值
 */
const getElementData = () => {
  conditionTypeOptions.value = CU.getConditionTypeOptions(scopedElement);

  varVisible.value = CU.isConditionEventDefinition(scopedElement);
  variableName.value = CU.getVariableNameValue(scopedElement);
  if (varVisible.value) {
    varEventVisible.value = !CU.isExtendStartEvent(scopedElement);
    variableEvents.value = CU.getVariableEventsValue(scopedElement);
  }

  conditionData.value.conditionType = CU.getConditionTypeValue(scopedElement);
  if (conditionData.value.conditionType === "expression") {
    conditionData.value.expression = CU.getConditionExpressionValue(scopedElement);
  }
};

const setElementVariableName = (value: string | undefined) => {
  CU.setVariableNameValue(scopedElement, value);
};

const setElementVariableEvents = (value: string | undefined) => {
  CU.setVariableEventsValue(scopedElement, value);
};

const setElementConditionType = (value: string) => {
  CU.setConditionTypeValue(scopedElement, value);
};

const setConditionExpression = (value: string | undefined) => {
  CU.setConditionExpressionValue(scopedElement, value);
};

/**
 * 初始化
 */
EventBus.on("element-init", function (modeler) {
  catchUndefElement((element) => {
    scopedElement = element;

    getElementData();
    let elementRegistry = modeler.get("elementRegistry");

    console.log("elements", elementRegistry._elements);
    console.log("element", element);
  });
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("element-init");
});
</script>
