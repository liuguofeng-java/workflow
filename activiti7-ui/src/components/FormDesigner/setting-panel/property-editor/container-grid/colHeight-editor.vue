<template>
  <div>
    <el-form-item :label="i18nt('designer.setting.gridColHeight')">
      <el-input type="number" v-model="optionModel.colHeight" @input="inputNumberHandler" min="0" class="hide-spin-button"></el-input>
    </el-form-item>
  </div>
</template>

<script>
import i18n from "@/components/FormDesigner/utils/i18n";
import propertyMixin from "@/components/FormDesigner/setting-panel/property-editor/propertyMixin";

export default {
  name: "colHeight-editor",
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
};
</script>

<style scoped></style>
