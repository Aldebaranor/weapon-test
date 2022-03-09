/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : weapon-test

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 09/03/2022 10:15:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of fire_threshold
-- ----------------------------
INSERT INTO `fire_threshold` VALUES ('1', '发射时间安全间隔', '001', '火力兼容', '2022-03-09 09:34:12', NULL, 'sz', NULL, NULL, '3s');
INSERT INTO `fire_threshold` VALUES ('10', '反导舰炮发射装置位置', '010', '火力兼容', '2022-03-09 09:45:00', NULL, 'sz', NULL, NULL, 'x=26m,y=15m');
INSERT INTO `fire_threshold` VALUES ('11', '舷外干扰发射装置位置', '011', '火力兼容', '2022-03-09 09:45:32', NULL, 'sz', NULL, NULL, 'x=-25m,y=-15m');
INSERT INTO `fire_threshold` VALUES ('12', '工作频率安全间隔', '012', '电磁兼容', '2022-03-09 09:46:05', NULL, 'sz', NULL, NULL, '10MHz');
INSERT INTO `fire_threshold` VALUES ('13', '工作频段安全间隔', '013', '水声兼容', '2022-03-09 09:46:46', NULL, 'sz', NULL, NULL, '5KHz');
INSERT INTO `fire_threshold` VALUES ('2', '发射偏转角偏差', '002', '火力兼容', '2022-03-09 09:35:02', NULL, 'sz', NULL, NULL, '0.05rad');
INSERT INTO `fire_threshold` VALUES ('3', '发射俯仰角偏差', '003', '火力兼容', '2022-03-09 09:35:52', NULL, 'sz', NULL, NULL, '0.05rad');
INSERT INTO `fire_threshold` VALUES ('4', '近程防空导弹发射架位置', '004', '火力兼容', '2022-03-09 09:36:19', NULL, 'sz', NULL, NULL, 'x=18m,y=5m');
INSERT INTO `fire_threshold` VALUES ('5', '中程防空导弹发射架位置', '005', '火力兼容', '2022-03-09 09:37:08', NULL, 'sz', NULL, NULL, 'x=23m,y=5m');
INSERT INTO `fire_threshold` VALUES ('6', '远程防空导弹发射架位置', '006', '火力兼容', '2022-03-09 09:37:40', NULL, 'sz', NULL, NULL, 'x=23m,y=-5m');
INSERT INTO `fire_threshold` VALUES ('7', '反舰导弹发射架位置', '007', '火力兼容', '2022-03-09 09:38:48', NULL, 'sz', NULL, NULL, 'x=-20m,y=-10m');
INSERT INTO `fire_threshold` VALUES ('8', '反潜导弹发射架位置', '008', '火力兼容', '2022-03-09 09:39:36', NULL, 'sz', NULL, NULL, 'x=-20m,y=15m');
INSERT INTO `fire_threshold` VALUES ('9', '主炮发射装置位置', '009', '火力兼容', '2022-03-09 09:40:18', NULL, 'sz', NULL, NULL, 'x=30m,y=0m');

SET FOREIGN_KEY_CHECKS = 1;
