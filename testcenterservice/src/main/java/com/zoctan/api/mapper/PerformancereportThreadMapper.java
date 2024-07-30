package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.PerformancereportThread;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface PerformancereportThreadMapper extends MyMapper<PerformancereportThread> {
    List<PerformancereportThread> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(PerformancereportThread params);
}