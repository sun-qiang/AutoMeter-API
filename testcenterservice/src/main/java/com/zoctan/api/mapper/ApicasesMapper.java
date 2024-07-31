package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Apicases;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface ApicasesMapper extends MyMapper<Apicases> {
    /**
     * 按微服务名或者协议名获取微服务内容
     *
     * @param params 参数
     * @return 微服务列表
     */
    List<Apicases> findApiCaseWithName(final Map<String, Object> params);

    List<Apicases> findApiCasebynameandcasetype(final Map<String, Object> params);

    /**
     * 更新微服务内容
     *
     * @param params 参数
     * @return 微服务列表
     */
    void updateApicase(Apicases params);

    int ifexist(Condition condition);

    List<Apicases> forupdateifexist(Apicases apicase);

    List<Apicases> getapicasebyName(@Param("deployunitid") long deployunitid, @Param("apiid") long apiid);

    Integer getcasenum(@Param("casetype") String casetype,@Param("projectid")long projectid);


    List<Apicases> getstaticsdeployunitcases(@Param("projectid")long projectid,@Param("limit")long limit);

    List<Apicases> getcasebydeployunitid(Long deployunitid);

    List<Apicases> getcasebyapiid(@Param("apiid") Long apiid);

}