package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ConditionApi;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConditionApiMapper extends MyMapper<ConditionApi> {
    List<ConditionApi> findtestconditionapiWithName(final Map<String, Object> params);

    void updateTestconditionapi(ConditionApi params);

    List<ConditionApi> getallTestconditionapi();

    List<ConditionApi> GetCaseListByConditionID(@Param("conditionid")Long conditionid,@Param("conditiontype")String conditiontype);
}