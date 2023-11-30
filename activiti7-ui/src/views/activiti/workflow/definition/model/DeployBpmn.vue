<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" destroy-on-close>
    <div id="designer-container">
      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" @click="submit">保存</el-button>
        <Scales></Scales>
        <Commands></Commands>
        <Previews></Previews>
        <div class="close">
          <el-icon @click="close"><Close /></el-icon>
        </div>
      </div>
      <div class="main-content">
        <Designer :xml="xml" v-if="xml !== undefined"></Designer>
        <Panel></Panel>
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, onBeforeUnmount } from "vue";
import Previews from "@/components/BpmnJs/components/Toolbar/components/Previews";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";

import Designer from "src/components/BpmnJs/components/Designer";
import Panel from "src/components/BpmnJs/components/Panel";
import EventBus from "@/utils/EventBus";
import Modeler from "bpmn-js/lib/Modeler";
import modelerStore from "@/components/BpmnJs/store/modeler";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox } from "element-plus";
import { nextTick } from "vue";

const modeler = modelerStore();

// bpmn.js 实例
let bpmnModel: Modeler;

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 流程设计器
let xml = ref<string>();

/**
 * 初始化设计器
 * @param deploymentId 部署id
 */
const open = (deploymentId: string | undefined) => {
  drawer.value = true;
  xml.value = undefined;
  modeler.clearFormJson();
  // 获取到上一个版本的流程图xml
  if (deploymentId) {
    nextTick(() => {
      baseService.get("/processDefinition/getDefinitionInfo", { deploymentId }).then((res) => {
        if (res.code === 200) {
          res.data.formJsonList.forEach((formJson) => {
            modeler.setFormJson(formJson);
          });
          xml.value = res.data.xml;
        }
      });
    });
  } else {
    xml.value = "";
  }
};

/**
 * 保存
 */
const submit = async () => {
  await ElMessageBox.confirm("确定要部署当前流程吗?", "提示");
  const formJsonList = modeler.getFormJsonList;
  const { xml } = await bpmnModel.saveXML({ format: true, preamble: true });
  baseService
    .post(`/processDefinition/deployProcess`, {
      xml,
      formJsonList
    })
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success("保存成功！");
        drawer.value = false;
        emit("ok");
      }
    });
};

/**
 * 关闭
 */
const close = async () => {
  await ElMessageBox.confirm("确定要关闭吗?", "提示");
  drawer.value = false;
};

defineExpose({
  open
});

/**
 * 获取bpmn事件
 */
EventBus.on("modeler-init", (modeler: Modeler) => {
  bpmnModel = modeler;
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("modeler-init");
});

const emit = defineEmits<{
  (event: "ok"): void;
}>();
</script>

<style scoped lang="scss">
#designer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.close {
  font-size: 20px;
  margin-left: auto;
  i {
    cursor: pointer;
  }
}
</style>
