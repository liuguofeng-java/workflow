import { IFunction, IObject } from "@/types/interface";
import { debounce, DebouncedFunc, DebounceSettings } from "lodash";
import type { App, Plugin } from "vue";

/**
 * 获取对象下的字段值
 * @param record {}
 * @param key 'a.b.c'
 * @param defaultValue
 * @returns
 */
export const getValueByKeys = (record: IObject = {}, key: string, defaultValue?: unknown): any => {
  const keys = key.split(".");
  for (let i = 0; i < keys.length; i++) {
    record = record[keys[i]] || (i === keys.length - 1 ? defaultValue : {});
  }
  return record || defaultValue;
};

/**
 * 数组转对象
 * @param {*} rs 集合
 * @param {*} key 需要转换目标key的名称
 */
export const arrayToObject = (
  rs: any[] = [],
  key: string | IFunction,
  render?: IFunction
): IObject => {
  const o: IObject = {};
  rs.forEach((x) => {
    o[typeof key === "function" ? key(x) : x[key]] = render ? render(x) : x;
  });
  return o;
};

/**
 * 数组转换格式
 * @param rs
 * @param render
 * @returns
 */
export const arrayToKeyValueArray = (rs: any[] = [], render?: IFunction): any[] => {
  return rs.map((x) => (render ? render(x) : typeof x === "object" ? x : { label: x, value: x }));
};

/**
 * 是否只null和undefined值
 * @param vl
 * @returns
 */
export const isNullOrUndefined = (vl: unknown): boolean => {
  return vl === null || typeof vl === "undefined";
};

/**
 * 是否外链
 * @param path
 * @returns
 */
export const isExternalLink = (path: string): boolean => {
  return /^(https?:|\/\/|mailto:|tel:|^{{\s?ApiUrl\s?}})/.test(path);
};

/**
 * 复制
 * @param value
 */
export const copyToClipboard = (value: string): void => {
  const transfer = document.createElement("textarea");
  document.body.appendChild(transfer);
  transfer.value = value;
  transfer.focus();
  transfer.select();
  if (document.execCommand("copy")) {
    document.execCommand("copy");
  }
  transfer.blur();
  document.body.removeChild(transfer);
};

/**
 * 检查是否有权限
 * @param permission
 * @param key
 * @returns
 */
export const checkPermission = (permission: string[], key: string): boolean => {
  return permission.includes(key);
};

/**
 * 获取uuid
 */
export const getUuid = (): string => {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0,
      v = c == "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
};

/**
 * 邮箱
 * @param {*} s
 */
export const isEmail = (s: string): boolean => {
  return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s);
};

/**
 * 手机号码
 * @param {*} s
 */
export const isMobile = (s: string): boolean => {
  return /^1[0-9]{10}$/.test(s);
};

/**
 * 电话号码
 * @param {*} s
 */
export const isPhone = (s: string): boolean => {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s);
};

/**
 * URL地址
 * @param {*} s
 */
export const isURL = (s: string): boolean => {
  return /^http[s]?:\/\/.*/.test(s);
};

/**
 * 正整数
 * @param {*} s
 */
export const isNumber = (s: string): boolean => {
  return /^\+?[1-9][0-9]*$/.test(s);
};

/**
 * 获取字典数据列表
 * @param dictType  字典类型
 */
export const getDictDataList = (list: IObject[], dictType?: string): IObject[] => {
  const type = list.find((element: IObject) => element.dictType === dictType);
  if (type) {
    return type.dataList;
  } else {
    return [];
  }
};

/**
 * 获取字典名称
 * @param dictType  字典类型
 * @param dictValue  字典值
 */
export const getDictLabel = (
  list: IObject[],
  dictType: string,
  dictValue: number
): string | number => {
  const type = list.find((element: IObject) => element.dictType === dictType);
  if (type) {
    const val = type.dataList.find((element: IObject) => element.dictValue === dictValue + "");
    if (val) {
      return val.dictLabel;
    } else {
      return dictValue;
    }
  } else {
    return dictValue;
  }
};

/**
 * 获取svg图标(id)列表
 */
export const getIconList = (): string[] => {
  const rs: string[] = [];
  const list = document.querySelectorAll("svg symbol");
  for (let i = 0; i < list.length; i++) {
    rs.push(list[i].id);
  }
  return rs;
};

/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export const treeDataTranslate = (data: IObject[], id?: string, pid?: string): IObject[] => {
  const res: IObject[] = [];
  const temp: IObject = {};
  id = id || "id";
  pid = pid || "pid";
  for (let i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i];
  }
  for (let k = 0; k < data.length; k++) {
    if (!temp[data[k][pid]] || data[k][id] === data[k][pid]) {
      res.push(data[k]);
      continue;
    }
    if (!temp[data[k][pid]]["children"]) {
      temp[data[k][pid]]["children"] = [];
    }
    temp[data[k][pid]]["children"].push(data[k]);
    data[k]["_level"] = (temp[data[k][pid]]._level || 0) + 1;
  }
  return res;
};

/**
 * 全局组件安装
 * @param component
 * @param alias
 * @returns
 */
export const withInstall = <T extends object>(component: T, alias?: string): T & Plugin => {
  const comp = component as any;
  comp.install = (app: App) => {
    app.component(comp.name || comp.displayName, component);
    if (alias) {
      app.config.globalProperties[alias] = component;
    }
  };
  return component as T & Plugin;
};

/**
 * 节流函数 `created() {this.dataFormSubmitHandle = useDebounce(this.dataFormSubmitHandle);}`
 * @param fn
 * @param wait
 * @returns
 */
export const useDebounce = (
  fn: (e?: any) => any,
  wait?: number,
  options?: DebounceSettings
): DebouncedFunc<any> => {
  return debounce(fn, wait ?? 1000, {
    leading: true,
    trailing: false,
    ...options
  });
};
