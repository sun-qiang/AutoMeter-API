package com.zoctan.api.service;

import com.zoctan.api.dto.TestVariablesDto;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.entity.Testvariables;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2021/07/14
*/
public interface TestvariablesService extends Service<Testvariables> {

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
