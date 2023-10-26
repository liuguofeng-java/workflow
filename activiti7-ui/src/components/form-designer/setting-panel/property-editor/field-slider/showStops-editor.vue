<template>
  <el-form-item :label="i18nt('designer.setting.showStops')">
    <el-switch v-model="optionModel.showStops"></el-switch>
  </el-form-item>
</template>

<script>
  import i18n from "@/components/form-designer/utils/i18n"

  export default {
    name: "showStops-editor",
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
