package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.TestsceneDispatch;
import com.zoctan.api.mapper.TestsceneDispatchMapper;
import com.zoctan.api.service.TestsceneDispatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/07
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestsceneDispatchServiceImpl extends AbstractService<TestsceneDispatch> implements TestsceneDispatchService {
@Resource
private TestsceneDispatchMapper testsceneDispatchMapper;


    @Override
    public List<TestsceneDispatch> findDicWithName(Long planid, Long batchid, Long Slaverid) {
        return testsceneDispatchMapper.findDicWithName(planid, batchid, Slaverid);
    }

    @Override
public int ifexist(Condition con) {
return countByCondition(con);
}

    @Override
    public void insertBatchDispatch(List<TestsceneDispatch> dispatchList) {
        testsceneDispatchMapper.insertBatchDispatch(dispatchList);
    }


}
