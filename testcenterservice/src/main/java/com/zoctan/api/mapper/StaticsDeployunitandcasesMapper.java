package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.StaticsDeployunitandcases;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StaticsDeployunitandcasesMapper extends MyMapper<StaticsDeployunitandcases> {
    List<StaticsDeployunitandcases> getdeployunitstatics(@Param("statics_date") String StaticDate);

    void savestaticsdeployunitandcases(@Param("casedataList") final List<StaticsDeployunitandcases> testcase);
    List<StaticsDeployunitandcases> getdeployunitpassratestatics(String statics_startdate,String statics_enddate,long projectid,long deployratelimit);


}