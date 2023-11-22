<template>
  <div class="root">
    <el-drawer v-model="drawer" size="100%" destroy-on-close>
      <el-tabs type="border-card" v-loading="loading" @tab-change="tabChange" v-model="tabsValue">
        <el-tab-pane label="审批记录" name="1">
          <div class="hint-container">
            <div class="history">已审批节点记录</div>
            <div class="next">活动的未审批节点</div>
          </div>
          <el-timeline>
            <el-timeline-item v-for="(item, index) in historyRecordList" :key="index" :color="item.status === 1 ? '#0bbd87' : '#e4e7ed'">
              <el-card>
                <el-descriptions :column="1">
                  <el-descriptions-item label="候选人" v-if="item.identity.userNames">
                    <el-tag v-for="(userName, index) in item.identity.userNames" :key="index">{{ userName }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="候选组" v-if="item.identity.groupNames">
                    <el-tag v-for="(groupName, index) in item.identity.groupNames" :key="index">{{ groupName }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item v-if="item.status === 1" label="时间">
                    <span>{{ item.startTime }} 到 {{ item.endTime }}</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="节点名称">{{ item.nodeName }}</el-descriptions-item>
                  <el-descriptions-item v-if="item.userName" label="审批人">{{ item.userName }}</el-descriptions-item>
                  <el-descriptions-item v-if="item.comment" label="审批意见">{{ item.comment }}</el-descriptions-item>
                </el-descriptions>
                <!-- 用户节点填写的表单 -->
                <NodeForm :form-json="item.formJson" :form-data="item.formData" />
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
        <el-tab-pane label="流程节点" name="2">
          <DesignerDetails :xml="highlightNode.xml" id="highlightNode" style="height: 100%" v-if="tabsValue === '2' && highlightNode.xml" />
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import baseService from "@/service/baseService";
import DesignerDetails from "@/components/BpmnJs/Designer/details";
import NodeForm from "./nodeForm.vue";

// 是否打开弹出框
const drawer = ref(false);
// 审批历史记录列表数据
const historyRecordList = ref<any[]>([]);
// 流程图高亮信息
const highlightNode = ref<any>();
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
const historyRecord = () => {
  historyRecordList.value = [];
  loading.value = true;
  baseService.get(`/processStart/getHistoryRecord?instanceId=${instanceId.value}`).then((res) => {
    if (res.code === 200) {
      historyRecordList.value = res.data;
      loading.value = false;
    }
  });
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

      // 高亮流程图
      // 因为渲染流程图需要时间,所以加延时
      setTimeout(() => {
        const nodeInfo = highlightNode.value.nodeInfo;
        var svg = document.getElementById("highlightNode");

        nodeInfo.forEach((item) => {
          var node = svg?.querySelector(`[data-element-id='${item.activityId}']`);
          node?.classList.add(item.status == 1 ? "executed" : "unfinished");
        });
      }, 10);
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

<style scoped></style>
