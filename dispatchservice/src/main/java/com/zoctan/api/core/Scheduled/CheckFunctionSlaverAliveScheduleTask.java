package com.zoctan.api.core.Scheduled;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.core.service.HttpHeader;
import com.zoctan.api.core.service.Httphelp;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fanseasn on 2020/11/21.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/11/21
*/
@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Component
public class CheckFunctionSlaverAliveScheduleTask {

    @Autowired(required = false)
    private SlaverMapper slaverMapper;

    @Autowired(required = false)
    private DispatchMapper dispatchMapper;

    @Autowired(required = false)
    private TestconditionMapper testconditionMapper;

    @Autowired(required = false)
    private ConditionApiService conditionApiService;

    @Autowired(required = false)
    private ConditionDbService conditionDbService;

    @Autowired(required = false)
    private ConditionScriptService conditionScriptService;

    @Autowired(required = false)
    private  ConditionDelayService conditionDelayService;

    @Autowired(required = false)
    private TestconditionReportService testconditionReportService;

    @Autowired(required = false)
    private TestvariablesValueService testvariablesValueService;

    @Autowired(required = false)
    private ApicasesReportService apicasesReportService;

    @Autowired(required = false)
    private ExecuteplanMapper executeplanMapper;


    @Autowired(required = false)
    RedisUtils redisUtils;
    private String redisKey = "";


