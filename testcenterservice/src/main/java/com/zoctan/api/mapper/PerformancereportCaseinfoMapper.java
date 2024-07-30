package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.PerformancereportCaseinfo;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface PerformancereportCaseinfoMapper extends MyMapper<PerformancereportCaseinfo> {
    List<PerformancereportCaseinfo> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(PerformancereportCaseinfo params);
}