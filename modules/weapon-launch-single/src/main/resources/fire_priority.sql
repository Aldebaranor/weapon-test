SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Records of fire_priority
-- ----------------------------
BEGIN;

-- 近程防空导弹
INSERT IGNORE INTO `fire_priority` VALUES ('1','1', '0',NULL ,NULL,'XinLai',0,1,'2','1');
INSERT IGNORE INTO `fire_priority` VALUES ('2','2', '0',NULL ,NULL,'XinLai',0,1,'3','1');
INSERT IGNORE INTO `fire_priority` VALUES ('3','3', '0',NULL ,NULL,'XinLai',0,1,'4','1');
INSERT IGNORE INTO `fire_priority` VALUES ('4','4', '0',NULL ,NULL,'XinLai',0,1,'5','1');
INSERT IGNORE INTO `fire_priority` VALUES ('5','5', '0',NULL ,NULL,'XinLai',0,1,'6','1');
INSERT IGNORE INTO `fire_priority` VALUES ('6','6', '0',NULL ,NULL,'XinLai',0,1,'7','1');
INSERT IGNORE INTO `fire_priority` VALUES ('7','7', '0',NULL ,NULL,'XinLai',0,1,'8','1');
INSERT IGNORE INTO `fire_priority` VALUES ('8','8', '0',NULL ,NULL,'XinLai',0,1,'9','1');
INSERT IGNORE INTO `fire_priority` VALUES ('8','8', '0',NULL ,NULL,'XinLai',0,1,'12','1');
INSERT IGNORE INTO `fire_priority` VALUES ('9','9', '0',NULL ,NULL,'XinLai',0,1,'18','1');

-- 中程防空导弹
INSERT IGNORE INTO `fire_priority` VALUES ('10','10', '0',NULL ,NULL,'XinLai',0,1,'3','2');
INSERT IGNORE INTO `fire_priority` VALUES ('11','11', '0',NULL ,NULL,'XinLai',0,1,'4','2');
INSERT IGNORE INTO `fire_priority` VALUES ('12','12', '0',NULL ,NULL,'XinLai',0,1,'5','2');
INSERT IGNORE INTO `fire_priority` VALUES ('13','13', '0',NULL ,NULL,'XinLai',0,1,'6','2');
INSERT IGNORE INTO `fire_priority` VALUES ('14','14', '0',NULL ,NULL,'XinLai',0,1,'7','2');
INSERT IGNORE INTO `fire_priority` VALUES ('15','15', '0',NULL ,NULL,'XinLai',0,1,'8','2');
INSERT IGNORE INTO `fire_priority` VALUES ('16','16', '0',NULL ,NULL,'XinLai',0,1,'9','2');
INSERT IGNORE INTO `fire_priority` VALUES ('17','17', '0',NULL ,NULL,'XinLai',0,1,'12','2');
INSERT IGNORE INTO `fire_priority` VALUES ('18','18', '0',NULL ,NULL,'XinLai',0,1,'18','2');

-- 远程防空导弹
INSERT IGNORE INTO `fire_priority` VALUES ('19','19', '0',NULL ,NULL,'XinLai',0,1,'4','3');
INSERT IGNORE INTO `fire_priority` VALUES ('20','20', '0',NULL ,NULL,'XinLai',0,1,'5','3');
INSERT IGNORE INTO `fire_priority` VALUES ('21','21', '0',NULL ,NULL,'XinLai',0,1,'6','3');
INSERT IGNORE INTO `fire_priority` VALUES ('22','22', '0',NULL ,NULL,'XinLai',0,1,'7','3');
INSERT IGNORE INTO `fire_priority` VALUES ('23','23', '0',NULL ,NULL,'XinLai',0,1,'8','3');
INSERT IGNORE INTO `fire_priority` VALUES ('24','24', '0',NULL ,NULL,'XinLai',0,1,'9','3');
INSERT IGNORE INTO `fire_priority` VALUES ('25','25', '0',NULL ,NULL,'XinLai',0,1,'12','3');
INSERT IGNORE INTO `fire_priority` VALUES ('26','26', '0',NULL ,NULL,'XinLai',0,1,'18','3');

-- 反舰导弹
INSERT IGNORE INTO `fire_priority` VALUES ('27','27', '0',NULL ,NULL,'XinLai',0,1,'5','4');
INSERT IGNORE INTO `fire_priority` VALUES ('28','28', '0',NULL ,NULL,'XinLai',0,1,'6','4');
INSERT IGNORE INTO `fire_priority` VALUES ('29','29', '0',NULL ,NULL,'XinLai',0,1,'7','4');
INSERT IGNORE INTO `fire_priority` VALUES ('30','30', '0',NULL ,NULL,'XinLai',0,1,'8','4');
INSERT IGNORE INTO `fire_priority` VALUES ('31','31', '0',NULL ,NULL,'XinLai',0,1,'9','4');
INSERT IGNORE INTO `fire_priority` VALUES ('32','32', '0',NULL ,NULL,'XinLai',0,1,'12','4');
INSERT IGNORE INTO `fire_priority` VALUES ('33','33', '0',NULL ,NULL,'XinLai',0,1,'18','4');

