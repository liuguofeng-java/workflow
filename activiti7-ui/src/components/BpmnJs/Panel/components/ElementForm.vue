<template>
  <div>
    <el-collapse-item title="动态表单" name="ElementForm">
      <el-form label-width="100px">
        <el-form-item label="表单" class="disabled-color">
          <el-input v-model="form.formName" disabled>
            <template #append>
              <el-button :icon="Search" @click="selectForm.handleOpen()" />
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </el-collapse-item>
    <SelectForm ref="selectForm" @ok="selectFormOk" />
  </div>
</template>

<script lang="ts" setup>
import { onBeforeUnmount, ref } from "vue";
import { getBusinessObject, type ModdleElement, type Element } from "bpmn-js/lib/util/ModelUtil";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import { getExPropValue, updateExModdleProp } from "@/components/BpmnJs/bo-utils/popsUtils";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import SelectForm from "@/components/SelectForm/index.vue";
import { Search } from "@element-plus/icons-vue";

// element The element.
let scopedElement: Element = undefined;

// moddleElement The model element.
let moddleElement: ModdleElement = undefined;

// 表单数据
const form = ref({
  formKey: "",
  formName: ""
});

// 选择表单
const selectForm = ref();

/**
 * 选择表单返回数据
 * @param data 表单数据
 */
const selectFormOk = (data) => {
  updateProp("formKey", data.formId);
  updateProp("formName", data.formName);
};

/**
 * 更新值
 * @param propKey 属性key
 * @param propValue 属性值
 */
const updateProp = (propKey, propValue) => {
  updateExModdleProp(scopedElement, moddleElement, propKey, propValue);
  getElementData();
};

/**
 * 获取节点数据
 */
const getElementData = () => {
  form.value.formKey = getExPropValue(moddleElement, "formKey") || "";
  form.value.formName = getExPropValue(moddleElement, "formName") || "";
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    moddleElement = getBusinessObject(element);
    getElementData();
  });
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("element-init");
});
</script>

<style scoped>
.disabled-color :deep() .is-disabled > .el-input__wrapper {
  background-color: var(--el-input-bg-color) !important;
}
.disabled-color :deep() .is-disabled > .el-input__wrapper > input {
  -webkit-text-fill-color: var(--el-input-text-color) !important;
}
</style>
