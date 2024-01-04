<template>
  <el-card v-if="nodeItem && Object.keys(nodeItem).length !== 0" class="item-node">
    <el-descriptions :column="1">
      <el-descriptions-item label="候选人" v-if="nodeItem.identity.userNames">
        <el-tag v-for="(userName, index) in nodeItem.identity.userNames" :key="index">{{ userName }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="候选组" v-if="nodeItem.identity.groupNames">
        <el-tag v-for="(groupName, index) in nodeItem.identity.groupNames" :key="index">{{ groupName }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="nodeItem.identity.userName" label="审批人">
        <el-tag type="success">{{ nodeItem.identity.userName }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="nodeItem.status === 1" label="开始时间">
        <span>{{ nodeItem.startTime }}</span>
      </el-descriptions-item>
      <el-descriptions-item v-if="nodeItem.status === 1" label="结束时间">
        <span>{{ nodeItem.endTime }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="节点名称">{{ nodeItem.nodeName }}</el-descriptions-item>
    </el-descriptions>
    <!-- 用户节点填写的表单 -->
    <NodeForm :form-json="nodeItem.formJson" :form-data="nodeItem.formData" />
  </el-card>
</template>
<script setup lang="ts">
import NodeForm from "./NodeForm.vue";
defineProps({
  nodeItem: {
    type: Object,
    default: () => {
      return {};
    }
  }
});
</script>

<style scoped lang="scss">
.item-node {
  width: 350px;
}
</style>
