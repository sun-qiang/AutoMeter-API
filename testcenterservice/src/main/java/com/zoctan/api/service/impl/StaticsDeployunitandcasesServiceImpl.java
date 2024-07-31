package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.StaticsDeployunitandcasesMapper;
import com.zoctan.api.entity.StaticsDeployunitandcases;
import com.zoctan.api.service.StaticsDeployunitandcasesService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author SeasonFan
* @date 2021/04/15
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class StaticsDeployunitandcasesServiceImpl extends AbstractService<StaticsDeployunitandcases> implements StaticsDeployunitandcasesService {
@Resource
private StaticsDeployunitandcasesMapper staticsDeployunitandcasesMapper;

    @Override
    public List<StaticsDeployunitandcases> getdeployunitstatics(String StaticDate) {
        return staticsDeployunitandcasesMapper.getdeployunitstatics(StaticDate);
    }

    @Override
    public List<StaticsDeployunitandcases> getdeployunitpassratestatics(String statics_startdate, String statics_enddate,long projectid, long deployratelimit) {
        return staticsDeployunitandcasesMapper.getdeployunitpassratestatics(statics_startdate,statics_enddate,projectid,deployratelimit);
    }
}
