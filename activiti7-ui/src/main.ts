import "@/assets/icons/iconfont/iconfont.js";
import ElementPlus from "element-plus";
import "element-plus/theme-chalk/display.css";
import "element-plus/theme-chalk/index.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import * as ElementPlusIcons from "@element-plus/icons-vue";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import zhCn from "element-plus/dist/locale/zh-cn.mjs";

import axios from "axios";
import "virtual:svg-icons-register";

// vfrom相关依赖引入
import "@/components/form-designer/styles/index.scss";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import Draggable from "@/../lib/vuedraggable/dist/vuedraggable.umd.js";
import "virtual:svg-icons-register";
import { registerIcon } from "@/components/form-designer/utils/el-icons";

import ContainerWidgets from "@/components/form-designer/form-widget/container-widget/index";
import ContainerItems from "@/components/form-designer/form-render/container-item/index";

import { addDirective } from "@/components/form-designer/utils/directive";
import { installI18n } from "@/components/form-designer/utils/i18n";
import { loadExtension } from "@/components/form-designer/extension/extension-loader";

const app = createApp(App);
Object.keys(ElementPlusIcons).forEach((iconName) => {
  app.component(iconName, ElementPlusIcons[iconName as keyof typeof ElementPlusIcons]);
});

app.use(ElementPlus, {
  locale: zhCn
});

app.use(ElementPlus);
registerIcon(app);

// vform 相关
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
app.component("draggable", Draggable);
addDirective(app);
installI18n(app);
app.use(ContainerWidgets);
app.use(ContainerItems);
loadExtension(app);

app.use(createPinia()).use(router).use(ElementPlus, { size: "default" }).mount("#app");

window.axios = axios;
