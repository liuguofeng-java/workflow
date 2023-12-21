import { defineComponent } from "vue";
import Imports from "@/components/BpmnJs/components/Toolbar/components/Imports";
import Previews from "@/components/BpmnJs/components/Toolbar/components/Previews";
import Scales from "@/components/BpmnJs/components/Toolbar/components/Scales";
import Commands from "@/components/BpmnJs/components/Toolbar/components/Commands";

const Toolbar = defineComponent({
  name: "ToolBar",
  setup() {
    return () => (
      <div class="toolbar">
        <Imports class="room"></Imports>
        <Previews class="room"></Previews>
        <Scales class="room"></Scales>
        <Commands class="room"></Commands>
      </div>
    );
  }
});

export default Toolbar;
