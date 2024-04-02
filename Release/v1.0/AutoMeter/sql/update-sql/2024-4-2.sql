use testcenter;
DROP TABLE IF EXISTS `scenecases_debug_report`;
CREATE TABLE `scenecases_debug_report` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `caseid` bigint(20) unsigned NOT NULL COMMENT '用例id',
  `casename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用例名',
  `testplanid` bigint(20) unsigned NOT NULL COMMENT '执行计划id',
  `batchname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '批次',
  `status` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '运行结果，成功，失败，异常',
  `respone` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '返回结果',
  `assertvalue` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '断言详细经过',
  `runtime` bigint(20) NOT NULL COMMENT '运行时长',
  `expect` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '期望值',
  `errorinfo` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `creator` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `requestheader` text COMMENT '请求头数据',
  `requestdatas` text COMMENT '请求数据',
  `url` varchar(200) DEFAULT NULL COMMENT '地址',
  `requestmethod` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  `sceneid` bigint(20) unsigned NOT NULL COMMENT '场景id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='场景用例调试报告表'