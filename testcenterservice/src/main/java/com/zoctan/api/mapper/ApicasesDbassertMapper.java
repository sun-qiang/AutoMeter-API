package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.entity.Enviroment;

import java.util.List;
import java.util.Map;

public interface ApicasesDbassertMapper extends MyMapper<ApicasesDbassert> {

    List<ApicasesDbassert> findDbAssertWithName(final Map<String, Object> params);

    void updateDbAssert(ApicasesDbassert params);
}