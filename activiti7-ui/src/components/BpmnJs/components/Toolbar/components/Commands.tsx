import { defineComponent } from "vue";
import EventBus from "@/components/BpmnJs/utils/EventBus";
import type Modeler from "bpmn-js/lib/Modeler";
import type CommandStack from "diagram-js/lib/command/CommandStack";
import { createNewDiagram } from "src/components/BpmnJs/utils";
import { ElMessageBox } from "element-plus";

const Commands = defineComponent({
  name: "Commands",
  setup() {
    let command: CommandStack | null = null;

    EventBus.on("modeler-init", (modeler: Modeler) => {
      command = modeler.get<CommandStack>("commandStack");
    });

    const undo = () => {
      command && command.canUndo() && command.undo();
    };

    const redo = () => {
      command && command.canRedo() && command.redo();
    };

    const restart = async () => {
      await ElMessageBox.confirm("确定要擦除重做吗?", "提示");
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
