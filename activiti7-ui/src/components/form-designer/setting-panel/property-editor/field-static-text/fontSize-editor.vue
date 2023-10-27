<template>
  <el-form-item :label="i18nt('designer.setting.fontSize')">
    <el-input v-model="optionModel.fontSize"></el-input>
  </el-form-item>
</template>

<script>
import i18n from "@/components/form-designer/utils/i18n";

export default {
  name: "fontSize-editor",
  mixins: [i18n],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
