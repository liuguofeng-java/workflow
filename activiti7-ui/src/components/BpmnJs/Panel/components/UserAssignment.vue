<template>
  <el-collapse-item title="用户分配" name="User">
    <el-form label-width="100px">
      {{ UAForm }}
      <el-form-item label="类型">
        <el-select v-model="UAForm.userType" @change="updateUserAssignProp('userType', $event)">
          <el-option v-for="item in userType" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="代理人" v-if="UAForm.userType === 'assignee'">
        <el-input v-model="UAForm.assignee" @change="updateUserAssignProp('assignee', $event)" />
      </el-form-item>

      <el-form-item label="候选人" v-if="UAForm.userType === 'candidateUsers'">
        <!-- 多选用户 -->
        <MultipleUser :list="multipleUserList" ref="multipleUser" @ok="multipleUserOk" />
      </el-form-item>

      <el-form-item label="候选组" v-if="UAForm.userType === 'candidateGroups'">
        <!-- 多选用户 -->
        <MultipleDept :list="multipleDeptList" ref="multipleUser" @ok="multipleDeptOk" />
        <!-- <el-input v-model="UAForm.candidateGroups" @change="updateUserAssignProp('candidateGroups', $event)" /> -->
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
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import editor from "@/components/BpmnJs/store/editor";
import modeler from "@/components/BpmnJs/store/modeler";
import MultipleUser from "@/components/MultipleUser/index.vue";
import MultipleDept from "@/components/MultipleDept/index.vue";
import EventBus from "@/components/BpmnJs/utils/EventBus";

// element The element.
let scopedElement: any = undefined;

// moddleElement The model element.
let moddleElement: ModdleElement | undefined = undefined;

// 表单参数
type UserAssigneeProp = "userType" | "assignee" | "candidateUsers" | "candidateGroups" | "names" | "dueDate" | "followUpDate" | "priority";
const PROP_KEYS: UserAssigneeProp[] = ["userType", "assignee", "candidateUsers", "candidateGroups", "names", "dueDate", "followUpDate", "priority"];

const EmptyUAForm = PROP_KEYS.reduce((a, b) => (a[b] = "") || a, {});
// 表单
const UAForm = ref(EmptyUAForm as Record<UserAssigneeProp, string>);

// 用户类型
const userType = ref<any>([
  {
    label: "代理人",
    value: "assignee"
  },
  {
    label: "候选人",
    value: "candidateUsers"
  },
  {
    label: "候选组",
    value: "candidateGroups"
  }
]);

// 多选用户
const multipleUser = ref();
// 多选用户 选择的数据
const multipleUserList = ref<any[]>([]);

// 多选部门
const multipleDept = ref();
// 多选部门 选择的数据
const multipleDeptList = ref<any[]>([]);

/**
 * 获取设置的值
 */
function getExPropValue<T>(element: any, propKey: string): T {
  const exPropKey = `${editor().getProcessEngine}:${propKey}`;
  return element && element.get ? element.get(exPropKey) : element ? element[exPropKey] : element;
}

/**
 * 更新设置的值
 */
const updateExModdleProp = function (element: any, moddleElement: ModdleElement, propKey: string, propValue: unknown) {
  const modeling = modeler().getModeling;
  const exPropKey = `${editor().getProcessEngine}:${propKey}`;
  modeling?.updateModdleProperties(element, moddleElement, {
    [exPropKey]: propValue === "" ? undefined : propValue
  });
};

/**
 * 更新表单值
 * @param key 表单属性key
 * @param value 表单值
 */
const updateUserAssignProp = (key: UserAssigneeProp, value: string) => {
  // 类型改变时清空之前填写的值,代理人、候选人、候选组
  if (key === "userType") {
    userType.value.forEach((item) => {
      updateExModdleProp(scopedElement, moddleElement, item.value, "");
    });
    // 重置变量
    multipleUserList.value = [];
    updateUserAssignProp("names", "");
  }
  // 更新组件
  updateExModdleProp(scopedElement, moddleElement, key, value);
  // 获取节点数据
  getElementData();
};

/**
 * 获取节点数据
 */
const getElementData = () => {
  for (const key of PROP_KEYS) {
    UAForm.value[key] = getExPropValue(moddleElement, key) || "";
  }
};

/**
 * 多选用户选择完成
 * @param list 选择的数据
 */
const multipleUserOk = (list: any[]) => {
  multipleUserList.value = list;
  const userIds = multipleUserList.value.map((item) => {
    return item.userId;
  });
  const userNames = multipleUserList.value.map((item) => {
    return item.username;
  });
  updateUserAssignProp("candidateUsers", userIds.join(","));
  updateUserAssignProp("names", userNames.join(","));
};

/**
 * 多选部门选择完成
 * @param list 选择的数据
 */
const multipleDeptOk = (list: any[]) => {
  multipleDeptList.value = list;
  const deptIds = multipleDeptList.value.map((item) => {
    return item.deptId;
  });
  const deptNames = multipleDeptList.value.map((item) => {
    return item.deptName;
  });
  updateUserAssignProp("candidateGroups", deptIds.join(","));
  updateUserAssignProp("names", deptNames.join(","));
};

// 先销毁事件，防止重复触发
EventBus.off("element-init");
// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    moddleElement = getBusinessObject(element);
    getElementData();

    // 重置变量
    multipleUserList.value = [];
    multipleDeptList.value = [];
    // 还原候选人选择项
    if (UAForm.value.userType === "candidateUsers" && UAForm.value.candidateUsers !== "") {
      const candidateUsers = UAForm.value.candidateUsers.split(",");
      const userNames = UAForm.value.names.split(",");
      for (let i = 0; i < candidateUsers.length; i++) {
        multipleUserList.value.push({
          userId: candidateUsers[i],
          username: userNames[i]
        });
      }
    }
    // 还原候选组选择项
    if (UAForm.value.userType === "candidateGroups" && UAForm.value.candidateGroups !== "") {
      const candidateGroups = UAForm.value.candidateGroups.split(",");
      const deptNames = UAForm.value.names.split(",");
      for (let i = 0; i < candidateGroups.length; i++) {
        multipleDeptList.value.push({
          deptId: candidateGroups[i],
          deptName: deptNames[i]
        });
      }
    }
    // 如果一个都没有选择
    if (UAForm.value.userType === "") {
      UAForm.value.userType = "assignee";
    }
  });
});
</script>

<style scoped></style>
