package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Scriptvariables;
import com.zoctan.api.mapper.ScriptvariablesMapper;
import com.zoctan.api.service.ScriptvariablesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/04/02
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ScriptvariablesServiceImpl extends AbstractService<Scriptvariables> implements ScriptvariablesService {
@Resource
private ScriptvariablesMapper scriptvariablesMapper;


    @Override
    public List<Scriptvariables> findscriptvariablesWithName(Map<String, Object> params) {
        return scriptvariablesMapper.findscriptvariablesWithName(params);
    }

    @Override
    public void updatescriptvariables(Scriptvariables params) {
        scriptvariablesMapper.updatescriptvariables(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public List<Scriptvariables> getbyconditionid(long conditionid) {
        return scriptvariablesMapper.getbyconditionid(conditionid);
    }


}
