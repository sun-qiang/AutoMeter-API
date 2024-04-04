package com.zoctan.api.service;

import com.zoctan.api.entity.ConditionDb;
import com.zoctan.api.entity.ConditionOrder;
import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.ExecuteplanTestcase;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/01/03
*/
public interface ConditionOrderService extends Service<ConditionOrder> {

    List<ConditionOrder> findconditionorderWithName(final Map<String, Object> params);
    List<ConditionOrder> findconditionorderWithid(final Map<String, Object> params);
    List<ConditionOrder> findconditionorderWithidandtype(Long conditionid,String subconditiontype);
    void saveconditionorder(final List<ConditionOrder> conditionOrderList);
    void updateBatchconditionOrderList(final List<ConditionOrder> conditionOrderList);

    void deleteconditionorderbyconid(Long conditionid);
    void deleteconditionorderbysubconid(Long conditionid,String conditiontype,String subconditiontype);
}
