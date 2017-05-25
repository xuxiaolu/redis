SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `item`
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `business_name` varchar(50) NOT NULL COMMENT '业务线名称',
  `name` varchar(50) NOT NULL COMMENT 'Item名字',
  `value` varchar(50) NOT NULL COMMENT 'Item值',
  `description` varchar(150) DEFAULT NULL COMMENT 'Item描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` int(11) DEFAULT NULL COMMENT '是否删除，0--未删除，1--已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='业务线中的Item表';

SET FOREIGN_KEY_CHECKS = 1;
