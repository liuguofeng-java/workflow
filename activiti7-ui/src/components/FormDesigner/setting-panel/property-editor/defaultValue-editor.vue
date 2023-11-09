<template>
  <el-form-item v-if="!hasConfig('optionItems')" :label="i18nt('designer.setting.defaultValue')">
    <el-input type="text" v-model="optionModel.defaultValue" @change="emitDefaultValueChange"></el-input>
  </el-form-item>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import propertyMixin from "@/components/FormDesigner/setting-panel/property-editor/propertyMixin";

export default {
  name: "defaultValue-editor",
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
