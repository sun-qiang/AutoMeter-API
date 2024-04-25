package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Plannmessage;

import java.util.List;
import java.util.Map;

public interface PlannmessageMapper extends MyMapper<Plannmessage> {
    List<Plannmessage> findPlannmessageWithName(final Map<String, Object> params);
    void updatePlannmessage(Plannmessage params);
}