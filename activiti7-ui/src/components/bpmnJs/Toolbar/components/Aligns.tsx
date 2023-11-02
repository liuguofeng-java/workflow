import { computed, ComputedRef, defineComponent } from "vue";
import Modeler from "bpmn-js/lib/Modeler";
import Selection from "diagram-js/lib/features/selection/Selection";
import Modeling from "bpmn-js/lib/features/modeling/Modeling.js";
import EventEmitter from "@/components/bpmnJs/utils//EventEmitter";
import { ElMessage } from "element-plus";

const Aligns = defineComponent({
  name: "Aligns",
  setup() {
    const buttons: ComputedRef<{ name: string; key: string; icon: string }[]> = computed(() => {
      return [
        { name: "左对齐", key: "left", icon: "AlignStartVertical" },
        { name: "水平居中", key: "center", icon: "AlignCenterVertical" },
        { name: "右对齐", key: "right", icon: "AlignEndVertical" },
        { name: "上对齐", key: "top", icon: "AlignStartHorizontal" },
        { name: "垂直居中", key: "middle", icon: "AlignCenterHorizontal" },
        { name: "下对齐", key: "bottom", icon: "AlignEndHorizontal" }
      ];
    });

    let modeling: Modeling | null = null;
    let selection: Selection | null = null;
    let align: any = null;

    EventEmitter.on("modeler-init", (modeler: Modeler) => {
      modeling = modeler.get("modeling");
      selection = modeler.get("selection");
      align = modeler.get("alignElements");
    });

    const alignElements = (tag: string) => {
      if (modeling && selection) {
        const SelectedElements = selection.get();
        if (!SelectedElements || SelectedElements.length <= 1) {
          return ElMessage.warning("请按住 Shift 键选择多个元素对齐");
        }
        align.trigger(SelectedElements, tag);
      }
    };

    return () => (
      <el-button-group>
        {buttons.value.map((item) => {
          return <el-button onClick={() => alignElements(item.key)}>{item.name}</el-button>;
        })}
      </el-button-group>
    );
  }
});

export default Aligns;
