import { createI18n } from "vue-i18n";
import zh_CN from "./zh_CN";

export const defaultLang = "zh_CN";

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: defaultLang,
  messages: {
    zh_CN
  }
});

export default i18n;
