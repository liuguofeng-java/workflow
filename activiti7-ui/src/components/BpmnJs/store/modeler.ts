import { defineStore } from "pinia";
import type { Moddle } from "moddle";
import type Modeler from "bpmn-js/lib/Modeler";
import type Modeling from "bpmn-js/lib/features/modeling/Modeling";
import type Canvas from "diagram-js/lib/core/Canvas";
import type ElementRegistry from "diagram-js/lib/core/ElementRegistry";
import { toRaw } from "vue";

const defaultState: ModelerStore = {
  activeElement: undefined,
  activeElementId: undefined,
  modeler: undefined,
  moddle: undefined,
  modeling: undefined,
  canvas: undefined,
  elementRegistry: undefined,
  formJsonList: [],
  tableInfo: undefined,
  widgetType: undefined,
  nodeColumns: []
};

export default defineStore("modeler", {
  state: (): ModelerStore => defaultState,
  getters: {
    getActive: (state) => toRaw(state.activeElement),
    getActiveId: (state) => state.activeElementId,
    getModeler: (state) => toRaw(state.modeler),
    getModdle: (state) => toRaw(state.moddle),
    getModeling: (state): Modeling => toRaw(state.modeling),
    getCanvas: (state): Canvas => toRaw(state.canvas),
    getElRegistry: (state) => toRaw(state.elementRegistry),
    getFormJsonList: (state) => toRaw(state.formJsonList),
    getTableInfo: (state) => toRaw(state.tableInfo),
    getWidgetType: (state) => toRaw(state.widgetType),
    getNodeColumns: (state) => toRaw(state.nodeColumns),
    getNodeColumn: (state) =>
      toRaw(state.nodeColumns.find((t) => t.activityId == state.activeElementId)?.columns)
  },
  actions: {
    setModeler(modeler: Modeler | undefined) {
      this.modeler = modeler;
      if (modeler) {
        this.modeling = modeler.get<Modeling>("modeling");
        this.moddle = modeler.get<Moddle>("moddle");
        this.canvas = modeler.get<Canvas>("canvas");
        this.elementRegistry = modeler.get<ElementRegistry>("elementRegistry");
      } else {
        this.modeling = this.moddle = this.canvas = this.elementRegistry = undefined;
      }
    },
    setElement(element: BpmnElement | undefined) {
      this.activeElement = element;
      this.activeElementId = element.id;
    },
    clearData() {
      this.formJsonList.splice(0, this.formJsonList.length);
      this.nodeColumns.splice(0, this.nodeColumns.length);
      this.widgetType = undefined;
      this.tableInfo = undefined;
    },
    setFormJson(formJson: FormJsonList) {
      const index = this.formJsonList.findIndex((t) => t.activityId === formJson.activityId);
      if (index !== -1) {
        this.formJsonList.splice(index, 1);
      }
      this.formJsonList.push(formJson);
    },
    setTableInfo(tableInfo: TableInfo) {
      // 过滤主键
      tableInfo.columns = tableInfo.columns.filter((t) => t.columnKey !== "PRI");
      this.tableInfo = tableInfo;
    },
    setTableColumns(tableColumns: TableColumns[]) {
      if (this.tableInfo) {
        this.tableInfo.columns = tableColumns;
      }
    },
    setWidgetType(widgetType: WidgetType) {
      this.widgetType = widgetType;
    },
    removeNodeColumn(tableColumn: TableColumns) {
      const activityId = this.activeElementId;
      if (!activityId) return;
      const tableItem = this.nodeColumns.find((t) => t.activityId === activityId);
      if (!tableItem) return;
      const columnIndex = tableItem.columns.findIndex(
        (t) => t.columnName === tableColumn.columnName
      );
      if (columnIndex == -1) return;
      tableItem.columns.splice(columnIndex, 1);
    },
    setNodeColumns(nodelColumns: NodelColumn[]) {
      this.nodeColumns = nodelColumns;
    },
    setNodeColumn(tableColumn: TableColumns) {
      const activityId = this.activeElementId;
      if (!activityId) return;
      let tableItem = this.nodeColumns.find((t) => t.activityId === activityId);
      if (!tableItem) {
        tableItem = {
          activityId: activityId,
          columns: [tableColumn]
        };
        this.nodeColumns.push(tableItem);
      } else {
        this.removeNodeColumn(tableColumn);
        tableItem.columns.push(tableColumn);
      }
      console.log("nodeColumns->>>/", this.nodeColumns);
    }
  }
});
