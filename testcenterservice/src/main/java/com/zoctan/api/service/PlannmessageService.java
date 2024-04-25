package com.zoctan.api.service;

import com.zoctan.api.entity.Machine;
import com.zoctan.api.entity.Plannmessage;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/25
*/
public interface PlannmessageService extends Service<Plannmessage> {
    int ifexist(Condition condition);

    List<Plannmessage> findPlannmessageWithName(final Map<String, Object> params);
    void updatePlannmessage(Plannmessage params);

}
