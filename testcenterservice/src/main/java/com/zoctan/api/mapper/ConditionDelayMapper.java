package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ConditionDelay;

import java.util.List;
import java.util.Map;

public interface ConditionDelayMapper extends MyMapper<ConditionDelay> {
    List<ConditionDelay> findtestconditiondelayWithName(final Map<String, Object> params);
    List<ConditionDelay> findtestconditiondelayWithType(final Map<String, Object> params);

    void updateTestconditiondelay(ConditionDelay params);
}