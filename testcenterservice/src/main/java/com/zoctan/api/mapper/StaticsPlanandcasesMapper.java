package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.StaticsPlanandcases;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StaticsPlanandcasesMapper extends MyMapper<StaticsPlanandcases> {
    List<StaticsPlanandcases> getplanstatics(@Param("statics_date") String StaticDate);

    void savestaticsplanandcases(@Param("casedataList") final List<StaticsPlanandcases> testcase);
    List<StaticsPlanandcases> getplanpassratestatics(String statics_startdate, String statics_enddate, long projectid, long deployratelimit);

}