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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `lession_id` bigint(20) NULL DEFAULT NULL,
  `present` bit(1) NOT NULL,
  `student_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
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
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `group_class_id` bigint(20) NULL DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `is_organization_class` bit(1) NULL DEFAULT NULL,
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
  `organization_id` bigint(20) NULL DEFAULT NULL,
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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  CONSTRAINT `tblLession_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblLession_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblLession_ibfk_3` FOREIGN KEY (`subject_id`) REFERENCES `tblSubject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tblLession
-- ----------------------------
INSERT INTO `tblLession` VALUES (2, 3, NULL, '2020-07-29 12:00:00', '2020-07-08 12:00:00', 1, 'Spring boot', 7);
INSERT INTO `tblLession` VALUES (3, 3, NULL, '2020-07-03 12:00:00', '2020-07-01 12:00:00', 2, 'Spring boot 1', 6);

-- ----------------------------
-- Table structure for tblNotify
-- ----------------------------
DROP TABLE IF EXISTS `tblNotify`;
CREATE TABLE `tblNotify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
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
  `created_by` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  CONSTRAINT `tblOrganization_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblOrganization_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblParentStudent
-- ----------------------------
DROP TABLE IF EXISTS `tblParentStudent`;
CREATE TABLE `tblParentStudent`  (
  `student_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`student_id`, `user_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `tblParentStudent_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblParentStudent_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblStudentClass
-- ----------------------------
DROP TABLE IF EXISTS `tblStudentClass`;
CREATE TABLE `tblStudentClass`  (
  `class_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  PRIMARY KEY (`student_id`, `class_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  CONSTRAINT `tblStudentClass_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tblStudentClass_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblSubject
-- ----------------------------
DROP TABLE IF EXISTS `tblSubject`;
CREATE TABLE `tblSubject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `organization_id` bigint(20) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_time` datetime(0) NULL DEFAULT NULL,
  `updated_by` bigint(20) NULL DEFAULT NULL,
  `class_id` bigint(20) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `organization_id`(`organization_id`) USING BTREE,
  CONSTRAINT `tblSubject_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tblUser
-- ----------------------------
DROP TABLE IF EXISTS `tblUser`;
CREATE TABLE `tblUser`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_41ghyi9y3ics4k7apr3alaqf`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tblUser
-- ----------------------------
INSERT INTO `tblUser` VALUES (2, 'admin', 'Địa chỉ', '/resources/image/1588848512591.JPEG', NULL, '2020-07-07 05:37:56', 'trungtrandb@gmai.com', 'TrungTQ', 0, NULL, NULL, NULL, '$2a$10$ZK2fjP7AuaHpkVoqCORny.oKSIEFaJ2HERSyqwFFwEWRAMZpiQYsy', '0987654321', 'ROLE_USER', 'ACTIVE', NULL, '2020-07-07 05:38:12');

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
