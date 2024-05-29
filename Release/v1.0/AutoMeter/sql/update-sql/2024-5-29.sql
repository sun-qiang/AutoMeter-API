use testcenter;
ALTER TABLE testcenter.executeplan add COLUMN `creatorid` bigint(20) unsigned NOT NULL COMMENT '创建人id' ;
ALTER TABLE testcenter.testscene add COLUMN `creatorid` bigint(20) unsigned NOT NULL COMMENT '创建人id' ;