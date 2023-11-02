import { ElementLike } from "diagram-js/lib/core";

declare global {
  interface Window {
    bpmnInstances: any;
    __messageBox: MessageApiInjection;
  }

  type BpmnElement = ElementLike & { type: string };
}

declare interface Window {
  bpmnInstances: any;
}
