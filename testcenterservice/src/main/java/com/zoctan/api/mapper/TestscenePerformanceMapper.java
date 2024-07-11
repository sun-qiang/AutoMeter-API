package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.TestscenePerformance;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface TestscenePerformanceMapper extends MyMapper<TestscenePerformance> {
    List<TestscenePerformance> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(TestscenePerformance params);
}