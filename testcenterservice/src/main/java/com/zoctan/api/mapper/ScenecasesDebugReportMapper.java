package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ScenecasesDebugReport;

import java.util.List;
import java.util.Map;

public interface ScenecasesDebugReportMapper extends MyMapper<ScenecasesDebugReport> {
    List<ScenecasesDebugReport> findscenecasedebugreportWithName(final Map<String, Object> params);

}