package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Api;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/04/24
*/
public interface ApiService extends Service<Api> {
    /**
     * 按条件查询Api内容
     *
     * @param params 参数
     * @return 用户列表
     */
    List<Api> findApiWithName(final Map<String, Object> params);

    /**
     * 更新Api内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updateApi(Api params);
    List<Api> listAllbydeploy(long deployunitid,long modelid);

    Api getresponetypebydeployandapiname(String deployunitname,String apiname);
    Api getapibydvap(Long deployunitid,String visittype,String path);

    int ifexist(Condition condition);

    Integer getapinum(long projectid);

    List<Api> getstaticsdeployapi(long projectid,long apilimit);

    List<Api> getapibydeployunitid(Long deployunitid);

}
