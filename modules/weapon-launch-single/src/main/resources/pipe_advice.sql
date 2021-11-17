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

 Date: 2021年11月17日16:18:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Records of fire_weapon
-- ----------------------------
BEGIN;
INSERT IGNORE  INTO `pipe_advice` VALUES ('1', '航空导弹武器通道', '航空导弹武器跟踪雷达', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('2', '航空导弹武器通道', '航空导弹武器火控系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('3', '航空导弹武器通道', '航空导弹武器发射系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('4', '航空导弹武器通道', '航空导弹武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('5', '反导舰炮武器通道', '反导舰炮武器跟踪雷达', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('6', '反导舰炮武器通道', '反导舰炮武器火控系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('7', '反导舰炮武器通道', '反导舰炮武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('8', '鱼雷防御武器通道', '声呐', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('9', '鱼雷防御武器通道', '鱼雷防御武器火控系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('10', '鱼雷防御武器通道', '鱼雷防御武器发射系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('11', '鱼雷防御武器通道', '鱼雷防御武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('12', '电子对抗武器通道', '电子侦察设备', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('13', '电子对抗武器通道', '电子对抗武器火控系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('14', '电子对抗武器通道', '多功能发射系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('15', '电子对抗武器通道', '舷外电子对抗武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('16', '电子对抗武器通道', '舷内电子对抗武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('17', '水声对抗武器通道', '声呐', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('18', '水声对抗武器通道', '水声对抗武器火控系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('19', '水声对抗武器通道', '多功能发射系统', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('20', '水声对抗武器通道', '水声对抗武器', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('21', '信息流程', '对空防御作战信息流程', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('22', '信息流程', '水下防御作战信息流程', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('23', '执行情况', '对空防御作战执行情况', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('24', '执行情况', '水下防御作战执行情况', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('25', '执行情况', '对空防御作战执行情况', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('26', '执行情况', '水下防御作战执行情况', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('27', '反应时间', '对空防御作战反应时间', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('28', '反应时间', '水下防御作战反应时间', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('29', '拦截距离', '对空防御作战拦截距离', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('30', '拦截距离', '水下防御作战拦截距离', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('31', '多目标拦截能力', '对空防御作战多目标拦截能力', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('32', '多目标拦截能力', '水下防御作战多目标拦截能力', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('33', '指示处理精度', '对空防御作战指示处理精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('34', '指示处理精度', '水下防御作战指示处理精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('35', '火控解算精度', '舰空导弹武器火控解算精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('36', '火控解算精度', '反导舰炮武器火控解算精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('37', '火控解算精度', '鱼雷防御武器火控解算精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('38', '火控解算精度', '电子对抗武器火控解算精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('39', '火控解算精度', '水声对抗武器火控解算精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('40', '发射架调转精度', '舰空导弹发射架调转精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('41', '发射架调转精度', '反导舰炮发射架调转精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('42', '发射架调转精度', '多功能发射架调转精度', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('43', '雷达航迹', '对空防御作战目标雷达航迹', 0, '',NULL, NULL, 'Xinlai', 1, 0);
INSERT IGNORE  INTO `pipe_advice` VALUES ('44', '威胁判断', '对空防御作战目标威胁判断', 0, '',NULL, NULL, 'Xinlai', 1, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
