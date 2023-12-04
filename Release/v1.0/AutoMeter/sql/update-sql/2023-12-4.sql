use testcenter;
ALTER TABLE testcenter.condition_delay add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-scencecase，scence,execplan';
ALTER TABLE testcenter.testscene add COLUMN casenums  bigint(20) unsigned NOT NULL default 0 COMMENT '用例数';
ALTER TABLE testcenter.executeplan add COLUMN scenenums  bigint(20) unsigned NOT NULL default 0 COMMENT '场景数';
ALTER TABLE testcenter.executeplanbatch add COLUMN memo varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注';
