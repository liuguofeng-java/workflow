import { defineStore } from "pinia";
import { defaultSettings } from "@/components/BpmnJs/config";
import { EditorSettings } from "@/components/BpmnJs/types/editor/settings";

const state = {
  editorSettings: defaultSettings
};

export default defineStore("editor", {
  state: () => state,
  getters: {
    getProcessEngine: (state): EditorSettings["processEngine"] => state.editorSettings.processEngine
  }
});
