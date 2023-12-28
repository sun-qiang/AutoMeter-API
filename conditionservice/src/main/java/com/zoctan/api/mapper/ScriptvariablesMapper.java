package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Scriptvariables;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ScriptvariablesMapper extends MyMapper<Scriptvariables> {
    List<Scriptvariables> findscriptvariablesWithName(final Map<String, Object> params);

    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updatescriptvariables(Scriptvariables params);

    List<Scriptvariables> getbyconditionid(@Param("conditionid") long conditionid);

}