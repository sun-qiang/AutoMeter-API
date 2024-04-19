package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.ApicasesDbassertValueMapper;
import com.zoctan.api.entity.ApicasesDbassertValue;
import com.zoctan.api.service.ApicasesDbassertValueService;
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
public class ApicasesDbassertValueServiceImpl extends AbstractService<ApicasesDbassertValue> implements ApicasesDbassertValueService {
@Resource
private ApicasesDbassertValueMapper apicasesDbassertValueMapper;

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }


    @Override
    public List<ApicasesDbassertValue> findDbAssertValueWithName(Map<String, Object> params) {
        return apicasesDbassertValueMapper.findDbAssertValueWithName(params);
    }

    @Override
    public void updateDbAssertValue(ApicasesDbassertValue params) {
        apicasesDbassertValueMapper.updateDbAssertValue(params);
    }
}
