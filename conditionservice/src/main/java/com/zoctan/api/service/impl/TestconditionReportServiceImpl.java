package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.TestconditionReport;
import com.zoctan.api.mapper.TestconditionReportMapper;
import com.zoctan.api.service.TestconditionReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2021/07/17
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestconditionReportServiceImpl extends AbstractService<TestconditionReport> implements TestconditionReportService {
@Resource
private TestconditionReportMapper testconditionReportMapper;

    @Override
    public List<TestconditionReport> findTestconditionReportWithName(Map<String, Object> params) {
        return testconditionReportMapper.findTestconditionReportWithName(params);
    }
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public List<TestconditionReport> getsubconditionnumswithstatus(Long testplanid, String batchname, String status, String conditionstatus) {
        return testconditionReportMapper.getsubconditionnumswithstatus(testplanid, batchname, status, conditionstatus);
    }

}
