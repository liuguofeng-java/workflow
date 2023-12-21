import { ViewerOptions } from "diagram-js/lib/model/Types";
import { ModuleDeclaration } from "didi";

export interface EditorSettings {
  processName: string;
  processId: string;
  processEngine: "flowable" | "activiti";
  paletteMode: "custom" | "rewrite";
  contextPadMode: "rewrite" | "custom";
  useLint: boolean;
  isLabelEditingProvider: boolean;
  isMove: boolean;
}

export type ModelerOptions<E extends Element> = ViewerOptions<E> & {
  additionalModules: ModuleDeclaration[];
  moddleExtensions: object;
};

// bpmn.js 事件参数
// 1. canvas 事件
type CanvasEventParams = {
  svg: SVGElement;
  viewport: SVGElement;
};
