package com.zoctan.api.service.impl;

import com.zoctan.api.dto.TestVariablesDto;
import com.zoctan.api.mapper.TestvariablesMapper;
import com.zoctan.api.entity.Testvariables;
import com.zoctan.api.service.TestvariablesService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2021/07/14
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestvariablesServiceImpl extends AbstractService<Testvariables> implements TestvariablesService {
@Resource
private TestvariablesMapper testvariablesMapper;

    @Override
    public List<TestVariablesDto> findtestvariablesWithName(Map<String, Object> params) {
        return testvariablesMapper.findtestvariablesWithName(params);
    }

    @Override
    public void updatetestvariables(Testvariables params) {
        testvariablesMapper.updatetestvariables(params);
    }

    @Override
    public List<Testvariables> findtestvariablesbycaseid(Map<String, Object> params) {
        return testvariablesMapper.findtestvariablesbycaseid(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

}
