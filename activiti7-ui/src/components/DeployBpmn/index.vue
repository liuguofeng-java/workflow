<template>
  <el-drawer v-model="drawer" size="100%" :with-header="false" destroy-on-close>
    <div class="conainer">
      <div class="header">
        <div class="back" @click="close">
          <el-icon><Back /></el-icon>
        </div>
        <div class="steps-conainer">
          <ol class="steps">
            <li :class="stepsActive === 1 ? 'active' : ''" @click="stepsClick(1)">表单设计器</li>
            <li :class="stepsActive === 2 ? 'active' : ''" @click="stepsClick(2)">流程设计器</li>
          </ol>
        </div>
        <div>
          <el-button type="primary">保存</el-button>
        </div>
      </div>
      <div class="content">
        <FormDesigner v-show="stepsActive === 1" ref="formDesignerRef" :fromJson="fromJson" />
        <BpmnDesigner v-show="stepsActive === 2" ref="bpmnDesignerRef" :xml="xml" />
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ElMessageBox } from "element-plus";
import FormDesigner from "./components/FormDesigner.vue";
import BpmnDesigner from "./components/BpmnDesigner.vue";
import EventBus from "@/utils/EventBus";
import modelerStore from "@/components/BpmnJs/store/modeler";

const modeler = modelerStore();

// 表单设计器
const formDesignerRef = ref();

// 流程设计器
const bpmnDesignerRef = ref();

// 是否加载抽屉
let drawer = ref<boolean>(false);

// 步骤记录，1:表单设计器
let stepsActive = ref<number>(1);

// 表单设计器,动态表单结构
let fromJson = ref<object>({});

// 流程设计器
let xml = ref<string>();

/**
 * 初始化设计器
 */
const open = () => {
  drawer.value = true;
};

/**
 * 关闭
 */
const close = () => {
  ElMessageBox.confirm("确定要关闭吗?", "提示").then(() => {
    drawer.value = false;
  });
};

/**
 * 点击步骤
 */
const stepsClick = async (num: number) => {
  if (num === 1) {
    xml.value = await bpmnDesignerRef.value.getXml();
  } else if (num === 2) {
    fromJson.value = formDesignerRef.value.getFormJson();
    modeler.setFormJson(fromJson);
  }
  stepsActive.value = num;
};

defineExpose({
  open
});
</script>

<style scoped lang="scss">
.conainer {
  height: 100%;
  width: 100%;
  .header {
    height: 50px;
    background-color: white;
    display: flex;
    justify-content: space-between;
    .back {
      height: 100%;
      width: fit-content;
      display: flex;
      align-items: center;
      font-size: 30px;
      cursor: pointer;
    }
    .steps-conainer {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      .steps {
        --normal-color: #666;
        --active-color: #06e;
        width: 600px;
        cursor: pointer;
        display: flex;
        justify-content: space-between;
        padding: 0;
        margin: 0;
        counter-reset: order;
      }
      /* 步骤项 */
      .steps > li {
        flex: auto;
        display: inline-flex;
        align-items: center;
        counter-increment: order;
        color: var(--active-color);
      }
      .steps > li:last-child {
        flex: none;
      }
      /* 步骤编号(带圈数字) */
      .steps > li::before {
        content: counter(order);
        flex-shrink: 0;
        width: 1.4em;
        line-height: 1.4em;
        margin-right: 0.5em;
        text-align: center;
        border-radius: 50%;
        border: 1px solid;
      }
      /* 步骤项引导线 */
      .steps > li:not(:last-child)::after {
        content: "";
        flex: 1;
        margin: 0 1em;
        border-bottom: 1px solid;
        opacity: 0.6;
      }
      /* 步骤状态 */
      .steps > .active {
        color: var(--active-color);
      }
      .steps > .active::before {
        color: #fff;
        background: var(--active-color);
        border-color: var(--active-color);
      }
      .steps > .active::after,
      .steps > .active ~ li {
        color: var(--normal-color);
      }
    }
  }
  .content {
    height: calc(100% - 50px);
  }
}
</style>

<style></style>
