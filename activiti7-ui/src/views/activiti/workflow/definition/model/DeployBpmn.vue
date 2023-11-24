<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" destroy-on-close>
    <div id="designer-container">
      <div class="close">
        <el-icon @click="drawer = false" size="20">
          <CloseBold />
        </el-icon>
      </div>
      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" class="room" @click="deployment">保存</el-button>
        <Aligns class="room"></Aligns>
        <Scales class="room"></Scales>
        <Commands class="room"></Commands>
      </div>
      <div class="main-content">
        <Designer :xml="xml"></Designer>
        <Panel></Panel>
      </div>
      <ContextMenu></ContextMenu>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Aligns from "@/components/BpmnJs/components/Toolbar/components/Aligns";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";

import Designer from "src/components/BpmnJs/components/Designer";
import Panel from "src/components/BpmnJs/components/Panel";
import ContextMenu from "@/components/BpmnJs/components/ContextMenu/index.vue";
import modeler from "@/components/BpmnJs/store/modeler";
import { ElMessage, ElMessageBox } from "element-plus";
import baseService from "@/service/baseService";

// 是否加载抽屉
let drawer = ref<boolean>(false);

const modelerStore = modeler();

// 初始化的xml
const xml = ref("");

/**
 * 初始化
 * @param lastXml 上一个部署流程图xml
 */
const open = (lastXml: string) => {
  xml.value = lastXml;
  drawer.value = true;
};

/**
 * 部署流程
 */
const deployment = async () => {
  await ElMessageBox.confirm("确定要部署当前流程吗?", "提示");
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  const modeler = modelerStore.getModeler!;
  if (!modeler) {
    return ElMessage.warning("模型加载失败，请刷新重试");
  }
  const { xml } = await modeler.saveXML({ format: true, preamble: true });
  console.log(xml);

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  baseService.post(`/processDefinition/deployment`, xml).then((res) => {
    if (res.code === 200) {
      drawer.value = false;
      ElMessage.success(res.msg);
      emit("ok");
    }
  });
};

const emit = defineEmits<{
  (event: "ok"): void;
}>();

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
