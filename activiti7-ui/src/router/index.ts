import Error from "@/views/error.vue";
import Login from "@/views/login.vue";
import Home from "@/views/home.vue";
import { getToken } from "@/utils/cache";
import { createWebHistory, createRouter, RouteRecordRaw } from "vue-router";

/**
 * 框架基础路由
 */
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: Home,
    meta: { title: "主页" },
    children: [
      {
        path: "/",
        name: "userInfo",// 用户信息
        component: () => import('@/views/activiti/userInfo/index.vue'),
      },
      {
        path: "/userTask",
        name: "userTask",// 我的任务
        component: () => import('@/views/activiti/userTask/index.vue'),
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
    const token = getToken();
    if (to.path === "/login") {
      if (token) router.push("/");
    } else {
      if (!token) router.push("/login");
    }
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  }
});

export default router;
