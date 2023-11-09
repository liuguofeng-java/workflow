<template>
  <el-form-item :label="i18nt('designer.setting.labelWidth')">
    <el-input type="number" v-model="optionModel.labelWidth" @input="inputNumberHandler" min="0" class="hide-spin-button"></el-input>
  </el-form-item>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import propertyMixin from "@/components/FormDesigner/setting-panel/property-editor/propertyMixin";

export default {
  name: "labelWidth-editor",
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
