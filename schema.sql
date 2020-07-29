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
  `class_id` bigint(20) NULL ,
  `lession_id` bigint(20) NULL ,
  `present` bit(1) NOT NULL,
  `student_id` bigint(20) NULL ,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  INDEX `created_by`(`created_by`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `tblCheckin_ibfk_1`(`class_id`) USING BTREE,
  CONSTRAINT `tblCheckin_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  CONSTRAINT `tblCheckin_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblCheckin_ibfk_3` FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblClass
-- ----------------------------
DROP TABLE IF EXISTS `tblClass`;
CREATE TABLE `tblClass`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `group_class_id` bigint(20) NULL ,
  `owner_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  INDEX `group_class_id`(`group_class_id`) USING BTREE,
  FOREIGN KEY (`owner_id`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`group_class_id`) REFERENCES `tblGroupClass` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblGroupClass
-- ----------------------------
DROP TABLE IF EXISTS `tblGroupClass`;
CREATE TABLE `tblGroupClass`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `school_year` year(4) NULL ,
  `organization_id` bigint(20) NULL ,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblLession
-- ----------------------------
DROP TABLE IF EXISTS `tblLession`;
CREATE TABLE `tblLession`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `class_id` bigint(20) NULL ,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `end_time` datetime(0) NULL ,
  `start_time` datetime(0) NULL ,
  `subject_id` bigint(20) NULL ,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `user_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  FOREIGN KEY (`subject_id`) REFERENCES `tblSubject` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblNotify
-- ----------------------------
DROP TABLE IF EXISTS `tblNotify`;
CREATE TABLE `tblNotify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `school_id` bigint(20), 
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblUserDevice
-- ----------------------------
DROP TABLE IF EXISTS `tblUserDevice`;
CREATE TABLE `tblUserDevice` (
  `device_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`device_token`),
  KEY `device_token` (`device_token`),
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblNotifyDevice
-- ----------------------------
DROP TABLE IF EXISTS `tblNotifyDevice`;
CREATE TABLE `tblNotifyDevice`  (
  `notify_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `device_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  PRIMARY KEY (`notify_id`, `device_token`),
  FOREIGN KEY (`notify_id`) REFERENCES `tblNotify` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblOrganization`;
CREATE TABLE `tblOrganization`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `user_id` bigint(20) NULL ,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblStudent
-- ----------------------------
DROP TABLE IF EXISTS `tblStudent`;
CREATE TABLE `tblStudent`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `date_of_birth` datetime(0) NULL ,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `class_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_date` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  FOREIGN KEY (`parent_id`) REFERENCES `tblUser` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblSubject
-- ----------------------------
DROP TABLE IF EXISTS `tblSubject`;
CREATE TABLE `tblSubject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `organization_id` bigint(20) NULL ,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `created_by` bigint(20) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_time` datetime(0) NULL ,
  `updated_by` bigint(20) NULL ,
  INDEX `organization_id`(`organization_id`) USING BTREE,
  CONSTRAINT `tblSubject_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblUser
-- ----------------------------
DROP TABLE IF EXISTS `tblUser`;
CREATE TABLE `tblUser`  (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `gender` int(1) NOT NULL,
  `identity_card` int(11) NULL ,
  `oauth2_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `oauth2_provider` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `pass_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `created_by` int(11) NULL ,
  `created_date` datetime(0) NULL ,
  `updated_by` int(11) NULL ,
  `updated_date` datetime(0) NULL 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tblUserOrganization
-- ----------------------------
DROP TABLE IF EXISTS `tblUserOrganization`;
CREATE TABLE `tblUserOrganization`  (
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`organization_id`, `user_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
	FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
	FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;


--------------------------------- Stored Procedure -----------------------------
-- getStudentByClassIds
DROP procedure IF EXISTS `getStudentByClassIds`;
DELIMITER $$
USE `test`$$
CREATE PROCEDURE `getStudentByClassIds` (
	IN classIds VARCHAR(100)
)
BEGIN
	SELECT s.*, c.name as class_name, u.full_name FROM tblStudent s
    JOIN tblClass c on s.class_id = c.id 
    JOIN tblUser u on u.id = s.parent_id 
    WHERE FIND_IN_SET(s.class_id, classIds);
END$$
DELIMITER ;


-- getDeviceByClassIds
DROP procedure IF EXISTS `getDeviceByClassIds`;
DELIMITER $$
USE `test`$$
CREATE PROCEDURE `getDeviceByClassIds` (
	IN classIds VARCHAR(250)
)
BEGIN
	SELECT ud.* FROM tblUserDevice ud
    JOIN tblStudent s on s.parent_id = ud.user_id 
    WHERE FIND_IN_SET(s.class_id, classIds);
END$$
DELIMITER ;