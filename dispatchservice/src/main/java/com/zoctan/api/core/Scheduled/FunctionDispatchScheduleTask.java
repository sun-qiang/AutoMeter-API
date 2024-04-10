package com.zoctan.api.core.Scheduled;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.core.service.HttpHeader;
import com.zoctan.api.core.service.HttpParamers;
import com.zoctan.api.core.service.Httphelp;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.*;
import com.zoctan.api.service.impl.ExecuteplanServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
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
public class FunctionDispatchScheduleTask {
    @Value("${spring.conditionserver.serverurl}")
    private String conditionserver;

    @Autowired(required = false)
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    RedisUtils redisUtils;
    @Autowired(required = false)
    private DispatchMapper dispatchMapper;
    @Autowired(required = false)
    private ExecuteplanMapper executeplanMapper;
    @Autowired(required = false)
    private ExecuteplanbatchMapper executeplanbatchMapper;

    @Autowired(required = false)
    private ExecuteplanbatchService executeplanbatchService;
    @Autowired(required = false)
    private TestconditionReportMapper testconditionReportMapper;
    @Autowired(required = false)
    private TestconditionService testconditionService;
    @Autowired(required = false)
    private ConditionApiService conditionApiService;
    @Autowired(required = false)
    private ConditionDbService conditionDbService;
    @Autowired(required = false)
    private ConditionScriptService conditionScriptService;

    @Autowired(required = false)
    private ConditionDelayService conditionDelayService;
    @Autowired(required = false)
    private TestconditionReportService testconditionReportService;
    @Autowired(required = false)
    private DispatchService dispatchService;
    private String redisKey = "";