-- 反潜导弹
INSERT IGNORE INTO `fire_priority` VALUES ('34','34', '0',NULL ,NULL,'XinLai',0,1,'6','5');
INSERT IGNORE INTO `fire_priority` VALUES ('35','35', '0',NULL ,NULL,'XinLai',0,1,'7','5');
INSERT IGNORE INTO `fire_priority` VALUES ('36','36', '0',NULL ,NULL,'XinLai',0,1,'8','5');
INSERT IGNORE INTO `fire_priority` VALUES ('37','37', '0',NULL ,NULL,'XinLai',0,1,'9','5');
INSERT IGNORE INTO `fire_priority` VALUES ('38','38', '0',NULL ,NULL,'XinLai',0,1,'12','5');
INSERT IGNORE INTO `fire_priority` VALUES ('39','39', '0',NULL ,NULL,'XinLai',0,1,'18','5');

-- 主炮
INSERT IGNORE INTO `fire_priority` VALUES ('40','40', '0',NULL ,NULL,'XinLai',0,1,'7','6');
INSERT IGNORE INTO `fire_priority` VALUES ('41','41', '0',NULL ,NULL,'XinLai',0,1,'8','6');
INSERT IGNORE INTO `fire_priority` VALUES ('42','42', '0',NULL ,NULL,'XinLai',0,1,'9','6');
INSERT IGNORE INTO `fire_priority` VALUES ('43','43', '0',NULL ,NULL,'XinLai',0,1,'12','6');
INSERT IGNORE INTO `fire_priority` VALUES ('44','44', '0',NULL ,NULL,'XinLai',0,1,'18','6');

-- 万发炮
INSERT IGNORE INTO `fire_priority` VALUES ('45','45', '0',NULL ,NULL,'XinLai',0,1,'8','7');
INSERT IGNORE INTO `fire_priority` VALUES ('46','46', '0',NULL ,NULL,'XinLai',0,1,'9','7');
INSERT IGNORE INTO `fire_priority` VALUES ('47','47', '0',NULL ,NULL,'XinLai',0,1,'12','7');
INSERT IGNORE INTO `fire_priority` VALUES ('48','48', '0',NULL ,NULL,'XinLai',0,1,'18','7');

-- 舷外有源电子干扰弹
INSERT IGNORE INTO `fire_priority` VALUES ('49','40', '0',NULL ,NULL,'XinLai',0,1,'9','8');
INSERT IGNORE INTO `fire_priority` VALUES ('50','50', '0',NULL ,NULL,'XinLai',0,1,'12','8');
INSERT IGNORE INTO `fire_priority` VALUES ('51','51', '0',NULL ,NULL,'XinLai',0,1,'18','8');

-- 舷外无源电子干扰弹
INSERT IGNORE INTO `fire_priority` VALUES ('52','52', '0',NULL ,NULL,'XinLai',0,1,'12','9');
INSERT IGNORE INTO `fire_priority` VALUES ('53','53', '0',NULL ,NULL,'XinLai',0,1,'18','9');

-- 水声对抗器材
INSERT IGNORE INTO `fire_priority` VALUES ('54','54', '0',NULL ,NULL,'XinLai',0,1,'18','12');

-- 电磁冲突
-- 舷外有源电子干扰弹
INSERT IGNORE INTO `fire_priority` VALUES ('55','55', '1',NULL ,NULL,'XinLai',0,1,'10','8');
INSERT IGNORE INTO `fire_priority` VALUES ('56','56', '1',NULL ,NULL,'XinLai',0,1,'11','8');
INSERT IGNORE INTO `fire_priority` VALUES ('57','57', '1',NULL ,NULL,'XinLai',0,1,'15','8');
INSERT IGNORE INTO `fire_priority` VALUES ('58','58', '1',NULL ,NULL,'XinLai',0,1,'16','8');
INSERT IGNORE INTO `fire_priority` VALUES ('59','59', '1',NULL ,NULL,'XinLai',0,1,'17','8');

-- 舷内电子干扰设备
INSERT IGNORE INTO `fire_priority` VALUES ('60','60', '1',NULL ,NULL,'XinLai',0,1,'11','10');
INSERT IGNORE INTO `fire_priority` VALUES ('61','61', '1',NULL ,NULL,'XinLai',0,1,'15','10');
INSERT IGNORE INTO `fire_priority` VALUES ('62','62', '1',NULL ,NULL,'XinLai',0,1,'16','10');
INSERT IGNORE INTO `fire_priority` VALUES ('63','63', '1',NULL ,NULL,'XinLai',0,1,'17','10');

-- 舰载电子侦察设备
INSERT IGNORE INTO `fire_priority` VALUES ('64','64', '1',NULL ,NULL,'XinLai',0,1,'15','11');
INSERT IGNORE INTO `fire_priority` VALUES ('65','65', '1',NULL ,NULL,'XinLai',0,1,'16','11');
INSERT IGNORE INTO `fire_priority` VALUES ('66','66', '1',NULL ,NULL,'XinLai',0,1,'17','11');

-- 主炮跟踪雷达
INSERT IGNORE INTO `fire_priority` VALUES ('67','67', '1',NULL ,NULL,'XinLai',0,1,'17','16');

-- 搜索雷达
INSERT IGNORE INTO `fire_priority` VALUES ('68','68', '1',NULL ,NULL,'XinLai',0,1,'16','15');
INSERT IGNORE INTO `fire_priority` VALUES ('69','69', '1',NULL ,NULL,'XinLai',0,1,'17','15');

-- 水声冲突
-- 舰壳声呐
INSERT IGNORE INTO `fire_priority` VALUES ('70','70', '2',NULL ,NULL,'XinLai',0,1,'14','13');
INSERT IGNORE INTO `fire_priority` VALUES ('71','71', '2',NULL ,NULL,'XinLai',0,1,'12','13');

-- 拖曳声呐
INSERT IGNORE INTO `fire_priority` VALUES ('72','72', '2',NULL ,NULL,'XinLai',0,1,'12','14');

COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
