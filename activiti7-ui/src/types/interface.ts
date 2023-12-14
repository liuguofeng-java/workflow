export interface IFunction<T = any> {
  (x?: any): T;
}

export interface IObject<T = any> {
  [key: string]: T;
}

export interface IHttpResponse {
  code: number;
  msg: string;
  data: any;
  rows: any;
  total: number;
}
