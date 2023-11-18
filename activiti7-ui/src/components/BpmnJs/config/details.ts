import { EditorSettings } from "@/components/BpmnJs/types/editor/settings";
import { defaultLang } from "@/components/BpmnJs/i18n";

export const defaultSettings: EditorSettings = {
  language: defaultLang,
  processId: `Process_${new Date().getTime()}`,
  processName: `新建流程`,
  processEngine: "activiti",
  paletteMode: "custom",
  penalMode: "custom",
  contextPadMode: "custom",
  rendererMode: "default",
  bg: "grid",
  toolbar: false,
  miniMap: false,
  contextmenu: false,
  customContextmenu: false,
  otherModule: true,
  templateChooser: false,
  useLint: false,
  customTheme: {},
  isLabelEditingProvider: false,
  isMove: false
};
