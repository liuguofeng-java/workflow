<template>
  <el-form-item :label="i18nt('designer.setting.suffixIcon')">
    <el-input type="text" v-model="optionModel.suffixIcon"></el-input>
  </el-form-item>
</template>

<script>
  import i18n from "@/components/form-designer/utils/i18n";

  export default {
    name: "suffixIcon-editor",
    mixins: [i18n],
    props: {
      designer: Object,
      selectedWidget: Object,
      optionModel: Object,
    },
  }
</script>

<style scoped>

</style>
