package com.zoctan.api.core.Scheduled;


import com.alibaba.fastjson.JSON;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.core.service.HttpHeader;
import com.zoctan.api.core.service.Httphelp;
import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.ConditionApiService;
import com.zoctan.api.service.DispatchService;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.service.ExecuteplanbatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;


@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Component
public class SomeDayExecScheduleTask {
    @Value("${spring.conditionserver.serverurl}")
    private String conditionserver;
    @Autowired(required = false)
    private ExecuteplanService executeplanService;
    @Autowired(required = false)
    private ExecuteplanbatchMapper executeplanbatchMapper;
    @Autowired(required = false)
    private PlanbantchexeclogMapper planbantchexeclogMapper;
    @Autowired(required = false)
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    private ExecuteplanbatchService executeplanbatchService;
    @Autowired(required = false)
    private ConditionApiService conditionApiService;
    @Autowired(required = false)
    private TestconditionReportMapper testconditionReportMapper;
    @Autowired(required = false)
    private DispatchMapper dispatchMapper;
    @Autowired(required = false)
    private DispatchService dispatchService;
    @Autowired(required = false)
    RedisUtils redisUtils;
    private String redisKey = "";


    @Scheduled(cron = "0/60 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            boolean lock = redisUtils.tryLock(redisKey, "SomeDayExecScheduleTask", redis_default_expire_time);
            Executeplanbatch executeplanbatch = executeplanbatchMapper.getrecentbatch("初始", "某天定时");
            if (lock) {
                long PlanID = 0;
                String BatchName = "";
                try {
                    if (executeplanbatch != null) {
                        PlanID = executeplanbatch.getExecuteplanid();
                        BatchName = executeplanbatch.getBatchname();
                        Calendar cal = Calendar.getInstance();
                        int Month = cal.get(Calendar.MONTH) + 1;
                        int DATE = cal.get(Calendar.DATE);
                        int Hour = cal.get(Calendar.HOUR_OF_DAY);
                        int Minitues = cal.get(Calendar.MINUTE);
                        String MonthData = FinishZERO(Month);
                        String DateData = FinishZERO(DATE);
                        String HourData = FinishZERO(Hour);
                        String MinitesData = FinishZERO(Minitues);
                        String CurrentTime = cal.get(Calendar.YEAR) + "-" + MonthData + "-" + DateData + " " + HourData + ":" + MinitesData + ":00";
                        SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============CurrentTime=======================" + CurrentTime);
                        FunctionDispatchScheduleTask functionDispatchScheduleTask = new FunctionDispatchScheduleTask();
                        String ExecDate = executeplanbatch.getExecdate();
                        SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============ExecDate=======================" + ExecDate);
                        if (CurrentTime.equals(ExecDate)) {
                            PlanID = executeplanbatch.getExecuteplanid();
                            BatchName = executeplanbatch.getBatchname();
                            //判断计划的所有前置条件是否已经完成，并且全部成功，否则更新Dispatch状态为前置条件失败
                            Dispatch dispatch = new Dispatch();
                            dispatch.setBatchname(BatchName);
                            dispatch.setExecplanid(PlanID);
                            dispatch.setExecplanname(executeplanbatch.getExecuteplanname());
                            dispatch.setSlaverid(executeplanbatch.getSlaverid());
                            RequestConditionService(dispatch, "/testcondition/execplancondition");
                            SomeDayExecScheduleTask.log.info("调度服务【某天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName );
                            String FinishRespon = "";
                            boolean conditionfinishflag = true;
                            while (conditionfinishflag) {
                                FinishRespon = RequestConditionService(dispatch, "/testcondition/planconditionfinish");
                                Thread.sleep(5000);
                                SomeDayExecScheduleTask.log.info("调度服务【某天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件服务FinishRespon:" + FinishRespon);
                                if (FinishRespon.contains("\"code\":200")) {
                                    conditionfinishflag = false;
                                }
                            }
                            SomeDayExecScheduleTask.log.info("调度服务【某天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件处理完成。。。。。。。。。。。。。。");
                            if (FinishRespon.contains("\"code\":200")) {
                                List<Executeplanbatch> executeplanbatchList = executeplanbatchMapper.getbatchtestscene("初始", PlanID, BatchName, "某天定时");
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
                                        SomeDayExecScheduleTask.log.info("【某天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " slaverid:" + slaverid);
                                        Slaver slaver = slaverMapper.findslaverbyid(slaverid);
                                        if (slaver != null) {
                                            SomeDayExecScheduleTask.log.info("【某天定时执行任务】】执行机 SlaverIP:" + slaver.getIp() + " 状态：" + slaver.getStatus());
                                            //检测slaver是否运行，如果异常认为已经挂了，
                                            functionDispatchScheduleTask.CheckAliveSlaver(slaver);
                                            if (slaver.getStatus().equals("空闲")) {
                                                //改为取测试场景请求到slaverservice  disdinct 场景id
                                                //SlaverDispathcList = dispatchMapper.getfunctiondispatchsbyslaverid(Slaverid, "待分配", "功能", PlanID, BatchName);
                                                SomeDayExecScheduleTask.log.info("【某天定时执行任务】 slaver:" + slaver.getSlavername() + " 获取dispatch数-：" + executeplanbatchList.size());
                                                String params = JSON.toJSONString(tmpmap.get(slaverid));
                                                SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============执行机id：" + slaver.getId() + "  执行机名：" + slaver.getSlavername() + " 执行的dispatch：" + params);
                                                HttpHeader header = new HttpHeader();
                                                String ServerUrl = "http://" + slaver.getIp() + ":" + slaver.getPort() + "/exectestplancase/execfunctiontest";
                                                String respon = Httphelp.doPost(ServerUrl, params, header, 30000);
                                                SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============请求slaver响应结果：" + respon);
                                                if (respon.contains("\"code\":200")) {
                                                    //更新batch状态为待执行
                                                    for (Executeplanbatch exp : tmpmap.get(slaverid)) {
                                                        exp.setStatus("待执行");
                                                        executeplanbatchService.update(exp);
                                                    }
                                                } else {
                                                    for (Executeplanbatch exp : tmpmap.get(slaverid)) {
                                                        exp.setMemo("执行机" + slaver.getSlavername() + "异常:" + respon);
                                                        executeplanbatchService.update(exp);
                                                    }
                                                }
                                            } else {
                                                //自动更换到可用的slaver上，如果没有可用的slaver再把dispatch状态更新为调度失败
                                                //CompensateAfterFail(dispatch, PlanID, SlaverDispathcList);
                                            }
                                        } else {
                                            //自动更换到可用的slaver上，如果没有可用的slaver再把dispatch状态更新为调度失败
                                            //CompensateAfterFail(dispatch, PlanID, SlaverDispathcList);
                                        }
                                    } catch (Exception ex) {
                                        //自动更换到可用的slaver上，如果没有可用的slaver再把dispatch状态更新为调度失败
                                        //CompensateAfterFail(dispatch, PlanID, SlaverDispathcList);
                                        SomeDayExecScheduleTask.log.info("【某天定时执行任务】请求执行服务异常：" + ex.getMessage());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    executeplanbatchService.updateconditionfail(PlanID, BatchName, "条件服务请求异常:" + ex.getMessage());
                    SomeDayExecScheduleTask.log.info("【某天定时执行任务异常: " + ex.getMessage());
                } finally {
                    //释放锁
                    redisUtils.deletekey(redisKey);
                    SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============释放分布式锁成功=======================");
                }
            } else {
                SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
                return;
            }
        } catch (Exception ex) {
            SomeDayExecScheduleTask.log.info("【某天定时执行任务】-异常: " + ex.getMessage());
        }
    }

    @PostConstruct
    public void Init() {
        redisKey = "SomeDayExec-ScheduleTask-RedisLock" + new Date();
        SomeDayExecScheduleTask.log.info("【某天定时执行任务】-redisKey is:" + redisKey);
    }


    private String RequestConditionService(Dispatch dispatch, String Url) throws Exception {
        String params = JSON.toJSONString(dispatch);
        String respone = "";
        HttpHeader header = new HttpHeader();
        String ServerUrl = conditionserver + Url;// "/testcondition/execplancondition";
        SomeDayExecScheduleTask.log.info("【某天定时执行任务】调度处理条件任务请求conditionserver。。。。。。。: " + ServerUrl);
        try {
            SomeDayExecScheduleTask.log.info("【某天定时执行任务】调度处理条件任务请求数据。。。。。。。: " + ServerUrl + params);
            respone = Httphelp.doPost(ServerUrl, params, header, 30000);
            SomeDayExecScheduleTask.log.info("【某天定时执行任务】调度处理条件任务请求条件服务响应: " + ServerUrl + respone);
        } catch (Exception ex) {
            SomeDayExecScheduleTask.log.info("【某天定时执行任务】-------------调度处理条件任务请求异常: " + ServerUrl + ex.getMessage());
            respone = ex.getMessage();
            throw new Exception("【某天定时执行任务】请求条件服务异常：" + respone);
        }
        return respone;
    }

    public boolean ConditionRequest(Dispatch dispatch) throws Exception {
        boolean flag = true;
        Long PlanID = dispatch.getExecplanid();
        String BatchName = dispatch.getBatchname();
//        List<Testcondition> testconditionList = testconditionService.GetConditionByPlanIDAndConditionType(PlanID, "前置条件", "测试集合");
//        if (testconditionList.size() > 0) {
        // Long ConditionID = testconditionList.get(0).getId();
        List<ConditionApi> conditionApiList = conditionApiService.GetCaseListByConditionID(dispatch.getExecplanid(), "execplan");
        int ApiConditionNums = conditionApiList.size();
//            List<ConditionDb> conditionDbList = conditionDbService.GetCaseListByConditionID(ConditionID);
//            int DBConditionNUms = conditionDbList.size();
//            List<ConditionScript> conditionScriptList = conditionScriptService.getconditionscriptbyid(ConditionID);
//            int ScriptConditionNUms = conditionScriptList.size();
//            List<ConditionDelay> conditionDelayList = conditionDelayService.GetCaseListByConditionID(ConditionID);
//            int DelayConditionNUms = conditionDelayList.size();
        int SubConditionNums = ApiConditionNums;

        //int SubConditionNums = ApiConditionNums + DBConditionNUms + ScriptConditionNUms + DelayConditionNUms;
        //表示有子条件需要处理
        if (SubConditionNums > 0) {
            //获取此计划批次条件报告的结果
            List<TestconditionReport> testconditionReportList = testconditionReportMapper.getunfinishapiconditionnums(PlanID, BatchName);
            //还未产生报告，需要请求条件服务
            if (testconditionReportList.size() == 0) {
                //todo发请求条件服务,异步请求
                RequestConditionServiceByPlanId(dispatch);
                flag = false;
            } else //已经产生条件报告，需要查看报告结果是成功还是失败
            {
                for (TestconditionReport testconditionReport : testconditionReportList) {
                    if (testconditionReport.getConditionstatus().equals(new String("失败"))) {
                        //有子条件已经执行失败，则此计划批次不再执行，更新当前计划批次的所有调度状态为条件失败，更新计划批次状态为条件失败
                        //todo
                        dispatchMapper.updatedispatchstatusbyplanandbatch("条件失败", PlanID, BatchName);
                        SomeDayExecScheduleTask.log.info("调度服务【功能】条件处理更新当前计划批次的所有调度状态为条件失败,计划： " + dispatch.getExecplanname() + "批次：" + BatchName);
                        executeplanbatchMapper.updatestatusbyplanandbatch("条件失败", PlanID, BatchName);
                        SomeDayExecScheduleTask.log.info("调度服务【功能】条件处理更新当前计划批次的状态为条件失败,计划： " + dispatch.getExecplanname() + "批次：" + BatchName);
                        flag = false;
                        break;
                    }
                }
                List<TestconditionReport> successtestconditionReportList = testconditionReportMapper.getsubconditionnumswithstatus(PlanID, BatchName, "已完成", "成功");
                if (successtestconditionReportList.size() == SubConditionNums) {
                    //条件报告中已完成，成功的条数等于子条件总条数表示子条件都已成功完成，可以开始执行用例
                    SomeDayExecScheduleTask.log.info("调度服务【功能】条件报告已完成成功的数量: " + successtestconditionReportList.size() + "  子条件总条数：" + SubConditionNums);
                    flag = true;
                }
            }
        }
        //}
        return flag;
    }

    private void RequestConditionServiceByPlanId(Dispatch dispatch) throws Exception {
        String params = JSON.toJSONString(dispatch);
        HttpHeader header = new HttpHeader();
        String ServerUrl = conditionserver + "/testcondition/execplancondition";
        SomeDayExecScheduleTask.log.info("调度处理条件任务请求conditionserver。。。。。。。: " + ServerUrl);
        try {
            SomeDayExecScheduleTask.log.info("调度处理条件任务请求数据。。。。。。。: " + params);
            String respone = Httphelp.doPost(ServerUrl, params, header, 30000);
            SomeDayExecScheduleTask.log.info("调度处理条件任务请求条件服务响应: " + respone);
        } catch (Exception ex) {
            SomeDayExecScheduleTask.log.info("-------------调度处理条件任务请求异常: " + ex.getMessage());
            dispatch.setMemo("无法连接conditionservice：" + ex.getMessage());
            dispatchService.update(dispatch);
        }
    }


    private String FinishZERO(int Nums) {
        String MonthDate = "";
        if (Nums < 10) {
            MonthDate = "0" + Nums;
        } else {
            MonthDate = String.valueOf(Nums);
        }
        return MonthDate;
    }


//    private void ExecPlanCase(Testplanandbatch testplanandbatch) {
//        executeplanService.execcase(testplanandbatch);
//    }

}
