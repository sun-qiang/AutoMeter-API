package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.TestsceneMapper;
import com.zoctan.api.entity.Testscene;
import com.zoctan.api.service.TestsceneService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2023/10/29
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestsceneServiceImpl extends AbstractService<Testscene> implements TestsceneService {
@Resource
private TestsceneMapper testsceneMapper;

    @Override
    public List<Testscene> findtestsceneWithName(Map<String, Object> params) {
        return testsceneMapper.findtestsceneWithName(params);
    }

    @Override
    public void updatescene(Testscene params) {
        testsceneMapper.updatescene(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

}
