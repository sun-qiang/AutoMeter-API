package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PerformancereportThreadMapper;
import com.zoctan.api.entity.PerformancereportThread;
import com.zoctan.api.service.PerformancereportThreadService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/29
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PerformancereportThreadServiceImpl extends AbstractService<PerformancereportThread> implements PerformancereportThreadService {
@Resource
private PerformancereportThreadMapper performancereportThreadMapper;

@Override
public List<PerformancereportThread> findDicWithName(Map<String, Object> params) {
return performancereportThreadMapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(PerformancereportThread params) {
performancereportThreadMapper.updateDic(params);
}

}
