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
        <Designer :xml="xml" :settings="settings" />
        <Panel></Panel>
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Previews from "@/components/BpmnJs/components/Toolbar/components/Previews";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";
import Designer from "@/components/BpmnJs/components/Designer";
import Panel from "@/components/BpmnJs/components/Panel";
import modelerStore from "@/store/modeler";
import baseService from "@/service/baseService";
import { ElMessage, ElMessageBox, ElLoading } from "element-plus";
import { checkd } from "@/components/BpmnJs/overwrite-modules/Lint/bpmnlint";
import { nextTick } from "vue";

const modeler = modelerStore();

// bpmn配置
const settings = {
  processId: `Process_${new Date().getTime()}`,
  processName: `新建流程`,
  processEngine: "activiti",
  paletteMode: "rewrite",
  contextPadMode: "rewrite",
  useLint: true,
  isLabelEditingProvider: true,
  isMove: true
};

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 流程设计器
let xml = ref<string>();

/**
 * 初始化设计器
 * @param deploymentId 部署id
 */
const open = async (deploymentId: string | undefined) => {
  const loading = ElLoading.service({
    lock: true,
    text: "加载中",
    background: "rgba(0, 0, 0, 0.7)"
  });
  modeler.clearData();
  const res = await baseService.get("/table/getWidgetDataType");
  modeler.setWidgetDataType(res.data);
  // 获取到上一个版本的流程图xml
  if (deploymentId) {
    nextTick(() => {
      baseService.get("/processDefinition/getDefinitionInfo", { deploymentId }).then((res) => {
        if (res.code === 200) {
          let tableInfo = res.data.tableInfo;
          if (tableInfo) {
            tableInfo.type = "ready";
            modeler.setTableInfo(tableInfo);
          }
          if (res.data.nodeColumns) {
            modeler.setNodeColumns(res.data.nodeColumns);
          }
          res.data.formJsonList.forEach((formJson) => {
            modeler.setFormJson(formJson);
          });
          xml.value = res.data.xml;
          loading.close();
          drawer.value = true;
        }
      });
    });
  } else {
    xml.value = "";
    drawer.value = true;
    loading.close();
  }
};

/**
 * 保存
 */
const submit = async () => {
  // 判断bpmn验证结果
  if (!checkd()) {
    ElMessage.error("流程存在错误,不能保存!");
    return;
  }
  await ElMessageBox.confirm("确定要部署当前流程吗?", "提示");
  modeler.updateData();
  // 动态表单数据
  const formJsonList = modeler.getFormJsonList;
  // 节点绑定字段信息
  const nodeColumns = modeler.getNodeColumns;
  // 表信息
  const tableInfo = modeler.getTableInfo;
  const { xml } = await modeler.getModeler.saveXML({ format: true, preamble: true });
  baseService
    .post(`/processDefinition/deployProcess`, {
      xml,
      formJsonList,
      nodeColumns,
      tableInfo
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
@/store/modeler
