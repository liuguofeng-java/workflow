<template>
  <div>
    <el-divider content-position="left">执行监听器</el-divider>
    <el-table :data="list">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="event" label="事件类型" width="150">
        <template #default="scoped">
          <template v-for="item in events" :key="item.value">
            <span v-if="item.value === scoped.row.event">{{ item.label }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="class" label="java类" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="handleOpen(scope.$index)">修改</el-button>
          <el-button link type="primary" @click="removeListener(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="width: 100%">
      <el-button type="primary" plain style="width: 100%" @click="handleOpen(-1)">新增</el-button>
    </div>
    <el-dialog v-model="open" title="添加" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="事件类型" prop="event">
          <el-select v-model="form.event">
            <el-option v-for="item in events" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="java类" prop="class">
          <el-input v-model="form.class" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveListener()">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, markRaw } from "vue";
import { ModdleElement } from "bpmn-moddle";
import { Element } from "bpmn-js/lib/model/Types";
import { getExecutionListeners, getExecutionListenerTypes, addExecutionListener, updateExecutionListener, removeExecutionListener } from "@/components/BpmnJs/bo-utils/executionListenersUtil";
import EventBus from "@/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";

// element The element.
let scopedElement: Element;

// 是否弹出
let open = ref<boolean>(false);

// 表格的数据
const list = ref<ModdleElement[]>([]);

// 事件类型
let events = ref<any[]>([]);

// 当前表单结构数据
const form = ref<any>({ event: "", type: "class", class: "" });

const formRules = {
  event: { required: true, trigger: ["blur", "change"], message: "事件类型不能为空" },
  type: { required: true, trigger: ["blur", "change"], message: "监听器类型不能为空" },
  class: { required: true, trigger: ["blur", "change"], message: "java类不能为空" }
};

// 行下表
let rowIndex = -1;

/**
 * 弹出
 * @param index 下标
 *
 */
const handleOpen = (index: number) => {
  rowIndex = index;
  open.value = true;
  form.value = { event: "", type: "class", class: "" };
  if (index !== -1) {
    const row = list.value[index];
    form.value.event = row.event;
    form.value.class = row.class;
  }
};

/**
 * 添加或修改数据
 */
const saveListener = () => {
  rowIndex === -1 ? addExecutionListener(scopedElement, form.value) : updateExecutionListener(scopedElement, form.value, list.value[rowIndex]);
  getElementData();
  open.value = false;
};

/**
 * 删除数据
 * @param index 下标
 */
const removeListener = (index: number) => {
  const listener: ModdleElement = list.value[index];
  removeExecutionListener(scopedElement, listener);
  getElementData();
};

/**
 * 获取数据
 */
const getElementData = () => {
  list.value = markRaw(getExecutionListeners(scopedElement as Element));
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    events.value = getExecutionListenerTypes(element);
    getElementData();
  });
});
</script>
