<template>
  <div class="root">
    <el-drawer v-model="drawer" size="100%" :destroy-on-close="true">
      <el-tabs type="border-card" v-loading="loading" @tab-change="tabChange" v-model="tabsValue">
        <el-tab-pane label="审批记录" name="1">
          <div class="history-root">
            <el-card class="box-card main-form" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>主表单信息</span>
                </div>
              </template>
              <MainForm ref="mainForm" :form-json="mainFormInfo.formJson" :form-data="mainFormInfo.formData" />
            </el-card>
            <el-card class="box-card history-container" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>历史记录</span>
                </div>
              </template>
              <div class="hint-container">
                <div class="history">已审批节点记录</div>
                <div class="next">活动的未审批节点</div>
              </div>
              <el-timeline>
                <el-timeline-item v-for="(item, index) in historyRecordList" :key="index" :color="item.status === 1 ? '#0bbd87' : '#e4e7ed'">
                  <HistoryNodeInfo :node-item="item" />
                </el-timeline-item>
              </el-timeline>
            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane label="流程节点" name="2">
          <HighlightNode :highlightNode="highlightNode" />
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import baseService from "@/service/baseService";
import HistoryNodeInfo from "./HistoryNodeInfo.vue";
import MainForm from "./MainForm.vue";
import HighlightNode from "./HighlightNode.vue";

// 是否打开弹出框
const drawer = ref(false);
// 审批历史记录列表数据
const historyRecordList = ref<any[]>([]);
// 流程图高亮信息
const highlightNode = ref<any>();
// 主表单信息
const mainFormInfo = ref<any>({});
// 流程实例id
let instanceId = ref("");

// 加载中
const loading = ref(false);
// tab 选择的值
const tabsValue = ref("1");

/**
 * 初始化
 * @param id 流程实例id
 */
const init = (id: string) => {
  drawer.value = true;
  instanceId.value = id;
  tabsValue.value = "1";
  historyRecord();
};

/**
 * 审批记录
 */
const historyRecord = async () => {
  loading.value = true;
  historyRecordList.value = [];
  const historyRecordRes = await baseService.get(`/processStart/getHistoryRecord?instanceId=${instanceId.value}`);
  if (historyRecordRes.code === 200) {
    historyRecordList.value = historyRecordRes.data;
  }

  const mainFormInfoRes = await baseService.get(`/processStart/getMainFormInfo?instanceId=${instanceId.value}`);
  if (mainFormInfoRes.code === 200) {
    mainFormInfo.value = mainFormInfoRes.data;
  }

  loading.value = false;
};

/**
 * 查询流程图信息(高亮信息)
 */
const highlightNodeInfo = () => {
  highlightNode.value = {};
  loading.value = true;
  baseService.get(`/processStart/getHighlightNodeInfo?instanceId=${instanceId.value}`).then((res) => {
    if (res.code === 200) {
      highlightNode.value = res.data;
      loading.value = false;
    }
  });
};

/**
 * 切换tab
 * @param name tab的item name的名称
 */
const tabChange = (name: string) => {
  switch (name) {
    case "1":
      historyRecord();
      break;
    case "2":
      highlightNodeInfo();
      break;
  }
};

defineExpose({
  init
});
</script>

<style scoped>
.history-root {
  display: flex;
  height: 100%;
}
.history-container {
  min-width: 500px;
}
.history-container :deep() .el-card__body {
  display: flex;
  flex-direction: column;
  height: calc(100% - 56px);
}
.main-form {
  width: 100%;
}
.hint-container {
  display: flex;
  justify-content: center;
  margin: 30px 10px;
}

.hint-container > div {
  margin: 0 20px;
  font-size: 16px;
  font-weight: 500;
  color: black;
  display: flex;
  justify-content: center;
  align-items: baseline;
}

.hint-container > div::before {
  content: " ";
  height: 12px;
  width: 12px;
  display: block;
  border-radius: 50%;
  margin-right: 8px;
}

.history::before {
  background-color: #0bbd87 !important;
}

.next::before {
  background-color: #e4e7ed !important;
}

:deep(.el-tabs__content) {
  height: 100% !important;
}

.root :deep() .el-drawer__body {
  overflow: hidden;
}

.root :deep() .el-tabs--border-card {
  height: 100% !important;
  display: flex;
  flex-direction: column;
}

.root :deep() .el-tab-pane {
  height: 100% !important;
  display: flex;
  flex-direction: column;
}

.root :deep() .el-timeline {
  overflow: auto;
}
</style>
