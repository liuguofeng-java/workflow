<template>
  <div>
    <el-divider content-position="left">动态表单</el-divider>
    <el-table :data="list">
      <el-table-column type="index" label="序号" width="60px" align="center" />
      <el-table-column prop="options.name" label="类型">
        <template #default="scoped">
          <span>
            <svg-icon :icon-class="scoped.row.icon" class-name="color-svg-icon" />
            {{ i18n.methods.i18n2t(`designer.widgetLabel.${scoped.row.type}`, `extension.widgetLabel.${scoped.row.type}`) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="options.label" label="名称" />
    </el-table>

    <div style="width: 100%">
      <el-button type="primary" plain style="width: 100%" @click="selectFormRef.handleOpen()">设计</el-button>
    </div>
    <SelectForm ref="selectFormRef" :formJson="formJson" @ok="selectFormOk" />
  </div>
</template>

<script lang="ts" setup>
import { ref, onBeforeUnmount } from "vue";
import modelerStore from "@/components/BpmnJs/store/modeler";
import SelectForm from "./SubChild/SelectForm.vue";
import EventBus from "@/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import { type Element } from "bpmn-js/lib/util/ModelUtil";
import i18n from "@/components/FormDesigner/utils/i18n";
import SvgIcon from "@/components/FormDesigner/svg-icon/index.vue";

const modeler = modelerStore();

// 当前节点信息
let scopedElement: Element;

// 表格数据
let list = ref<any[]>();

// 选择表单
const selectFormRef = ref();

// 当前表单结构数据
let formJson = ref<any>({});

// 编辑表单成功
const selectFormOk = (data: any) => {
  modeler.setFormJson({
    activityId: scopedElement.id,
    formJson: data
  });
  getElementData();
};

/**
 * 获取节点表单数据
 */
const getElementData = () => {
  formJson.value = modeler.getFormJsonList.find((t) => t.activityId === scopedElement.id)?.formJson || {};
  list.value = formJson.value.widgetList;
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    getElementData();
  });
});

/**
 * 销毁事件，防止重复触发
 */
onBeforeUnmount(async () => {
  await EventBus.off("element-init");
});
</script>

<style scoped></style>
