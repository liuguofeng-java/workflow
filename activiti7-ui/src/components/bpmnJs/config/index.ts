import { EditorSettings } from "@/components/bpmnJs/types/editor/settings";
import { defaultLang } from "@/components/bpmnJs/i18n";

export const defaultSettings: EditorSettings = {
  language: defaultLang,
  processId: `Process_${new Date().getTime()}`,
  processName: `业务流程`,
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
  customTheme: {}
};