/*
 Navicat Premium Data Transfer

 Source Server         : 119.3.177.255-测试
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 119.3.177.255:3306
 Source Schema         : activiti

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 30/11/2023 15:59:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ACT_EVT_LOG
-- ----------------------------
DROP TABLE IF EXISTS `ACT_EVT_LOG`;
CREATE TABLE `ACT_EVT_LOG`  (
  `LOG_NR_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DATA_` longblob NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_PROCESSED_` tinyint(4) NULL DEFAULT 0,
  PRIMARY KEY (`LOG_NR_`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_EVT_LOG
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_GE_BYTEARRAY
-- ----------------------------
DROP TABLE IF EXISTS `ACT_GE_BYTEARRAY`;
CREATE TABLE `ACT_GE_BYTEARRAY`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTES_` longblob NULL,
  `GENERATED_` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_GE_BYTEARRAY
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_GE_PROPERTY
-- ----------------------------
DROP TABLE IF EXISTS `ACT_GE_PROPERTY`;
CREATE TABLE `ACT_GE_PROPERTY`  (
  `NAME_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VALUE_` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`NAME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_GE_PROPERTY
-- ----------------------------
INSERT INTO `ACT_GE_PROPERTY` VALUES ('cfg.execution-related-entities-count', 'false', 1);
INSERT INTO `ACT_GE_PROPERTY` VALUES ('next.dbid', '1', 1);
INSERT INTO `ACT_GE_PROPERTY` VALUES ('schema.history', 'create(7.0.0.0)', 1);
INSERT INTO `ACT_GE_PROPERTY` VALUES ('schema.version', '7.0.0.0', 1);

-- ----------------------------
-- Table structure for ACT_HI_ACTINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_ACTINST`;
CREATE TABLE `ACT_HI_ACTINST`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_START`(`START_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_END`(`END_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_PROCINST`(`PROC_INST_ID_`, `ACT_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_EXEC`(`EXECUTION_ID_`, `ACT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_ACTINST
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_ATTACHMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_ATTACHMENT`;
CREATE TABLE `ACT_HI_ATTACHMENT`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `URL_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CONTENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_ATTACHMENT
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_COMMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_COMMENT`;
CREATE TABLE `ACT_HI_COMMENT`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACTION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MESSAGE_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `FULL_MSG_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_COMMENT
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_DETAIL
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_DETAIL`;
CREATE TABLE `ACT_HI_DETAIL`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_PROC_INST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_ACT_INST`(`ACT_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_TIME`(`TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_NAME`(`NAME_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_TASK_ID`(`TASK_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_DETAIL
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_IDENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_IDENTITYLINK`;
CREATE TABLE `ACT_HI_IDENTITYLINK`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `GROUP_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_USER`(`USER_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_TASK`(`TASK_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_PROCINST`(`PROC_INST_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_IDENTITYLINK
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_PROCINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_PROCINST`;
CREATE TABLE `ACT_HI_PROCINST`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `END_ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `PROC_INST_ID_`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PRO_INST_END`(`END_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_PRO_I_BUSKEY`(`BUSINESS_KEY_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_PROCINST
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_TASKINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_TASKINST`;
CREATE TABLE `ACT_HI_TASKINST`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PRIORITY_` int(11) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
  `FORM_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_TASK_INST_PROCINST`(`PROC_INST_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_TASKINST
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_HI_VARINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_VARINST`;
CREATE TABLE `ACT_HI_VARINST`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_PROC_INST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_NAME_TYPE`(`NAME_`, `VAR_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_TASK_ID`(`TASK_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_HI_VARINST
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_PROCDEF_INFO
-- ----------------------------
DROP TABLE IF EXISTS `ACT_PROCDEF_INFO`;
CREATE TABLE `ACT_PROCDEF_INFO`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `INFO_JSON_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_UNIQ_INFO_PROCDEF`(`PROC_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_INFO_PROCDEF`(`PROC_DEF_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_PROCDEF_INFO
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RE_DEPLOYMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_DEPLOYMENT`;
CREATE TABLE `ACT_RE_DEPLOYMENT`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
  `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RE_DEPLOYMENT
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RE_MODEL
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_MODEL`;
CREATE TABLE `ACT_RE_MODEL`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) NULL DEFAULT NULL,
  `META_INFO_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RE_MODEL
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RE_PROCDEF
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_PROCDEF`;
CREATE TABLE `ACT_RE_PROCDEF`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) NULL DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_UNIQ_PROCDEF`(`KEY_`, `VERSION_`, `TENANT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RE_PROCDEF
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_DEADLETTER_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_DEADLETTER_JOB`;
CREATE TABLE `ACT_RU_DEADLETTER_JOB`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_DEADLETTER_JOB
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_EVENT_SUBSCR
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_EVENT_SUBSCR`;
CREATE TABLE `ACT_RU_EVENT_SUBSCR`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CONFIGURATION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_EVENT_SUBSCR_CONFIG_`(`CONFIGURATION_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_EVENT_SUBSCR
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_EXECUTION
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_EXECUTION`;
CREATE TABLE `ACT_RU_EXECUTION`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ROOT_PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) NULL DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) NULL DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) NULL DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) NULL DEFAULT NULL,
  `IS_MI_ROOT_` tinyint(4) NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `CACHED_ENT_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_COUNT_ENABLED_` tinyint(4) NULL DEFAULT NULL,
  `EVT_SUBSCR_COUNT_` int(11) NULL DEFAULT NULL,
  `TASK_COUNT_` int(11) NULL DEFAULT NULL,
  `JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `TIMER_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `SUSP_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `DEADLETTER_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `VAR_COUNT_` int(11) NULL DEFAULT NULL,
  `ID_LINK_COUNT_` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_EXEC_BUSKEY`(`BUSINESS_KEY_`) USING BTREE,
  INDEX `ACT_IDC_EXEC_ROOT`(`ROOT_PROC_INST_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_EXECUTION
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_IDENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_IDENTITYLINK`;
CREATE TABLE `ACT_RU_IDENTITYLINK`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `GROUP_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_USER`(`USER_ID_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_GROUP`(`GROUP_ID_`) USING BTREE,
  INDEX `ACT_IDX_ATHRZ_PROCEDEF`(`PROC_DEF_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_IDENTITYLINK
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_INTEGRATION
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_INTEGRATION`;
CREATE TABLE `ACT_RU_INTEGRATION`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `FLOW_NODE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATED_DATE_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_INTEGRATION
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_JOB`;
CREATE TABLE `ACT_RU_JOB`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_JOB
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_SUSPENDED_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_SUSPENDED_JOB`;
CREATE TABLE `ACT_RU_SUSPENDED_JOB`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_SUSPENDED_JOB
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_TASK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_TASK`;
CREATE TABLE `ACT_RU_TASK`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DELEGATION_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PRIORITY_` int(11) NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `FORM_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_TASK_CREATE`(`CREATE_TIME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_TASK
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_TIMER_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_TIMER_JOB`;
CREATE TABLE `ACT_RU_TIMER_JOB`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_TIMER_JOB
-- ----------------------------

-- ----------------------------
-- Table structure for ACT_RU_VARIABLE
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_VARIABLE`;
CREATE TABLE `ACT_RU_VARIABLE`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_VARIABLE_TASK_ID`(`TASK_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ACT_RU_VARIABLE
-- ----------------------------

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
  `is_sys` tinyint(4) NULL DEFAULT 0 COMMENT '是否是系统内置,1:是,0:否',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('0f45e0320d93832c90dd3ed323129dd6', '研发部', 'liu', '18888888888', '1396198931@qq.com', 0, '2023-10-22 12:18:00', '2023-10-27 17:56:12');
INSERT INTO `sys_dept` VALUES ('db3760209b816f6d350278dd2f3b1351', '销售部', 'liu', '1888888888', '1396198931@qq.com', 0, '2023-10-22 12:39:42', '2023-10-22 12:39:42');

-- ----------------------------
-- Table structure for sys_node_form
-- ----------------------------
DROP TABLE IF EXISTS `sys_node_form`;
CREATE TABLE `sys_node_form`  (
  `node_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '节点id',
  `deploy_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部署id',
  `activity_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '流程定义节点唯一标识',
  `form_json` json NOT NULL COMMENT '表单详情',
  `is_main_from` bigint(20) NOT NULL DEFAULT 0 COMMENT '是否是主表单,1:是,2:否',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '流程定义节点表单信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_node_form
-- ----------------------------
INSERT INTO `sys_node_form` VALUES ('02181d5af1acd27b7e858ea585d75d71', 'b114957d-8f3c-11ee-8bfd-30c9aba6c580', 'Event_1ev3y4u', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"radio109038\", \"key\": 22799, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio109038\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"textarea48784\", \"key\": 72054, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea48784\", \"rows\": 3, \"size\": \"\", \"label\": \"textarea\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 1, '2023-11-30 12:55:33');
INSERT INTO `sys_node_form` VALUES ('188ff0ac1acd33891747f9600b9f7711', 'a408e48a-8f3c-11ee-8bfd-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"radio27864\", \"key\": 77278, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio27864\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-30 12:55:11');
INSERT INTO `sys_node_form` VALUES ('1d87cb65b377bb00feab7e7dfafc42a1', '8a00cf03-8f48-11ee-889a-30c9aba6c580', 'Event_1ev3y4u', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"radio109038\", \"key\": 22799, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio109038\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"textarea48784\", \"key\": 72054, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea48784\", \"rows\": 3, \"size\": \"\", \"label\": \"textarea\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"select79648\", \"key\": 107703, \"icon\": \"select-field\", \"type\": \"select\", \"options\": {\"name\": \"select79648\", \"size\": \"\", \"label\": \"select\", \"hidden\": false, \"onBlur\": \"\", \"remote\": false, \"onFocus\": \"\", \"disabled\": false, \"multiple\": false, \"onChange\": \"\", \"required\": false, \"clearable\": true, \"onCreated\": \"\", \"onMounted\": \"\", \"filterable\": false, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"allowCreate\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"select 1\", \"value\": 1}, {\"label\": \"select 2\", \"value\": 2}, {\"label\": \"select 3\", \"value\": 3}], \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"multipleLimit\": 0, \"onRemoteQuery\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"automaticDropdown\": false, \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 1, '2023-11-30 14:20:21');
INSERT INTO `sys_node_form` VALUES ('28e8ac744d02f6b2e00e278432517e6c', 'de54c5eb-8f4f-11ee-a219-30c9aba6c580', 'Event_11v8quf', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"grid67770\", \"key\": 88809, \"cols\": [{\"id\": \"grid-col-71875\", \"icon\": \"grid-col\", \"type\": \"grid-col\", \"options\": {\"md\": 12, \"sm\": 12, \"xs\": 12, \"name\": \"gridCol71875\", \"pull\": 0, \"push\": 0, \"span\": 12, \"hidden\": false, \"offset\": 0, \"responsive\": false, \"customClass\": \"\"}, \"category\": \"container\", \"internal\": true, \"widgetList\": [{\"id\": \"number74749\", \"key\": 69932, \"icon\": \"number-field\", \"type\": \"number\", \"options\": {\"max\": 100, \"min\": 1, \"name\": \"number74749\", \"size\": \"\", \"step\": 1, \"label\": \"请假天数\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"disabled\": false, \"onChange\": \"\", \"required\": true, \"onCreated\": \"\", \"onMounted\": \"\", \"precision\": 0, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": 0, \"labelTooltip\": null, \"requiredHint\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"controlsPosition\": \"right\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}, {\"id\": \"grid-col-21799\", \"icon\": \"grid-col\", \"type\": \"grid-col\", \"options\": {\"md\": 12, \"sm\": 12, \"xs\": 12, \"name\": \"gridCol21799\", \"pull\": 0, \"push\": 0, \"span\": 12, \"hidden\": false, \"offset\": 0, \"responsive\": false, \"customClass\": \"\"}, \"category\": \"container\", \"internal\": true, \"widgetList\": [{\"id\": \"select16320\", \"key\": 106964, \"icon\": \"select-field\", \"type\": \"select\", \"options\": {\"name\": \"select16320\", \"size\": \"\", \"label\": \"请假类型\", \"hidden\": false, \"onBlur\": \"\", \"remote\": false, \"onFocus\": \"\", \"disabled\": false, \"multiple\": false, \"onChange\": \"\", \"required\": true, \"clearable\": true, \"onCreated\": \"\", \"onMounted\": \"\", \"filterable\": false, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"allowCreate\": false, \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"optionItems\": [{\"label\": \"病假\", \"value\": 1}, {\"label\": \"事假\", \"value\": 2}, {\"label\": \"其他\", \"value\": 3}], \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"multipleLimit\": 0, \"onRemoteQuery\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"automaticDropdown\": false, \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}], \"icon\": \"grid\", \"type\": \"grid\", \"options\": {\"name\": \"grid67770\", \"gutter\": 12, \"hidden\": false, \"colHeight\": null, \"customClass\": []}, \"category\": \"container\"}, {\"id\": \"textarea76205\", \"key\": 66152, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea76205\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"maxLength\": null, \"minLength\": null, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"showWordLimit\": false, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 1, '2023-11-30 15:12:49');
INSERT INTO `sys_node_form` VALUES ('38f1f70cc43b429092a83bec0951597b', '8a00cf03-8f48-11ee-889a-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"radio27864\", \"key\": 77278, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio27864\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-30 14:20:21');
INSERT INTO `sys_node_form` VALUES ('4c8a147d3c38760df9f7c01b2a473316', 'b114957d-8f3c-11ee-8bfd-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"radio27864\", \"key\": 77278, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio27864\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-30 12:55:33');
INSERT INTO `sys_node_form` VALUES ('6d8adb3d7102a201d616db6c7016e9ce', 'de54c5eb-8f4f-11ee-a219-30c9aba6c580', 'Activity_074i6hi', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"select71625\", \"key\": 15742, \"icon\": \"select-field\", \"type\": \"select\", \"options\": {\"name\": \"status\", \"size\": \"\", \"label\": \"审批状态\", \"hidden\": false, \"onBlur\": \"\", \"remote\": false, \"onFocus\": \"\", \"disabled\": false, \"multiple\": false, \"onChange\": \"\", \"required\": true, \"clearable\": true, \"onCreated\": \"\", \"onMounted\": \"\", \"filterable\": false, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"allowCreate\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"驳回\", \"value\": 1}, {\"label\": \"通过\", \"value\": 2}], \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"multipleLimit\": 0, \"onRemoteQuery\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"automaticDropdown\": false, \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-30 15:12:49');
INSERT INTO `sys_node_form` VALUES ('8a12aab65fb615da5fd441d97212c833', 'a408e48a-8f3c-11ee-8bfd-30c9aba6c580', 'Event_1ev3y4u', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"radio109038\", \"key\": 22799, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio109038\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"defaultValue\": null, \"displayStyle\": \"inline\", \"labelTooltip\": null, \"requiredHint\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"textarea48784\", \"key\": 72054, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea48784\", \"rows\": 3, \"size\": \"\", \"label\": \"textarea\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"maxLength\": null, \"minLength\": null, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"showWordLimit\": false, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 1, '2023-11-30 12:55:11');
INSERT INTO `sys_node_form` VALUES ('c33c56ef12f7d7dc5336b6659f9f2e88', '7170893b-8eb1-11ee-9278-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"maxLength\": null, \"minLength\": null, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"showWordLimit\": false, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-29 20:18:46');
INSERT INTO `sys_node_form` VALUES ('d217e3be957d8e54eed493fb884a6fdb', 'de54c5eb-8f4f-11ee-a219-30c9aba6c580', 'Activity_08326ys', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": []}', 0, '2023-11-30 15:12:49');
INSERT INTO `sys_node_form` VALUES ('dc0a484217605db3452890a55e289be0', '0718e156-8f2f-11ee-864f-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"radio27864\", \"key\": 77278, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio27864\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"displayStyle\": \"inline\", \"requiredHint\": \"\", \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-30 11:17:44');
INSERT INTO `sys_node_form` VALUES ('f818ae0974b980614bb141e2c83e263e', 'b8c5bfb4-8ebb-11ee-afff-30c9aba6c580', 'Activity_1jtdgj7', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"textarea102303\", \"key\": 51314, \"icon\": \"textarea-field\", \"type\": \"textarea\", \"options\": {\"name\": \"textarea102303\", \"rows\": 3, \"size\": \"\", \"label\": \"备注\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": \"\", \"requiredHint\": \"\", \"showWordLimit\": false, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"radio27864\", \"key\": 77278, \"icon\": \"radio-field\", \"type\": \"radio\", \"options\": {\"name\": \"radio27864\", \"size\": \"\", \"label\": \"radio\", \"border\": false, \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"buttonStyle\": false, \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"optionItems\": [{\"label\": \"radio 1\", \"value\": 1}, {\"label\": \"radio 2\", \"value\": 2}, {\"label\": \"radio 3\", \"value\": 3}], \"defaultValue\": null, \"displayStyle\": \"inline\", \"labelTooltip\": null, \"requiredHint\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"daterange95401\", \"key\": 51062, \"icon\": \"date-range-field\", \"type\": \"date-range\", \"options\": {\"name\": \"daterange95401\", \"size\": \"\", \"type\": \"daterange\", \"label\": \"date-range\", \"format\": \"YYYY-MM-DD\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"disabled\": false, \"editable\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"clearable\": true, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"valueFormat\": \"YYYY-MM-DD\", \"defaultValue\": null, \"labelTooltip\": null, \"requiredHint\": \"\", \"autoFullWidth\": true, \"endPlaceholder\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"startPlaceholder\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', 0, '2023-11-29 21:32:21');

-- ----------------------------
-- Table structure for sys_listener
-- ----------------------------
DROP TABLE IF EXISTS `sys_listener`;
CREATE TABLE `sys_listener`  (
   `listener_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '监听器id',
   `listener_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '监听器名称',
   `event` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '事件类型',
   `java_class` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'java类',
   `is_sys` tinyint(4) NULL DEFAULT 0 COMMENT '是否是系统内置,1:是,0:否',
   `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
   `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`listener_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '执行监听器' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_listener
-- ----------------------------
INSERT INTO `sys_listener` VALUES ('504ee91cbde9b16192c69871a25acb08', '测试1', 'take', 'com.activiti.modules.listener.MyExecutionListener', 0, 'xxxxx', '2023-12-06 23:17:42', '2023-12-06 22:55:00');


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
  `is_sys` tinyint(4) NULL DEFAULT 0 COMMENT '是否是系统内置,1:是,0:否',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '0f45e0320d93832c90dd3ed323129dd6', '管理员', '123', 'admin', '1396198931@qq.com', '18888888888', 0, '2023-10-11 14:26:47', '2023-10-11 14:26:47');
INSERT INTO `sys_user` VALUES ('10', 'db3760209b816f6d350278dd2f3b1351', '测试账号9', '123', 'test10', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:58', '2023-10-22 12:39:58');
INSERT INTO `sys_user` VALUES ('11', 'db3760209b816f6d350278dd2f3b1351', '测试账号10', '123', 'test11', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:59', '2023-10-22 12:39:59');
INSERT INTO `sys_user` VALUES ('12', 'db3760209b816f6d350278dd2f3b1351', '测试账号11', '123', 'test12', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:44:50', '2023-10-22 12:44:50');
INSERT INTO `sys_user` VALUES ('13', 'db3760209b816f6d350278dd2f3b1351', '测试账号12', '123', 'test13', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:45:50', '2023-10-22 12:45:50');
INSERT INTO `sys_user` VALUES ('14', 'db3760209b816f6d350278dd2f3b1351', '测试账号13', '123', 'test14', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:46:50', '2023-10-22 12:46:50');
INSERT INTO `sys_user` VALUES ('15', 'db3760209b816f6d350278dd2f3b1351', '测试账号14', '123', 'test15', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:47:50', '2023-10-22 12:47:50');
INSERT INTO `sys_user` VALUES ('16', 'db3760209b816f6d350278dd2f3b1351', '测试账号15', '123', 'test16', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:48:50', '2023-10-22 12:48:50');
INSERT INTO `sys_user` VALUES ('17', 'db3760209b816f6d350278dd2f3b1351', '测试账号16', '123', 'test17', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:49:50', '2023-10-22 12:49:50');
INSERT INTO `sys_user` VALUES ('18', 'db3760209b816f6d350278dd2f3b1351', '测试账号17', '123', 'test18', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:50:50', '2023-10-22 12:50:50');
INSERT INTO `sys_user` VALUES ('2', 'db3760209b816f6d350278dd2f3b1351', '测试账号1', '123', 'test', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:50', '2023-10-22 12:39:50');
INSERT INTO `sys_user` VALUES ('3', 'db3760209b816f6d350278dd2f3b1351', '测试账号2', '123', 'test2', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:51', '2023-10-22 12:39:51');
INSERT INTO `sys_user` VALUES ('4', 'db3760209b816f6d350278dd2f3b1351', '测试账号3', '123', 'test3', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:52', '2023-10-22 12:39:52');
INSERT INTO `sys_user` VALUES ('5', 'db3760209b816f6d350278dd2f3b1351', '测试账号4', '123', 'test5', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:53', '2023-10-22 12:39:53');
INSERT INTO `sys_user` VALUES ('6', 'db3760209b816f6d350278dd2f3b1351', '测试账号5', '123', 'test6', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:54', '2023-10-22 12:39:54');
INSERT INTO `sys_user` VALUES ('7', 'db3760209b816f6d350278dd2f3b1351', '测试账号6', '123', 'test7', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:55', '2023-10-22 12:39:55');
INSERT INTO `sys_user` VALUES ('8', 'db3760209b816f6d350278dd2f3b1351', '测试账号7', '123', 'test8', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:56', '2023-10-22 12:39:56');
INSERT INTO `sys_user` VALUES ('9', 'db3760209b816f6d350278dd2f3b1351', '测试账号8', '123', 'test9', '1396198931@qq.com', '18888888888', 0, '2023-10-22 12:39:57', '2023-10-22 12:39:57');

SET FOREIGN_KEY_CHECKS = 1;
