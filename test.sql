/*
Navicat MySQL Data Transfer

Source Server         : GG
Source Server Version : 50731
Source Host           : 35.240.181.244:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50731
File Encoding         : 65001

Date: 2020-07-31 09:04:05
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `tblCheckin`
-- ----------------------------
DROP TABLE IF EXISTS `tblCheckin`;
CREATE TABLE `tblCheckin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_id` bigint(20) DEFAULT NULL,
  `lession_id` bigint(20) DEFAULT NULL,
  `present` bit(1) NOT NULL,
  `student_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`) USING BTREE,
  KEY `student_id` (`student_id`) USING BTREE,
  KEY `tblCheckin_ibfk_1` (`class_id`) USING BTREE,
  CONSTRAINT `tblCheckin_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  CONSTRAINT `tblCheckin_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblCheckin_ibfk_3` FOREIGN KEY (`student_id`) REFERENCES `tblStudent` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblCheckin
-- ----------------------------

-- ----------------------------
-- Table structure for `tblClass`
-- ----------------------------
DROP TABLE IF EXISTS `tblClass`;
CREATE TABLE `tblClass` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_class_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_id` (`owner_id`) USING BTREE,
  KEY `group_class_id` (`group_class_id`) USING BTREE,
  CONSTRAINT `tblClass_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblClass_ibfk_2` FOREIGN KEY (`group_class_id`) REFERENCES `tblGroupClass` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblClass
-- ----------------------------
INSERT INTO tblClass VALUES ('1', 'Spring boot lớp 1', 'No note', null, '1', '2', null, null, null, null);
INSERT INTO tblClass VALUES ('2', 'Spring boot  2', 'Note', null, '1', '2', null, null, null, null);

-- ----------------------------
-- Table structure for `tblGroupClass`
-- ----------------------------
DROP TABLE IF EXISTS `tblGroupClass`;
CREATE TABLE `tblGroupClass` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `school_year` year(4) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `organization_id` (`organization_id`),
  CONSTRAINT `tblGroupClass_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblGroupClass
-- ----------------------------
INSERT INTO tblGroupClass VALUES ('1', 'Lớp 1', null, '2020', '1', null, null, null, null);
INSERT INTO tblGroupClass VALUES ('2', 'Lớp 2', null, '2020', '1', null, null, null, null);

-- ----------------------------
-- Table structure for `tblLession`
-- ----------------------------
DROP TABLE IF EXISTS `tblLession`;
CREATE TABLE `tblLession` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_id` bigint(20) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `subject_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `class_id` (`class_id`) USING BTREE,
  KEY `subject_id` (`subject_id`) USING BTREE,
  CONSTRAINT `tblLession_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblLession_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  CONSTRAINT `tblLession_ibfk_3` FOREIGN KEY (`subject_id`) REFERENCES `tblSubject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblLession
-- ----------------------------

-- ----------------------------
-- Table structure for `tblNotify`
-- ----------------------------
DROP TABLE IF EXISTS `tblNotify`;
CREATE TABLE `tblNotify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8mb4_unicode_ci,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `school_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblNotify
-- ----------------------------
INSERT INTO tblNotify VALUES ('1', 'Nội dung thông báo', 'ACTIVE', 'Test Notify', '1', '1', '2020-07-31 02:00:05', null, null);

-- ----------------------------
-- Table structure for `tblNotifyDevice`
-- ----------------------------
DROP TABLE IF EXISTS `tblNotifyDevice`;
CREATE TABLE `tblNotifyDevice` (
  `notify_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `device_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_read` bit(1) DEFAULT b'0',
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`notify_id`,`device_token`),
  CONSTRAINT `tblNotifyDevice_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `tblNotify` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblNotifyDevice
-- ----------------------------
INSERT INTO tblNotifyDevice VALUES ('1', '4', 'efaStG33moI:APA91bE6tWZKnRhnPFQoWqon_aIMixsjiY14Lf-Go2v5CZbmgNelO8ugOotbCv1URZVtBqdrTYtj4kj1sghE7-vm8AJTpqkxoh4LKYWnw-0zSf6ahK2MVevbbyMwk1zPELRBoBhb5eL9', 'COMPLETE', null, '', '1', '2020-07-31 02:00:05', '2020-07-31 02:01:01', null);

-- ----------------------------
-- Table structure for `tblOrganization`
-- ----------------------------
DROP TABLE IF EXISTS `tblOrganization`;
CREATE TABLE `tblOrganization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tblOrganization_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblOrganization_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblOrganization
-- ----------------------------
INSERT INTO tblOrganization VALUES ('1', 'Dũng Tiến, Thường Tín, Hà Nội', 'THPT', null, '0962543321', '1', '1', null, null, null);

-- ----------------------------
-- Table structure for `tblStudent`
-- ----------------------------
DROP TABLE IF EXISTS `tblStudent`;
CREATE TABLE `tblStudent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_of_birth` datetime DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `class_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `class_id` (`class_id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `tblStudent_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `tblClass` (`id`),
  CONSTRAINT `tblStudent_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `tblUser` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblStudent
-- ----------------------------
INSERT INTO tblStudent VALUES ('1', 'Dũng Tiến, Thường Tín, Hà Nội', '/resources/image/avatar2.png', '2020-07-30 00:00:00', null, 'Nguyễn Minh Phương', null, '0962543321', '1', '4', null, null, null, null);

-- ----------------------------
-- Table structure for `tblSubject`
-- ----------------------------
DROP TABLE IF EXISTS `tblSubject`;
CREATE TABLE `tblSubject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `organization_id` (`organization_id`) USING BTREE,
  CONSTRAINT `tblSubject_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblSubject
-- ----------------------------
INSERT INTO tblSubject VALUES ('1', 'Spring Oauth2', 'Note', '1', 'ACTIVE', '1', '2020-07-31 01:50:36', null, null);
INSERT INTO tblSubject VALUES ('2', 'Spring boot Authorize', null, '1', 'DRAFT', '1', '2020-07-31 01:54:09', null, null);

-- ----------------------------
-- Table structure for `tblUser`
-- ----------------------------
DROP TABLE IF EXISTS `tblUser`;
CREATE TABLE `tblUser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` int(1) NOT NULL,
  `identity_card` int(11) DEFAULT NULL,
  `oauth2_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `oauth2_provider` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pass_word` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblUser
-- ----------------------------
INSERT INTO tblUser VALUES ('1', 'admin', null, '/resources/image/1588848512591.JPEG', 'trungtrandb@gmail.com', 'Trần Quang Trung', '0', null, null, null, '$2a$10$SNpM9SIASfw9.OmzY6pkc.9at.N3ZHEV0d8EEwUuHmNzNI7H4oxZW', '', 'ROLE_USER', 'ACTIVE', null, '2020-07-30 08:40:47', null, '2020-07-30 08:41:54');
INSERT INTO tblUser VALUES ('2', 'trungtrandb1@gmail.com', 'Dũng Tiến, Thường Tín, Hà Nội', null, 'trungtrandb1@gmail.com', 'Nguyễn Minh Phương', '0', null, null, null, '$2a$10$uNT3CpyQBdPBTRCY70tUt.8FikgDHkQzAKRx6yIjjE.pSGPgpaoB.', '0962543321', 'ROLE_USER', 'ACTIVE', '1', '2020-07-30 08:58:17', null, '2020-07-30 10:26:43');
INSERT INTO tblUser VALUES ('3', 'trungtq@sopen.vn', 'Dũng Tiến, Thường Tín, Hà Nội', null, 'trungtq@sopen.vn', 'Nguyễn Minh Phương 1', '0', null, null, null, '$2a$10$OOXr/FwuNEGybigaQUw0m.5Upvk6eZjrfj0wi4WsVIYscuKfGHLQy', '0962543321', 'ROLE_USER', 'ACTIVE', '1', '2020-07-30 08:58:46', null, null);
INSERT INTO tblUser VALUES ('4', 'trungtrandb@gmail.com', 'Dũng Tiến, Thường Tín, Hà Nội', null, 'trungtrandb@gmail.com', 'Nguyễn Minh Phương', '0', null, null, null, '$2a$10$iViGjJKrJCZCBbZ0NJ25I.rg3AO0cvawx3hQWzN4U12aKqkLlNPWC', null, 'ROLE_USER', 'ACTIVE', null, '2020-07-31 01:56:23', null, null);

-- ----------------------------
-- Table structure for `tblUserDevice`
-- ----------------------------
DROP TABLE IF EXISTS `tblUserDevice`;
CREATE TABLE `tblUserDevice` (
  `device_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`device_token`),
  KEY `device_token` (`device_token`),
  CONSTRAINT `tblUserDevice_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblUserDevice
-- ----------------------------
INSERT INTO tblUserDevice VALUES ('efaStG33moI:APA91bE6tWZKnRhnPFQoWqon_aIMixsjiY14Lf-Go2v5CZbmgNelO8ugOotbCv1URZVtBqdrTYtj4kj1sghE7-vm8AJTpqkxoh4LKYWnw-0zSf6ahK2MVevbbyMwk1zPELRBoBhb5eL9', '1');
INSERT INTO tblUserDevice VALUES ('efaStG33moI:APA91bE6tWZKnRhnPFQoWqon_aIMixsjiY14Lf-Go2v5CZbmgNelO8ugOotbCv1URZVtBqdrTYtj4kj1sghE7-vm8AJTpqkxoh4LKYWnw-0zSf6ahK2MVevbbyMwk1zPELRBoBhb5eL9', '4');

-- ----------------------------
-- Table structure for `tblUserOrganization`
-- ----------------------------
DROP TABLE IF EXISTS `tblUserOrganization`;
CREATE TABLE `tblUserOrganization` (
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  PRIMARY KEY (`organization_id`,`user_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `tblUserOrganization_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tblUser` (`id`),
  CONSTRAINT `tblUserOrganization_ibfk_2` FOREIGN KEY (`organization_id`) REFERENCES `tblOrganization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tblUserOrganization
-- ----------------------------
INSERT INTO tblUserOrganization VALUES ('2', '1');
INSERT INTO tblUserOrganization VALUES ('3', '1');

-- ----------------------------
-- Procedure structure for `getByUserName`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getByUserName`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `getByUserName`(
	IN userName varchar(250)
)
BEGIN
	select * from tblUser where user_name = userName;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `getDeviceByClassIds`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getDeviceByClassIds`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `getDeviceByClassIds`(
	IN classIds VARCHAR(250)
)
BEGIN
	SELECT ud.* FROM tblUserDevice ud
    JOIN tblStudent s on s.parent_id = ud.user_id 
    WHERE FIND_IN_SET(s.class_id, classIds);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `getStudentByClassIds`
-- ----------------------------
DROP PROCEDURE IF EXISTS `getStudentByClassIds`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `getStudentByClassIds`(
	IN classIds VARCHAR(100)
)
BEGIN
	SELECT s.*, c.name as class_name, u.full_name FROM tblStudent s
    JOIN tblClass c on s.class_id = c.id 
    JOIN tblUser u on u.id = s.parent_id 
    WHERE FIND_IN_SET(s.class_id, classIds);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `updateStatusRead`
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateStatusRead`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `updateStatusRead`(
	IN notifyIds VARCHAR(250),
	IN userId BIGINT
)
BEGIN
	UPDATE tblNotifyDevice nd
	SET nd.is_read = 1
  WHERE FIND_IN_SET(nd.notify_id, notifyIds) AND nd.user_id = userId;
END
;;
DELIMITER ;
