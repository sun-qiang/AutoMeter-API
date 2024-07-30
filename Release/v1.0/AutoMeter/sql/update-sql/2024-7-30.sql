use testcenter;
DROP TABLE IF EXISTS `performancereport_thread`;
CREATE TABLE `performancereport_thread` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `execplanid` bigint(20) unsigned NOT NULL COMMENT '执行计划Id',
  `batchname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '批次名',
  `slaverid` bigint(20) unsigned NOT NULL COMMENT '执行机Id',
  `testsceneid` bigint(20) unsigned NOT NULL COMMENT '场景id',
  `contenttype` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='性能报告来源表';



DROP TABLE IF EXISTS `performancereport_caseinfo`;
CREATE TABLE `performancereport_caseinfo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `execplanid` bigint(20) unsigned NOT NULL COMMENT '执行计划Id',
  `batchname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '批次名',
  `casename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用例名',
  `slaverid` bigint(20) unsigned NOT NULL COMMENT '执行机Id',
  `testsceneid` bigint(20) unsigned NOT NULL COMMENT '场景id',
  `contenttype` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='性能报告用例来源表';

