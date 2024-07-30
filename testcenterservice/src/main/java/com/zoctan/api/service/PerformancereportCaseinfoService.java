package com.zoctan.api.service;

import com.zoctan.api.entity.PerformancereportCaseinfo;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/30
*/
public interface PerformancereportCaseinfoService extends Service<PerformancereportCaseinfo> {
List<PerformancereportCaseinfo> findDicWithName(final Map<String, Object> params);
int ifexist(Condition condition);
void updateDic(PerformancereportCaseinfo params);
}
