use testcenter;

ALTER TABLE testcenter.executeplan add COLUMN `domian` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '域名' ;

ALTER TABLE testcenter.apicases_dbassert_value add COLUMN `assertcondition` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条件' ;

INSERT INTO testcenter.dictionary
(id, dicname, diccode, dicitemname, dicitmevalue, create_time, lastmodify_time)
VALUES(60, '环境部署内容', 'machinedeploy', '数据库', '金仓', '2024-04-26 20:54:42', '2024-04-26 20:54:42');


ALTER TABLE testcenter.api MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.api_params MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_assert MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_dbassert MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_condition MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_condition_report MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_dbassert_value MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_debug_condition MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_performanceapdex MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_performancestatistics MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_report MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_report_performance MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_reportstatics MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_variables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_api MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_api MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_db MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_delay MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_order MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.condition_script MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.dbvariables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.deployunit MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.deployunit_model MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.dispatch MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.enviroment MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.enviroment_assemble MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.enviromentvariables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.envmachine MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.executeplan MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.executeplan_testcase MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.executeplanbatch MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.globalheader MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.globalvariables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.macdepunit MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.machine MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.mockapi MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.mockapirespone MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.mockmodel MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.performancereportsource MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.project MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.project_account MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.scenecases_debug_report MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.scriptvariables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testcondition MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testcondition_report MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testplan_testscene MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testscene MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testscene_testcase MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.testvariables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.variables MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_dbassert MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';
ALTER TABLE testcenter.apicases_dbassert_value MODIFY COLUMN creator varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建者';


