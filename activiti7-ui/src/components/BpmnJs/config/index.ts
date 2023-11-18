import { EditorSettings } from "@/components/BpmnJs/types/editor/settings";
import { defaultLang } from "@/components/BpmnJs/i18n";

export const defaultSettings: EditorSettings = {
  language: defaultLang,
  processId: `Process_${new Date().getTime()}`,
  processName: `新建流程`,
  processEngine: "activiti",
  paletteMode: "rewrite",
  penalMode: "custom",
  contextPadMode: "rewrite",
  rendererMode: "default",
  bg: "grid",
  toolbar: true,
  miniMap: false,
  contextmenu: false,
  customContextmenu: false,
  otherModule: true,
  templateChooser: true,
  useLint: false,
  customTheme: {},
  isLabelEditingProvider: true,
  isMove: true
};
