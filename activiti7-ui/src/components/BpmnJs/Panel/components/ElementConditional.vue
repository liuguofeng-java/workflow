<template>
  <el-collapse-item title="条件设置" name="conditionalSettings">
    <el-form label-width="100px">
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

      <template v-if="conditionData.conditionType && conditionData.conditionType === 'script'">
        <el-form-item label="脚本类型">
          <el-select v-model="conditionData.conditionType" @change="setElementConditionScriptType">
            <el-option v-for="item in scriptTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="脚本语言">
          <el-input v-model="conditionData.language" @change="setConditionScriptLanguage" />
        </el-form-item>

        <el-form-item v-show="conditionData.scriptType === 'inline'" label="脚本内容">
          <el-input v-model="conditionData.body" @change="setConditionScriptBody" />
        </el-form-item>

        <el-form-item v-show="conditionData.scriptType === 'external'" label="资源地址">
          <el-input v-model="conditionData.resource" @change="setConditionScriptResource" />
        </el-form-item>
      </template>
    </el-form>
  </el-collapse-item>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import modeler from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import { scriptTypeOptions } from "@/components/BpmnJs/config/selectOptions";
import * as CU from "@/components/BpmnJs/bo-utils/conditionUtil";
import EventBus from "@/components/BpmnJs/utils/EventBus";

export default defineComponent({
  name: "ElementConditional",
  setup() {
    const modelerStore = modeler();
    const getActive = computed<Element | null>(() => modelerStore.getActive!);

    // 变量配置部分
    const varVisible = ref<boolean>(false);
    const variableName = ref<string | undefined>(undefined);
    const varEventVisible = ref<boolean>(false);
    const variableEvents = ref<string | undefined>(undefined);
    const getElementVariables = () => {
      varVisible.value = CU.isConditionEventDefinition(getActive.value!);
      variableName.value = CU.getVariableNameValue(getActive.value!);
      if (varVisible.value) {
        varEventVisible.value = !CU.isExtendStartEvent(getActive.value!);
        variableEvents.value = CU.getVariableEventsValue(getActive.value!);
      }
    };
    const setElementVariableName = (value: string | undefined) => {
      CU.setVariableNameValue(getActive.value!, value);
    };
    const setElementVariableEvents = (value: string | undefined) => {
      CU.setVariableEventsValue(getActive.value!, value);
    };

    // 条件类型配置部分
    const conditionTypeOptions = ref<Record<string, string>[]>([]);
    const conditionData = ref<ConditionalForm>({});
    const getElementConditionType = () => {
      conditionData.value.conditionType = CU.getConditionTypeValue(getActive.value!);
      conditionData.value.conditionType === "expression" && getConditionExpression();
      conditionData.value.conditionType === "script" && getConditionScript();
    };
    const setElementConditionType = (value: string) => {
      CU.setConditionTypeValue(getActive.value!, value);
    };

    const getConditionExpression = () => {
      conditionData.value.expression = CU.getConditionExpressionValue(getActive.value!);
    };
    const setConditionExpression = (value: string | undefined) => {
      CU.setConditionExpressionValue(getActive.value!, value);
    };

    const getConditionScript = () => {
      conditionData.value.language = CU.getConditionScriptLanguageValue(getActive.value!);
      conditionData.value.scriptType = CU.getConditionScriptTypeValue(getActive.value!);
      conditionData.value.body = CU.getConditionScriptBodyValue(getActive.value!);
      conditionData.value.resource = CU.getConditionScriptResourceValue(getActive.value!);
    };
    const setConditionScriptLanguage = (value: string | undefined) => {
      CU.setConditionScriptLanguageValue(getActive.value!, value);
    };
    const setElementConditionScriptType = (value: string | undefined) => {
      CU.setConditionScriptTypeValue(getActive.value!, value);
    };
    const setConditionScriptBody = (value: string | undefined) => {
      CU.setConditionScriptBodyValue(getActive.value!, value);
    };
    const setConditionScriptResource = (value: string | undefined) => {
      CU.setConditionScriptResourceValue(getActive.value!, value);
    };

    onMounted(() => {
      getElementVariables();
      getElementConditionType();
      conditionTypeOptions.value = CU.getConditionTypeOptions(getActive.value!);
      EventBus.on("element-update", () => {
        conditionTypeOptions.value = CU.getConditionTypeOptions(getActive.value!);
        getElementVariables();
        getElementConditionType();
      });
    });

    return {
      varVisible,
      varEventVisible,
      variableName,
      variableEvents,
      setElementVariableName,
      setElementVariableEvents,
      conditionTypeOptions,
      conditionData,
      scriptTypeOptions,
      setElementConditionType,
      setConditionExpression,
      setConditionScriptLanguage,
      setElementConditionScriptType,
      setConditionScriptBody,
      setConditionScriptResource
    };
  }
});
</script>
