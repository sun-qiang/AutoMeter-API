package com.zoctan.api.service;

import com.zoctan.api.entity.ApicasesDbassertValue;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/04/16
*/
public interface ApicasesDbassertValueService extends Service<ApicasesDbassertValue> {
    List<ApicasesDbassertValue> findDbAssertValueWithName(final Map<String, Object> params);
    int ifexist(Condition condition);

    void updateDbAssertValue(ApicasesDbassertValue params);
}
