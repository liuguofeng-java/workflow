<template>
  <el-card shadow="never" class="container">
    <el-divider content-position="left">条件设置</el-divider>
    <el-form label-width="80px">
      <template v-if="varVisible">
        <el-form-item label="变量名称">
          <el-input v-model="variableName" maxlength="32" @change="setElementVariableName" />
        </el-form-item>
        <el-form-item label="变量事件" v-if="varEventVisible">
          <el-input v-model="variableEvents" maxlength="32" @change="setElementVariableEvents" />
        </el-form-item>
      </template>

      <el-form-item label="类型">
        <el-radio-group v-model="conditionData.conditionType" @change="setElementConditionType">
          <el-radio-button v-for="item in conditionTypeOptions" :key="item.value" :label="item.value">{{ item.label }}</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <div v-if="conditionData.conditionType && conditionData.conditionType === 'expression'">
        <el-alert title="表达式" :description="expression" v-if="expression" type="success" :closable="false" />
        <el-table :data="list">
          <el-table-column label="逻辑" width="90" align="center">
            <template #default="scoped">
              <el-select v-model="scoped.row.logical" placeholder="逻辑" v-if="scoped.$index !== 0" size="small">
                <el-option v-for="item in logicalList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column label="字段" align="center">
            <template #default="scoped">
              <el-select v-model="scoped.row.field" size="small">
                <el-option-group v-for="item in nodeWidgets" :key="item.activityId" :label="item.activityName">
                  <el-option v-for="widget in item.widgetList" :key="widget.key" :value="widget.options.name" :label="widget.options.label">
                    <svg-icon v-if="widget.icon" :icon-class="widget.icon" class-name="color-svg-icon" />
                    {{ widget.options.label }}
                  </el-option>
                </el-option-group>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="比较" width="80" align="center">
            <template #default="scoped">
              <el-select v-model="scoped.row.compare" placeholder="比较" size="small">
                <el-option v-for="compare in compareList" :label="compare" :value="compare" :key="compare" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="值" align="center">
            <template #default="scoped">
              <el-select v-model="scoped.row.value" size="small" placeholder="值" v-if="getFieldType(scoped.row.field)?.type === 'Array'">
                <el-option v-for="item in getFieldType(scoped.row.field)?.value" :label="item.label" :value="item.value" :key="item.value" />
              </el-select>
              <el-select v-model="scoped.row.value" size="small" placeholder="值" v-else-if="getFieldType(scoped.row.field)?.type === 'Boolean'">
                <el-option label="真" :value="true" />
                <el-option label="假" :value="false" />
              </el-select>
              <el-input v-model="scoped.row.value" size="small" placeholder="值" v-else-if="getFieldType(scoped.row.field)?.type === 'Number'" type="number" />
              <el-input v-model="scoped.row.value" size="small" placeholder="值" v-else />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="55" align="center">
            <template #default="scope">
              <el-button link size="small" type="primary" @click="handleDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div style="width: 100%">
          <el-button type="primary" plain style="width: 100%" @click="handleAdd">新增</el-button>
        </div>
      </div>
    </el-form>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, computed } from "vue";
import { Element } from "bpmn-js/lib/model/Types";
import * as CU from "@/components/BpmnJs/bo-utils/conditionUtil";
import EventBus from "@/utils/EventBus";
import { type NodeWidget, getWidgetTree } from "@/components/BpmnJs/bo-utils/variableUtil";
import SvgIcon from "@/components/FormDesigner/svg-icon/index.vue";
import { ElMessage } from "element-plus";
import debounce from "lodash.debounce";
import modelerStore from "@/store/modeler";

const modeler = modelerStore();

// element The element.
let scopedElement: Element;

// 运算符
const compareList = ["==", "!=", ">", "<", ">=", "<="];

// 逻辑符
const logicalList = [
  { label: "并且", value: "&&" },
  { label: "或者", value: "||" }
];

// 控件类型
type WidgetType = {
  name: "select" | "radio" | "checkbox" | "switch" | "input" | "textarea" | "number" | "rate" | "slider";
  type: "Text" | "Number" | "Boolean" | "Array";
  value: any;
};

// 控件类型
const widgetTypes: WidgetType[] = [
  { name: "select", type: "Array", value: [] },
  { name: "checkbox", type: "Array", value: [] },
  { name: "radio", type: "Array", value: [] },
  { name: "switch", type: "Boolean", value: false },
  { name: "input", type: "Text", value: "" },
  { name: "number", type: "Number", value: 0 },
  { name: "rate", type: "Number", value: 0 },
  { name: "slider", type: "Number", value: 0 }
];

