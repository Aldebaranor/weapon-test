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
-- Records of com_dictionary_item
-- ----------------------------

BEGIN;
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00001', 'weapon-type1', '1', '硬杀伤WQ', '', 'weapon-test:weapon-type', '', '', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00002', 'weapon-type2', '2', '软杀伤WQ', '', 'weapon-test:weapon-type', '', '', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00003', 'weapon-type3', '3', '传感器', '', 'weapon-test:weapon-type', '', '', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00004', 'test1', '1', '舰空导弹武器通道测试', NULL, 'weapon-test:pipe-test-type', '', '00101', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00005', 'test2', '2', '反导舰炮武器通道测试', NULL, 'weapon-test:pipe-test-type', NULL, '00101', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00006', 'test3', '3', '鱼雷防御武器通道测试', NULL, 'weapon-test:pipe-test-type', NULL, '00101', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00007', 'test4', '4', '电子对抗武器通道测试', NULL, 'weapon-test:pipe-test-type', NULL, '00101', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00008', 'test5', '5', '水声对抗武器测试', NULL, 'weapon-test:pipe-test-type', NULL, '00101', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00009', 'test6', '6', '信息流程测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00010', 'test7', '7', '威胁判断测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00011', 'test8', '8', '指示处理精度测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00012', 'test9', '9', '执行情况测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00013', 'test10', '10', '雷达航迹测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00014', 'test11', '11', '拦截距离测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00015', 'test12', '12', '火控解算精度测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00016', 'test13', '13', '反应时间测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00017', 'test14', '14', '发射架调转精度测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00018', 'test15', '15', '多目标拦截能力测试', NULL, 'weapon-test:pipe-test-type', NULL, '00102', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00101', 'pipe-type1', '1', '单武器通道级测试', NULL, 'weapon-test:pipe-test-type', NULL, '', 'weapon-test', NULL);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00102', 'pipe-type2', '2', '自防御系统级测试', NULL, 'weapon-test:pipe-test-type', NULL, '', 'weapon-test', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

