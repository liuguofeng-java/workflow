<template>
  <v-form-designer ref="vfdRef"></v-form-designer>
</template>

<script setup>
import { ref } from "vue";
import VFormDesigner from "@/components/form-designer/index.vue";

const vfdRef = ref(null);
</script>

<style lang="scss">
body {
  margin: 0;
}
</style>