// 变量配置部分
const varVisible = ref<boolean>(false);
const variableName = ref<string | undefined>(undefined);
const varEventVisible = ref<boolean>(false);
const variableEvents = ref<string | undefined>(undefined);

// 条件类型配置部分
const conditionTypeOptions = ref<Record<string, string>[]>([]);
const conditionData = ref<any>({});

// 组件列表
const nodeWidgets = ref<NodeWidget[]>([]);
const list = ref<any[]>([]);

// 流程表达式
let expression = ref<string>("");

/**
 * 回显表单值
 */
const getElementData = () => {
  conditionTypeOptions.value = CU.getConditionTypeOptions(scopedElement);

  varVisible.value = CU.isConditionEventDefinition(scopedElement);
  variableName.value = CU.getVariableNameValue(scopedElement);
  if (varVisible.value) {
    varEventVisible.value = !CU.isExtendStartEvent(scopedElement);
    variableEvents.value = CU.getVariableEventsValue(scopedElement);
  }

  conditionData.value.conditionType = CU.getConditionTypeValue(scopedElement);
  if (conditionData.value.conditionType === "expression") {
    conditionData.value.expression = CU.getConditionExpressionValue(scopedElement);
  }
};

const setElementVariableName = (value: string | undefined) => {
  CU.setVariableNameValue(scopedElement, value);
};

const setElementVariableEvents = (value: string | undefined) => {
  CU.setVariableEventsValue(scopedElement, value);
};

const setElementConditionType = (value: string) => {
  CU.setConditionTypeValue(scopedElement, value);
};

/**
 * 添加表达式
 */
const handleAdd = () => {
  const widget = nodeWidgets.value[0]?.widgetList[0];
  if (!widget) {
    ElMessage.warning("没有表单数据不能添加!");
    return;
  }
  list.value.push({
    field: widget.options.name,
    compare: "==",
    value: "",
    logical: "&&"
  });
};

/**
 * 删除
 * @param index 下标
 */
const handleDelete = (index: number) => {
  list.value.splice(index, 1);
  if (list.value.length === 0) {
    setExpression();
  }
};

/**
 * 获取字段类型
 * @param field 字段名称
 */
const getFieldType = computed(() => (field: string): WidgetType | undefined => {
  setExpression();
  const input = widgetTypes.find((t) => t.name === "input");
  for (let i = 0; i < nodeWidgets.value.length; i++) {
    const item = nodeWidgets.value[i];
    for (let j = 0; j < item.widgetList.length; j++) {
      const widget = item.widgetList[j];
      // 找出控件
      if (widget.options.name === field) {
        const widgetType = widgetTypes.find((t) => t.name == widget.type);
        // 如果未知控件直接返回 input
        if (!widgetType) return JSON.parse(JSON.stringify(input));
        if (widgetType.type === "Array") {
          widgetType.value = widget.options.optionItems;
        }
        return widgetType;
      }
    }
  }
  return JSON.parse(JSON.stringify(input));
});

/**
 * 设置表达式
 */
const setExpression = debounce(() => {
  if (list.value.length == 0) {
    expression.value = "";
    CU.setConditionExpressionValue(scopedElement, undefined);
    return;
  }
  expression.value = "${";
  for (let i = 0; i < list.value.length; i++) {
    const element = list.value[i];
    // 如果当前不是第一个就添加 ‘逻辑运算符’
    if (i !== 0) expression.value += ` ${element.logical} `;
    // 添加表达式
    let value = `${element.value}`;
    if (typeof element.value === "string") {
      value = `"${element.value}"`;
    }
    expression.value += `${element.field} ${element.compare} ${value}`;
  }
  expression.value += "}";
  CU.setConditionExpressionValue(scopedElement, expression.value);
}, 100);

/**
 * 回显数据
 */
const getExpression = () => {
  if (!conditionData.value.expression) {
    return;
  }
  const expression: string = conditionData.value.expression.replace(/^\${|\}$/g, "");
  const data = ` ${expression}`.split(" ");
  const len = data.length / 4;
  let dataList: any[] = [];
  for (let i = 0; i < len; i++) {
    const value = data[i * 4 + 3];
    dataList.push({
      logical: data[i * 4 + 0],
      field: data[i * 4 + 1],
      compare: data[i * 4 + 2],
      value: Number.parseInt(value) ? Number.parseInt(value) : value.replaceAll('"', "")
    });
  }
  list.value = dataList;
};

/**
 * 初始化
 */
EventBus.on("element-init", function () {
  scopedElement = modeler.getActive;
  // 获取表单数据
  getElementData();
  nodeWidgets.value = getWidgetTree();
  // 获取表达式数据
  getExpression();
});
</script>
