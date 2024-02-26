package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.dto.AssembleDeploy;
import com.zoctan.api.entity.Envmachine;
import com.zoctan.api.entity.Macdepunit;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/05/21
*/
public interface MacdepunitService extends Service<Macdepunit> {
    /**
     * 按环境名或者服务器名获取服务器微服务内容
     *
     * @param params 参数
     * @return 环境服务器列表
     */


    List<Macdepunit> findMacAndDepWithName(final Map<String, Object> params);
    AssembleDeploy  findassembledeploybyid(long id);

    List<Macdepunit> findMacAndDepWithEnv(final Map<String, Object> params);
    List<AssembleDeploy> findMacAndAssembleWithEnv(final Map<String, Object> params);




    List<Macdepunit> findMacAndDepWithid(final Map<String, Object> params);


    /**
     * 更新服务器微服务内容
     *
     * @param params 参数
     * @return 环境服务器列表
     */
    void updateMacAndDep(AssembleDeploy params);

    int ifexist(Condition condition);

    Integer findmachinenumbyenvidanddeployid(long envid,long depunitid);

    Integer findmachinenumbyenvidanddeployidforup(long envid,long depunitid,long id);

    Integer findmachinenumbyenvidandassemidforup(long envid,long assembleid,long id);

    List<Macdepunit> getmacdepbyenvidandassid(long envid,long depunitid);
    void deletemacdepbyenvidandassid(long envid,long depunitid);



    Macdepunit getmacdepbyenvidanddepid(long envid,long depunitid);

    List<Macdepunit> getenvassemblelistbyenvidandtype(long envid,String  assembletype);

    List<Macdepunit> findmachinebyid(long machineid);
    List<Macdepunit> findenviromentbyenvid(long envid);

    List<Macdepunit> findassemblebyassid(long assid);



}
