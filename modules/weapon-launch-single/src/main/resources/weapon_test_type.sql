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
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00004', 'weapon-type4', '4', '舰空导弹武器通道测试-1', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00005', 'weapon-type5', '5', '反导舰炮武器通道测试-2', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00006', 'weapon-type6', '6', '鱼雷防御武器通道测试-3', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00007', 'weapon-type7', '7', '电子对抗武器通道测试-4', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00008', 'weapon-type8', '8', '鱼雷防御武器通道测试-5', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00009', 'weapon-type9', '9', '信息流程测试-6', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00010', 'weapon-type10', '10', '威胁判断测试-7', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00011', 'weapon-type11', '11', '指示处理精度测试-8', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00012', 'weapon-type12', '12', '执行情况测试-9', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00013', 'weapon-type13', '13', '雷达航迹测试-10', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00014', 'weapon-type14', '14', '拦截距离测试-11', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00015', 'weapon-type15', '15', '火控解算精度测试-12', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00016', 'weapon-type16', '16', '反应时间测试-13', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00017', 'weapon-type17', '17', '发射架调转精度测试-14', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
INSERT IGNORE  INTO `com_dictionary_item` VALUES ('00018', 'weapon-type18', '18', '多目标拦截能力测试-15', NULL, 'weapon-test:pipe-test-type', NULL, NULL,'weapon-test', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
