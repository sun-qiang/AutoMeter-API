package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Apicases;
import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.mapper.ApicasesMapper;
import com.zoctan.api.mapper.ExecuteplanTestcaseMapper;
import com.zoctan.api.service.ApicasesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/09/11
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ApicasesServiceImpl extends AbstractService<Apicases> implements ApicasesService {
@Resource
private ApicasesMapper apicasesMapper;
    @Resource
    private ExecuteplanTestcaseMapper executeplanTestcaseMapper;



    @Override
    public List<Apicases> findApiCaseWithName(Map<String, Object> params) {
        return this.apicasesMapper.findApiCaseWithName(params);
    }

    @Override
    public List<Apicases> findApiCaseleft(Map<String, Object> params) {
        String  casetype= params.get("casetype").toString();
        Long executeplanid= Long.parseLong(params.get("executeplanid").toString());
        Long deployunitid= Long.parseLong(params.get("deployunitid").toString());
        List<Apicases> apicasesPlanList=new ArrayList<>();
        long apiid=0;
        if(params.get("apiid")==null)
        {
            apicasesPlanList= executeplanTestcaseMapper.findcasebyplanid(executeplanid,deployunitid,apiid,casetype);
        }
        else
        {
            apiid= Long.parseLong(params.get("apiid").toString());
            apicasesPlanList= executeplanTestcaseMapper.findcasebyplanid(executeplanid,deployunitid,apiid,casetype);
        }
        return apicasesPlanList;
    }

    @Override
    public List<Apicases> findApiCasebynameandcasetype(Map<String, Object> params) {
        return apicasesMapper.findApiCasebynameandcasetype(params);
    }

    @Override
    public void updateApicase(Apicases params) {
        apicasesMapper.updateApicase(params);

    }

    @Override
    public int ifexist(Condition condition) {
        return countByCondition(condition);
    }

    @Override
    public List<Apicases> forupdateifexist(Apicases apicase) {
        return apicasesMapper.forupdateifexist(apicase);
    }

    @Override
    public List<Apicases> getapicasebyName(long deployunitid, long apiid) {
        return apicasesMapper.getapicasebyName(deployunitid,apiid);
    }

    @Override
    public Integer getcasenum(String casetype,long projectid) {
        return apicasesMapper.getcasenum(casetype,projectid);
    }

    @Override
    public List<Apicases> getstaticsdeployunitcases(long projectid,long limit) {
        return apicasesMapper.getstaticsdeployunitcases(projectid,limit);
    }

    @Override
    public List<Apicases> getcasebydeployunitid(Long deployunitid) {
        return apicasesMapper.getcasebydeployunitid(deployunitid);
    }

    @Override
    public List<Apicases> getcasebyapiid(Long apiid) {
        return apicasesMapper.getcasebyapiid(apiid);
    }
}
