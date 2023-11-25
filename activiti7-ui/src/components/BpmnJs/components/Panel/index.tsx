import { defineComponent, Component, markRaw, onMounted, ref } from "vue";
import { Element, Connection, Label, Shape } from "diagram-js/lib/model/Types";
import debounce from "lodash.debounce";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import modelerStore from "@/components/BpmnJs/store/modeler";

import getBpmnIconType from "@/components/BpmnJs/bpmn-icons/getIconType";
import bpmnIcons from "@/components/BpmnJs/bpmn-icons";
import BpmnIcon from "./components/SubChild/BpmnIcon.vue";

import { isAsynchronous } from "@/components/BpmnJs/bo-utils/asynchronousContinuationsUtil";
import { isExecutable } from "@/components/BpmnJs/bo-utils/executionListenersUtil";
import { isJobExecutable } from "@/components/BpmnJs/bo-utils/jobExecutionUtil";
import {
  isStartInitializable,
  isUserAssignmentSupported
} from "@/components/BpmnJs/bo-utils/initiatorUtil";

import ElementGenerations from "./components/ElementGenerations.vue";
import ElementConditional from "./components/ElementConditional.vue";
import ElementExecutionListeners from "./components/ElementExecutionListeners.vue";
import ElementExtensionProperties from "./components/ElementExtensionProperties.vue";
import ElementAsyncContinuations from "./components/ElementAsyncContinuations.vue";
import ElementJobExecution from "./components/ElementJobExecution.vue";
import ElementStartInitiator from "./components/ElementStartInitiator.vue";
import ElementForm from "./components/ElementForm.vue";

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

    const bpmnIconName = ref<string>("Process");
    const bpmnElementName = ref<string>("Process");
    const renderComponents = markRaw<Component[]>([]);

    const activeNames = ref("NormalInfo");

    const setCurrentComponents = (element: BpmnElement) => {
      // 清空
      renderComponents.splice(0, renderComponents.length);
      renderComponents.push(ElementGenerations);
      isCanbeConditional(element) && renderComponents.push(ElementConditional);
      isJobExecutable(element) && renderComponents.push(ElementJobExecution);
      renderComponents.push(ElementExtensionProperties);
      isAsynchronous(element) && renderComponents.push(ElementAsyncContinuations);
      isStartInitializable(element) && renderComponents.push(ElementStartInitiator);
      isUserAssignmentSupported(element) && renderComponents.push(UserAssignment);
      isUserAssignmentSupported(element) && renderComponents.push(ElementForm);
      isExecutable(element) && renderComponents.push(ElementExecutionListeners);
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
          return console.log("No Element found!");
        }
      }
      activatedElementTypeName = getBpmnIconType(activatedElement);

      modeler.setElement(markRaw(activatedElement));
      currentElementId.value = activatedElement.id;
      currentElementType.value = activatedElement.type.split(":")[1];

      bpmnIconName.value = bpmnIcons[activatedElementTypeName];
      bpmnElementName.value = activatedElementTypeName;

      setCurrentComponents(activatedElement);
      EventBus.emit("element-update", activatedElement);
      activatedId = activatedElement.id;
    }, 100);

    EventBus.on("modeler-init", (modeler) => {
      // 导入完成后默认选中 process 节点
      modeler.on("import.done", () => {
        setCurrentElement(null);
      });
      // 监听选择事件，修改当前激活的元素以及表单
      modeler.on("selection.changed", ({ newSelection }) => {
        if (newSelection[0]) {
          setTimeout(() => {
            EventBus.emit("element-init", modeler);
          }, 200);
        }
        setCurrentElement(newSelection[0] || null);
      });
      // 节点表单修改时触发
      modeler.on("element.changed", ({ element }) => {
        // 保证 修改 "默认流转路径" 等类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
        if (element && element.id === currentElementId.value) {
          setCurrentElement(element);
        }
      });
      // 点击节点触发
      // modeler.on("element.click", (event) => {
      //   console.log("Element Click", event);
      // });
    });

    onMounted(() => !currentElementId.value && setCurrentElement(null));

    return () => (
      <div ref={panel} class="panel">
        <div class="panel-header">
          <BpmnIcon name={bpmnIconName.value}></BpmnIcon>
          <div class="panel-title">
            <p>{bpmnElementName.value}</p>
            <p>{customTranslate(currentElementType.value || "Process")}</p>
          </div>
        </div>

        <el-collapse v-model={activeNames.value}>
          {renderComponents.map((component) => (
            <component is={component}></component>
          ))}
        </el-collapse>
      </div>
    );
  }
});

export default Panel;
