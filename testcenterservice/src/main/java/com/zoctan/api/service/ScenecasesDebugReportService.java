package com.zoctan.api.service;

import com.zoctan.api.entity.ApicasesReport;
import com.zoctan.api.entity.ScenecasesDebugReport;
import com.zoctan.api.core.service.Service;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/03/31
*/
public interface ScenecasesDebugReportService extends Service<ScenecasesDebugReport> {
    List<ScenecasesDebugReport> findscenecasedebugreportWithName(final Map<String, Object> params);

}
