package com.zoctan.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.zoctan.api.core.exception.ServiceException;
import com.zoctan.api.core.service.*;
import com.zoctan.api.dto.TestResponeData;
import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.service.ExecuteplanbatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2020/09/27
 */
@Service
@Slf4j
@Transactional  //(rollbackFor = Exception.class)
public class ExecuteplanServiceImpl extends AbstractService<Executeplan> implements ExecuteplanService {
    @Value("${spring.dispatchserver.serverurl}")
    private String dispatchserver;
    @Resource
    private ExecuteplanMapper executeplanMapper;
    @Resource
    private SlaverMapper slaverMapper;
    @Resource
    private ExecuteplanbatchMapper executeplanbatchMapper;
    @Resource
    private TestplanTestsceneMapper testplanTestsceneMapper;

    @Resource
    private ExecuteplanbatchService executeplanbatchService;


    @Override
    public List<Executeplan> findexplanWithName(Map<String, Object> params) {
        return this.executeplanMapper.findexplanWithName(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public List<Executeplan> getallexplan(long projectid) {
        return executeplanMapper.getallexplan(projectid);
    }

    @Override
    public List<Executeplan> getallexplanbytype(String usetype, long projectid) {
        return executeplanMapper.getallexplanbytype(usetype, projectid);
    }

    @Override
    public Integer getexecuteplannum(long projectid) {
        return executeplanMapper.getexecuteplannum(projectid);
    }

    @Override
    public List<String> getstaticsplan(long projectid) {
        return executeplanMapper.getstaticsplan(projectid);
    }

    @Override
    public void createNewTable(String tableName) {
        executeplanMapper.createNewTable(tableName);
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public void executeplancase(List<Executeplanbatch> testplanlist, String Exectype) {
        for (Executeplanbatch plan : testplanlist) {
            execcase(plan);
        }
    }


    @Override
    public void execcase(Executeplanbatch testplanandbatch) {
        Executeplan ep = executeplanMapper.findexplanWithid(testplanandbatch.getExecuteplanid());
        HttpHeader header = new HttpHeader();
        String DispatchServerurl = dispatchserver + "/exectestplancase/exec";
        String plantype = ep.getUsetype();
        List<Slaver> slaverlist = slaverMapper.findslaveralive(plantype, "已下线");
        //slaverlist = GetAliveSlaver(slaverlist);
        if (slaverlist.size() == 0) {
            ExecuteplanServiceImpl.log.info("未找到可用的：" + plantype + "的测试执行机，或者执行机已下线，请检查部署");
            throw new ServiceException("未找到可用的：" + plantype + "的测试执行机，或者执行机已下线，请检查部署");
        } else {
            String params = JSON.toJSONString(testplanandbatch);
            ExecuteplanServiceImpl.log.info("计划请求调度参数：" + params);
            try {
                ExecuteplanServiceImpl.log.info("计划开始请求调度。。。。。。。。。。。。。。。。。。。。。。。。");
                TestHttp testHttp = new TestHttp();
                header.addParam("Content-Type", "application/json;charset=utf-8");
                TestResponeData testResponeData = testHttp.doService("http", "", DispatchServerurl, header, new HttpParamers(), params, "POST", "", 3000);
                ExecuteplanServiceImpl.log.info("计划发送调度请求响应。。。。。。。。。。。。。。。。。。。。。。。。：" + testResponeData.getResponeContent());
            } catch (Exception e) {
                ExecuteplanServiceImpl.log.info("计划发送调度请求异常：" + e.getMessage());
                if (e.getMessage().contains("Connection refused") || e.getMessage().contains("connect timed out")) {
                    throw new ServiceException("未能连接到调度服务DispatchService，请检查是否已正常启动，或者检查到调度服务的网络是否通！");
                } else {
                    throw new ServiceException(e.getMessage());
                }
            }
        }
    }

    @Transactional(noRollbackFor = Exception.class)
    public List<Slaver> GetAliveSlaver(List<Slaver> SlaverList) {
        List<Slaver> AliveList = new ArrayList<>();
        for (Slaver slaver : SlaverList) {
            String IP = slaver.getIp();
            String Port = slaver.getPort();
            String ServerUrl = "http://" + IP + ":" + Port + "/exectestplancase/test";
            ExecuteplanTestcase plancase = new ExecuteplanTestcase();
            String params = JSON.toJSONString(plancase);
            HttpHeader header = new HttpHeader();
            String respon = "";
            try {
                TestHttp testHttp = new TestHttp();
                header.addParam("Content-Type", "application/json;charset=utf-8");
                TestResponeData testResponeData = testHttp.doService("http", "", ServerUrl, header, new HttpParamers(), params, "POST", "", 30000);
                respon = testResponeData.getResponeContent();
                //respon = HttphelpB1.doPost(ServerUrl, params, header, 5000, 5000);
            } catch (Exception e) {
                ExecuteplanServiceImpl.log.info("检测：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
                respon = e.getMessage();
            }
            if (respon.contains("200")) {
                AliveList.add(slaver);
            }
        }
        return AliveList;
    }

    @Override
    public void updatetestplanstatus(Long id, String status) {
        this.executeplanMapper.updatetestplanstatus(id, status);
    }

    @Override
    public Executeplan findexplanWithid(Long id) {
        return null;
    }

    @Override
    public void updateexecuteplanname(Executeplan params) {
        this.executeplanMapper.updateexecuteplanname(params);
    }
}
