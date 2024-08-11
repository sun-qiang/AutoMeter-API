use testcenter;
ALTER TABLE testcenter.executeplan add COLUMN   `conditionstatus` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '初始' COMMENT '初始，运行中，已完成' ;

