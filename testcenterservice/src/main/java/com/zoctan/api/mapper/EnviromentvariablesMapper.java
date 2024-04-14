package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Enviromentvariables;

import java.util.List;
import java.util.Map;

public interface EnviromentvariablesMapper extends MyMapper<Enviromentvariables> {
    List<Enviromentvariables> findEnviromentvariablesWithName(final Map<String, Object> params);
    void updateEnviromentvariables(Enviromentvariables params);
}