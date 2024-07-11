use testcenter;
ALTER TABLE testcenter.apicases_report MODIFY COLUMN respone text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回结果';
ALTER TABLE testcenter.apicases_report MODIFY COLUMN respone LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回结果';

ALTER TABLE testcenter.performancereportfilelog add COLUMN `sceneid` bigint(20) unsigned NOT NULL COMMENT '场景id' ;


DROP TABLE IF EXISTS `testscene_performance`;
CREATE TABLE `testscene_performance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `testsceneid` bigint(20) unsigned NOT NULL COMMENT '场景id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  `targetconcurrency` bigint(20) unsigned NOT NULL default 0 COMMENT '并发线程',
  `rampuptime` bigint(20) unsigned NOT NULL default 0 COMMENT '启动时间',
  `stepscount` bigint(20) unsigned NOT NULL default 0 COMMENT '阶梯次数',
  `holdtime` bigint(20) unsigned NOT NULL default 0 COMMENT '持续时间',
  `timeunit`varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '时间单位',
  `iterations` bigint(20) unsigned NOT NULL default 0 COMMENT '循环次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='测试场景性能';

DROP TABLE IF EXISTS `testscene_dispatch`;
CREATE TABLE `testscene_dispatch` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `execplanid` bigint(20) unsigned NOT NULL COMMENT '执行计划Id',
  `execplanname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '执行计划名',
  `batchid` bigint(20) unsigned NOT NULL COMMENT '批次Id',
  `batchname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '批次名',
  `slaverid` bigint(20) unsigned NOT NULL COMMENT '执行机Id',
  `slavername` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '执行机名',
  `testsceneid` bigint(20) unsigned NOT NULL COMMENT '场景id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  `targetconcurrency` bigint(20) unsigned NOT NULL default 0 COMMENT '并发线程',
  `rampuptime` bigint(20) unsigned NOT NULL default 0 COMMENT '启动时间',
  `stepscount` bigint(20) unsigned NOT NULL default 0 COMMENT '阶梯次数',
  `holdtime` bigint(20) unsigned NOT NULL default 0 COMMENT '持续时间',
  `timeunit`varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '时间单位',
  `iterations` bigint(20) unsigned NOT NULL default 0 COMMENT '循环次数',
  `ordernum` bigint(20) unsigned DEFAULT NULL COMMENT '顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='测试场景调度';