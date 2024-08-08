package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.dto.TestplanCase;
import com.zoctan.api.entity.JmeterPerformanceObject;

import java.io.IOException;

/**
 * @author Zoctan
 * @date 2020/04/17
 */
public interface TestPlanCaseService extends Service<TestplanCase> {

    void ExecuteHttpPerformancePlanCase(JmeterPerformanceObject jmeterPerformanceObject, String DeployName, String JmeterPath, String JmxPath, String JmxCaseName, String JmeterPerformanceReportPath, String JmeterPerformanceReportLogFilePath, Long Thread, Long Loop, String creator) throws IOException;

    void ExecuteHttpPerformancePlanScene(String JmxFile, String MysqlUrl, String MysqlUserName, String MysqlPassword, String PlanName, String SceneName, long SlaverId, long PlanId, long Sceneid, long batchid, String BatchName, String JmeterPath, String JmxPath, String JmeterPerformanceReportPath, String JmeterPerformanceReportLogFilePath, Long Thread, Long Loop, String creator) throws IOException;

    void ExecuteHttpPlanFunctionCase(Long Slaverid, Long planid,String planname, String batchname, String JmeterPath, String JmxPath, String DispatchIds, String MysqlUrl, String MysqlUsername, String MysqlPassword, long JmeterLogFileNum) throws IOException;


}
