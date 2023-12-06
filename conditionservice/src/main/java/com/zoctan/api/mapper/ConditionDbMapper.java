package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionDb;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConditionDbMapper extends MyMapper<ConditionDb> {
    List<ConditionDb> finddbconditionWithName(final Map<String, Object> params);

    void updateTestconditiondb(ConditionDb params);

    List<ConditionDb> GetDBConditionByConditionID(@Param("conditionid") long conditionid);

    List<ConditionDb> GetCaseListByConditionID(@Param("conditionid")Long conditionid, @Param("conditiontype")String conditiontype);

}