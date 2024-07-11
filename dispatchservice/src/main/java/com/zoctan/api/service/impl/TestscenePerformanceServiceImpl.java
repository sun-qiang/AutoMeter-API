package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.TestscenePerformance;
import com.zoctan.api.mapper.TestscenePerformanceMapper;
import com.zoctan.api.service.TestscenePerformanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/08
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestscenePerformanceServiceImpl extends AbstractService<TestscenePerformance> implements TestscenePerformanceService {
@Resource
private TestscenePerformanceMapper testscenePerformanceMapper;

@Override
public List<TestscenePerformance> findDicWithName(Map<String, Object> params) {
return testscenePerformanceMapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(TestscenePerformance params) {
testscenePerformanceMapper.updateDic(params);
}

}
