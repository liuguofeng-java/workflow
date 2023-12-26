import zh_CN from "../../i18n/zh_CN";

const languages = {
  zh_CN
};

const lang = sessionStorage.getItem("lang");

export function customTranslate(template: string, replacements?: Record<string, string>) {
  replacements = replacements || {};
  const translations = languages[lang || "zh_CN"];
  // Translate
  template = translations.elements[template] || translations.lint[template] || template;
  // Replace
  return template.replace(/{([^}]+)}/g, function (_, key) {
    return replacements ? replacements[key] : "{" + key + "}";
  });
}

export default {
  translate: ["value", customTranslate]
};
