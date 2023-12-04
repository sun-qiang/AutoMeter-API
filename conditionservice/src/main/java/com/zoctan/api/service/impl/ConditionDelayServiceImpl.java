package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionDelay;
import com.zoctan.api.mapper.ConditionDelayMapper;
import com.zoctan.api.service.ConditionDelayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/03/30
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ConditionDelayServiceImpl extends AbstractService<ConditionDelay> implements ConditionDelayService {
@Resource
private ConditionDelayMapper conditionDelayMapper;

    @Override
    public List<ConditionDelay> findtestconditiondelayWithName(Map<String, Object> params) {
        return conditionDelayMapper.findtestconditiondelayWithName(params);
    }

    @Override
    public void updateTestconditiondelay(ConditionDelay params) {
        conditionDelayMapper.updateTestconditiondelay(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public List<ConditionDelay> GetDelayConditionByConditionID(long conditionid) {
        return conditionDelayMapper.GetDelayConditionByConditionID(conditionid);
    }

    @Override
    public List<ConditionDelay> GetCaseListByConditionID(Long conditionid, String conditiontype) {
        return conditionDelayMapper.GetCaseListByConditionID(conditionid,conditiontype);
    }

}
