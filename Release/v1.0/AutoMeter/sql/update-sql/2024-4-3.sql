use testcenter;
DROP TABLE IF EXISTS `enviromentvariables`;
CREATE TABLE `enviromentvariables` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `variablesname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'key名',
  `variablesvalue` text,
  `envid` bigint(20) unsigned NOT NULL COMMENT '环境id',
  `envname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '环境名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境变量';


INSERT INTO permission(id, resource, code, handle) VALUES(281, '环境变量', 'enviromentvariables:search', '查询');
INSERT INTO permission(id, resource, code, handle) VALUES(282, '环境变量', 'enviromentvariables:update', '修改');
INSERT INTO permission(id, resource, code, handle) VALUES(283, '环境变量', 'enviromentvariables:delete', '删除');
INSERT INTO permission(id, resource, code, handle) VALUES(284, '环境变量', 'enviromentvariables:list', '列表');
INSERT INTO permission(id, resource, code, handle) VALUES(285, '环境变量', 'enviromentvariables:add', '新增');