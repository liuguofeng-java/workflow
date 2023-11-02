import { defineComponent } from "vue";
import Imports from "@/components/bpmnJs/Toolbar/components/Imports";
import Exports from "@/components/bpmnJs/Toolbar/components/Exports";
import Previews from "@/components/bpmnJs/Toolbar/components/Previews";
import Aligns from "@/components/bpmnJs/Toolbar/components/Aligns";
import Scales from "@/components/bpmnJs/Toolbar/components/Scales";
import Commands from "@/components/bpmnJs/Toolbar/components/Commands";

const Toolbar = defineComponent({
  name: "ToolBar",
  setup() {
    return () => (
      <div class="toolbar">
        <Imports></Imports>
        <Exports></Exports>
        <Previews></Previews>
        <Aligns></Aligns>
        <Scales></Scales>
        <Commands></Commands>
      </div>
    );
  }
});

export default Toolbar;
