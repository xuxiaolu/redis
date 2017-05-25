SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `cluster`
-- ----------------------------
DROP TABLE IF EXISTS `cluster`;
CREATE TABLE `cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(50) NOT NULL COMMENT 'redis集群名字',
  `description` varchar(150) DEFAULT '' COMMENT '集群描述',
  `host_and_ports` varchar(150) NOT NULL COMMENT 'redis集群主机和端口号',
  `password` varchar(50) DEFAULT NULL COMMENT '连接redis集群的密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` int(11) DEFAULT NULL COMMENT '是否删除，0--未删除，1--已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='集群信息表';

SET FOREIGN_KEY_CHECKS = 1;
