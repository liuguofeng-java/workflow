<template>
  <el-form-item :label="i18nt('designer.setting.requiredHint')">
    <el-input type="text" v-model="optionModel.requiredHint"></el-input>
  </el-form-item>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import propertyMixin from "@/components/FormDesigner/setting-panel/property-editor/propertyMixin";

export default {
  name: "requiredHint-editor",
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
