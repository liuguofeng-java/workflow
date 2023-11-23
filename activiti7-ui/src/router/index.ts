import Error from "@/views/error.vue";
import Login from "@/views/login.vue";
import Home from "@/views/home.vue";
import { getToken } from "@/utils/cache";
import { createWebHistory, createRouter, RouteRecordRaw } from "vue-router";
import NProgress from "nprogress";
import "nprogress/nprogress.css";

/**
 * 框架基础路由
 */
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: Home,
    redirect: "/sys/user",
    meta: { title: "主页" },
    children: [
      {
        path: "sys/user",
        name: "sysUser", // 用户信息
        component: () => import("@/views/system/user/index.vue")
      },
      {
        path: "sys/dept",
        name: "sysDept", // 部门信息
        component: () => import("@/views/system/dept/index.vue")
      },
      {
        path: "workflow/definition",
        name: "definition", // 流程定义
        component: () => import("@/views/activiti/workflow/definition/index.vue")
      },
      {
        path: "workflow/vform",
        name: "vform", // 流程定义
        component: () => import("@/views/activiti/workflow/vform/index.vue")
      },
      {
        path: "my/start",
        name: "start", // 我发起的
        component: () => import("@/views/activiti/my/start/index.vue")
      },
      {
        path: "my/todo",
        name: "todo", // 我的代办
        component: () => import("@/views/activiti/my/todo/index.vue")
      }
    ]
  },
  {
    path: "/login",
    component: Login,
    meta: { title: "登录" }
  },
  {
    path: "/error",
    name: "error",
    component: Error,
    meta: { title: "错误页面" }
  },
  {
    path: "/:path(.*)*",
    redirect: { path: "/error", query: { to: 404 }, replace: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
});

// 设置路由守卫
router.beforeEach((to, from, next) => {
  // 执行进度条开始加载
  NProgress.start();
  const token = getToken();
  if (to.path === "/login") {
    if (token) next({ path: "/" });
  } else {
    if (!token) next({ path: "/login" });
  }
  next();
});

// 路由跳转后钩子函数中 - 执行进度条加载结束
router.afterEach(() => {
  NProgress.done();
});

export default router;
