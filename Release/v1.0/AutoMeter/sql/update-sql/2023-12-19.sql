use testcenter;
ALTER TABLE testcenter.dbvariables add COLUMN conditionid bigint(20) unsigned NOT NULL COMMENT '备注';
ALTER TABLE testcenter.dbvariables add COLUMN conditionname varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件名';
ALTER TABLE testcenter.dbvariables add COLUMN fieldname varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '列名';
ALTER TABLE testcenter.dbvariables add COLUMN roworder bigint(20) unsigned NOT NULL default 0 COMMENT '行号';