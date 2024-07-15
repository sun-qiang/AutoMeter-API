package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.ApicasesReportPerformance;
import com.zoctan.api.mapper.ApicasesReportPerformanceMapper;
import com.zoctan.api.service.ApicasesReportPerformanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/12/14
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ApicasesReportPerformanceServiceImpl extends AbstractService<ApicasesReportPerformance> implements ApicasesReportPerformanceService {
@Resource
private ApicasesReportPerformanceMapper apicasesReportPerformanceMapper;

    @Override
    public List<ApicasesReportPerformance> findApicasereportWithName(Map<String, Object> params) {
        return apicasesReportPerformanceMapper.findApicasereportWithName(params);
    }

    @Override
    public List<ApicasesReportPerformance> listallresult() {
        return apicasesReportPerformanceMapper.listallresult();
    }

    @Override
    public void adddynamiccaseperformancereport(long caseid, long testplanid, String batchname, long slaverid, String status, String respone, String assertvalue, long runtime, String expect, String errorinfo, Date create_time, Date lastmodify_time, String creator, String requestheader, String requestdatas, String url, String requestmethod, String tablename,long projectid,long sceneid) {
        apicasesReportPerformanceMapper.adddynamiccaseperformancereport(caseid, testplanid, batchname, slaverid, status, respone, assertvalue, runtime, expect, errorinfo, create_time, lastmodify_time, creator, requestheader, requestdatas, url, requestmethod, tablename,projectid,sceneid);
    }


}
