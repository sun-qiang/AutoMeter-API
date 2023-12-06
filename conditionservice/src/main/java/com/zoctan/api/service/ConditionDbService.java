package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionDb;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2021/05/30
*/
public interface ConditionDbService extends Service<ConditionDb> {

    List<ConditionDb> finddbconditionWithName(final Map<String, Object> params);
    List<ConditionDb> GetDBConditionByConditionID(long conditionid);
    void updateTestconditiondb(ConditionDb params);
    List<ConditionDb> GetCaseListByConditionID(Long conditionid, String conditiontype);

    int ifexist(Condition condition);

}
