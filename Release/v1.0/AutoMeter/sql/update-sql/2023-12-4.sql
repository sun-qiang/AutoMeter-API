use testcenter;
ALTER TABLE testcenter.executeplanbatch add COLUMN memo varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注';
