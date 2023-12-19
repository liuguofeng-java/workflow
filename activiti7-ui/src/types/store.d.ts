/**
 * 组件详情
 */
type WidgetOptions = {
  name: string;
  label: string;
  optionItems: [];
};

/**
 * 组件列表
 */
type WidgetList = {
  id: string;
  key: number;
  icon: string;
  type: string;
  options: WidgetOptions;
};
/**
 * 动态表单数据
 */
type FormJson = {
  formConfig: [];
  widgetList: WidgetList[];
};
/**
 * 动态表单数据
 */
type FormJsonList = {
  activityId: string;
  formJson: FormJson;
  isMainFrom: number | 0;
};

/**
 * 节点绑定的数据库字段类型
 */
type NodelColumn = {
  activityId: string; // 节点id
  columns: TableColumns[]; // 表备注
};

/**
 * 数据库信息
 */
type TableInfo = {
  tableName: string; // 表名称
  type: "" | "create" | "ready"; // create: 新表结构,ready:现有的表结构
  tableComment: string; // 表备注
  columns: TableColumns[]; // 表备注
};

/**
 * 表结构信息
 */
type TableColumns = {
  columnName: string; // 行名称
  dataType: string; // 数据类型
  columnComment: string; // 备注
  columnLength: number | -1; // 字段长度
  columnKey: "" | "PRI"; // 行键
};

/**
 * 组件与表类型对应
 */
type WidgetDataType = {
  widgetDataType: any; // 组件类型
  widgetDefaultDataType: any; // 组件默认类型
};

type ModelerStore = {
  activeElement: BpmnElement | undefined;
  activeElementId: string | undefined;
  modeler: Modeler | undefined;
  moddle: Moddle | undefined;
  modeling: any | undefined;
  canvas: Canvas | undefined;
  elementRegistry: ElementRegistry | undefined;
  formJsonList: FormJsonList[];
  tableInfo: TableInfo | undefined;
  widgetDataType: WidgetDataType | undefined;
  nodeColumns: NodelColumn[];
};
