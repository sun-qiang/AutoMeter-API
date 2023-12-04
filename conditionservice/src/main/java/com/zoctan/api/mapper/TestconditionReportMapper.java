package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.TestconditionReport;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface TestconditionReportMapper extends MyMapper<TestconditionReport> {
    List<TestconditionReport> findTestconditionReportWithName(final Map<String, Object> params);

    int ifexist(Condition condition);
    List<TestconditionReport> getsubconditionnumswithstatus(@Param("testplanid") Long testplanid, @Param("batchname") String batchname, @Param("status") String status, @Param("conditionstatus") String conditionstatus);

}