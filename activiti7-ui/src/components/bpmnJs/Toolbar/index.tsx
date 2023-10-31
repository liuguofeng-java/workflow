import { defineComponent } from "vue";
import { NButtonGroup } from "naive-ui";
import Imports from "@/components/bpmnJs/Toolbar/components/Imports";
import Exports from "@/components/bpmnJs/Toolbar/components/Exports";
import Previews from "@/components/bpmnJs/Toolbar/components/Previews";
import Aligns from "@/components/bpmnJs/Toolbar/components/Aligns";
import Scales from "@/components/bpmnJs/Toolbar/components/Scales";
import Commands from "@/components/bpmnJs/Toolbar/components/Commands";
import ExternalTools from "@/components/bpmnJs/Toolbar/components/ExternalTools";

const Toolbar = defineComponent({
  name: "ToolBar",
  setup() {
    return () => (
      <div class="toolbar">
        <NButtonGroup>
          <Imports></Imports>
          <Exports></Exports>
          <Previews></Previews>
        </NButtonGroup>
        <Aligns></Aligns>
        <Scales></Scales>
        <Commands></Commands>
        <ExternalTools></ExternalTools>
      </div>
    );
  }
});

export default Toolbar;
