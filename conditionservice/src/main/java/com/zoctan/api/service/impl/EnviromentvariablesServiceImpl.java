package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Enviromentvariables;
import com.zoctan.api.mapper.EnviromentvariablesMapper;
import com.zoctan.api.service.EnviromentvariablesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/14
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class EnviromentvariablesServiceImpl extends AbstractService<Enviromentvariables> implements EnviromentvariablesService {
@Resource
private EnviromentvariablesMapper enviromentvariablesMapper;

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }


    @Override
    public List<Enviromentvariables> findEnviromentvariablesWithName(Map<String, Object> params) {
        return enviromentvariablesMapper.findEnviromentvariablesWithName(params);
    }

    @Override
    public void updateEnviromentvariables(Enviromentvariables params) {
        enviromentvariablesMapper.updateEnviromentvariables(params);
    }
}
