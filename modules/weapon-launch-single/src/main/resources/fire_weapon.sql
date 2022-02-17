/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.5.42
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 192.168.5.42:3306
 Source Schema         : weapon-test

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 10/11/2021 18:36:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Records of fire_weapon
-- ----------------------------
BEGIN;
INSERT IGNORE INTO `fire_weapon` VALUES ('1', '搜索雷达', '001', '3', NULL, NULL, 'Shizuan',1, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('2', '舰空导弹跟踪雷达', '002', '3', NULL, NULL, 'Shizuan', 2, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('3', '舰空导弹火控系统', '003', '1', NULL, NULL, 'Shizuan', 3, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('4', '舰空导弹发射系统', '004', '1', NULL, NULL, 'Shizuan', 4, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('5', '近程防空导弹', '005', '1', NULL, NULL, 'Shizuan', 5, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('6', '中程防空导弹', '006', '1', NULL, NULL, 'Shizuan', 6, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('7', '远程防空导弹', '007', '1', NULL, NULL, 'Shizuan', 7, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('8', '反舰导弹', '008', '1', NULL, NULL, 'Shizuan', 8, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('9', '反潜导弹', '009', '1', NULL, NULL, 'Shizuan', 9, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('10', '主炮', '010', '1', NULL, NULL, 'Shizuan', 10, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('11', '主炮跟踪雷达', '011', '3', NULL, NULL, 'Shizuan',11, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('12', '反导舰炮跟踪雷达', '012', '3', NULL, NULL, 'Shizuan',12, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('13', '反导舰炮火控系统', '013', '1', NULL, NULL, 'Shizuan',13, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('14', '反导舰炮', '014', '1', NULL, NULL, 'Shizuan',14, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('15', '舰载机', '015', '1', NULL, NULL, 'Shizuan',15, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('16', '舰壳声纳', '016', '2', NULL, NULL, 'Shizuan',16, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('17', '拖曳声纳', '017', '2', NULL, NULL, 'Shizuan',17, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('18', '鱼雷防御武器火控系统', '018', '1', NULL, NULL, 'Shizuan',18, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('19', '鱼雷防御武器发射系统', '019', '1', NULL, NULL, 'Shizuan',19, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('20', '鱼雷防御武器', '020', '1', NULL, NULL, 'Shizuan',20, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('21', '电子侦察设备', '021', '3', NULL, NULL, 'Shizuan',21, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('22', '电子对抗武器火控系统', '022', '1', NULL, NULL, 'Shizuan',22, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('23', '多功能发射系统', '023', '1', NULL, NULL, 'Shizuan',23, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('24', '舷外电子对抗武器', '024', '1', NULL, NULL, 'Shizuan',24, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('25', '舷外无源电子干扰弹', '025', '2', NULL, NULL, 'Shizuan',25, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('26', '舷内电子对抗武器', '026', '1', NULL, NULL, 'Shizuan',26, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('27', '舰载电子侦察设备', '027', '3', NULL, NULL, 'Shizuan',27, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('28', '水声对抗武器火控系统', '028', '1', NULL, NULL, 'Shizuan',28, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('29', '水声对抗武器', '029', '1', NULL, NULL, 'Shizuan',29, NULL, 0, 0, 0, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
