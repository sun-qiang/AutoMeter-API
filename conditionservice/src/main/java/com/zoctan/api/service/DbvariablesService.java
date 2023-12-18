package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.DbconditionVariables;
import com.zoctan.api.entity.Dbvariables;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/03/25
*/
public interface DbvariablesService extends Service<Dbvariables> {
    List<Dbvariables> finddbvariablesWithName(final Map<String, Object> params);

    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updatedbvariables(Dbvariables params);

    List<Dbvariables> getbyconditionid(long conditionid);

    int ifexist(Condition condition);

}
