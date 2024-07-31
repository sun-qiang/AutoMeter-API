package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Api;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface ApiMapper extends MyMapper<Api> {
    /**
     * 按微服务名或者协议名获取微服务内容
     *
     * @param params 参数
     * @return 微服务列表
     */
    List<Api> findApiWithName(final Map<String, Object> params);

    /**
     * 更新微服务内容
     *
     * @param params 参数
     * @return 微服务列表
     */
    void updateApi(Api params);

    List<Api> listAllbydeploy(@Param("deployunitid") long deployunitid,@Param("modelid") long modelid);

    Api getresponetypebydeployandapiname(@Param("deployunitname") String deployunitname, @Param("apiname") String apiname);

    Api getapibydvap(@Param("deployunitid") Long deployunitid, @Param("visittype") String visittype, @Param("path") String path);

    int ifexist(Condition condition);

    Integer getapinum(long projectid);

    List<Api> getstaticsdeployapi(@Param("projectid")long projectid,@Param("apilimit")long apilimit);

    List<Api> getapibydeployunitid(@Param("deployunitid")Long deployunitid);
}