    //3.添加定时任务,处理并行多机并发性能测试任务
    @Scheduled(cron = "0/10 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            boolean lock = redisUtils.tryLock(redisKey, "FunctionDispatchScheduleTask", redis_default_expire_time);
            if (lock) {
                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】lock......................................................................");
                Executeplanbatch executeplanbatch = executeplanbatchMapper.getrecentbatch("初始", "立即执行");
                long PlanID = 0;
                String BatchName = "";
                try {
                    if (executeplanbatch != null) {
                        PlanID = executeplanbatch.getExecuteplanid();
                        BatchName = executeplanbatch.getBatchname();
                        //判断计划的所有前置条件是否已经完成，并且全部成功，否则更新Dispatch状态为前置条件失败
                        Dispatch dispatch = new Dispatch();
                        dispatch.setBatchname(BatchName);
                        dispatch.setExecplanid(PlanID);
                        dispatch.setExecplanname(executeplanbatch.getExecuteplanname());
                        dispatch.setSlaverid(executeplanbatch.getSlaverid());
                        String RedayRespon = RequestConditionService(dispatch, "/testcondition/planconditionreday");
                        if (!RedayRespon.contains("\"code\":200")) {
                            FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件服务RedayRespon:" + RedayRespon);
                            RequestConditionService(dispatch, "/testcondition/execplancondition");
                        } else {
                            String FinishRespon = RequestConditionService(dispatch, "/testcondition/planconditionfinish");
                            FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件服务FinishRespon:" + FinishRespon);
                            if (FinishRespon.contains("\"code\":200")) {
                                List<Executeplanbatch> executeplanbatchList = executeplanbatchMapper.getbatchtestscene("初始", PlanID, BatchName, "立即执行");
                                HashMap<Long, List<Executeplanbatch>> tmpmap = new HashMap<>();
                                for (Executeplanbatch executeplanbatch1 : executeplanbatchList) {
                                    Long slaverid = executeplanbatch1.getSlaverid();
                                    if (!tmpmap.containsKey(slaverid)) {
                                        List<Executeplanbatch> tmpList = new ArrayList<>();
                                        tmpList.add(executeplanbatch1);
                                        tmpmap.put(slaverid, tmpList);
                                    } else {
                                        tmpmap.get(slaverid).add(executeplanbatch1);
                                    }
                                }
                                for (Long slaverid : tmpmap.keySet()) {
                                    try {
                                        Long Slaverid = slaverid;
                                        FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器..................PlanID:" + PlanID + " BatchName:" + BatchName + " slaverid:" + Slaverid);
                                        Slaver slaver = slaverMapper.findslaverbyid(Slaverid);
                                        if (slaver != null) {
                                            FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】执行机 SlaverIP:" + slaver.getIp() + " 状态：" + slaver.getStatus());
                                            //检测slaver是否运行，如果异常认为已经挂了，
                                            //CheckAliveSlaver(slaver); //有心跳在检测，这边不需要了
                                            if (slaver.getStatus().equals("空闲")) {
                                                //改为取测试场景请求到slaverservice  disdinct 场景id
                                                //SlaverDispathcList = dispatchMapper.getfunctiondispatchsbyslaverid(Slaverid, "待分配", "功能", PlanID, BatchName);
                                                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器 slaver:" + slaver.getSlavername() + " 获取dispatch数-：" + executeplanbatchList.size());
                                                String params = JSON.toJSONString(tmpmap.get(Slaverid));
                                                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============执行机id：" + slaver.getId() + "  执行机名：" + slaver.getSlavername() + " 执行的dispatch：" + params);
                                                HttpHeader header = new HttpHeader();
                                                String ServerUrl = "http://" + slaver.getIp() + ":" + slaver.getPort() + "/exectestplancase/execfunctiontest";
                                                String respon = "";
                                                try {
                                                    respon = Httphelp.doPost(ServerUrl, params, header, 30000);
                                                    if (!respon.contains("\"code\":200")) {
                                                        for (Executeplanbatch ex : tmpmap.get(Slaverid)) {
                                                            ex.setStatus("已停止");
                                                            ex.setMemo("请求slaverservice执行任务异常：" + respon);
                                                            executeplanbatchService.update(ex);
                                                            dispatchMapper.updatedispatchfail("调度失败", "请求slaverservice执行任务异常：" + respon, ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                                                        }
                                                    }
                                                } catch (Exception ex) {
                                                    //slaver不可用，补偿到其他slaver
                                                    FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============请求slaver地址: " + ServerUrl + " 响应结果异常：" + respon + " 开始补偿。。。");
                                                    CompensateAfterFail(PlanID, tmpmap.get(slaverid));
                                                }
                                                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============请求slaver响应结果：" + respon);
                                            }
                                            if (slaver.getStatus().equals("已下线")) {
                                                //已下线尝试补偿到其他的slaver
                                                CompensateAfterFail(PlanID, tmpmap.get(slaverid));
                                            }
                                            if (slaver.getStatus().equals("运行中")) {
                                                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============请求的slaver在运行中，待下一次尝试请求此slaver");
                                            }
                                        } else {
                                            //可能slaver已被删除，自动更换到可用的slaver上，如果没有可用的slaver则把execplanbatch,dispatch状态更新为失败
                                            CompensateAfterFail(PlanID, tmpmap.get(slaverid));
                                        }
                                    } catch (Exception ex) {
                                        //检测slaver，无法连接访问，自动更换到可用的slaver上，如果没有可用的slaver把execplanbatch，dispatch状态更新为调度失败
                                        CompensateAfterFail(PlanID, tmpmap.get(slaverid));
                                        FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器请求执行服务异常：" + ex.getMessage());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    executeplanbatchService.updateconditionfail(PlanID, BatchName, "条件服务请求异常:" + ex.getMessage());
                    //增加钉钉通知
                    FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器条件服务请求异常=======================" + ex.getMessage());
                } finally {
                    //TODO 执行任务结束后需要释放锁
                    //释放锁
                    redisUtils.deletekey(redisKey);
                    FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============释放分布式锁成功=======================");
                }
            } else {
                FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
                return;
            }
        } catch (Exception ex) {
            FunctionDispatchScheduleTask.log.info("调度服务【立即执行功能】测试定时器-异常: " + ex.getMessage());
        }
    }


    private void CompensateAfterFail(Long PlanID, List<Executeplanbatch> executeplanbatchList) {
        List<Slaver> allliveslaver = GetAllAliveSlaver();
        if (allliveslaver.size() == 0) {
            for (Executeplanbatch ex : executeplanbatchList) {
                ex.setStatus("已停止");
                ex.setMemo("未找到任何在线可用的slaver执行机，请联系管理员确认slaverservice是否部署成功");
                executeplanbatchService.update(ex);
                dispatchMapper.updatedispatchfail("调度失败", "未找到任何可以用的功能执行机，请联系管理员确认slaverservice是否部署成功", ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
            }
            FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，未找到任何可访问的功能执行机，已将执行计划，调度用例都更新为失败状态，请检查slaverservice是否启动。。。。。。。。。。。。。。。。。。。。");
        } else {
            FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，发现有可用的slaver，开始重新分配待下一轮尝试执行。。。。。。。。。。。");
            Executeplan ep = executeplanMapper.findexplanWithid(PlanID);
            if (ep != null) {
                //if (ep.getRunmode().equalsIgnoreCase("单机运行")) {
                FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，发现有可用的slaver，开始重新分配，。。。。。。。。。。。分配到新的slaver：" + allliveslaver.get(0).getSlavername());
                boolean idelflag = false;
                //优先找空闲的slaver
                for (Slaver slaveridel : allliveslaver) {
                    if (!idelflag) {
                        if (slaveridel.getStatus().equalsIgnoreCase("空闲")) {
                            for (Executeplanbatch ex : executeplanbatchList) {
                                ex.setSlaverid(slaveridel.getId());
                                ex.setMemo("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + slaveridel.getSlavername() + "，待下一次运行");
                                executeplanbatchService.update(ex);
                                dispatchMapper.updatedispatchnewslaver("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + slaveridel.getSlavername() + "，待下一次运行", ex.getSlaverid(), slaveridel.getId(), slaveridel.getSlavername(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                            }
                            idelflag = true;
                        }
                    }
                }
                //没有空闲的slaver，只能给一个在线的slaver
                if (!idelflag) {
                    for (Executeplanbatch ex : executeplanbatchList) {
                        ex.setSlaverid(allliveslaver.get(0).getId());
                        ex.setMemo("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + allliveslaver.get(0).getSlavername() + "，待下一次运行");
                        executeplanbatchService.update(ex);
                        dispatchMapper.updatedispatchnewslaver("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + allliveslaver.get(0).getSlavername() + "，待下一次运行", ex.getSlaverid(), allliveslaver.get(0).getId(), allliveslaver.get(0).getSlavername(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                    }
                }
                FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，重新分配完成。。。。。。。。。。。" + allliveslaver.get(0).getSlavername());
                //}
                //多机平均分配
//                else {
//                    FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，发现有可用的slaver，开始重新分配，当前集合为多机运行模式。。。。。。。。。。。分配到新的slaver：" + allliveslaver.get(0).getSlavername());
//                    List<Dispatch> SlaverDispathcList = new ArrayList<>();
//                    for (Executeplanbatch ex : executeplanbatchList) {
//                        ex.setSlaverid(allliveslaver.get(0).getId());
//                        List<Dispatch> tmp = dispatchMapper.getdispatchsbypbssid(ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
//                        SlaverDispathcList.addAll(tmp);
//                    }
//                    int slaversize = allliveslaver.size();
//                    List<List<Dispatch>> partitions = Lists.partition(SlaverDispathcList, slaversize);
//                    for (int i = 0; i < partitions.size(); i++) {
//                        for (Dispatch dis : partitions.get(i)) {
//                            dis.setSlaverid(allliveslaver.get(i).getId());
//                            dis.setSlavername(allliveslaver.get(i).getSlavername());
//                            dis.setLastmodifyTime(new Date());
//                            dis.setMemo("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + allliveslaver.get(i).getSlavername() + "，待下一次运行");
//                            dispatchMapper.updateByPrimaryKey(dis);
//                        }
//                    }
//                    FunctionDispatchScheduleTask.log.info("【立即执行功能】补偿处理，重新分配，测试集合为多机模式，完成用例平均分配到所有空闲的执行机。。。。。。。。。。。");
//                }
            }
        }
    }

    private String RequestConditionService(Dispatch dispatch, String Url) throws Exception {
        String params = JSON.toJSONString(dispatch);
        String respone = "";
        HttpHeader header = new HttpHeader();
        String ServerUrl = conditionserver + Url;// "/testcondition/execplancondition";
        FunctionDispatchScheduleTask.log.info("调度处理条件任务请求conditionserver。。。。。。。: " + ServerUrl);
        try {
            FunctionDispatchScheduleTask.log.info("调度处理条件任务请求数据。。。。。。。: " + ServerUrl + params);
            respone = Httphelp.doPost(ServerUrl, params, header, 30000);
            FunctionDispatchScheduleTask.log.info("调度处理条件任务请求条件服务响应: " + ServerUrl + respone);
        } catch (Exception ex) {
            FunctionDispatchScheduleTask.log.info("-------------调度处理条件任务请求异常: " + ServerUrl + ex.getMessage());
            respone = ex.getMessage();
            throw new Exception("请求条件服务异常：" + respone);
        }
        return respone;
    }

    private void RequestConditionServiceByPlanId(Dispatch dispatch, String Url) throws Exception {
        String params = JSON.toJSONString(dispatch);
        HttpHeader header = new HttpHeader();
        String ServerUrl = conditionserver + Url;// "/testcondition/execplancondition";
        FunctionDispatchScheduleTask.log.info("调度处理条件任务请求conditionserver。。。。。。。: " + ServerUrl);
        try {
            FunctionDispatchScheduleTask.log.info("调度处理条件任务请求数据。。。。。。。: " + params);
            String respone = Httphelp.doPost(ServerUrl, params, header, 30000);
            FunctionDispatchScheduleTask.log.info("调度处理条件任务请求条件服务响应: " + respone);
        } catch (Exception ex) {
            FunctionDispatchScheduleTask.log.info("-------------调度处理条件任务请求异常: " + ex.getMessage());
            dispatch.setMemo("无法连接conditionservice：" + ex.getMessage());
            dispatchService.update(dispatch);
        }
    }

    public void CheckAliveSlaver(Slaver slaver) throws Exception {
        String IP = slaver.getIp();
        String Port = slaver.getPort();
        String ServerUrl = "http://" + IP + ":" + Port + "/exectestplancase/test";
        ExecuteplanTestcase plancase = new ExecuteplanTestcase();
        String params = JSON.toJSONString(plancase);
        HttpHeader header = new HttpHeader();
        String respon = "";
        try {
            respon = Httphelp.doPost(ServerUrl, params, header, 3000);
            FunctionDispatchScheduleTask.log.info("检测CheckAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
        } catch (Exception e) {
            slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
            FunctionDispatchScheduleTask.log.info("检测CheckAliveSlaver：" + ServerUrl + "请求响应结果异常。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage() + "更新slaver" + slaver.getSlavername() + "为已下线");
            throw new Exception("Salver无法连接：" + slaver.getSlavername());
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
                FunctionDispatchScheduleTask.log.info("检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
            } catch (Exception e) {
                FunctionDispatchScheduleTask.log.info("检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
            }
        }
        return slaverlistresult;
    }

    @PostConstruct
    public void Init() {
        redisKey = "Dispatchservice-FunctionDispatchScheduleTask-RedisLock" + new Date();
        FunctionDispatchScheduleTask.log.info("调度服务【功能】测试定时器-redisKey is:" + redisKey);
    }
}
