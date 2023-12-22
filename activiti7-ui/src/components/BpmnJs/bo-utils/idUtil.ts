import { Element } from "diagram-js/lib/model/Types";
import modelerStore from "@/store/modeler";

export function getIdValue(element: Element): string {
  return element.businessObject.id;
}

export function setIdValue(element: Element, value: string) {
  const store = modelerStore();
  const modeling = store.getModeling;

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  modeling.updateProperties(element, {
    id: value
  });
}
