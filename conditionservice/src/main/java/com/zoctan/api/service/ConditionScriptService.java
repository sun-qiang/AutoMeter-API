package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.ConditionDb;
import com.zoctan.api.entity.ConditionScript;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2021/11/20
*/
public interface ConditionScriptService extends Service<ConditionScript> {
    List<ConditionScript> findtestconditionapiWithName(final Map<String, Object> params);

    List<ConditionScript> findtestconditionscriptwithid(Long conditionid,String conditiontype);
    List<ConditionScript> GetCaseListByConditionID(Long conditionid, String conditiontype);


    void updateTestconditionapi(ConditionScript params);

    int ifexist(Condition condition);
}
