import { type Ref } from "vue";
import type { ModuleDeclaration } from "didi";
import type { EditorSettings } from "@/components/BpmnJs/types/editor/settings";
import modeler from "@/store/modeler";

// 官方网格背景
import GridLineModule from "diagram-js-grid-bg";

// ** 官方流程模拟 module
import TokenSimulationModule from "bpmn-js-token-simulation";

// moddle 定义文件
import activitiModdleDescriptors from "@/components/BpmnJs/config/activiti.json";
import flowableModdleDescriptors from "@/components/BpmnJs/config/flowable.json";

// 自定义 modules 扩展模块
import translate from "@/components/BpmnJs/overwrite-modules/Translate";
import ElementFactory from "@/components/BpmnJs/overwrite-modules/ElementFactory";
import RewritePalette from "@/components/BpmnJs/overwrite-modules/Palette";
import RewriteContextPad from "@/components/BpmnJs/overwrite-modules/ContextPad";

// 流程图校验部分
import lintModule from "bpmn-js-bpmnlint";
import bpmnlint from "@/components/BpmnJs/overwrite-modules/Lint/bpmnlint";

export type ModulesAndModdles = [
  ModuleDeclaration[],
  { [key: string]: any },
  { [key: string]: unknown }
];

export default function (settings: Ref<EditorSettings>): ModulesAndModdles {
  const modules: ModuleDeclaration[] = []; // modules 扩展模块数组
  const moddle: { [key: string]: any } = {}; // moddle 声明文件对象
  const options: { [key: string]: unknown } = {}; // modeler 其他配置

  // 配置 palette (可覆盖 paletteProvider 取消原生侧边栏菜单)
  settings.value.paletteMode === "rewrite" && modules.push(RewritePalette);
  settings.value.paletteMode === "custom" &&
    modules.push({
      paletteProvider: [
        "type",
        function () {
          return {};
        }
      ]
    });

  // 配置 contextPad (可覆盖 contextPadProvider 取消原生上下文菜单)
  settings.value.contextPadMode === "rewrite" && modules.push(RewriteContextPad);
  settings.value.contextPadMode === "custom" &&
    modules.push({
      contextPadProvider: [
        "type",
        function () {
          return {};
        }
      ]
    });

  // 是否双击编辑
  !settings.value.isLabelEditingProvider &&
    modules.push({
      labelEditingProvider: [
        "type",
        function () {
          return {};
        }
      ]
    });

  // 是否禁止拖动
  !settings.value.isMove &&
    modules.push({
      move: [
        "type",
        function () {
          return {};
        }
      ],
      bendpoints: [
        "type",
        function () {
          return {};
        }
      ]
    });

  // 设置 lint 校验
  if (settings.value.useLint) {
    modules.push(lintModule);
    options["linting"] = {
      active: true,
      bpmnlint: bpmnlint
    };
  }

  // 设置键盘事件绑定
  options["keyboard"] = {
    bindTo: document
  };

  modules.push(ElementFactory);
  options["elementFactory"] = {
    "bpmn:Task": { width: 120, height: 120 },
    "bpmn:SequenceFlow": { width: 100, height: 80 }
  };

  // 官方流程模拟 module
  modules.push(TokenSimulationModule);

  // 官方网格背景
  modules.push(GridLineModule);

  // 配置 翻译 与 流程模拟
  modules.push(translate);

  // 设置对应的 moddle 解析配置文件
  if (settings.value.processEngine === "activiti") moddle["activiti"] = activitiModdleDescriptors;
  if (settings.value.processEngine === "flowable") moddle["flowable"] = flowableModdleDescriptors;
  modeler().setProcessEngine(settings.value.processEngine);

  return [modules, moddle, options];
}
