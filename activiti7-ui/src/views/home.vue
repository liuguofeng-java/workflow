<template>
  <div class="root">
    <div class="left-container">
      <div class="title-container">
        <img src="@/assets/images/icon.png" width="40" height="40" />
        <span class="title-text">工作流引擎</span>
      </div>
      <el-menu background-color="#343844" class="el-menu-container" :default-active="defaultActive" text-color="#fff" @select="handleSelect" unique-opened>
        <el-sub-menu index="/sys">
          <template #title>
            <el-icon>
              <SetUp />
            </el-icon>
            <span>基本设置</span>
          </template>
          <el-menu-item index="/sys/user">
            <el-icon>
              <User />
            </el-icon>
            <span>账号信息</span>
          </el-menu-item>
          <el-menu-item index="/sys/dept">
            <el-icon>
              <Operation />
            </el-icon>
            <span>部门信息</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/workflow">
          <template #title>
            <el-icon>
              <Connection />
            </el-icon>
            <span>流程管理</span>
          </template>
          <el-menu-item index="/workflow/definition">
            <el-icon>
              <Tickets />
            </el-icon>
            <span>流程定义</span>
          </el-menu-item>
          <el-menu-item index="/workflow/vform">
            <el-icon>
              <MessageBox />
            </el-icon>
            <span>表单配置</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/my">
          <template #title>
            <el-icon>
              <Position />
            </el-icon>
            <span>与我有关</span>
          </template>
          <el-menu-item index="/my/start">
            <el-icon>
              <Odometer />
            </el-icon>
            <span>我发起的</span>
          </el-menu-item>
          <el-menu-item index="/my/todo">
            <el-icon>
              <ChatLineRound />
            </el-icon>
            <span>我的代办</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>

    <dir class="rigth-container">
      <div class="head-container">
        <span class="menu-name">欢迎登录！今天是{{ dateVal }}</span>
        <div class="head-user-container">
          <div>{{ user.account }}</div>
          <img class="head-user-close" src="../assets/images/close.png" @click="logout" />
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
let defaultActive = ref("/sys/user");
const user = ref({
  account: String
});

let dateVal = ref<string>();

// 初始化
const onInit = () => {
  const userInfo = getCache(CacheToken);
  user.value = userInfo;
  router.push(defaultActive.value);
};

// 退出登录
const logout = () => {
  removeCache(CacheToken);
  router.replace("/login");
};

// 菜单选择
const handleSelect = (key: string) => {
  router.push(key);
};

// 获取时间
const getDate = () => {
  var date = new Date(); // 获取时间
  var year = date.getFullYear(); // 获取年
  var month = date.getMonth() + 1; // 获取月
  var day = date.getDate(); // 获取日
  // 周一返回的是1，周六是6，但是周日是0
  var week = "日一二三四五六".charAt(new Date().getDay());
  dateVal.value = year + "年" + getNum(month) + "月" + getNum(day) + "日" + " 星期" + week;
};

// 如果一个数字则在前面添加0
const getNum = (num: number) => {
  return num < 10 ? "0" + num : num;
};

getDate();
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

.el-menu-container /deep/ .is-active {
  background: #fcbc02;
  border-radius: 0 80px 80px 0;
  color: #532f00;
}

.el-menu-container /deep/ .is-active > ul > .is-active {
  margin-right: 40px;
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
  font-weight: 600;
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
  height: calc(100vh - 80px);
  overflow-y: auto;
  background: #f0f2f5;
}

.router-container {
  background-color: white;
  padding: 20px;
}
</style>
