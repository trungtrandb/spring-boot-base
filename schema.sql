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

 Date: 09/07/2020 14:56:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tblCheckin
-- ----------------------------
DROP TABLE IF EXISTS `tblCheckin`;
CREATE TABLE `tblCheckin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `lession_id` bigint(20) NULL DEFAULT NULL,
  `present` bit(1) NOT NULL,
  `student_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  INDEX `created_by`(`created_by`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `tblCheckin_ibfk_1`(`class_id`) USING BTREE,
  CONSTRAINT `tblCheckin_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblCheckin_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblCheckin_ibfk_3` FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblClass
-- ----------------------------
DROP TABLE IF EXISTS `tblClass`;
CREATE TABLE `tblClass`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `group_class_id` bigint(20) NULL DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  INDEX `group_class_id`(`group_class_id`) USING BTREE,
  CONSTRAINT `tblClass_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblClass_ibfk_2` FOREIGN KEY (`group_class_id`) REFERENCES `tblGroupClass` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblGroupClass
-- ----------------------------
DROP TABLE IF EXISTS `tblGroupClass`;
CREATE TABLE `tblGroupClass`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `school_year` year(4) NULL DEFAULT NULL,
  `organization_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  CONSTRAINT `tblGroupClass_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblLession
-- ----------------------------
DROP TABLE IF EXISTS `tblLession`;
CREATE TABLE `tblLession`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `subject_id` bigint(20) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  CONSTRAINT `tblLession_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblLession_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblLession_ibfk_3` FOREIGN KEY (`subject_id`) REFERENCES `tblSubject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblNotify
-- ----------------------------
DROP TABLE IF EXISTS `tblNotify`;
CREATE TABLE `tblNotify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `tblNotify_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblNotifyDevice
-- ----------------------------
DROP TABLE IF EXISTS `tblNotifyDevice`;
CREATE TABLE `tblNotifyDevice`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `tblNotifyDevice_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblOrganization`;
CREATE TABLE `tblOrganization`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  CONSTRAINT `tblOrganization_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblOrganization_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblStudent
-- ----------------------------
DROP TABLE IF EXISTS `tblStudent`;
CREATE TABLE `tblStudent`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `date_of_birth` datetime(0) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `class_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  FOREIGN KEY (`parent_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblSubject
-- ----------------------------
DROP TABLE IF EXISTS `tblSubject`;
CREATE TABLE `tblSubject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `organization_id` bigint(20) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_time` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  INDEX `organization_id`(`organization_id`) USING BTREE,
  CONSTRAINT `tblSubject_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblUser
-- ----------------------------
DROP TABLE IF EXISTS `tblUser`;
CREATE TABLE `tblUser`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gender` int(1) NOT NULL,
  `identity_card` int(11) NULL DEFAULT NULL,
  `oauth2_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `oauth2_provider` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pass_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` int(11) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `updated_by` int(11) NULL DEFAULT NULL,
  `updated_date` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblUserOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblUserOrganization`;
CREATE TABLE `tblUserOrganization`  (
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`organization_id`, `user_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `tblUserOrganization_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblUserOrganization_ibfk_2` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
