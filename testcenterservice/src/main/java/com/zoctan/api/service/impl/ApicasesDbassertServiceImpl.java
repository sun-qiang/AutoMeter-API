package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.ApicasesDbassertMapper;
import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.service.ApicasesDbassertService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/16
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ApicasesDbassertServiceImpl extends AbstractService<ApicasesDbassert> implements ApicasesDbassertService {
@Resource
private ApicasesDbassertMapper apicasesDbassertMapper;

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }


    @Override
    public List<ApicasesDbassert> findDbAssertWithName(Map<String, Object> params) {
        return apicasesDbassertMapper.findDbAssertWithName(params);
    }

    @Override
    public void updateDbAssert(ApicasesDbassert params) {
        apicasesDbassertMapper.updateDbAssert(params);
    }
}
