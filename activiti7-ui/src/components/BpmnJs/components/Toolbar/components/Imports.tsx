import { defineComponent, ref } from "vue";
import modeler from "@/components/BpmnJs/store/modeler";

const Imports = defineComponent({
  name: "Imports",
  setup() {
    const modelerStore = modeler();
    const importRef = ref<HTMLInputElement | null>(null);

    const openImportWindow = () => {
      importRef.value && importRef.value.click();
    };

    const changeImportFile = () => {
      if (importRef.value && importRef.value.files) {
        const file = importRef.value.files[0];
        const reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function () {
          const xmlStr = this.result;
          modelerStore.getModeler!.importXML(xmlStr as string);
        };
        importRef.value.value = "";
        importRef.value.files = null;
      }
    };

    return () => (
      <span>
        <el-button onClick={openImportWindow}>打开文件</el-button>
        <input
          type="file"
          ref={importRef}
          style="display: none"
          accept=".xml,.bpmn"
          onChange={changeImportFile}
        ></input>
      </span>
    );
  }
});

export default Imports;
