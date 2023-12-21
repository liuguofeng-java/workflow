import { Element, Connection } from "bpmn-js/lib/model/Types";
import { ModdleElement } from "bpmn-moddle";
import { getBusinessObject, is, isAny } from "bpmn-js/lib/util/ModelUtil";
import { getEventDefinition } from "@/components/BpmnJs/utils/BpmnImplementationType";
import modeler from "@/store/modeler";
import { createModdleElement } from "@/components/BpmnJs/utils/BpmnExtensionElementsUtil";

///////////////////////////////// 配置项可见性
const CONDITIONAL_SOURCES = [
  "bpmn:Activity",
  "bpmn:ExclusiveGateway",
  "bpmn:InclusiveGateway",
  "bpmn:ComplexGateway"
];
const defaultConditionTypeOptions: Record<string, string>[] = [
  { label: "无条件", value: "none" },
  { label: "默认路径", value: "default" },
  { label: "条件表达式", value: "expression" }
];
// 父节点符合条件的连线
export function isConditionalSource(element) {
  return isAny(element, CONDITIONAL_SOURCES);
}
// 是否是 定义条件的事件 （ 控制变量 Variables 配置 ）
export function isConditionEventDefinition(element: Element): boolean {
  return (
    is(element, "bpmn:Event") && !!getEventDefinition(element, "bpmn:ConditionalEventDefinition")
  );
}
// 元素是否开始节点
export function isExtendStartEvent(element: Element): boolean {
  return is(element, "bpmn:StartEvent");
}
// 元素 是否符合 可以设置条件 的情况
export function isCanbeConditional(element: BpmnElement): boolean {
  return (
    (is(element, "bpmn:SequenceFlow") && isConditionalSource((element as Connection)?.source)) ||
    isConditionEventDefinition(element)
  );
}

///////////////////////////
// 1. 条件变量部分
export function getVariableNameValue(element: Element): string | undefined {
  if (getConditionalEventDefinition(element)) {
    return (getConditionalEventDefinition(element) as ModdleElement).get("variableName");
  }
}
export function setVariableNameValue(element: Element, value: string | undefined) {
  const modeling = modeler().getModeling;
  const eventDefinition = getConditionalEventDefinition(element);
  if (eventDefinition) {
    modeling.updateModdleProperties(element, eventDefinition, { variableName: value || "" });
  }
}

// 2. 条件事件部分
export function getVariableEventsValue(element: Element): string | undefined {
  if (getConditionalEventDefinition(element)) {
    return (getConditionalEventDefinition(element) as ModdleElement).get("variableEvents");
  }
}
export function setVariableEventsValue(element: Element, value: string | undefined) {
  const modeling = modeler().getModeling;
  const eventDefinition = getConditionalEventDefinition(element);
  if (eventDefinition) {
    modeling.updateModdleProperties(element, eventDefinition, { variableName: value || "" });
  }
}

// 3. 元素条件类型
export function getConditionTypeValue(element: Element): string {
  const conditionExpression = getConditionExpression(element);
  if (conditionExpression) {
    return conditionExpression.get("language") === undefined ? "expression" : "script";
  }
  if (element.source?.businessObject?.default === element.businessObject) return "default";
  return "none";
}
export function setConditionTypeValue(element: Element, value: string) {
  if (!value || value === "none" || value === "default") {
    updateCondition(element);
    return setDefaultCondition(element as Connection, value === "default");
  }
  const attributes = {
    language: value === "script" ? "" : undefined
  };
  const parent = is(element, "bpmn:SequenceFlow")
    ? getBusinessObject(element)
    : (getConditionalEventDefinition(element) as ModdleElement);
  const formalExpressionElement = createModdleElement("bpmn:FormalExpression", attributes, parent);
  updateCondition(element, formalExpressionElement);
}

// 4. 元素条件表达式
export function getConditionExpressionValue(element: Element): string | undefined {
  const conditionExpression = getConditionExpression(element);
  if (conditionExpression) {
    return conditionExpression.get("body");
  }
}
export function setConditionExpressionValue(element: Element, body: string | undefined) {
  const parent = is(element, "bpmn:SequenceFlow")
    ? getBusinessObject(element)
    : (getConditionalEventDefinition(element) as ModdleElement);
  const formalExpressionElement = createModdleElement("bpmn:FormalExpression", { body }, parent);
  updateCondition(element, formalExpressionElement);
}

///////// helpers
// 获取事件的条件定义
export function getConditionTypeOptions(element: Element): Record<string, string>[] {
  if (is(element, "bpmn:SequenceFlow")) {
    return defaultConditionTypeOptions;
  }
  return defaultConditionTypeOptions.filter((condition) => condition.value !== "default");
}
function getConditionalEventDefinition(
  element: Element | ModdleElement
): ModdleElement | false | undefined {
  if (!is(element, "bpmn:Event")) return false;
  return getEventDefinition(element, "bpmn:ConditionalEventDefinition");
}
//获取给定元素的条件表达式的值
function getConditionExpression(element: Element | ModdleElement): ModdleElement | undefined {
  const businessObject = getBusinessObject(element);
  if (is(businessObject, "bpmn:SequenceFlow")) {
    return businessObject.get("conditionExpression");
  }
  if (getConditionalEventDefinition(businessObject)) {
    return (getConditionalEventDefinition(businessObject) as ModdleElement).get("condition");
  }
}
//
function updateCondition(element: Element, condition?: string | ModdleElement) {
  const modeling = modeler().getModeling;
  if (is(element, "bpmn:SequenceFlow")) {
    modeling.updateProperties(element, { conditionExpression: condition });
  } else {
    modeling.updateModdleProperties(element, getConditionalEventDefinition(element), { condition });
  }
}
//
function setDefaultCondition(element: Connection, isDefault: boolean) {
  const modeling = modeler().getModeling;
  console.log(modeling);
  console.log(element, element.source);

  modeling.updateProperties(element.source as Element, {
    default: isDefault ? element : undefined
  });
}
