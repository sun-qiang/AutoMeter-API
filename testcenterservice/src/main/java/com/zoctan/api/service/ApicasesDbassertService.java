package com.zoctan.api.service;

import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/16
*/
public interface ApicasesDbassertService extends Service<ApicasesDbassert> {
    List<ApicasesDbassert> findDbAssertWithName(final Map<String, Object> params);
    int ifexist(Condition condition);

    void updateDbAssert(ApicasesDbassert params);
}
