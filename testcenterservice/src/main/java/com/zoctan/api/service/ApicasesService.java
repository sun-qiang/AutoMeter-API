package com.zoctan.api.service;

import com.zoctan.api.entity.Apicases;
import com.zoctan.api.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/09/11
*/
public interface ApicasesService extends Service<Apicases> {
    /**
     * 按微服务名或者协议名获取微服务内容
     *
     * @param params 参数
     * @return 微服务列表
     */
    List<Apicases> findApiCaseWithName(final Map<String, Object> params);

    List<Apicases> findApiCaseleft(final Map<String, Object> params);

    //计划装载case
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

    List<Apicases> getapicasebyName(long deployunitid,long apiid);

    Integer getcasenum(String casetype,long projectid);


    List<Apicases> getstaticsdeployunitcases(long projectid,long limit);

    List<Apicases> getcasebydeployunitid(Long deployunitid);
    List<Apicases> getcasebyapiid(Long apiid);


}
