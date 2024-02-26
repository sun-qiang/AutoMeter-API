package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.dto.AssembleDeploy;
import com.zoctan.api.entity.Envmachine;
import com.zoctan.api.entity.Macdepunit;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface MacdepunitMapper extends MyMapper<Macdepunit> {
    /**
     * 按环境名或者服务器名获取服务器微服务内容
     *
     * @param params 参数
     * @return 环境服务器列表
     */
    List<Macdepunit> findMacAndDepWithName(final Map<String, Object> params);
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
    AssembleDeploy  findassembledeploybyid(long id);

    int ifexist(Condition condition);

    Integer findmachinenumbyenvidanddeployid(@Param("envid") long envid, @Param("depunitid") long depunitid);

    Integer findmachinenumbyenvidanddeployidforup(@Param("envid") long envid, @Param("depunitid") long depunitid,@Param("id")long id);

    Integer findmachinenumbyenvidandassemidforup(@Param("envid") long envid, @Param("assembleid") long assembleid,@Param("id")long id);

    List<Macdepunit> getmacdepbyenvidandassid(@Param("envid") long envid, @Param("assembleid") long assembleid);
    void deletemacdepbyenvidandassid(@Param("envid") long envid, @Param("assembleid") long assembleid);


    Macdepunit getmacdepbyenvidanddepid(@Param("envid")long envid, @Param("depunitid")long depunitid);

    List<Macdepunit> getenvassemblelistbyenvidandtype(@Param("envid") long envid, @Param("assembletype") String assembletype);

    List<Macdepunit> findmachinebyid(@Param("machineid") long machineid);

    List<Macdepunit> findenviromentbyenvid(@Param("envid") long envid);

    List<Macdepunit> findassemblebyassid(@Param("assembleid") long assid);

}