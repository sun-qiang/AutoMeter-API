package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.dto.TestVariablesDto;
import com.zoctan.api.entity.Testvariables;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface TestvariablesMapper extends MyMapper<Testvariables> {
    List<TestVariablesDto> findtestvariablesWithName(final Map<String, Object> params);

    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updatetestvariables(Testvariables params);
    List<Testvariables> findtestvariablesbycaseid(final Map<String, Object> params);


    int ifexist(Condition condition);
}