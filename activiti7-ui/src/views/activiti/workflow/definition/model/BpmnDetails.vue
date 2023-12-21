<template>
  <el-drawer v-model="drawer" size="100%" destroy-on-close>
    <Designer :xml="xml" :settings="settings" style="height: 100%"></Designer>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Designer from "@/components/BpmnJs/components/Designer";
import baseService from "@/service/baseService";

// 是否加载抽屉
let drawer = ref<boolean>(false);
let xml = ref<string>();

// bpmn配置
const settings = {
  processEngine: "activiti",
  paletteMode: "custom",
  contextPadMode: "custom"
};

/**
 * 初始化表单
 * @param deploymentId 流程部署id
 */
const open = (deploymentId) => {
  baseService
    .get("/processDefinition/getDefinitionXml", {
      deploymentId
    })
    .then((res) => {
      if (res.code === 200) {
        drawer.value = true;
        xml.value = res.data;
      }
    });
};

defineExpose({
  open
});
</script>

<style scoped lang="scss">
.close {
  display: flex;
  justify-content: flex-end;
  margin: 5px;

  i {
    cursor: pointer;
  }
}

#designer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>
