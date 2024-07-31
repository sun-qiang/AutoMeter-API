package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.StaticsDeployunitandcases;

import java.util.List;

/**
* @author SeasonFan
* @date 2021/04/15
*/
public interface StaticsDeployunitandcasesService extends Service<StaticsDeployunitandcases> {
    List<StaticsDeployunitandcases> getdeployunitstatics(String StaticDate);
    List<StaticsDeployunitandcases> getdeployunitpassratestatics(String statics_startdate,String statics_enddate,long projectid,long deployratelimit);
}
