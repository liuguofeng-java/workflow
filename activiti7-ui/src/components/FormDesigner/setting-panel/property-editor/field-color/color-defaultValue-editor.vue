<template>
  <el-form-item :label="i18nt('designer.setting.defaultValue')">
    <el-color-picker v-model="optionModel.defaultValue" @change="emitDefaultValueChange"> </el-color-picker>
  </el-form-item>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import propertyMixin from "@/components/FormDesigner/setting-panel/property-editor/propertyMixin";

export default {
  name: "color-defaultValue-editor",
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
