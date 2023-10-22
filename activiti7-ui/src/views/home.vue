<template>
  <div class="root">
    <div class="left-container">
      <div class="title-container">
        <img src="@/assets/images/icon.png" width="40" height="40">
        <span class="title-text">工作流引擎</span>
      </div>
      <el-menu background-color="#343844" class="el-menu-container" default-active="/" text-color="#fff"
        @open="handleOpen" @select="handleSelect">

        <el-sub-menu index="/">
          <template #title>
            <el-icon>
              <SetUp />
            </el-icon>
            <span>基本设置</span>
          </template>
          <el-menu-item index="/">
            <el-icon>
              <User />
            </el-icon>
            <span>账号信息</span>
          </el-menu-item>
          <el-menu-item index="/dept">
            <el-icon>
              <Operation />
            </el-icon>
            <span>部门信息</span>
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/processDefinition">
          <el-icon>
            <Tickets />
          </el-icon>
          <span>流程管理</span>
        </el-menu-item>
        <el-menu-item index="/userTask">
          <el-icon>
            <Tickets />
          </el-icon>
          <span>我的任务</span>
        </el-menu-item>
      </el-menu>
    </div>

    <dir class="rigth-container">
      <div class="head-container">
        <span class="menu-name">账号信息</span>
        <div class="head-user-container">
          <div>{{ user.account }}</div>
          <img class="head-user-close" src="../assets/images/close.png" @click="logout">
        </div>
      </div>

      <div class="page">
        <div class="router-container">
          <router-view />
        </div>
      </div>
    </dir>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { getCache, removeCache } from "@/utils/cache";
import { CacheToken } from "@/constants/cacheKey";

const router = useRouter();

const user = ref({
  account: String
});

// 初始化
const onInit = () => {
  const userInfo = getCache(CacheToken);
  user.value = userInfo;
};

// 退出登录
const logout = () => {
  removeCache(CacheToken)
  router.push("/")
}

// 菜单打开
const handleOpen = (key: string, keyPath: string[]) => {
  router.push(key)
}

// 菜单选择
const handleSelect = (key: string, keyPath: string[]) => {
  router.push(key)
}


onInit();
</script>

<style scoped>
.root {
  display: flex;
  height: 100vh;
  width: 100%;
}

.left-container {
  width: 350px;
  background: #343844;
  min-width: 200px;
}

.rigth-container {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

.title-container {
  background: #343844;
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-right: solid 1px white;
}

.title-text {
  margin-left: 10px;
  font-family: "幼圆";
  font-size: 23px;
  font-weight: 900;
  color: white;
}

.el-menu-container {
  height: calc(100vh - 150px);
}

.el-menu-container>>>.is-active {
  background: #fcbc02;
  border-radius: 0 80px 80px 0;
  margin-right: 40px;
  color: #532f00;
}

.head-container {
  height: 80px;
  display: flex;
  width: 100%;
  align-items: center;
  background: white;
  border: solid 1px #e8e8e8;
}

.menu-name {
  font-size: 20px;
  font-weight: 700;
  color: #000000;
  margin-left: 20px;
}

.head-user-container {
  position: absolute;
  right: 20px;
  font-size: 16px;
  font-weight: 500;
  color: #000000;
  margin-left: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.head-user-close {
  width: 30px;
  height: 30px;
  margin-left: 10px;
  cursor: pointer;
}

.page {
  padding: 10px;
}

.router-container {
  background-color: white;
  padding: 20px;
}
</style>
