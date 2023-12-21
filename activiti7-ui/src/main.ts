import "element-plus/theme-chalk/display.css";
import "element-plus/theme-chalk/index.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import zhCn from "element-plus/dist/locale/zh-cn.mjs";

import axios from "axios";
import "virtual:svg-icons-register";

const app = createApp(App);
Object.keys(ElementPlusIcons).forEach((iconName) => {
  app.component(iconName, ElementPlusIcons[iconName as keyof typeof ElementPlusIcons]);
});

app.use(ElementPlus, {
  locale: zhCn,
  size: "default"
});

// vfrom相关依赖引入---------------------------------------------
import "@/components/FormDesigner/styles/index.scss";
import Draggable from "@/../lib/vuedraggable/dist/vuedraggable.umd.js";
import { registerIcon } from "@/components/FormDesigner/utils/el-icons";

import ContainerWidgets from "@/components/FormDesigner/form-widget/container-widget/index";
import ContainerItems from "@/components/FormDesigner/form-render/container-item/index";

import { addDirective } from "@/components/FormDesigner/utils/directive";
import { installI18n } from "@/components/FormDesigner/utils/i18n";
import { loadExtension } from "@/components/FormDesigner/extension/extension-loader";
registerIcon(app);
app.component("draggable", Draggable);
addDirective(app);
installI18n(app);
app.use(ContainerWidgets);
app.use(ContainerItems);
loadExtension(app);
// vfrom相关依赖引入---------------------------------------------

// bpmn.js 相关-------------------------------------------------
import "@/components/BpmnJs/styles/index.scss";
import i18n from "@/components/BpmnJs/i18n";
app.use(i18n);
// bpmn.js 相关-------------------------------------------------

app.use(createPinia()).use(router).mount("#app");

window.axios = axios;
