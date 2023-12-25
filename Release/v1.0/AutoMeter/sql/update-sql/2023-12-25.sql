use testcenter;
ALTER TABLE testcenter.testscene_testcase add COLUMN threadnums bigint(20) unsigned DEFAULT 1 COMMENT '线程数';
ALTER TABLE testcenter.testscene_testcase add COLUMN loopnums bigint(20) unsigned DEFAULT 1 COMMENT '循环次数';
ALTER TABLE testcenter.testscene_testcase add COLUMN stopflag varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT "无" COMMENT '断言失败处理标志';
ALTER TABLE testcenter.`dispatch` add COLUMN stopflag varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT "无" COMMENT '断言失败处理标志';
