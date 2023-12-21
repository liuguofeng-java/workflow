import { defineComponent, nextTick, ref } from "vue";

import modeler from "@/components/BpmnJs/store/modeler";
import { ElMessage } from "element-plus";

import hljs from "highlight.js";
import "highlight.js/styles/atom-one-dark-reasonable.css";

const Previews = defineComponent({
  name: "Previews",
  setup() {
    const modelerStore = modeler();

    const xmlStr = ref("");
    const drawer = ref(false);

    const openXMLPreviewModel = async () => {
      try {
        const modeler = modelerStore.getModeler;
        if (!modeler) {
          return ElMessage.warning("模型加载失败，请刷新重试");
        }
        const { xml } = await modeler.saveXML({ format: true, preamble: true });
        xmlStr.value = xml || "";
        drawer.value = true;
        nextTick(() => {
          const blocks = document.querySelector("#xmlCode") as HTMLElement;
          hljs.highlightBlock(blocks);
        });
      } catch (e) {
        ElMessage.error((e as Error).message || (e as string));
      }
    };

    return () => (
      <div>
        <el-button onClick={openXMLPreviewModel}>浏览xml</el-button>
        <el-drawer v-model={drawer.value} size="900px" title="浏览" destroy-on-close>
          <pre>
            <code class="xml hljs">
              <div id="xmlCode">{xmlStr.value}</div>
            </code>
          </pre>
        </el-drawer>
      </div>
    );
  }
});

export default Previews;
