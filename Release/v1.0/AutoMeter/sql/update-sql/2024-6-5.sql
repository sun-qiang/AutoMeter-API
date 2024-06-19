use testcenter;
ALTER TABLE testcenter.apicases_reportstatics add COLUMN `projectid` bigint(20) unsigned NOT NULL COMMENT '项目id' ;
ALTER TABLE testcenter.apicases_reportstatics add COLUMN `deployunitname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '发布单元名' ;
ALTER TABLE testcenter.apicases_reportstatics add COLUMN `executeplanname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '集合名' ;