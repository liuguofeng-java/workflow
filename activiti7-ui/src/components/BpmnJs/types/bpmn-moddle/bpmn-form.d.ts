declare interface ExecutionListenerForm extends BpmnExecutionListener {
  type: string;
}

declare interface FormItemVisible {
  listenerType: string;
  scriptType: string;
}

declare interface ConditionalForm {
  conditionType?: string;
  expression?: string;
  scriptType?: string;
  language?: string;
  body?: string;
  resource?: string;
}
