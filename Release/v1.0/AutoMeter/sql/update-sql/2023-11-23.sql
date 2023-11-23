use testcenter;
ALTER TABLE testcenter.testvariables_value add COLUMN conditionid  bigint(20) unsigned NOT NULL COMMENT 'conditionid';
ALTER TABLE testcenter.testvariables_value add COLUMN conditiontype varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型';
