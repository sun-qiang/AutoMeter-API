package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Api;
import com.zoctan.api.mapper.ApiMapper;
import com.zoctan.api.service.ApiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/04/24
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ApiServiceImpl extends AbstractService<Api> implements ApiService {
@Resource
private ApiMapper apiMapper;

    @Override
    public List<Api> findApiWithName(Map<String, Object> params) {
        return this.apiMapper.findApiWithName(params);
    }

    @Override
    public void updateApi(Api params) {
        apiMapper.updateApi(params);

    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public Integer getapinum(long projectid) {
        return apiMapper.getapinum(projectid);
    }

    @Override
    public List<Api> getstaticsdeployapi(long projectid,long apilimit) {
        return apiMapper.getstaticsdeployapi(projectid,apilimit);
    }

    @Override
    public List<Api> getapibydeployunitid(Long deployunitid) {
        return apiMapper.getapibydeployunitid(deployunitid);
    }

    @Override
    public List<Api> listAllbydeploy(long deployunitid,long modelid) {
        return apiMapper.listAllbydeploy(deployunitid,modelid);
    }

    @Override
    public Api getresponetypebydeployandapiname(String deployunitname, String apiname) {
        return apiMapper.getresponetypebydeployandapiname(deployunitname,apiname);
    }

    @Override
    public Api getapibydvap(Long deployunitid, String visittype, String path) {
        return apiMapper.getapibydvap(deployunitid, visittype, path);
    }
}
