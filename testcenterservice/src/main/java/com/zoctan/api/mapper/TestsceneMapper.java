package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Machine;
import com.zoctan.api.entity.Testscene;

import java.util.List;
import java.util.Map;

public interface TestsceneMapper extends MyMapper<Testscene> {

    List<Testscene> findtestsceneWithName(final Map<String, Object> params);

    void updatescene(Testscene params);
}