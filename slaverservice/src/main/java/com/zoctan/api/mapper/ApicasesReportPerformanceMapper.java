package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ApicasesReport;
import com.zoctan.api.entity.ApicasesReportPerformance;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ApicasesReportPerformanceMapper extends MyMapper<ApicasesReportPerformance> {
    List<ApicasesReportPerformance> findApicasereportWithName(final Map<String, Object> params);

    List<ApicasesReportPerformance> listallresult();

    void addcasereport(ApicasesReport ar);

    void adddynamiccaseperformancereport(@Param("caseid") long caseid, @Param("testplanid") long testplanid, @Param("batchname") String batchname, @Param("slaverid") long slaverid, @Param("status") String status, @Param("respone") String respone, @Param("assertvalue") String assertvalue, @Param("runtime") long runtime, @Param("expect") String expect, @Param("errorinfo") String errorinfo, @Param("create_time") Date create_time, @Param("lastmodify_time") Date lastmodify_time, @Param("creator") String creator, @Param("requestheader") String requestheader, @Param("requestdatas") String requestdatas, @Param("url") String url, @Param("requestmethod") String requestmethod, @Param("tableName") String tablename,@Param("projectid") long projectid,@Param("sceneid") long sceneid);

}