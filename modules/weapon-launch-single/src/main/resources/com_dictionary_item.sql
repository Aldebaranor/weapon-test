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
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00102', 'pipe-type2', '2', '自防御装置级测试', NULL, 'weapon-test:pipe-test-type', NULL, '', 'weapon-test', NULL);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00201', 'test1-1', '16', '舰空导弹武器跟踪雷达', NULL, 'weapon-test:pipe-test-type', '', '00004', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00202', 'test1-2', '17', '舰空导弹武器火控装置', NULL, 'weapon-test:pipe-test-type', '', '00004', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00203', 'test1-3', '18', '舰空导弹武器发射装置', NULL, 'weapon-test:pipe-test-type', '', '00004', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00204', 'test1-4', '19', '舰空导弹武器', NULL, 'weapon-test:pipe-test-type', '', '00004', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00205', 'test2-1', '20', '反导舰炮武器跟踪雷达', NULL, 'weapon-test:pipe-test-type', '', '00005', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00206', 'test2-2', '21', '反导舰炮武器火控装置', NULL, 'weapon-test:pipe-test-type', '', '00005', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00207', 'test2-3', '22', '反导舰炮武器', NULL, 'weapon-test:pipe-test-type', '', '00005', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00208', 'test3-1', '23', '声呐', NULL, 'weapon-test:pipe-test-type', '', '00006', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00209', 'test3-2', '24', '鱼雷防御武器火控装置', NULL, 'weapon-test:pipe-test-type', '', '00006', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00210', 'test3-3', '25', '鱼雷防御武器发射装置', NULL, 'weapon-test:pipe-test-type', '', '00006', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00211', 'test3-4', '26', '鱼雷防御武器', NULL, 'weapon-test:pipe-test-type', '', '00006', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00212', 'test4-1', '27', '电子侦察设备', NULL, 'weapon-test:pipe-test-type', '', '00007', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00213', 'test4-2', '28', '电子对抗武器火控装置', NULL, 'weapon-test:pipe-test-type', '', '00007', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00214', 'test4-3', '29', '多功能发射装置', NULL, 'weapon-test:pipe-test-type', '', '00007', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00215', 'test4-4', '30', '舷外电子对抗武器', NULL, 'weapon-test:pipe-test-type', '', '00007', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00216', 'test4-5', '31', '舷内电子对抗武器', NULL, 'weapon-test:pipe-test-type', '', '00007', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00217', 'test5-1', '32', '声呐', NULL, 'weapon-test:pipe-test-type', '', '00008', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00218', 'test5-2', '33', '水声对抗武器火控装置', NULL, 'weapon-test:pipe-test-type', '', '00008', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00219', 'test5-3', '34', '多功能发射装置', NULL, 'weapon-test:pipe-test-type', '', '00008', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00220', 'test5-4', '35', '水声对抗武器', NULL, 'weapon-test:pipe-test-type', '', '00008', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00221', 'test6-1', '36', '对空防御作战信息流程', NULL, 'weapon-test:pipe-test-type', '', '00009', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00222', 'test6-2', '37', '水下防御作战信息流程', NULL, 'weapon-test:pipe-test-type', '', '00009', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00223', 'test7-1', '38', '对空防御作战目标威胁判断', NULL, 'weapon-test:pipe-test-type', '', '00010', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00224', 'test8-1', '39', '对空防御作战指示处理精度', NULL, 'weapon-test:pipe-test-type', '', '00011', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00226', 'test8-2', '41', '水下防御作战指示处理精度', NULL, 'weapon-test:pipe-test-type', '', '00011', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00227', 'test9-1', '42', '对空防御作战执行情况', NULL, 'weapon-test:pipe-test-type', '', '00012', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00228', 'test9-2', '43', '水下防御作战执行情况', NULL, 'weapon-test:pipe-test-type', '', '00012', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00229', 'test10-1', '44', '对空防御作战目标雷达航迹', NULL, 'weapon-test:pipe-test-type', '', '00013', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00231', 'test11-1', '46', '对空防御作战拦截距离', NULL, 'weapon-test:pipe-test-type', '', '00014', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00232', 'test11-2', '47', '水下防御作战拦截距离', NULL, 'weapon-test:pipe-test-type', '', '00014', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00233', 'test12-1', '48', '舰空导弹武器火控解算精度', NULL, 'weapon-test:pipe-test-type', '', '00015', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00234', 'test12-2', '49', '反导舰炮武器火控解算精度', NULL, 'weapon-test:pipe-test-type', '', '00015', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00235', 'test12-3', '50', '鱼雷防御武器火控解算精度', NULL, 'weapon-test:pipe-test-type', '', '00015', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00236', 'test12-4', '51', '电子对抗武器火控解算精度', NULL, 'weapon-test:pipe-test-type', '', '00015', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00237', 'test12-5', '52', '水声对抗武器火控解算精度', NULL, 'weapon-test:pipe-test-type', '', '00015', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00238', 'test13-1', '53', '对空防御作战反应时间', NULL, 'weapon-test:pipe-test-type', '', '00016', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00239', 'test13-2', '54', '水下防御作战反应时间', NULL, 'weapon-test:pipe-test-type', '', '00016', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00240', 'test14-1', '55', '舰空导弹发射架调转精度', NULL, 'weapon-test:pipe-test-type', '', '00017', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00241', 'test14-2', '56', '反导舰炮发射架调转精度', NULL, 'weapon-test:pipe-test-type', '', '00017', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00242', 'test14-3', '57', '多功能发射架调转精度', NULL, 'weapon-test:pipe-test-type', '', '00017', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00243', 'test15-1', '58', '对空防御作战多目标拦截能力', NULL, 'weapon-test:pipe-test-type', '', '00018', 'weapon-test', 0);
INSERT IGNORE INTO `com_dictionary_item` VALUES ('00244', 'test15-2', '59', '水下防御作战多目标拦截能力', NULL, 'weapon-test:pipe-test-type', '', '00018', 'weapon-test', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

