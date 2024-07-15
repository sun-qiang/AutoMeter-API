package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.ApicasesReportPerformance;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/12/14
*/
public interface ApicasesReportPerformanceService extends Service<ApicasesReportPerformance> {

    /**
     * 按条件查询Api内容
     *
     * @param params 参数
     * @return 用户列表
     */
    List<ApicasesReportPerformance> findApicasereportWithName(final Map<String, Object> params);

    List<ApicasesReportPerformance> listallresult();

    void adddynamiccaseperformancereport(long caseid, long testplanid, String batchname, long slaverid, String status, String respone, String assertvalue, long runtime, String expect, String errorinfo, Date create_time, Date lastmodify_time, String creator, String requestheader, String requestdatas, String url, String requestmethod, String tablename,long projectid,long sceneid );


}
