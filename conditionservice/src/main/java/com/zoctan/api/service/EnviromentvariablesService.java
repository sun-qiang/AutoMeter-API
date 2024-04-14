package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Enviromentvariables;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/14
*/
public interface EnviromentvariablesService extends Service<Enviromentvariables> {
    int ifexist(Condition condition);
    List<Enviromentvariables> findEnviromentvariablesWithName(final Map<String, Object> params);
    void updateEnviromentvariables(Enviromentvariables params);
}
