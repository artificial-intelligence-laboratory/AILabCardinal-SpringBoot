/*
 Navicat Premium Data Transfer

 Source Server         : Hf
 Source Server Type    : MySQL
 Source Server Version : 80023 (8.0.23)
 Source Host           : localhost:3306
 Source Schema         : ailabcardinal

 Target Server Type    : MySQL
 Target Server Version : 80023 (8.0.23)
 File Encoding         : 65001

 Date: 26/02/2023 15:59:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award`  (
  `award_id` int NOT NULL AUTO_INCREMENT COMMENT '奖项id',
  `competition_id` int NOT NULL COMMENT '比赛id',
  `user_id` int NOT NULL COMMENT '用户id',
  `award_level` tinyint NOT NULL COMMENT '奖项级别（0代表国家级、1代表省级、2代表校级）',
  `award_time` date NOT NULL COMMENT '获奖时间',
  `role` tinyint NOT NULL COMMENT '角色（0代表小组负责人、1代表小组成员、2代表独狼）',
  PRIMARY KEY (`award_id`) USING BTREE,
  INDEX `fk_award_competition_id`(`competition_id` ASC) USING BTREE,
  INDEX `fk_award_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_award_competition_id` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`competition_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_award_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for competition
-- ----------------------------
DROP TABLE IF EXISTS `competition`;
CREATE TABLE `competition`  (
  `competition_id` int NOT NULL AUTO_INCREMENT COMMENT '比赛id',
  `competition_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '比赛名',
  `year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年度',
  `competition_status` tinyint NOT NULL COMMENT '比赛状态（0代表正在参赛、1代表已经结束）',
  PRIMARY KEY (`competition_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for competition_situation
-- ----------------------------
DROP TABLE IF EXISTS `competition_situation`;
CREATE TABLE `competition_situation`  (
  `competition_situation_id` int NOT NULL AUTO_INCREMENT COMMENT '比赛参加情况id',
  `competition_id` int NOT NULL COMMENT '比赛id',
  `user_id` int NOT NULL COMMENT '用户id',
  PRIMARY KEY (`competition_situation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for in_out_registration
-- ----------------------------
DROP TABLE IF EXISTS `in_out_registration`;
CREATE TABLE `in_out_registration`  (
  `sign_in_id` int NOT NULL AUTO_INCREMENT COMMENT '签到id',
  `sign_in_time` datetime NULL DEFAULT NULL COMMENT '签到时间',
  `sign_in_user_real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签到者姓名',
  `sign_in_user_class` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签到者班级',
  `student_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `task` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务',
  `check_out_time` datetime NULL DEFAULT NULL COMMENT '签出时间',
  `remark` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`sign_in_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100031 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for intranet
-- ----------------------------
DROP TABLE IF EXISTS `intranet`;
CREATE TABLE `intranet`  (
  `intranet_id` int NOT NULL COMMENT '内网IP的id',
  `user_id` int NULL DEFAULT NULL COMMENT '内网所有者的id',
  `intranet_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内网IP',
  PRIMARY KEY (`intranet_id`) USING BTREE,
  UNIQUE INDEX `intranet_ip_index`(`intranet_ip` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名',
  `level` tinyint NOT NULL COMMENT '奖项级别（0代表国家级、1代表省级、2代表校级）',
  `year` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年度',
  `teacher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '指导老师',
  `status` tinyint NOT NULL COMMENT '项目状态（0代表未完成、1代表已完成）',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_member
-- ----------------------------
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE `project_member`  (
  `project_user_id` int NOT NULL AUTO_INCREMENT COMMENT '项目与用户关系id',
  `project_id` int NOT NULL COMMENT '项目id',
  `user_id` int NOT NULL COMMENT '用户id',
  `role` tinyint NOT NULL COMMENT '角色（0代表负责人、1代表普通成员）',
  PRIMARY KEY (`project_user_id`) USING BTREE,
  INDEX `fk_project_id`(`project_id` ASC) USING BTREE,
  INDEX `fk_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site`  (
  `site_id` int NOT NULL AUTO_INCREMENT COMMENT '网站id',
  `site_show_serial_number` int NULL DEFAULT NULL COMMENT '显示序号',
  `site_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站名',
  `site_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接',
  `site_intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `site_type_code` int NULL DEFAULT NULL COMMENT '网站类型代码',
  `site_info_remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `enter_user_id` int NULL DEFAULT NULL COMMENT '录入用户id',
  `site_info_status` int NULL DEFAULT 1 COMMENT '网站信息状态（0代表被删除，1代表正常）',
  `site_record_time` datetime NULL DEFAULT NULL COMMENT '网站录入时间',
  PRIMARY KEY (`site_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for site_type
-- ----------------------------
DROP TABLE IF EXISTS `site_type`;
CREATE TABLE `site_type`  (
  `site_type_id` int NOT NULL AUTO_INCREMENT COMMENT '网站类型id',
  `site_type_show_serial_number` int NULL DEFAULT NULL COMMENT '网站类型显示序号',
  `site_type_code` int NULL DEFAULT NULL COMMENT '网站类型代码',
  `site_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站类型',
  PRIMARY KEY (`site_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `student_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学号',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `user_right` int NOT NULL DEFAULT 2 COMMENT '0代表管理员，1代表老师，2代表实验室负责人，3代表实验室成员，4代表实验室合作伙伴，5代表非实验室成员',
  `last_online_time` datetime NULL DEFAULT NULL COMMENT '最后一次上线时间',
  `last_online_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次上线的ip',
  `last_online_ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次上线的ip',
  `user_status` int NOT NULL DEFAULT 1 COMMENT '状态码（0代表账号被禁用，1代表正常，默认为1， 2表示已经毕业）',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_name`(`student_number` ASC) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_info_id` int NOT NULL AUTO_INCREMENT COMMENT '用户信息id',
  `user_id` int NOT NULL COMMENT '用户id',
  `real_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `enrollment_year` datetime NOT NULL COMMENT '入学年份',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户的年级',
  `native_place` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `join_ailab_time` datetime NULL DEFAULT NULL COMMENT '加入实验室时间',
  `development_direction` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开发方向',
  `college` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `class_number` int NULL DEFAULT NULL COMMENT '班级',
  `github_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'github id',
  `dormitory_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '宿舍号',
  PRIMARY KEY (`user_info_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
