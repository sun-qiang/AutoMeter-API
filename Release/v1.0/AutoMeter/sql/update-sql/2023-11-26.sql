use testcenter;
ALTER TABLE testcenter.testscene add COLUMN casenums  bigint(20) unsigned NOT NULL default 0 COMMENT '用例数';
ALTER TABLE testcenter.executeplan add COLUMN scenenums  bigint(20) unsigned NOT NULL default 0 COMMENT '场景数';