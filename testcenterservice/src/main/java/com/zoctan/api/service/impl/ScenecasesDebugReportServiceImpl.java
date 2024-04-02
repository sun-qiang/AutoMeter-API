package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.ScenecasesDebugReportMapper;
import com.zoctan.api.entity.ScenecasesDebugReport;
import com.zoctan.api.service.ScenecasesDebugReportService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/03/31
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ScenecasesDebugReportServiceImpl extends AbstractService<ScenecasesDebugReport> implements ScenecasesDebugReportService {
@Resource
private ScenecasesDebugReportMapper scenecasesDebugReportMapper;

    @Override
    public List<ScenecasesDebugReport> findscenecasedebugreportWithName(Map<String, Object> params) {
        return scenecasesDebugReportMapper.findscenecasedebugreportWithName(params);
    }
}
