declare interface BpmnField {
  name: string;
  expression?: string;
  string?: string;
  type?: "string" | "expression";
}

declare interface BpmnExtensionElements {
  values: any[];
}

declare interface BpmnExecutionListener {
  name: string;
  event: string;
  expression?: string;
  class?: string;
  delegateExpression?: string;
  fields?: BpmnField[];
}

declare interface BpmnExtensionProperty {
  id?: string;
  name?: string;
  value?: string;
}

declare interface BpmnExtensionProperties {
  values: BpmnExtensionProperty[];
}
