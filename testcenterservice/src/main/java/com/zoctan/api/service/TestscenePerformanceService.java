package com.zoctan.api.service;

import com.zoctan.api.entity.TestscenePerformance;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/07/08
 */
public interface TestscenePerformanceService extends Service<TestscenePerformance> {
    List<TestscenePerformance> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);

    void updateDic(TestscenePerformance params);
}
