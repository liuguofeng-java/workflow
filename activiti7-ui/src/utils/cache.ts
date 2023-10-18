import { CacheToken } from "@/constants/cacheKey";

/**
 * 取缓存值
 * @param {*} key
 * @param {*} options
 */
export const getCache = (key: string): any => {
  const data = localStorage.getItem(key);
  if (!data) {
    return {};
  }
  return JSON.parse(data);
};

/**
 * 设置缓存值
 * @param {*} key
 * @param {*} value
 */
export const setCache = (
  key: string,
  value: string | Record<string, unknown> | Array<any>[]
): void => {
  localStorage.setItem(key, typeof value === "object" ? JSON.stringify(value) : value);
};

/**
 * 清除缓存
 * @param key
 * @param isSessionStorage
 */
export const removeCache = (key: string): void => {
  localStorage.removeItem(key);
};

export const getToken = (): string => {
  return getCache(CacheToken)["userId"];
};
