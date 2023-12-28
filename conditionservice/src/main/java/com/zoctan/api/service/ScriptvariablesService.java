package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.DbconditionVariables;
import com.zoctan.api.entity.Scriptvariables;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/04/02
*/
public interface ScriptvariablesService extends Service<Scriptvariables> {

    List<Scriptvariables> findscriptvariablesWithName(final Map<String, Object> params);

    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updatescriptvariables(Scriptvariables params);


    int ifexist(Condition condition);


    List<Scriptvariables> getbyconditionid(long conditionid);

}
