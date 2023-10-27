/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : activiti

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 27/10/2023 17:31:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `dept_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '部门id',
  `account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户姓名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '登录用户名',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '0f45e0320d93832c90dd3ed323129dd6', '管理员', '123', 'admin', '1396198931@qq.com', '18888888888', '2023-10-16 13:49:00', '2023-10-26 14:26:47');
INSERT INTO `sys_user` VALUES ('2', 'db3760209b816f6d350278dd2f3b1351', '测试账号', '123', 'test', '1396198931@qq.com', '18888888888', '2023-10-16 13:49:00', '2023-10-22 12:39:50');

-- ----------------------------
-- Table structure for sys_form
-- ----------------------------
DROP TABLE IF EXISTS `sys_form`;
CREATE TABLE `sys_form`  (
  `form_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表单id',
  `form_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表单名称',
  `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '表单数据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'activiti相关->流程表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_form
-- ----------------------------
INSERT INTO `sys_form` VALUES ('edf38c74a1705027fae46c842b1686b9', '测试1', '{\"widgetList\":[{\"key\":39587,\"type\":\"textarea\",\"icon\":\"textarea-field\",\"formItemFlag\":true,\"options\":{\"name\":\"textarea37414\",\"label\":\"textarea\",\"labelAlign\":\"\",\"rows\":3,\"defaultValue\":\"\",\"placeholder\":\"\",\"columnWidth\":\"200px\",\"size\":\"\",\"labelWidth\":null,\"labelHidden\":false,\"readonly\":false,\"disabled\":false,\"hidden\":false,\"required\":false,\"requiredHint\":\"\",\"validation\":\"\",\"validationHint\":\"\",\"customClass\":[],\"labelIconClass\":null,\"labelIconPosition\":\"rear\",\"labelTooltip\":null,\"minLength\":null,\"maxLength\":null,\"showWordLimit\":false,\"onCreated\":\"\",\"onMounted\":\"\",\"onInput\":\"\",\"onChange\":\"\",\"onFocus\":\"\",\"onBlur\":\"\",\"onValidate\":\"\"},\"id\":\"textarea37414\"},{\"key\":39030,\"type\":\"radio\",\"icon\":\"radio-field\",\"formItemFlag\":true,\"options\":{\"name\":\"radio88113\",\"label\":\"radio\",\"labelAlign\":\"\",\"defaultValue\":null,\"columnWidth\":\"200px\",\"size\":\"\",\"displayStyle\":\"inline\",\"buttonStyle\":false,\"border\":false,\"labelWidth\":null,\"labelHidden\":false,\"disabled\":false,\"hidden\":false,\"optionItems\":[{\"label\":\"radio 1\",\"value\":1},{\"label\":\"radio 2\",\"value\":2},{\"label\":\"radio 3\",\"value\":3}],\"required\":true,\"requiredHint\":\"请输入\",\"validation\":\"\",\"validationHint\":\"\",\"customClass\":[],\"labelIconClass\":null,\"labelIconPosition\":\"rear\",\"labelTooltip\":null,\"onCreated\":\"\",\"onMounted\":\"\",\"onChange\":\"\",\"onValidate\":\"\"},\"id\":\"radio88113\"}],\"formConfig\":{\"modelName\":\"formData\",\"refName\":\"vForm\",\"rulesName\":\"rules\",\"labelWidth\":80,\"labelPosition\":\"top\",\"size\":\"\",\"labelAlign\":\"label-left-align\",\"cssCode\":\"\",\"customClass\":[],\"functions\":\"\",\"layoutType\":\"PC\",\"jsonVersion\":3,\"onFormCreated\":\"\",\"onFormMounted\":\"\",\"onFormDataChange\":\"\"}}', '2023-10-27 16:58:00', '2023-10-27 17:28:47');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门id',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('0f45e0320d93832c90dd3ed323129dd6', '研发部', 'liu', '18888888888', '1396198931@qq.com', '2023-10-22 12:18:47', '2023-10-22 12:18:47');
INSERT INTO `sys_dept` VALUES ('db3760209b816f6d350278dd2f3b1351', '销售部', 'liu', '1888888888', '1396198931@qq.com', '2023-10-22 12:39:42', '2023-10-22 12:39:42');

SET FOREIGN_KEY_CHECKS = 1;
