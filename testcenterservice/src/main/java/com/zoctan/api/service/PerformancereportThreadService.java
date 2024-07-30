package com.zoctan.api.service;

import com.zoctan.api.entity.PerformancereportThread;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/29
*/
public interface PerformancereportThreadService extends Service<PerformancereportThread> {
List<PerformancereportThread> findDicWithName(final Map<String, Object> params);
int ifexist(Condition condition);
void updateDic(PerformancereportThread params);
}
