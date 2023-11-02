import { Element } from "diagram-js/lib/model/Types";
import modelerStore from "@/components/bpmnJs/store/modeler";
import { isIdValid } from "@/components/bpmnJs/utils//BpmnValidator";
import { ElMessage } from "element-plus";

export function getIdValue(element: Element): string {
  return element.businessObject.id;
}

export function setIdValue(element: Element, value: string) {
  const errorMsg = isIdValid(element.businessObject, value);

  if (errorMsg && errorMsg.length) {
    return ElMessage.warning(errorMsg);
  }

  const store = modelerStore();
  const modeling = store.getModeling;

  modeling.updateProperties(element, {
    id: value
  });
}
