use testcenter;
ALTER TABLE testcenter.condition_order add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-case,scencecase，scence,execplan';