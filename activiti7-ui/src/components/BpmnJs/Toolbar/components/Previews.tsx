import { defineComponent, ref } from "vue";

import modeler from "@/components/BpmnJs/store/modeler";
import { ElMessage } from "element-plus";

const Previews = defineComponent({
  name: "Previews",
  setup() {
    const modelerStore = modeler();

    const xmlStr = ref("");
    const drawer = ref(false);

    const openXMLPreviewModel = async () => {
      try {
        // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
        const modeler = modelerStore.getModeler!;

        if (!modeler) {
          return ElMessage.warning("模型加载失败，请刷新重试");
        }

        const { xml } = await modeler.saveXML({ format: true, preamble: true });
        xmlStr.value = xml || "";
        drawer.value = true;
      } catch (e) {
        ElMessage.error((e as Error).message || (e as string));
      }
    };

    return () => (
      <div>
        <el-button onClick={openXMLPreviewModel}>浏览xml</el-button>

        <el-drawer v-model={drawer.value} with-header={false}>
          {xmlStr.value}
        </el-drawer>
      </div>
    );
  }
});

export default Previews;
