package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.entity.ApicasesDbassertValue;

import java.util.List;
import java.util.Map;

public interface ApicasesDbassertValueMapper extends MyMapper<ApicasesDbassertValue> {

    List<ApicasesDbassertValue> findDbAssertValueWithName(final Map<String, Object> params);

    void updateDbAssertValue(ApicasesDbassertValue params);
}