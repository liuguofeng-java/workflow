# 介绍

> 工作流项目基于技术组合（SpringBoot、MyBatis、Vue3、TypeScript）主要目: 但愿人长久，搬砖不再有

#### 1.系统需求

- JDK >= 1.8
- MySQL >= 5.7 (后续会兼容更多数据库)
- Maven >= 3.0
- Node.js >= 16.0.0



#### 2.目录结构

```
├── activiti7-java																	后端项目
│   ├── sql
│   │   └── mysql.sql																mysql脚本文件
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.activiti
│       │   │           ├── ActivitiApplication.java								后端程序启动类
│       │   │           ├── config													配置相关文件
│       │   │           ├── modules
│       │   │           │   ├── controller											controller
│       │   │           │   ├── dao													数据访问对象
│       │   │           │   ├── entity												实体类
│       │   │           │   ├── listener											工作流执行监听器
│       │   │           │   └── service												主要业务逻辑
│       │   │           └── utils													工具类
│       │   └── resources
│       │       ├── application-dev.yml												springBoot配置文件
│       │       ├── application.yml													springBoot配置文件
│       │       ├── logback.xml														logback日志配置文件
│       │       └── mapper															mybatis映射文件

├── activiti7-ui																	前端项目
│   ├── src
│   │   ├── components
│   │   │   ├── BpmnJs																bpmn设计器组件
│   │   │   ├── FormDesigner														vfrom设计器组件
│   │   ├── service
│   │   │   └── baseService.ts														网络请求
│   │   ├── utils																	工具类
│   │   └── views																	页面文件
```



#### 2.环境部署

1. 前往`Gitee`(https://gitee.com/liu_guo_feng/spring-boot-activiti7)下载页面下载解压到工作目录
2. 把`activiti7-java`导入到`idea`,并设置maven和jdk安装所在目录
3. 创建数据库`activiti`并导入数据脚本`activiti7-java.sql.xxx.sql`
4. 打开项目运行`com.activiti.ActivitiApplication.java`,没有报错即启动成功
5. 在`activiti7-ui`使用`cmd`命令执行 `npm install`安装前端依赖
6. 在`activiti7-ui`使用`cmd`命令执行 `npm run dev` 启动前端项目
5. 打开浏览器，输入：(http://localhost:9092) （默认账户/密码 `admin/123`）

若能正确展示登录页面，并能成功登录，则表明环境搭建成功



#### 3.必要配置

- 修改数据库连接，编辑`resources`目录下的`application-dev.yml`

```yml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: 数据库地址
      username: 数据库账号
      password: 数据库密码
```

- 修改服务器配置，`编辑resources目录下的application.yml`

```yml
# 开发环境配置
server:
  port: 9090 #端口
  servlet:
    context-path: /app
```

- 设置前端服务器地址，`编辑activiti7-ui目录下的.env.development`

```sh
NODE_ENV=development
VITE_APP_API=http://localhost:9090/app #服务器地址
```

