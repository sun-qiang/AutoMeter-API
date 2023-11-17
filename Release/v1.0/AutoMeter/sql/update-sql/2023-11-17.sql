use testcenter;


DROP TABLE IF EXISTS `apicases_report_extinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apicases_report_extinfo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `reportid` bigint(20) unsigned NOT NULL COMMENT '报告id',
  `responeinfo` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '响应详细信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='api用例功能报告扩展信息表';



DROP TABLE IF EXISTS `apicases_condition_report_extinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apicases_condition_report_extinfo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `condition_reportid` bigint(20) unsigned NOT NULL COMMENT '条件报告id',
  `responeinfo` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '响应详细信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='条件报告扩展信息表';



DROP TABLE IF EXISTS `testscene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testscene` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '场景Id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  `usetype` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '运行类型，function，performance，来区分分配什么slaver',
  `memo` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试场景表';


DROP TABLE IF EXISTS `testscene_testcase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testscene_testcase` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `testscenenid` bigint(20) unsigned NOT NULL COMMENT '测试场景id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  `apiid` bigint(20) unsigned NOT NULL COMMENT 'apiid',
  `deployunitid` bigint(20) unsigned NOT NULL COMMENT '微服务id',
  `deployunitname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '微服务',
  `modelid` bigint(20) unsigned NOT NULL COMMENT '模块id',
  `modelname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '模块名',
  `apiname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'API名',
  `testcaseid` bigint(20) unsigned NOT NULL COMMENT '用例id',
  `casename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用例名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `creator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `caseorder` bigint(20) unsigned DEFAULT NULL COMMENT '用例顺序',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试场景用例表';



DROP TABLE IF EXISTS `testplan_testscene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testplan_testscene` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `testscenenid` bigint(20) unsigned NOT NULL COMMENT '测试场景id',
  `scenename` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名',
  `testplanid` bigint(20) unsigned NOT NULL COMMENT '集合id',
  `planname` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '集合名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上一次修改时间',
  `ordernum` bigint(20) unsigned DEFAULT NULL COMMENT '顺序',
  `creator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `projectid` bigint(20) unsigned DEFAULT '1' COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试集合场景表';


INSERT INTO `permission` VALUES (255,'测试场景','testscene:search','搜索');
INSERT INTO `permission` VALUES (256,'测试场景','testscene:add','添加');
INSERT INTO `permission` VALUES (257,'测试场景','testscene:delete','删除');
INSERT INTO `permission` VALUES (258,'测试场景','testscene:update','更新');
INSERT INTO `permission` VALUES (259,'测试场景','testscene:detail','修改');
INSERT INTO `permission` VALUES (260,'测试场景','testscene:list','查询');


INSERT INTO `permission` VALUES (261,'测试场景','testscene:scenecondition','场景前置条件');
INSERT INTO `permission` VALUES (262,'测试场景','testscene:scenecasecondition','用例前置条件');
INSERT INTO `permission` VALUES (263,'测试场景','testscene:loadcase','装载用例');
INSERT INTO `permission` VALUES (264,'测试场景','testscene:deletecase','删除用例');

INSERT INTO `permission` VALUES (265,'测试场景','testscene:casedeleteapicondition','删除用例接口前置条件');
INSERT INTO `permission` VALUES (266,'测试场景','testscene:caseupdateapicondition','修改用例接口前置条件');

INSERT INTO `permission` VALUES (267,'测试场景','testscene:casedeletedbcondition','删除用例数据库前置条件');
INSERT INTO `permission` VALUES (268,'测试场景','testscene:caseupdatedbcondition','修改用例数据库前置条件');

INSERT INTO `permission` VALUES (269,'测试场景','testscene:casedeletescriptcondition','删除用例脚本前置条件');
INSERT INTO `permission` VALUES (270,'测试场景','testscene:caseupdatescriptcondition','修改用例脚本前置条件');

INSERT INTO `permission` VALUES (271,'测试场景','testscene:casedeletedelaycondition','删除用例延时前置条件');
INSERT INTO `permission` VALUES (272,'测试场景','testscene:caseupdatedelaycondition','修改用例延时前置条件');



INSERT INTO `permission` VALUES (273,'测试场景','testscene:scendeleteapicondition','删除场景接口前置条件');
INSERT INTO `permission` VALUES (274,'测试场景','testscene:scenupdateapicondition','修改场景接口前置条件');

INSERT INTO `permission` VALUES (275,'测试场景','testscene:scendeletedbcondition','删除场景数据库前置条件');
INSERT INTO `permission` VALUES (276,'测试场景','testscene:scenupdatedbcondition','修改场景数据库前置条件');

INSERT INTO `permission` VALUES (277,'测试场景','testscene:scendeletescriptcondition','删除场景脚本前置条件');
INSERT INTO `permission` VALUES (278,'测试场景','testscene:scenupdatescriptcondition','修改场景脚本前置条件');

INSERT INTO `permission` VALUES (279,'测试场景','testscene:scendeletedelaycondition','删除场景延时前置条件');
INSERT INTO `permission` VALUES (280,'测试场景','testscene:scenupdatedelaycondition','修改场景延时前置条件');




ALTER TABLE testcenter.condition_api add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-scencecase，scence,execplan';
ALTER TABLE testcenter.condition_api add COLUMN modelname varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '模块名';
ALTER TABLE testcenter.condition_api add COLUMN modelid bigint(20) unsigned NOT NULL COMMENT '模块id';
ALTER TABLE testcenter.condition_db add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-scencecase，scence,execplan';
ALTER TABLE testcenter.condition_delay add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-scencecase，scence,execplan';
ALTER TABLE testcenter.condition_script add COLUMN conditiontype varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件类型-scencecase，scence,execplan';

ALTER TABLE testcenter.testvariables add COLUMN casename varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用例名';
ALTER TABLE testcenter.testvariables add COLUMN caseid bigint(20) unsigned NOT NULL COMMENT '用例id';



ALTER TABLE testcenter.`dispatch` add COLUMN sceneid  bigint(20) unsigned NOT NULL COMMENT '场景id';
ALTER TABLE testcenter.`dispatch` add COLUMN scenename varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名';
ALTER TABLE testcenter.`dispatch` add COLUMN caseorder bigint(20) unsigned DEFAULT NULL COMMENT '用例顺序';
ALTER TABLE testcenter.executeplanbatch add COLUMN sceneid  bigint(20) unsigned NOT NULL COMMENT '场景id';
ALTER TABLE testcenter.executeplanbatch add COLUMN scenename varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名';
ALTER TABLE testcenter.executeplanbatch add COLUMN slaverid  bigint(20) unsigned DEFAULT 0  NULL COMMENT 'slaverid';
ALTER TABLE testcenter.testcondition_report drop COLUMN conditionid;
ALTER TABLE testcenter.apicases_report add COLUMN sceneid  bigint(20) unsigned NOT NULL COMMENT '场景id';
ALTER TABLE testcenter.apicases_report add COLUMN scenename varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '场景名';
ALTER TABLE testcenter.testvariables_value add COLUMN slaverid  bigint(20) unsigned NOT NULL COMMENT 'slaverid';
