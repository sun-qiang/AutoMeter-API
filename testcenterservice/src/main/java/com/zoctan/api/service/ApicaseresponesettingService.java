package com.zoctan.api.service;

import com.zoctan.api.entity.ApiCasedata;
import com.zoctan.api.entity.Apicaseresponesetting;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/05/24
*/
public interface ApicaseresponesettingService extends Service<Apicaseresponesetting> {
List<Apicaseresponesetting> findDicWithName(final Map<String, Object> params);
int ifexist(Condition condition);
void updateDic(Apicaseresponesetting params);

}
