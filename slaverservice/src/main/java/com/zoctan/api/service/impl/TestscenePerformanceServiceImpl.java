package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.TestscenePerformanceMapper;
import com.zoctan.api.entity.TestscenePerformance;
import com.zoctan.api.service.TestscenePerformanceService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author Season
* @date 2024/06/03
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestscenePerformanceServiceImpl extends AbstractService<TestscenePerformance> implements TestscenePerformanceService {
@Resource
private TestscenePerformanceMapper testscenePerformanceMapper;

}
