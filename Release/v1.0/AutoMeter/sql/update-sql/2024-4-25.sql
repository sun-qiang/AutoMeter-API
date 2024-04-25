use testcenter;
DROP TABLE IF EXISTS `plannmessage`;
CREATE TABLE `plannmessage` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `executeplanid` bigint(20) unsigned NOT NULL COMMENT '集合id',
  `messagetype` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '通知类型',
  `hookcontent` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT  'hookurl',
  `mid` bigint(20) unsigned NOT NULL COMMENT '维护者id',
  `memo` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='集合通知';

INSERT INTO permission(id, resource, code, handle) VALUES(296, '集合通知', 'plannmessage:search', '查询');
INSERT INTO permission(id, resource, code, handle) VALUES(297, '集合通知', 'plannmessage:update', '修改');
INSERT INTO permission(id, resource, code, handle) VALUES(298, '集合通知', 'plannmessage:delete', '删除');
INSERT INTO permission(id, resource, code, handle) VALUES(299, '集合通知', 'plannmessage:list', '列表');
INSERT INTO permission(id, resource, code, handle) VALUES(300, '集合通知', 'plannmessage:add', '新增');
