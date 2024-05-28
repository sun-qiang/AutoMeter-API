use testcenter;

ALTER TABLE testcenter.api_casedata add COLUMN `encyptype` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '无' COMMENT '加密方式' ;
ALTER TABLE testcenter.api_params add COLUMN `encyptype` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '无' COMMENT '加密方式' ;
ALTER TABLE testcenter.globalheader_params add COLUMN `encyptype` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '无' COMMENT '加密方式' ;


DROP TABLE IF EXISTS `apicaseresponesetting`;
CREATE TABLE `apicaseresponesetting` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `caseid` bigint(20) unsigned NOT NULL COMMENT '用例id',
  `descry` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '解密类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用例响应设置';

DROP TABLE IF EXISTS `planmail`;
CREATE TABLE `planmail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `executeplanid` bigint(20) unsigned NOT NULL COMMENT '集合id',
  `accountid` bigint(20) unsigned NOT NULL COMMENT '账户id',
  `nickname` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='集合邮件通知';

INSERT INTO permission(id, resource, code, handle) VALUES(301, '集合邮件通知', 'planmail:delete', '删除');
INSERT INTO permission(id, resource, code, handle) VALUES(302, '集合邮件通知', 'planmail:list', '列表');
INSERT INTO permission(id, resource, code, handle) VALUES(303, '集合邮件通知', 'planmail:add', '添加');
INSERT INTO permission(id, resource, code, handle) VALUES(304, '集合邮件通知', 'planmail:search', '查询');