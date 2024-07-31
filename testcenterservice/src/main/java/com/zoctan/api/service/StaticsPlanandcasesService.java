package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.StaticsDeployunitandcases;
import com.zoctan.api.entity.StaticsPlanandcases;

import java.util.List;

/**
* @author SeasonFan
* @date 2021/04/14
*/
public interface StaticsPlanandcasesService extends Service<StaticsPlanandcases> {

    List<StaticsPlanandcases> getplanstatics(String StaticDate);
    List<StaticsPlanandcases> getplanpassratestatics(String statics_startdate, String statics_enddate, long projectid, long deployratelimit);

}
