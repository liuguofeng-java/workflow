import {
  defineComponent,
  Component,
  markRaw,
  onMounted,
  onBeforeUnmount,
  ref,
  getCurrentInstance
} from "vue";
import { Element, Connection, Label, Shape } from "diagram-js/lib/model/Types";
import debounce from "lodash.debounce";
import EventBus from "@/utils/EventBus";
import modelerStore from "@/components/BpmnJs/store/modeler";

import getBpmnIconType from "@/components/BpmnJs/bpmn-icons/getIconType";
import bpmnIcons from "@/components/BpmnJs/bpmn-icons";
import BpmnIcon from "./components/SubChild/BpmnIcon.vue";

import { isCanbeConditional, isExtendStartEvent } from "@/components/BpmnJs/bo-utils/conditionUtil";
import { customTranslate } from "@/components/BpmnJs/additional-modules/Translate";
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
    const {
      proxy: { $forceUpdate }
    }: any = getCurrentInstance();

    /**
     * 更新组件
     * @param element 当前节点
     */
    const setCurrentComponents = (element: BpmnElement) => {
      // 清空
      renderComponents.splice(0, renderComponents.length);
      // 添加组件
      renderComponents.push(ElementGenerations); // 基本信息
      isExtendStartEvent(element) && renderComponents.push(ElementForm); // 是否是开始节点
      isCanbeConditional(element) && renderComponents.push(ElementConditional);
      isJobExecutable(element) && renderComponents.push(ElementJobExecution);
      renderComponents.push(ElementExtensionProperties);
      isAsynchronous(element) && renderComponents.push(ElementAsyncContinuations);
      isStartInitializable(element) && renderComponents.push(ElementStartInitiator);
      isUserAssignmentSupported(element) && renderComponents.push(UserAssignment);
      isUserAssignmentSupported(element) && renderComponents.push(ElementForm);
      isExecutable(element) && renderComponents.push(ElementExecutionListeners);

      // 发现ElementConditional未更,新强制更新组件
      $forceUpdate();
    };

    /**
     * 设置选中元素，更新 store
     */
    const setCurrentElement = debounce((element: Shape | Element | Connection | Label | null) => {
      let activatedElement: BpmnElement | undefined = element;
      let activatedElementTypeName = "";

      if (!activatedElement) {
        activatedElement = modeler.getElRegistry?.find(
          (el) => el.type === "bpmn:Process" || el.type === "bpmn:Collaboration"
        );

        if (!activatedElement) {
          return console.log("No Element found!");
        }
      }
      if (activatedElement.type === "label") {
        return;
      }

      activatedElementTypeName = getBpmnIconType(activatedElement);

      modeler.setElement(markRaw(activatedElement));
      currentElementId.value = activatedElement.id;
      currentElementType.value = activatedElement.type.split(":")[1];

      bpmnIconName.value = bpmnIcons[activatedElementTypeName];
      bpmnElementName.value = activatedElementTypeName;

      setCurrentComponents(activatedElement);
      EventBus.emit("element-update", activatedElement);
    }, 100);

    /**
     * 初始化
     */
    EventBus.on("modeler-init", (modeler) => {
      // 导入完成后默认选中 process 节点
      modeler.on("import.done", () => {
        setCurrentElement(null);
      });
      // 监听选择事件，修改当前激活的元素以及表单
      modeler.on("selection.changed", ({ newSelection }) => {
        setCurrentElement(newSelection[0] || null);
        setTimeout(() => {
          EventBus.emit("element-init", modeler);
        }, 200);
      });
      // 节点表单修改时触发
      modeler.on("element.changed", ({ element }) => {
        // 保证 修改 "默认流转路径" 等类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
        if (element && element.id === currentElementId.value) {
          setCurrentElement(element);
        }
      });
    });

    /**
     * 初始化
     */
    onMounted(() => {
      !currentElementId.value && setCurrentElement(null);
    });

    /**
     * 销毁事件，防止重复触发
     */
    onBeforeUnmount(() => {
      console.log("------>>>>>?end");
      EventBus.all.clear();
    });

    return () => (
      <div ref={panel} class="panel">
        <div class="panel-header">
          <BpmnIcon name={bpmnIconName.value}></BpmnIcon>
          <div class="panel-title">
            <p>{bpmnElementName.value}</p>
            <p>{customTranslate(currentElementType.value || "Process")}</p>
          </div>
        </div>

        <div>
          {renderComponents.map((component) => (
            <component is={component}></component>
          ))}
        </div>
      </div>
    );
  }
});

export default Panel;
