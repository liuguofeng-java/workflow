<template>
  <div>
    <el-dialog v-model="open" title="审批记录" width="1200px" append-to-body>
      <el-timeline>
        <el-timeline-item v-for="(item, index) in list" :key="index" :color="item.status === 1 ? '#0bbd87' : '#e4e7ed'">
          <el-card>
            <el-descriptions :column="1">
              <el-descriptions-item v-if="item.status === 1" label="时间">{{ item.startTime }} 到 {{ item.endTime }}</el-descriptions-item>
              <el-descriptions-item label="节点名称">{{ item.nodeName }}</el-descriptions-item>
              <el-descriptions-item v-if="item.userName" label="审批人">{{ item.userName }}</el-descriptions-item>
              <el-descriptions-item v-if="item.comment" label="审批意见">{{ item.comment }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import baseService from "@/service/baseService";
import { ElLoading } from "element-plus";

// 是否打开弹出框
const open = ref(false);
// 列表数据
const list = ref<any[]>([]);

// 初始化
const init = (instanceId: string) => {
  const loading = ElLoading.service({
    lock: true,
    text: "加载中..."
  });
  baseService.get(`/processStart/getHistoryRecord?instanceId=${instanceId}`).then((res) => {
    if (res.code === 200) {
      list.value = res.data;
      open.value = true;
      loading.close();
    }
  });
};

defineExpose({
  init
});
</script>

<style scoped></style>
