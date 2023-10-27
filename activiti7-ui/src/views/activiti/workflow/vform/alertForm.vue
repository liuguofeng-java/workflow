<template>
  <el-drawer v-model="drawer" :title="title" :direction="direction" :before-close="handleClose">
    <v-form-designer ref="vfdRef"></v-form-designer>
  </el-drawer>
</template>

<script setup>
import { ref } from "vue";
import VFormDesigner from "@/components/form-designer/index.vue";
let title = ref("");

const vfdRef = ref(null);
</script>

<style lang="scss"></style>
