package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.ApicaseresponesettingMapper;
import com.zoctan.api.entity.Apicaseresponesetting;
import com.zoctan.api.service.ApicaseresponesettingService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/05/24
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ApicaseresponesettingServiceImpl extends AbstractService<Apicaseresponesetting> implements ApicaseresponesettingService {
@Resource
private ApicaseresponesettingMapper apicaseresponesettingMapper;

@Override
public List<Apicaseresponesetting> findDicWithName(Map<String, Object> params) {
return apicaseresponesettingMapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(Apicaseresponesetting params) {
apicaseresponesettingMapper.updateDic(params);
}

}
