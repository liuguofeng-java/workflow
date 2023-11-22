// 详情专业
import { defineComponent, ref, toRefs, nextTick, watch } from "vue";
import type { PropType } from "vue";

import modulesAndModdle from "@/components/BpmnJs/components/Designer/modulesAndModdle";
import initModeler from "@/components/BpmnJs/components/Designer/initModeler";
import { createNewDiagram } from "src/components/BpmnJs/utils";
import { defaultSettings } from "@/components/BpmnJs/config/details";

const Designer = defineComponent({
  name: "BpmnDesigner",
  props: {
    xml: {
      type: String as PropType<string>,
      default: undefined
    }
  },
  emits: ["update:xml", "command-stack-changed"],
  setup(props, { emit }) {
    const editorSettings = ref(defaultSettings);
    const { xml } = toRefs(props);
    const designer = ref<HTMLDivElement | null>(null);

    watch(
      () => editorSettings.value,
      async () => {
        const modelerModules = modulesAndModdle(editorSettings);
        await nextTick();
        await initModeler(designer, modelerModules, emit);
        await createNewDiagram(xml.value, editorSettings.value);
      },
      { deep: true, immediate: true }
    );

    return () => <div ref={designer} class="designer"></div>;
  }
});

export default Designer;
