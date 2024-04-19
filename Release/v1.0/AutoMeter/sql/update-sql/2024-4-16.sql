use testcenter;
DROP TABLE IF EXISTS `apicases_dbassert`;
CREATE TABLE `apicases_dbassert` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '断言Id',
  `caseid` bigint(20) unsigned NOT NULL COMMENT '用例id',
  `assembleid` bigint(20) unsigned NOT NULL COMMENT '组件id',
  `assemblename` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '组件',
  `envid` bigint(20) unsigned NOT NULL COMMENT '环境id',
  `enviroment` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '断言值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `creator` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `expression` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '断言sql表达式',
  `expectrecordsnums` bigint(20) unsigned DEFAULT NULL COMMENT '期望结果条数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据库断言表';


-- testcenter.dbvariables definition
DROP TABLE IF EXISTS `apicases_dbassert_value`;
CREATE TABLE `apicases_dbassert_value` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `dbassertid` bigint(20) unsigned NOT NULL COMMENT '用例id',
  `fieldname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '列名',
  `roworder` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '行号',
  `valuetype` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '值类型',
  `expectvalue` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT  '期望值',
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `memo` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据库断言值表';

INSERT INTO permission(id, resource, code, handle) VALUES(286, '数据库断言', 'apicasesdbassert:search', '查询');
INSERT INTO permission(id, resource, code, handle) VALUES(287, '数据库断言', 'apicasesdbassert:update', '修改');
INSERT INTO permission(id, resource, code, handle) VALUES(288, '数据库断言', 'apicasesdbassert:delete', '删除');
INSERT INTO permission(id, resource, code, handle) VALUES(289, '数据库断言', 'apicasesdbassert:list', '列表');
INSERT INTO permission(id, resource, code, handle) VALUES(290, '数据库断言', 'apicasesdbassert:add', '新增');
INSERT INTO permission(id, resource, code, handle) VALUES(291, '数据库断言值', 'apicasesdbassertvalue:search', '查询');
INSERT INTO permission(id, resource, code, handle) VALUES(292, '数据库断言值', 'apicasesdbassertvalue:update', '修改');
INSERT INTO permission(id, resource, code, handle) VALUES(293, '数据库断言值', 'apicasesdbassertvalue:delete', '删除');
INSERT INTO permission(id, resource, code, handle) VALUES(294, '数据库断言值', 'apicasesdbassertvalue:list', '列表');
INSERT INTO permission(id, resource, code, handle) VALUES(295, '数据库断言值', 'apicasesdbassertvalue:add', '新增');