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
        name: "sysUser",// 用户信息
        component: () => import('@/views/activiti/system/user/index.vue'),
      },
      {
        path: "/dept",
        name: "sysDept",// 部门信息
        component: () => import('@/views/activiti/system/dept/index.vue'),
      },
      {
        path: "/processDefinition",
        name: "processDefinition",// 流程管理
        component: () => import('@/views/activiti/processDefinition/index.vue'),
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
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

// 设置路由守卫
router.beforeEach((to, from, next) => {
  const token = getToken();
  if (to.path === "/login") {
    if (token) next({ path: '/' })
  } else {
    if (!token) next({ path: '/login' })
  }
  next()
})

export default router;
