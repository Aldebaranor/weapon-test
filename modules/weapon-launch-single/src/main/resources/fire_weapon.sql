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
INSERT IGNORE  INTO `fire_weapon` VALUES ('1', '近程防空导弹', '001', '1', NULL, NULL, 'Xinlai', 1, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('10', '舷内电子干扰设备', '010', '2', NULL, NULL, 'Xinlai', 10, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('11', '舰载电子干扰设备', '011', '2', NULL, NULL, 'Xinlai', 11, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('12', '水声对抗器材', '012', '2', NULL, NULL, 'Xinlai', 12, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('13', '舰壳声呐', '013', '3', NULL, NULL, 'Xinlai', 13, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('14', '拖拽声呐', '014', '3', NULL, NULL, 'Xinlai', 14, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('15', '搜索雷达', '015', '3', NULL, NULL, 'Xinlai', 15, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('16', '主炮跟踪雷达', '016', '3', NULL, NULL, 'Xinlai', 16, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('17', '万发炮跟踪雷达', '017', '3', NULL, NULL, 'Xinlai', 17, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('18', '舰载机', '018', '1', NULL, NULL, 'Xinlai', 18, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('2', '中程防空导弹', '002', '1', NULL, NULL, 'Xinlai', 2, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('3', '远程防空导弹', '003', '1', NULL, NULL, 'Xinlai', 3, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('4', '反舰导弹', '004', '1', NULL, NULL, 'Xinlai', 4, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('5', '反潜导弹', '005', '1', NULL, NULL, 'Xinlai', 5, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('6', '主炮', '006', '1', NULL, NULL, 'Xinlai', 6, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('7', '万发炮', '007', '1', NULL, NULL, 'Xinlai', 7, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('8', '舷外有源电子干扰弹', '008', '2', NULL, NULL, 'Xinlai', 8, NULL, 0, 0, 0, 0);
INSERT IGNORE INTO `fire_weapon` VALUES ('9', '舷外无源电子干扰弹', '009', '2', NULL, NULL, 'Xinlai', 9, NULL, 0, 0, 0, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
