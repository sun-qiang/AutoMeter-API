package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PlanmailMapper;
import com.zoctan.api.entity.Planmail;
import com.zoctan.api.service.PlanmailService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/05/19
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PlanmailServiceImpl extends AbstractService<Planmail> implements PlanmailService {
@Resource
private PlanmailMapper planmailMapper;

@Override
public List<Planmail> findDicWithName(Map<String, Object> params) {
return planmailMapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(Planmail params) {
planmailMapper.updateDic(params);
}

    @Override
    public void saveplanmail(List<Planmail> planmailList) {
        planmailMapper.saveplanmail(planmailList);
    }

}
