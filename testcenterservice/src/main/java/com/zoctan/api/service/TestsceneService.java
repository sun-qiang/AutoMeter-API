package com.zoctan.api.service;

import com.zoctan.api.entity.Testscene;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2023/10/29
*/
public interface TestsceneService extends Service<Testscene> {

    List<Testscene> findtestsceneWithName(final Map<String, Object> params);
    int ifexist(Condition condition);

    void updatescene(Testscene params);
}
