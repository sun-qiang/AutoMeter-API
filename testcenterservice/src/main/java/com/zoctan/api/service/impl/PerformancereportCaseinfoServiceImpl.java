package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PerformancereportCaseinfoMapper;
import com.zoctan.api.entity.PerformancereportCaseinfo;
import com.zoctan.api.service.PerformancereportCaseinfoService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/30
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PerformancereportCaseinfoServiceImpl extends AbstractService<PerformancereportCaseinfo> implements PerformancereportCaseinfoService {
@Resource
private PerformancereportCaseinfoMapper performancereportCaseinfoMapper;

@Override
public List<PerformancereportCaseinfo> findDicWithName(Map<String, Object> params) {
return performancereportCaseinfoMapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(PerformancereportCaseinfo params) {
performancereportCaseinfoMapper.updateDic(params);
}

}
