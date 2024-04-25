package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PlannmessageMapper;
import com.zoctan.api.entity.Plannmessage;
import com.zoctan.api.service.PlannmessageService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/25
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PlannmessageServiceImpl extends AbstractService<Plannmessage> implements PlannmessageService {
@Resource
private PlannmessageMapper plannmessageMapper;

    @Override
    public int ifexist(Condition condition) {
        return countByCondition(condition);
    }

    @Override
    public List<Plannmessage> findPlannmessageWithName(Map<String, Object> params) {
        return plannmessageMapper.findPlannmessageWithName(params);
    }

    @Override
    public void updatePlannmessage(Plannmessage params) {
        plannmessageMapper.updatePlannmessage(params);
    }
}
