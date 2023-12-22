import modeler from "@/store/modeler";
import { find } from "min-dash";
import { getBusinessObject, is } from "bpmn-js/lib/util/ModelUtil";
import { Element } from "diagram-js/lib/model/Types";
import { ModdleElement } from "bpmn-moddle";

///////////////////////////////////////////// bpmn 基础类方法

// 获取节点事件定义
export function getEventDefinition(
  element: Element | ModdleElement,
  eventType: string
): ModdleElement | undefined {
  const businessObject = getBusinessObject(element);
  const eventDefinitions = businessObject.get("eventDefinitions") || [];
  return find(eventDefinitions, function (definition) {
    return is(definition, eventType);
  });
}
// 获取节点消息事件
export function getMessageEventDefinition(element: Element): ModdleElement | undefined {
  if (is(element, "bpmn:ReceiveTask")) {
    return getBusinessObject(element);
  }
  return getEventDefinition(element, "bpmn:MessageEventDefinition");
}

/////////////////////////////////////////// bpmn 根据流程引擎的扩展方法

// Check whether an element is ServiceTaskLike 检查元素是否为 'ServiceTaskLike'
export function isServiceTaskLike(element: Element | ModdleElement): boolean {
  return is(element, `${modeler().getProcessEngine}:ServiceTaskLike`);
}

/**
 * getServiceTaskLikeBusinessObject
 * 获取一个 'ServiceTaskLike' 业务对象。
 * 如果给定的元素不是 'servicetasklike '，则返回 'false'
 */
export function getServiceTaskLikeBusinessObject(element): ModdleElement | false {
  if (is(element, "bpmn:IntermediateThrowEvent") || is(element, "bpmn:EndEvent")) {
    const messageEventDefinition = getMessageEventDefinition(element);
    if (messageEventDefinition) {
      element = messageEventDefinition;
    }
  }
  return isServiceTaskLike(element) && getBusinessObject(element);
}
