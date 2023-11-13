<template>
  <el-collapse-item title="用户分配" name="User">
    <el-form label-width="100px">
      <el-form-item label="代理人">
        <el-input v-model="UAForm.assignee" @change="updateUserAssignProp('assignee', $event)" />
      </el-form-item>

      <el-form-item label="任务执行人">
        <el-input v-model="UAForm.candidateUsers" @change="updateUserAssignProp('candidateUsers', $event)" />
      </el-form-item>

      <el-form-item label="候选组">
        <el-input v-model="UAForm.candidateGroups" @change="updateUserAssignProp('candidateGroups', $event)" />
      </el-form-item>

      <el-form-item label="到期日">
        <el-input v-model="UAForm.dueDate" @change="updateUserAssignProp('dueDate', $event)" />
      </el-form-item>

      <el-form-item label="跟进日期">
        <el-input v-model="UAForm.followUpDate" @change="updateUserAssignProp('followUpDate', $event)" />
      </el-form-item>

      <el-form-item label="优先">
        <el-input v-model="UAForm.priority" @change="updateUserAssignProp('priority', $event)" />
      </el-form-item>
    </el-form>
  </el-collapse-item>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { getBusinessObject, type ModdleElement } from "bpmn-js/lib/util/ModelUtil";
import debounce from "lodash.debounce";
import useElementUpdateListener from "@/components/BpmnJs/hooks/useElementUpdateListener";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import editor from "@/components/BpmnJs/store/editor";
import modeler from "@/components/BpmnJs/store/modeler";

function getExPropValue<T>(element: any, propKey: string): T {
  const exPropKey = `${editor().getProcessEngine}:${propKey}`;
  return element && element.get ? element.get(exPropKey) : element ? element[exPropKey] : element;
}
const updateExModdleProp = debounce(
  function (element: BpmnElement, moddleElement: ModdleElement, propKey: string, propValue: unknown) {
    const modeling = modeler().getModeling;
    const exPropKey = `${editor().getProcessEngine}:${propKey}`;
    modeling!.updateModdleProperties(element, moddleElement, {
      [exPropKey]: propValue === "" ? undefined : propValue
    });
  },
  300,
  { leading: true }
);

let scopedElement: BpmnElement | undefined = undefined;
let scopedBO: ModdleElement | undefined = undefined;

type UserAssigneeProp = "assignee" | "candidateUsers" | "candidateGroups" | "dueDate" | "followUpDate" | "priority";

const PROP_KEYS: UserAssigneeProp[] = ["assignee", "candidateUsers", "candidateGroups", "dueDate", "followUpDate", "priority"];

const EmptyUAForm = PROP_KEYS.reduce((a, b) => (a[b] = "") || a, {});

const UAForm = ref(EmptyUAForm as Record<UserAssigneeProp, string>);

const updateUserAssignProp = (key: UserAssigneeProp, value: string) => {
  updateExModdleProp(scopedElement!, scopedBO!, key, value);
};

const reloadElementData = () =>
  catchUndefElement((element) => {
    console.log(element.id);
    scopedElement = element;
    scopedBO = getBusinessObject(element);
    for (const key of PROP_KEYS) {
      UAForm.value[key] = getExPropValue(scopedBO!, key) || "";
    }
    console.log(UAForm.value);
  });
useElementUpdateListener(reloadElementData);
</script>
