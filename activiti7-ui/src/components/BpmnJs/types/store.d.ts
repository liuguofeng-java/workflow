/**
 * 动态表单数据
 */
type FormJson = {
  activityId: string;
  formJson: any;
  isMainFrom: number | 0;
};

/**
 * 数据库信息
 */
type TableInfo = {
  tableName: string; // 表名称
  type: "" | "create" | "ready"; // create: 新表结构,ready:现有的表结构
  comment: string; // 表备注
  columns: TableColumns[]; // 表备注
};

/**
 * 表结构信息
 */
type TableColumns = {
  columnName: string; // 行名称
  dataType: string; // 数据类型
  comment: string; // 备注
};

type ModelerStore = {
  activeElement: BpmnElement | undefined;
  activeElementId: string | undefined;
  modeler: Modeler | undefined;
  moddle: Moddle | undefined;
  modeling: any | undefined;
  canvas: Canvas | undefined;
  elementRegistry: ElementRegistry | undefined;
  formJsonList: FormJson[];
  tableInfo: TableInfo | undefined;
};
