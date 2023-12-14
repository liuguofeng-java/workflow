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
  nodelColumns: []
};

export default defineStore("modeler", {
  state: (): ModelerStore => defaultState,
  getters: {
    getActive: (state) => toRaw(state.activeElement),
    getActiveId: (state) => state.activeElementId,
    getModeler: (state) => toRaw(state.modeler),
    getModdle: (state) => toRaw(state.moddle),
    getModeling: (state): Modeling => toRaw(state.modeling),
    getCanvas: (state): Canvas | undefined => toRaw(state.canvas),
    getElRegistry: (state) => toRaw(state.elementRegistry),
    getFormJsonList: (state) => toRaw(state.formJsonList),
    getTableInfo: (state) => toRaw(state.tableInfo),
    getWidgetType: (state) => toRaw(state.widgetType),
    getNodelColumns: (state) =>
      toRaw(state.nodelColumns.find((t) => t.activityId == state.activeElementId)?.columns)
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
    clearFormJson() {
      this.formJsonList.splice(0, this.formJsonList.length);
    },
    setFormJson(formJson: FormJson) {
      const index = this.formJsonList.findIndex((t) => t.activityId === formJson.activityId);
      if (index !== -1) {
        this.formJsonList.splice(index, 1);
      }
      this.formJsonList.push(formJson);
    },
    setTableInfo(tableInfo: TableInfo) {
      this.tableInfo = tableInfo;
      this.nodelColumns = [];
    },
    setTableColumns(tableColumns: TableColumns[]) {
      if (this.tableInfo) {
        this.tableInfo.columns = tableColumns;
      }
    },
    setWidgetType(widgetType: WidgetType) {
      this.widgetType = widgetType;
    },
    setNodelColumn(tableColumn: TableColumns) {
      const activityId = this.activeElementId;
      if (!activityId) return;
      let tableItem = this.nodelColumns.find((t) => t.activityId === activityId);
      if (!tableItem) {
        tableItem = {
          activityId: activityId,
          columns: [tableColumn]
        };
        this.nodelColumns.push(tableItem);
      } else {
        const columnIndex = tableItem.columns.findIndex(
          (t) => t.columnName === tableColumn.columnName
        );
        if (columnIndex !== -1) {
          tableItem.columns.splice(columnIndex, 1);
        } else {
          tableItem.columns.push(tableColumn);
        }
      }
      console.log("nodelColumns->>>/", this.nodelColumns);
    }
  }
});
