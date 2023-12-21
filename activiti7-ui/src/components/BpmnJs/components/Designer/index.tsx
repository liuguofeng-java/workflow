// 详情专业
import { defineComponent, ref, toRefs, nextTick, watch } from "vue";
import type { PropType } from "vue";

import modulesAndModdle from "@/components/BpmnJs/components/Designer/modulesAndModdle";
import initModeler from "@/components/BpmnJs/components/Designer/initModeler";
import { createNewDiagram } from "src/components/BpmnJs/utils";
import { EditorSettings } from "@/components/BpmnJs/types/editor/settings";
const Designer = defineComponent({
  name: "BpmnDesigner",
  props: {
    xml: {
      type: String as PropType<string>,
      default: undefined
    },
    settings: {
      type: Object,
      default: undefined
    }
  },
  emits: ["update:xml", "command-stack-changed"],
  setup(props, { emit }) {
    const { xml } = toRefs(props);
    const designer = ref<HTMLDivElement | null>(null);
    watch(
      () => xml.value,
      async (value) => {
        if (!value) return;
        const settings = ref(props.settings as EditorSettings);
        const modelerModules = modulesAndModdle(settings);
        await nextTick();
        await initModeler(designer, modelerModules, emit);
        await createNewDiagram(xml.value, settings.value);
      },
      { deep: true, immediate: true }
    );

    return () => <div ref={designer} class="designer"></div>;
  }
});

export default Designer;
