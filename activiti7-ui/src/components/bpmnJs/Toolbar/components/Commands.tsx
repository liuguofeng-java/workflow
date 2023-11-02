import { defineComponent } from "vue";
import EventEmitter from "@/components/bpmnJs/utils//EventEmitter";
import type Modeler from "bpmn-js/lib/Modeler";
import type CommandStack from "diagram-js/lib/command/CommandStack";
import { createNewDiagram } from "@/components/bpmnJs/utils/";

const Commands = defineComponent({
  name: "Commands",
  setup() {
    let command: CommandStack | null = null;

    EventEmitter.on("modeler-init", (modeler: Modeler) => {
      command = modeler.get<CommandStack>("commandStack");
    });

    const undo = () => {
      command && command.canUndo() && command.undo();
    };

    const redo = () => {
      command && command.canRedo() && command.redo();
    };

    const restart = () => {
      command && command.clear();
      createNewDiagram();
    };

    return () => (
      <el-button-group>
        <el-button onClick={undo}>撤销</el-button>
        <el-button onClick={redo}>恢复</el-button>
        <el-button onClick={restart}>擦除重做</el-button>
      </el-button-group>
    );
  }
});

export default Commands;
