package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ConditionOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConditionOrderMapper extends MyMapper<ConditionOrder> {
    List<ConditionOrder> findconditionorderWithName(final Map<String, Object> params);

    List<ConditionOrder> findconditionorderWithid(final Map<String, Object> params);

    List<ConditionOrder> findconditionorderWithidandtype(@Param("conditionid") Long conditionid, @Param("subconditiontype") String subconditiontype);

    void saveconditionorder(@Param("conditionOrderList") final List<ConditionOrder> conditionOrderList);

    void updateBatchconditionOrderList(@Param("conditionOrderList")final List<ConditionOrder> conditionOrderList);


    void deleteconditionorderbyconid(@Param("conditionid") Long conditionid);

    void deleteconditionorderbysubconid(@Param("conditionid") Long conditionid, @Param("conditiontype") String conditiontype, @Param("subconditiontype") String subconditiontype);
}