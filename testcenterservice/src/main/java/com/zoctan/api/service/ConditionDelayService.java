package com.zoctan.api.service;

import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionDelay;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/03/30
*/
public interface ConditionDelayService extends Service<ConditionDelay> {
    List<ConditionDelay> findtestconditiondelayWithName(final Map<String, Object> params);

    List<ConditionDelay> findtestconditiondelayWithType(final Map<String, Object> params);

    void updateTestconditiondelay(ConditionDelay params);


    int ifexist(Condition condition);
}
