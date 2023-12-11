<template>
  <el-card shadow="never">
    <el-divider content-position="left">执行监听器</el-divider>
    <el-table :data="list">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="" label="名称">
        <template #default="scoped">
          <span v-if="scoped.row.$attrs.name">{{ scoped.row.$attrs.name }}</span>
          <span v-else>{{ scoped.row.$attrs["activiti:name"] }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="event" label="事件类型" width="80">
        <template #default="scoped">
          <template v-for="item in events" :key="item.value">
            <span v-if="item.value === scoped.row.event">{{ item.label }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="class" label="java类" show-overflow-tooltip />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button link type="primary" @click="openListener(scope.$index)">修改</el-button>
          <el-button link type="primary" @click="removeListener(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="width: 100%">
      <el-button type="primary" plain style="width: 100%" @click="openListener(-1)">新增</el-button>
    </div>

    <!-- 添加监听器 -->
    <el-dialog v-model="LOpen" title="添加" width="700px">
      <el-form ref="lformRef" :model="Lform" :rules="formRules" label-width="80px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="事件类型" prop="event">
              <el-select v-model="Lform.event">
                <el-option v-for="item in events" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="java类" prop="class" class="disabled-color">
              <el-input v-model="Lform.name" disabled>
                <template #append>
                  <el-button :icon="Search" @click="selectListenerRef.handleOpen()" />
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-card shadow="never">
          <div style="width: 100%">
            <el-button type="primary" @click="openField(-1)">新增</el-button>
          </div>
          <el-table :data="Lform.fields" style="width: 100%" max-height="250">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="name" label="字段名" width="120" />
            <el-table-column label="字段类型" width="120">
              <template #default="scope">
                <span v-if="scope.row.string">字符串</span>
                <span v-else>表达式</span>
              </template>
            </el-table-column>
            <el-table-column label="值">
              <template #default="scope">
                <span v-if="scope.row.string">{{ scope.row.string }}</span>
                <span v-else>{{ scope.row.expression }}</span>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="120">
              <template #default="scope">
                <el-button link type="primary" size="small" @click="openField(scope.$index)">修改</el-button>
                <el-button link type="primary" size="small" @click="removeField(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveListener()">确 定</el-button>
          <el-button @click="LOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加字段 -->
    <el-dialog v-model="FOpen" title="添加字段" width="700px">
      <el-form ref="fformRef" :model="Fform" :rules="formRules" label-width="80px">
        <el-form-item label="字段名" prop="name">
          <el-input v-model="Fform.name" placeholder="请输入字段名" />
        </el-form-item>
        <el-form-item label="字段类型" prop="type">
          <el-radio-group v-model="Fform.type">
            <el-radio-button v-for="item in fieldType" :label="item.value" :key="item.value">{{ item.name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="字符串" prop="string" v-if="Fform.type === 'string'">
          <el-input v-model="Fform.string" placeholder="请输入值" />
        </el-form-item>
        <el-form-item label="表达式" prop="expression" v-if="Fform.type === 'expression'">
          <el-input v-model="Fform.expression" placeholder="请输入值" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveField">确 定</el-button>
          <el-button @click="FOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 选择监听器 -->
    <SelectListener ref="selectListenerRef" @ok="selectListenerOk" />
  </el-card>
</template>

<script lang="ts" setup>
import { ref, markRaw } from "vue";
import { ModdleElement } from "bpmn-moddle";
import { Element } from "bpmn-js/lib/model/Types";
import { getExecutionListeners, getExecutionListenerTypes, addExecutionListener, updateExecutionListener, removeExecutionListener } from "@/components/BpmnJs/bo-utils/executionListenersUtil";
import EventBus from "@/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import { Search } from "@element-plus/icons-vue";
import SelectListener from "./sub/SelectListener.vue";
import { nextTick } from "vue";

// element The element.
let scopedElement: Element;

// 是否弹出添加监听器
let LOpen = ref<boolean>(false);

// 是否弹出添加字段
let FOpen = ref<boolean>(false);

// 表格的数据
const list = ref<ModdleElement[]>([]);

// 事件类型
let events = ref<any[]>([]);

// 监听器表单实例
const lformRef = ref();
// 字段表单实例
const fformRef = ref();

// 监听器表单结构数据
const Lform = ref<any>({ name: "", event: "", type: "class", class: "", fields: [] });

// 字段表单结构数据
// eslint-disable-next-line no-undef
const Fform = ref<BpmnField>({ name: "", type: "string", expression: "", string: "" });

// 监听器行下标
let lrowIndex = -1;

// 字段行下标
let frowIndex = -1;

const formRules = {
  event: { required: true, trigger: ["blur", "change"], message: "事件类型不能为空" },
  class: { required: true, trigger: ["blur", "change"], message: "java类不能为空" },
  name: { required: true, trigger: ["blur", "change"], message: "字段名称不能为空" },
  expression: { required: true, trigger: ["blur", "change"], message: "值不能为空" },
  string: { required: true, trigger: ["blur", "change"], message: "值不能为空" }
};

// 字段类型
const fieldType = [
  { name: "字符串", value: "string" },
  { name: "表达式", value: "expression" }
];

// 选择监听器实例
const selectListenerRef = ref();

/**
 * 弹出监听器表单
 * @param index 下标
 *
 */
const openListener = (index: number) => {
  lrowIndex = index;
  LOpen.value = true;
  nextTick(() => {
    lformRef.value.resetFields();
    Lform.value = { name: "", event: "", type: "class", class: "", fields: [] };
    if (index !== -1) {
      const row = list.value[index];
      Lform.value.name = row.$attrs.name ? row.$attrs.name : row.$attrs["activiti:name"];
      Lform.value.event = row.event;
      Lform.value.class = row.class;
      Lform.value.fields = row.fields;
    }
  });
};

/**
 * 添加或修改数据
 */
const saveListener = () => {
  lformRef.value.validate((valid: boolean) => {
    if (!valid) return;
    lrowIndex === -1 ? addExecutionListener(scopedElement, Lform.value) : updateExecutionListener(scopedElement, Lform.value, list.value[lrowIndex]);
    getElementData();
    LOpen.value = false;
  });
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
 * 选择监听器成功事件
 * @param data 选择的数据
 */
const selectListenerOk = (data: any) => {
  Lform.value.name = data.listenerName;
  Lform.value.class = data.javaClass;
};

/**
 * 弹出字段表单
 * @param index 行下标
 *
 */
const openField = (index: number) => {
  frowIndex = index;
  FOpen.value = true;
  nextTick(() => {
    fformRef.value.resetFields();
    if (frowIndex !== -1) {
      Fform.value = JSON.parse(JSON.stringify(Lform.value.fields[index]));
      if (Fform.value.string) {
        Fform.value.type = "string";
      } else {
        Fform.value.type = "expression";
      }
    } else {
      Fform.value = { name: "", type: "string", expression: "", string: "" };
    }
  });
};

/**
 * 添加或修改数据
 */
const saveField = () => {
  fformRef.value.validate((valid: boolean) => {
    if (!valid) return;
    FOpen.value = false;
    if (frowIndex === -1) {
      Lform.value.fields.push(JSON.parse(JSON.stringify(Fform.value)));
    } else {
      Lform.value.fields[frowIndex] = JSON.parse(JSON.stringify(Fform.value));
    }
  });
};

/**
 * 删除数据
 * @param index 下标
 */
const removeField = (index: number) => {
  Lform.value.fields.splice(index, 1);
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

<style scoped>
.disabled-color :deep() .is-disabled > .el-input__wrapper {
  background-color: var(--el-input-bg-color) !important;
}
.disabled-color :deep() .is-disabled > .el-input__wrapper > input {
  -webkit-text-fill-color: var(--el-input-text-color) !important;
}
</style>