    //3.添加定时任务,补偿检查slaveservice是否在线，如下线则补偿
    @Scheduled(cron = "0/2 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            boolean lock = redisUtils.tryLock(redisKey, "CheckFunctionSlaverAliveScheduleTask", redis_default_expire_time);
            if (lock) {
                try {
                    CheckAliveSlaver();
                } catch (Exception ex) {
                    CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver是否Alive定时器异常=======================" + ex.getMessage());
                } finally {
                    //TODO 执行任务结束后需要释放锁
                    //释放锁
                    redisUtils.deletekey(redisKey);
                    CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver是否Alive定时器-============释放分布式锁成功=======================");
                }
            } else {
                CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver是否Alive定时器-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
                return;
            }
        } catch (Exception ex) {
            CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver是否Alive定时器-异常: " + ex.getMessage());
        }
    }

    public void CheckAliveSlaver() {
        List<Slaver> SlaverList = slaverMapper.findslaverbytype("功能");
        for (Slaver slaver : SlaverList) {
            if (!slaver.getStatus().equalsIgnoreCase("已下线")) {
                String IP = slaver.getIp();
                String Port = slaver.getPort();
                String ServerUrl = "http://" + IP + ":" + Port + "/exectestplancase/test";
                ExecuteplanTestcase plancase = new ExecuteplanTestcase();
                String params = JSON.toJSONString(plancase);
                HttpHeader header = new HttpHeader();
                String respon = "";
                try {
                    respon = Httphelp.doPost(ServerUrl, params, header, 2000);
                    CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver检测：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
                } catch (Exception e) {
                    //1.置为已下线
                    slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
                    CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver "+slaver.getSlavername() + "检测结果为下线：" + ServerUrl + "请求响应结果异常。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                    //补偿
                    //2.获取当前slaver状态为非已完成的dispatch
//                    List<Dispatch> dispatchList = dispatchMapper.findnotfinishdis(slaver.getId(), "已完成");
//                    //3.删除dispatch生成的用例报告，条件报告，条件变量值数据
//                    HashMap<String, Dispatch> PlanidBatchidmap = new HashMap<>();
//                    for (Dispatch dis : dispatchList) {
//                        Long Planid = dis.getExecplanid();
//                        Long Caseid = dis.getTestcaseid();
//                        Long Batchid = dis.getBatchid();
//                        String PlanidBatchid = Planid.toString() + Batchid.toString();
//                        if (!PlanidBatchidmap.containsKey(PlanidBatchid)) {
//                            PlanidBatchidmap.put(PlanidBatchid, dis);
//                        }
//                        String BatchName = dis.getBatchname();
//                        //条件报告，条件变量值
//                        List<Testcondition> testconditionList = testconditionMapper.GetConditionByPlanIDAndConditionType(Caseid,"前置条件", "测试用例");
//                        if (testconditionList.size() > 0) {
//                            Long ConditionID = testconditionList.get(0).getId();
//                            //删除接口条件报告
//                            ConditionApi conditionApi = conditionApiService.getBy("conditionid", ConditionID);
//                            if (conditionApi != null) {
//                                Long ApiSubConditionID = conditionApi.getId();
//                                Condition apicon = new Condition(TestconditionReport.class);
//                                apicon.createCriteria().andCondition("conditionid = " + ConditionID)
//                                        .andCondition("testplanid = " + Planid)
//                                        .andCondition("batchname = '" + BatchName + " '")
//                                        .andCondition("subconditionid = " + ApiSubConditionID);
//                                if (testconditionReportService.ifexist(apicon) > 0) {
//                                    testconditionReportService.deleteByCondition(apicon);
//                                }
//                                //删除接口产生的变量
//                                Long BindCaseid = conditionApi.getCaseid();
//                                Condition variablevaluecon = new Condition(TestvariablesValue.class);
//                                variablevaluecon.createCriteria().andCondition("caseid = " + BindCaseid)
//                                        .andCondition("planid = " + Planid)
//                                        .andCondition("batchname = '" + BatchName + " '");
//                                if (testvariablesValueService.ifexist(variablevaluecon) > 0) {
//                                    testvariablesValueService.deleteByCondition(variablevaluecon);
//                                }
//                            }
//                            //删除数据库条件报告
//                            ConditionDb conditionDb = conditionDbService.getBy("conditionid", ConditionID);
//                            if (conditionDb != null) {
//                                Long DBSubConditionID = conditionDb.getId();
//                                Condition dbcon = new Condition(TestconditionReport.class);
//                                dbcon.createCriteria().andCondition("conditionid = " + ConditionID)
//                                        .andCondition("testplanid = " + Planid)
//                                        .andCondition("batchname = '" + BatchName + " '")
//                                        .andCondition("subconditionid = " + DBSubConditionID);
//
//                                if (testconditionReportService.ifexist(dbcon) > 0) {
//                                    testconditionReportService.deleteByCondition(dbcon);
//                                }
//                            }
//                            //删除脚本条件报告
//                            ConditionScript conditionScript = conditionScriptService.getBy("conditionid", ConditionID);
//                            if (conditionScript != null) {
//                                Long ScriptSubConditionID = conditionScript.getId();
//                                Condition scriptcon = new Condition(TestconditionReport.class);
//                                scriptcon.createCriteria().andCondition("conditionid = " + ConditionID)
//                                        .andCondition("testplanid = " + Planid)
//                                        .andCondition("batchname = '" + BatchName + " '")
//                                        .andCondition("subconditionid = " + ScriptSubConditionID);
//                                if (testconditionReportService.ifexist(scriptcon) > 0) {
//                                    testconditionReportService.deleteByCondition(scriptcon);
//                                }
//                            }
//                            //删除延时条件报告
//                            ConditionDelay conditionDelay = conditionDelayService.getBy("conditionid", ConditionID);
//                            if (conditionDelay != null) {
//                                Long DelaySubConditionID = conditionDelay.getId();
//                                Condition delaycon = new Condition(TestconditionReport.class);
//                                delaycon.createCriteria().andCondition("conditionid = " + ConditionID)
//                                        .andCondition("testplanid = " + Planid)
//                                        .andCondition("batchname = '" + BatchName + " '")
//                                        .andCondition("subconditionid = " + DelaySubConditionID);
//                                if (testconditionReportService.ifexist(delaycon) > 0) {
//                                    testconditionReportService.deleteByCondition(delaycon);
//                                }
//                            }
//                        }
//                        //用例报告
//                        Condition reportcon = new Condition(ApicasesReport.class);
//                        reportcon.createCriteria().andCondition("caseid = " + Caseid)
//                                .andCondition("testplanid = " + Planid)
//                                .andCondition("batchname = '" + BatchName + " '")
//                                .andCondition("slaverid = " + slaver.getId());
//                        if (apicasesReportService.ifexist(reportcon) > 0) {
//                            apicasesReportService.deleteByCondition(reportcon);
//                        }
//                    }
//                    //4.更新dispatch状态为待分配，更新为可用的slaverid
//                    for (String ids : PlanidBatchidmap.keySet()) {
//                        Long Planid = PlanidBatchidmap.get(ids).getExecplanid();
//                        CompensateAfterFail("用例所分配的执行机:" + slaver.getSlavername() + "已下线，请检查此slaverservice是否正常运行！", PlanidBatchidmap.get(ids), Planid, dispatchList);
//                    }
                }
            }
        }
    }


    private void CompensateAfterFail(String ErrorMessage, Dispatch dispatch, Long PlanID, List<Dispatch> SlaverDispathcList) {
        List<Slaver> allliveslaver = GetAllAliveSlaver();
        if (allliveslaver.size() == 0) {
            //dispatchMapper.updatedispatchfail("调度失败", ErrorMessage, dispatch.getSlaverid(), dispatch.getExecplanid(), dispatch.getBatchid());
        } else {
            Executeplan ep = executeplanMapper.findexplanWithid(PlanID);
            if (ep.getRunmode().equalsIgnoreCase("单机运行")) {
                for (Dispatch dis : SlaverDispathcList) {
                    dis.setSlaverid(allliveslaver.get(0).getId());
                    dis.setSlavername(allliveslaver.get(0).getSlavername());
                    dis.setLastmodifyTime(new Date());
                    dispatchMapper.updateByPrimaryKey(dis);
                }
            }
            //平均分配
            else {
                int slaversize = allliveslaver.size();
                List<List<Dispatch>> partitions = Lists.partition(SlaverDispathcList, slaversize);
                for (int i = 0; i < partitions.size(); i++) {
                    for (Dispatch dis : partitions.get(i)) {
                        dis.setSlaverid(allliveslaver.get(i).getId());
                        dis.setSlavername(allliveslaver.get(i).getSlavername());
                        dis.setLastmodifyTime(new Date());
                        dispatchMapper.updateByPrimaryKey(dis);
                    }
                }
            }
        }
    }

    public List<Slaver> GetAllAliveSlaver() {
        List<Slaver> slaverlist = slaverMapper.findslaveralive("功能", "已下线");
        List<Slaver> slaverlistresult = new ArrayList<>();
        for (Slaver slaver : slaverlist) {
            String IP = slaver.getIp();
            String Port = slaver.getPort();
            String ServerUrl = "http://" + IP + ":" + Port + "/exectestplancase/test";
            ExecuteplanTestcase plancase = new ExecuteplanTestcase();
            String params = JSON.toJSONString(plancase);
            HttpHeader header = new HttpHeader();
            String respon = "";
            try {
                respon = Httphelp.doPost(ServerUrl, params, header, 3000);
                slaverlistresult.add(slaver);
                CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
            } catch (Exception e) {
                CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
            }
        }
        return slaverlistresult;
    }

    @PostConstruct
    public void Init() {
        redisKey = "CheckFunctionSlaverAliveScheduleTask-RedisLock" + new Date();
        CheckFunctionSlaverAliveScheduleTask.log.info("调度服务检查功能Slaver调度服务检查Slaver是否Alive定时器-redisKey is:" + redisKey);
    }
}
