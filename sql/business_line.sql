SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `business_line`
-- ----------------------------
DROP TABLE IF EXISTS `business_line`;
CREATE TABLE `business_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `cluster_name` varchar(50) NOT NULL COMMENT '集群名字',
  `name` varchar(50) NOT NULL COMMENT '业务线名字',
  `description` varchar(150) DEFAULT NULL COMMENT '业务线描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='业务线表';

SET FOREIGN_KEY_CHECKS = 1;
