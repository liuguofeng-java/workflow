import EmptyXML from "@/components/BpmnJs/utils/EmptyXML";
import { EditorSettings } from "@/components/BpmnJs/types/editor/settings";
import modelerStore from "@/components/BpmnJs/store/modeler";

export const createNewDiagram = async function (newXml?: string, settings?: EditorSettings) {
  try {
    const store = modelerStore();
    const timestamp = Date.now();
    const { processId, processName, processEngine } = settings || {};
    const newId: string = processId ? processId : `Process_${timestamp}`;
    const newName: string = processName || `新建流程_${timestamp}`;
    const xmlString = newXml || EmptyXML(newId, newName, processEngine);
    const modeler = store.getModeler;
    const { warnings } = await modeler!.importXML(xmlString);
    console.log("bpmb warnings->", warnings);
  } catch (e) {
    console.error(`[Process Designer Warn]: ${typeof e === "string" ? e : (e as Error)?.message}`);
  }
};
