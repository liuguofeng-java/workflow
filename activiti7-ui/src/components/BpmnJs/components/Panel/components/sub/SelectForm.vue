<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" append-to-body>
    <div class="container">
      <VFormDesigner v-if="drawer" ref="vfdRef" :designerConfig="designerConfig">
        <template #customToolButtons>
          <el-button type="primary" :icon="Edit" plain @click="submit">保存</el-button>
        </template>
      </VFormDesigner>
    </div>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref } from "vue";
import VFormDesigner from "@/components/FormDesigner/index.vue";
import { nextTick } from "vue";
import { Edit } from "@element-plus/icons-vue";

const designerConfig = ref<any>({
  languageMenu: true, //是否显示语言切换菜单
  externalLink: false, //是否显示GitHub、文档等外部链接
  formTemplates: false, //是否显示表单模板
  eventCollapse: false, //是否显示组件事件属性折叠面板
  widgetNameReadonly: false, //禁止修改组件名称

  clearDesignerButton: true, //是否显示清空设计器按钮
  previewFormButton: true, //是否显示预览表单按钮
  importJsonButton: true, //是否显示导入JSON按钮
  exportJsonButton: true, //是否显示导出JSON器按钮
  exportCodeButton: false, //是否显示导出代码按钮
  generateSFCButton: false, //是否显示生成SFC按钮

  toolbarMaxWidth: 450, //设计器工具按钮栏最大宽度（单位像素）
  toolbarMinWidth: 300, //设计器工具按钮栏最小宽度（单位像素）

  presetCssCode: "", //设计器预设CSS样式代码

  resetFormJson: true //是否在设计器初始化时将表单内容重置为空
});

// 是否打开弹出框
const drawer = ref(false);

// vform实例
const vfdRef = ref();

const pops = defineProps({
  formJson: {
    type: Object,
    default: () => {
      return {};
    }
  }
});

/**
 * 打开选择器
 */
const handleOpen = () => {
  drawer.value = true;
  nextTick(() => {
    console.log(pops.formJson);

    vfdRef.value.setFormJson(pops.formJson);
  });
};

/**
 * 提交
 */
const submit = () => {
  const formJson = vfdRef.value.getFormJson();
  emit("ok", JSON.parse(JSON.stringify(formJson)));
  drawer.value = false;
};

defineExpose({
  handleOpen
});

const emit = defineEmits<{
  (event: "ok", formJson: any): void;
}>();
</script>

<style scoped lang="scss">
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
  :deep() .full-height {
    overflow-y: hidden;
    display: block;
  }
}
</style>
