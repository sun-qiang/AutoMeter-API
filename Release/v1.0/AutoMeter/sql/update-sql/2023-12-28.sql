use testcenter;
ALTER TABLE testcenter.scriptvariables add COLUMN conditionid bigint(20) unsigned NOT NULL COMMENT '条件id';
ALTER TABLE testcenter.scriptvariables add COLUMN conditionname varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件名';