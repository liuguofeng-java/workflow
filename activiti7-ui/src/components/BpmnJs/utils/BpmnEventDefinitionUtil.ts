import { getBusinessObject, is } from "bpmn-js/lib/util/ModelUtil";
import { Element } from "diagram-js/lib/model/Types";
import { ModdleElement } from "bpmn-moddle";
import { find } from "min-dash";


export function getTimerEventDefinition(element: Element): ModdleElement | undefined {
  return getEventDefinition(element, "bpmn:TimerEventDefinition");
}

export function getEventDefinition(element: Element, eventType: string): ModdleElement | undefined {
  const businessObject = getBusinessObject(element);

  const eventDefinitions = businessObject.get("eventDefinitions") || [];

  return find(eventDefinitions, function (definition) {
    return is(definition, eventType);
  });
}

export function getMessageEventDefinition(element: Element): ModdleElement | undefined {
  if (is(element, "bpmn:ReceiveTask")) {
    return getBusinessObject(element);
  }

  return getEventDefinition(element, "bpmn:MessageEventDefinition");
}

export function getMessage(element: Element): ModdleElement | undefined {
  const messageEventDefinition = getMessageEventDefinition(element);

  return messageEventDefinition && messageEventDefinition.get("messageRef");
}

export function getSignalEventDefinition(element: Element): ModdleElement | undefined {
  return getEventDefinition(element, "bpmn:SignalEventDefinition");
}

export function getSignal(element: Element): ModdleElement | undefined {
  const signalEventDefinition = getSignalEventDefinition(element);

  return signalEventDefinition && signalEventDefinition.get("signalRef");
}

export function getEscalationEventDefinition(element: Element): ModdleElement | undefined {
  return getEventDefinition(element, "bpmn:EscalationEventDefinition");
}

export function getEscalation(element: Element): ModdleElement | undefined {
  const escalationEventDefinition = getEscalationEventDefinition(element);

  return escalationEventDefinition && escalationEventDefinition.get("escalationRef");
}
