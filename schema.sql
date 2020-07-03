/*
 Navicat Premium Data Transfer

 Source Server         : GG
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 35.240.181.244:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 02/07/2020 16:20:12
*/
CREATE DATABASE IF NOT EXISTS test;
USE test;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tblUser
-- ----------------------------
DROP TABLE IF EXISTS `tblUser`;
CREATE TABLE `tblUser`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` int(11) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gender` int(11) NOT NULL,
  `identity_card` int(11) NULL DEFAULT NULL,
  `oauth2_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `oauth2_provider` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pass_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_by` int(11) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  UNIQUE INDEX `UK_41ghyi9y3ics4k7apr3alaqf`(`user_name`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for tblStudent
-- ----------------------------
DROP TABLE IF EXISTS `tblStudent`;
CREATE TABLE `tblStudent`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `date_of_birth` datetime(0) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for tblSubject
-- ----------------------------
DROP TABLE IF EXISTS `tblSubject`;
CREATE TABLE `tblSubject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `organization_id` bigint(20) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_time` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblCheckin
-- ----------------------------
DROP TABLE IF EXISTS `tblCheckin`;
CREATE TABLE `tblCheckin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `lession_id` bigint(20) NULL DEFAULT NULL,
  `present` bit(1) NOT NULL,
  `student_id` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`class_id`) REFERENCES `tblLession` (`id`),
  FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblClass
-- ----------------------------
DROP TABLE IF EXISTS `tblClass`;
CREATE TABLE `tblClass`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `group_class_id` bigint(20) NULL DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  FOREIGN KEY (`owner_id`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`group_class_id`) REFERENCES `tblGroupClass` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblGroupClass
-- ----------------------------
DROP TABLE IF EXISTS `tblGroupClass`;
CREATE TABLE `tblGroupClass`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `organization_id` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblLession
-- ----------------------------
DROP TABLE IF EXISTS `tblLession`;
CREATE TABLE `tblLession`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `subject_id` bigint(20) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblNotify
-- ----------------------------
DROP TABLE IF EXISTS `tblNotify`;
CREATE TABLE `tblNotify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblNotifyDevice
-- ----------------------------
DROP TABLE IF EXISTS `tblNotifyDevice`;
CREATE TABLE `tblNotifyDevice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblOrganization`;
CREATE TABLE `tblOrganization`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblParentStudent
-- ----------------------------
DROP TABLE IF EXISTS `tblParentStudent`;
CREATE TABLE `tblParentStudent`  (
  `student_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`student_id`, `user_id`),
  FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblStudentClass
-- ----------------------------
DROP TABLE IF EXISTS `tblStudentClass`;
CREATE TABLE `tblStudentClass`  (
  `class_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  PRIMARY KEY (`student_id`, `class_id`),
  FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`),
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tblUserOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblUserOrganization`;
CREATE TABLE `tblUserOrganization`  (
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`organization_id`, `user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;


SET FOREIGN_KEY_CHECKS = 1;
