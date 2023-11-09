import { defineComponent, Component, markRaw, onMounted, ref } from "vue";
import { Element, Connection, Label, Shape } from "diagram-js/lib/model/Types";
import { Translate } from "diagram-js/lib/i18n/translate";
import debounce from "lodash.debounce";

import EventEmitter from "@/components/bpmnJs/utils//EventEmitter";
import modelerStore from "@/components/BpmnJs/store/modeler";
import Logger from "@/components/BpmnJs/utils/Logger";

import getBpmnIconType from "@/components/BpmnJs/bpmn-icons/getIconType";
import bpmnIcons from "@/components/BpmnJs/bpmn-icons";

import { isAsynchronous } from "@/components/BpmnJs/bo-utils/asynchronousContinuationsUtil";
import { isExecutable } from "@/components/BpmnJs/bo-utils/executionListenersUtil";
import { isJobExecutable } from "@/components/BpmnJs/bo-utils/jobExecutionUtil";
import {
  isStartInitializable,
  isUserAssignmentSupported
} from "@/components/BpmnJs/bo-utils/initiatorUtil";

import ElementGenerations from "./components/ElementGenerations.vue";
import ElementConditional from "./components/ElementConditional.vue";
import ElementDocumentations from "./components/ElementDocumentations.vue";
import ElementExecutionListeners from "./components/ElementExecutionListeners.vue";
import ElementExtensionProperties from "./components/ElementExtensionProperties.vue";
import ElementAsyncContinuations from "./components/ElementAsyncContinuations.vue";
import ElementJobExecution from "./components/ElementJobExecution.vue";
import ElementStartInitiator from "./components/ElementStartInitiator.vue";

import UserAssignment from "./components/UserAssignment.vue";

import { isCanbeConditional } from "@/components/BpmnJs/bo-utils/conditionUtil";
import { customTranslate } from "@/components/BpmnJs/additional-modules/Translate";

const Panel = defineComponent({
  name: "PropertiesPanel",
  setup() {
    const modeler = modelerStore();
    const panel = ref<HTMLDivElement | null>(null);
    const currentElementId = ref<string | undefined>(undefined);
    const currentElementType = ref<string | undefined>(undefined);

    const penalTitle = ref<string | undefined>("属性配置");
    const bpmnIconName = ref<string>("Process");
    const bpmnElementName = ref<string>("Process");

    const renderComponents = markRaw<Component[]>([]);

    const activeNames = ref("NormalInfo");

    const setCurrentComponents = (element: BpmnElement) => {
      // 清空
      renderComponents.splice(0, renderComponents.length);
      renderComponents.push(ElementGenerations);
      renderComponents.push(ElementDocumentations);
      isCanbeConditional(element) && renderComponents.push(ElementConditional);
      isJobExecutable(element) && renderComponents.push(ElementJobExecution);
      renderComponents.push(ElementExtensionProperties);
      isExecutable(element) && renderComponents.push(ElementExecutionListeners);
      isAsynchronous(element) && renderComponents.push(ElementAsyncContinuations);
      isStartInitializable(element) && renderComponents.push(ElementStartInitiator);
      isUserAssignmentSupported(element) && renderComponents.push(UserAssignment);
    };

    // 记录前一个节点id
    let activatedId = null;
    // 设置选中元素，更新 store
    const setCurrentElement = debounce((element: Shape | Element | Connection | Label | null) => {
      let activatedElement: BpmnElement | undefined = element;
      let activatedElementTypeName = "";

      // 如果切换节点,就收起 el-collapse
      if (activatedElement) {
        if (activatedId != null && activatedId != activatedElement.id) {
          activeNames.value = "NormalInfo";
        }
      }

      if (!activatedElement) {
        activatedElement =
          modeler.getElRegistry?.find((el) => el.type === "bpmn:Process") ||
          modeler.getElRegistry?.find((el) => el.type === "bpmn:Collaboration");

        if (!activatedElement) {
          return Logger.prettyError("No Element found!");
        }
      }
      activatedElementTypeName = getBpmnIconType(activatedElement);

      modeler.setElement(markRaw(activatedElement));
      currentElementId.value = activatedElement.id;
      currentElementType.value = activatedElement.type.split(":")[1];

      penalTitle.value = modeler.getModeler?.get<Translate>("translate")(currentElementType.value);
      bpmnIconName.value = bpmnIcons[activatedElementTypeName];
      bpmnElementName.value = activatedElementTypeName;

      setCurrentComponents(activatedElement);
      EventEmitter.emit("element-update", activatedElement);

      Logger.prettyPrimary(
        "Selected element changed",
        `ID: ${activatedElement.id} , type: ${activatedElement.type}`
      );
      activatedId = activatedElement.id;
    }, 100);

    EventEmitter.on("modeler-init", (modeler) => {
      // 导入完成后默认选中 process 节点
      modeler.on("import.done", () => {
        setCurrentElement(null);
      });
      // 监听选择事件，修改当前激活的元素以及表单
      modeler.on("selection.changed", ({ newSelection }) => {
        setCurrentElement(newSelection[0] || null);
      });
      modeler.on("element.changed", ({ element }) => {
        // 保证 修改 "默认流转路径" 等类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
        if (element && element.id === currentElementId.value) {
          setCurrentElement(element);
        }
      });

      modeler.on("element.click", (event) => {
        Logger.prettyInfo("Element Click", event);
      });
    });

    onMounted(() => !currentElementId.value && setCurrentElement(null));

    return () => (
      <div ref={panel} class="panel">
        <div class="panel-header">
          <p>{bpmnElementName.value}</p>
          <p>{customTranslate(currentElementType.value || "Process")}</p>
        </div>

        <el-tabs v-model={activeNames.value}>
          {renderComponents.map((component) => (
            <component is={component}></component>
          ))}
        </el-tabs>
      </div>
    );
  }
});

export default Panel;
