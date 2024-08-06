use testcenter;
ALTER TABLE testcenter.executeplanbatch add COLUMN   `usetype` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '运行类型，function，performance，来区分分配什么slaver' ;


