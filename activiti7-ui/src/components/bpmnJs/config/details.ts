import { EditorSettings } from "@/components/bpmnJs/types/editor/settings";
import { defaultLang } from "@/components/bpmnJs/i18n";

undefined;
export const defaultSettings: EditorSettings = {
  language: defaultLang,
  processId: `Process_${new Date().getTime()}`,
  processName: `业务流程`,
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
  customTheme: {}
};
