import { defineComponent } from "vue";
import Imports from "@/components/BpmnJs/components/Toolbar/components/Imports";
import Exports from "@/components/BpmnJs/components/Toolbar/components/Exports";
import Previews from "@/components/BpmnJs/components/Toolbar/components/Previews";
import Aligns from "@/components/BpmnJs/components/Toolbar/components/Aligns";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";

const Toolbar = defineComponent({
  name: "ToolBar",
  setup() {
    return () => (
      <div class="toolbar">
        <Imports class="room"></Imports>
        <Exports class="room"></Exports>
        <Previews class="room"></Previews>
        <Aligns class="room"></Aligns>
        <Scales class="room"></Scales>
        <Commands class="room"></Commands>
      </div>
    );
  }
});

export default Toolbar;
