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

// bpmn.js
import "@/components/bpmnJs/styles/index.scss";

import {
  createDiscreteApi,
  create,
  NColorPicker,
  NConfigProvider,
  NMessageProvider,
  NDialogProvider,
  NButton,
  NButtonGroup,
  NTag,
  NCollapse,
  NCollapseItem,
  NDataTable,
  NPopover,
  NDrawer,
  NDrawerContent,
  NModal,
  NCode,
  NForm,
  NFormItem,
  NInput,
  NInputNumber,
  NRadio,
  NRadioGroup,
  NCheckbox,
  NCheckboxGroup,
  NSelect,
  NSwitch
} from "naive-ui";

const naive = create({
  components: [
    NColorPicker,
    NConfigProvider,
    NMessageProvider,
    NDialogProvider,
    NButton,
    NButtonGroup,
    NTag,
    NCollapse,
    NCollapseItem,
    NDataTable,
    NPopover,
    NDrawer,
    NDrawerContent,
    NModal,
    NCode,
    NForm,
    NFormItem,
    NInput,
    NInputNumber,
    NRadio,
    NRadioGroup,
    NCheckbox,
    NCheckboxGroup,
    NSelect,
    NSwitch
  ]
});
const { message } = createDiscreteApi(["message", "dialog", "notification", "loadingBar"]);
window.__messageBox = message;
import LucideIcon from "@/components/bpmnJs/common/LucideIcon.vue";
import EditItem from "@/components/bpmnJs/common/EditItem.vue";
import CollapseTitle from "@/components/bpmnJs/common/CollapseTitle.vue";
import "virtual:svg-icons-register";
import i18n from "@/components/bpmnJs/i18n";
app.use(i18n);
app.use(naive);
app.component("LucideIcon", LucideIcon);
app.component("EditItem", EditItem);
app.component("CollapseTitle", CollapseTitle);

app.use(createPinia()).use(router).use(ElementPlus, { size: "default" }).mount("#app");

window.axios = axios;
