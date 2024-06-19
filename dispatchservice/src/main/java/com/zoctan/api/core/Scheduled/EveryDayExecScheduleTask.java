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
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Component
public class EveryDayExecScheduleTask {
    @Value("${spring.conditionserver.serverurl}")
    private String conditionserver;
    @Autowired(required = false)
    private ExecuteplanService executeplanService;
    @Autowired(required = false)
    private ExecuteplanbatchService executeplanbatchService;
    @Autowired(required = false)
    private ExecuteplanbatchMapper executeplanbatchMapper;
    @Autowired(required = false)
    private TestconditionReportMapper testconditionReportMapper;
    @Autowired(required = false)
    private ExecuteplanMapper executeplanMapper;
    @Autowired(required = false)
    RedisUtils redisUtils;
    @Autowired(required = false)
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    private ConditionApiService conditionApiService;
    @Autowired(required = false)
    private DispatchMapper dispatchMapper;
    @Autowired(required = false)
    private DispatchService dispatchService;
    private String redisKey = "";


    @Scheduled(cron = "0/60 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            boolean lock = redisUtils.tryLock(redisKey, "EveryDayExecScheduleTask", redis_default_expire_time);
            if (lock) {
                try {
                    Calendar cal = Calendar.getInstance();
                    int Month = cal.get(Calendar.MONTH) + 1;
                    int DATE = cal.get(Calendar.DATE);
                    int Hour = cal.get(Calendar.HOUR_OF_DAY);
                    int Minitues = cal.get(Calendar.MINUTE);
                    String MonthData = FinishZERO(Month);
                    String DateData = FinishZERO(DATE);
                    String HourData = FinishZERO(Hour);
                    String MinitesData = FinishZERO(Minitues);
                    String CurrentTime = HourData + ":" + MinitesData + ":00";
//                    String CurrentTime = cal.get(Calendar.YEAR) + "-" + MonthData + "-" + DateData + " " + HourData + ":" + MinitesData + ":00";
                    EveryDayExecScheduleTask.log.info("【每天定时执行任务开始】-============当前CurrentTime=======================" + CurrentTime);
                    List<Executeplanbatch> executeplanbatchallList = executeplanbatchMapper.getrecentallbatch("初始", "每天定时","功能");
                    for (Executeplanbatch executeplanbatchtmp : executeplanbatchallList) {
                        Executeplanbatch executeplanbatch = executeplanbatchMapper.getrecentsinglebatch("初始", "每天定时", executeplanbatchtmp.getExecuteplanid(), executeplanbatchtmp.getBatchname());
                        if (executeplanbatch != null) {
                            String ExecDate = executeplanbatch.getExecdate();
                            EveryDayExecScheduleTask.log.info("【每天定时执行任务】-============计划时间ExecDate=======================" + ExecDate);
                            if (CurrentTime.equals(ExecDate)) {
                                Long PlanID = executeplanbatch.getExecuteplanid();
                                String BatchName = executeplanbatch.getBatchname();
                                //判断计划的所有前置条件是否已经完成，并且全部成功，否则更新Dispatch状态为前置条件失败
                                Dispatch dispatch = new Dispatch();
                                dispatch.setBatchname(BatchName);
                                dispatch.setExecplanid(PlanID);
                                dispatch.setExecplanname(executeplanbatch.getExecuteplanname());
                                dispatch.setSlaverid(executeplanbatch.getSlaverid());
                                String FinishRespon = "";
                                try {
                                    EveryDayExecScheduleTask.log.info("调度服务【每天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName);
                                    RequestConditionService(dispatch, "/testcondition/execplancondition");
                                    boolean conditionfinishflag = true;
                                    //每天进来只有一次机会，所以用while
                                    int trynums = 0;
                                    while (conditionfinishflag) {
                                        FinishRespon = RequestConditionService(dispatch, "/testcondition/planconditionfinish");
                                        Thread.sleep(5000);
                                        trynums++;
                                        EveryDayExecScheduleTask.log.info("调度服务【每天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件服务FinishRespon:" + FinishRespon);
                                        if (FinishRespon.contains("\"code\":200")) {
                                            conditionfinishflag = false;
                                        } else {
                                            //重试60次5分钟后处理为失败，人工介入查找条件执行失败原因
                                            if (trynums == 60) {
                                                executeplanbatch.setStatus("已停止");
                                                executeplanbatch.setMemo("conditionservice-条件服务尝试等待5分钟未正常完成，主动停止，请联系管理员查看日志原因");
                                                executeplanbatchService.update(executeplanbatch);
                                                conditionfinishflag = false;
                                            }
                                        }
                                    }
                                }
                                //如果conditionservice挂了，或者异常,将execplanbatch刷成失败，备注原因
                                catch (Exception ex) {
                                    executeplanbatch.setStatus("已停止");
                                    executeplanbatch.setMemo("conditionservice-条件服务不可访问，主动停止，请联系管理员");
                                    executeplanbatchService.update(executeplanbatch);
                                }
                                EveryDayExecScheduleTask.log.info("调度服务【每天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " 条件处理完成。。。。。。。。。。。。。。");
                                if (FinishRespon.contains("\"code\":200")) {
                                    List<Executeplanbatch> executeplanbatchList = executeplanbatchMapper.getbatchtestscene("初始", PlanID, BatchName, "每天定时");
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
                                    Long Slaverid = new Long(0);
                                    for (Long slaverid : tmpmap.keySet()) {
                                        try {
                                            Slaverid = slaverid;
                                            EveryDayExecScheduleTask.log.info("【每天定时执行任务】..................PlanID:" + PlanID + " BatchName:" + BatchName + " slaverid:" + Slaverid);
                                            Slaver slaver = slaverMapper.findslaverbyid(Slaverid);
                                            if (slaver != null) {
                                                EveryDayExecScheduleTask.log.info("【每天定时执行任务】】执行机 SlaverIP:" + slaver.getIp() + " 状态：" + slaver.getStatus());
                                                if (slaver.getStatus().equals("空闲")) {
                                                    //改为取测试场景请求到slaverservice  disdinct 场景id
                                                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器 slaver:" + slaver.getSlavername() + " 获取dispatch数-：" + executeplanbatchList.size());
                                                    String params = JSON.toJSONString(tmpmap.get(Slaverid));
                                                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============执行机id：" + slaver.getId() + "  执行机名：" + slaver.getSlavername() + " 执行的dispatch：" + params);
                                                    HttpHeader header = new HttpHeader();
                                                    String ServerUrl = "http://" + slaver.getIp() + ":" + slaver.getPort() + "/exectestplancase/execfunctiontest";
                                                    String respon = Httphelp.doPost(ServerUrl, params, header, 30000);
                                                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============请求slaver响应结果：" + respon);
                                                    if (respon.contains("\"code\":200")) {
                                                        generaltomorow(tmpmap.get(Slaverid), ExecDate, MonthData, DateData);
                                                    } else {
                                                        for (Executeplanbatch ex : tmpmap.get(Slaverid)) {
                                                            ex.setStatus("已停止");
                                                            ex.setMemo("请求slaverservice执行任务异常：" + respon);
                                                            executeplanbatchService.update(ex);
                                                            dispatchMapper.updatedispatchfail("调度失败", "请求slaverservice执行任务异常：" + respon, ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                                                        }
                                                    }
                                                } else {
                                                    //slaver已下线或者进行中尝试补偿到其他的slaver并且执行
                                                    retry(PlanID, tmpmap.get(slaverid), ExecDate, MonthData, DateData);
                                                }
                                            } else {
                                                //slaver被删除尝试补偿到其他的slaver并且执行
                                                retry(PlanID, tmpmap.get(slaverid), ExecDate, MonthData, DateData);
                                            }
                                        } catch (Exception ex) {
                                            //请求slaverservice异常，尝试补偿到其他的slaver并且执行
                                            retry(PlanID, tmpmap.get(slaverid), ExecDate, MonthData, DateData);
                                            EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器请求执行服务异常：" + ex.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }


                } catch (Exception ex) {
                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】异常=======================" + ex.getMessage());
                } finally {
                    //释放锁
                    redisUtils.deletekey(redisKey);
                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】-============释放分布式锁成功=======================");
                }
            } else {
                EveryDayExecScheduleTask.log.info("【每天定时执行任务】-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
                return;
            }
        } catch (Exception ex) {
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】-异常: " + ex.getMessage());
        }
    }

    @PostConstruct
    public void Init() {
        redisKey = "EveryDayExec-ScheduleTask-RedisLock" + new Date();
        EveryDayExecScheduleTask.log.info("【每天定时执行任务】-redisKey is:" + redisKey);
    }


    private void generaltomorow(List<Executeplanbatch> executeplanbatchList, String Execdata, String MonthData, String DataData) {
        for (Executeplanbatch exp : executeplanbatchList) {
            //复制生成明天的exeplanbatch
            long oldbatchid = exp.getId();
            Executeplanbatch executeplanbatch1 = new Executeplanbatch();
            executeplanbatch1 = exp;
            executeplanbatch1.setId(null);
            Calendar nextcalendar = Calendar.getInstance();
            nextcalendar.add(Calendar.DAY_OF_MONTH, 1);
            int nextdat = nextcalendar.get(Calendar.DATE);
            String NextDateData = FinishZERO(nextdat);
            String NextCurrentTime = Execdata;
//            String NextCurrentTime = nextcalendar.get(Calendar.YEAR) + "-" + MonthData + "-" + NextDateData + " " + HourData + ":" + MinitesData + ":00";
            executeplanbatch1.setExecdate(NextCurrentTime);
            String batchname = exp.getBatchname();
            if (batchname.contains("#")) {
                batchname = batchname.substring(0, batchname.indexOf("#") - 1);
            }
            executeplanbatch1.setBatchname(batchname + "-#-" + nextcalendar.get(Calendar.YEAR) + "-" + MonthData + "-" + NextDateData);
            executeplanbatch1.setStatus("初始");
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalTime currentTime = LocalTime.now();
            LocalDateTime tomorrowNow = LocalDateTime.of(tomorrow, currentTime);
            Date date = Date.from(tomorrowNow.atZone(ZoneId.systemDefault()).toInstant());
            executeplanbatch1.setCreateTime(date);
            executeplanbatch1.setLastmodifyTime(date);
            executeplanbatchService.save(executeplanbatch1);
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============完成复制明天待执行的executeplanbatch时间为：" + executeplanbatch1.getExecdate());

            Long execplanid = exp.getExecuteplanid();
            Long sceneid = exp.getSceneid();
            Long slaverid = exp.getSlaverid();
            List<Dispatch> dispatchList = dispatchMapper.getcopydispatch(slaverid, execplanid, oldbatchid, sceneid);
            List<Dispatch> last = new ArrayList<>();
            for (Dispatch dis : dispatchList) {
                dis.setId(null);
                dis.setBatchid(executeplanbatch1.getId());
                dis.setStatus("待分配");
                dis.setBatchname(batchname + "-#-" + nextcalendar.get(Calendar.YEAR) + "-" + MonthData + "-" + NextDateData);
                dis.setCreateTime(date);
                dis.setLastmodifyTime(date);
                last.add(dis);
            }
            dispatchMapper.insertBatchDispatch(last);
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============完成复制明天待执行的dispatch时间为：" + last.size());

        }
    }

    private void retry(long PlanID, List<Executeplanbatch> executeplanbatchList, String Execdate, String MonthData, String Datadate) throws InterruptedException {
        int trynums = 0;
        boolean flag = CompensateAfterFail(PlanID, executeplanbatchList, Execdate, MonthData, Datadate);
        while (!flag) {
            trynums++;
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器没有可以补偿的slaver-============开始retry CompensateAfterFail 次数：" + trynums);
            flag = CompensateAfterFail(PlanID, executeplanbatchList, Execdate, MonthData, Datadate);
            for (Executeplanbatch ex : executeplanbatchList) {
                ex.setMemo("系统未找到任何空闲的slaver执行机，第" + trynums + "次尝试等待30秒再次寻找其他空闲的slaver执行机");
                executeplanbatchService.update(ex);
            }
            Thread.sleep(30000);
            if (trynums == 100) {
                flag = true;
                for (Executeplanbatch ex : executeplanbatchList) {
                    ex.setMemo("系统尝试100次等待，耗时半小时后未找到空闲的slaver执行机，取消当前执行计划");
                    ex.setStatus("已取消");
                    executeplanbatchService.update(ex);
                }
            }
        }
    }

    private String RequestConditionService(Dispatch dispatch, String Url) throws Exception {
        String params = JSON.toJSONString(dispatch);
        String respone = "";
        HttpHeader header = new HttpHeader();
        String ServerUrl = conditionserver + Url;// "/testcondition/execplancondition";
        EveryDayExecScheduleTask.log.info("【每天定时执行任务】调度处理条件任务请求conditionserver。。。。。。。: " + ServerUrl);
        try {
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】调度处理条件任务请求数据。。。。。。。: " + ServerUrl + params);
            respone = Httphelp.doPost(ServerUrl, params, header, 30000);
            EveryDayExecScheduleTask.log.info("【每天定时执行任务】调度处理条件任务请求条件服务响应: " + ServerUrl + respone);
        } catch (Exception ex) {
            EveryDayExecScheduleTask.log.info("-【每天定时执行任务】------------调度处理条件任务请求异常: " + ServerUrl + ex.getMessage());
            respone = ex.getMessage();
            throw new Exception("【每天定时执行任务】请求条件服务异常：" + respone);
        }
        return respone;
    }

    private boolean CompensateAfterFail(Long PlanID, List<Executeplanbatch> executeplanbatchList, String ExecDate, String MonthData, String DateData) {
        boolean fixflag = true;
        List<Slaver> allliveslaver = GetAllAliveSlaver();
        if (allliveslaver.size() == 0) {
            for (Executeplanbatch ex : executeplanbatchList) {
                ex.setStatus("已停止");
                ex.setMemo("未找到任何在线可用的slaver执行机，请联系管理员确认slaverservice是否部署成功");
                executeplanbatchService.update(ex);
                dispatchMapper.updatedispatchfail("调度失败", "未找到任何可以用的功能执行机，请联系管理员确认slaverservice是否部署成功", ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
            }
            EveryDayExecScheduleTask.log.info("【立即执行功能】补偿处理，未找到任何可访问的功能执行机，已将执行计划，调度用例都更新为失败状态，请检查slaverservice是否启动。。。。。。。。。。。。。。。。。。。。");
        } else {
            for (Slaver slaveridel : allliveslaver) {
                if (slaveridel.getStatus().equalsIgnoreCase("空闲")) {
                    EveryDayExecScheduleTask.log.info("【每天定时执行任务】补偿处理，发现有可用的slaver，开始重新分配待下一轮尝试执行。。。。。。。。。。。");
                    Executeplan ep = executeplanMapper.findexplanWithid(PlanID);
                    if (ep != null) {
                        EveryDayExecScheduleTask.log.info("【每天定时执行任务】补偿处理，发现有可用的slaver，开始重新分配，。。。。。。。。。。。分配到新的slaver：" + allliveslaver.get(0).getSlavername());
                        for (Executeplanbatch ex : executeplanbatchList) {
                            ex.setSlaverid(slaveridel.getId());
                            ex.setMemo("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + slaveridel.getSlavername() + "，运行");
                            executeplanbatchService.update(ex);
                            dispatchMapper.updatedispatchnewslaver("初次分配的slaver不可用，补偿处理，重新分配到可用的slaver：" + slaveridel.getSlavername() + "，运行", ex.getSlaverid(), slaveridel.getId(), slaveridel.getSlavername(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                        }
                        EveryDayExecScheduleTask.log.info("【每天定时执行任务】补偿处理，重新分配完成。。。。。。。。。。。" + slaveridel.getSlavername() + " 开始重新请求slaverservice");
                        //重新请求slaver
                        String params = JSON.toJSONString(executeplanbatchList);
                        EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============执行机id：" + slaveridel.getId() + "  执行机名：" + slaveridel.getSlavername() + " 执行的dispatch：" + params);
                        HttpHeader header = new HttpHeader();
                        String ServerUrl = "http://" + slaveridel.getIp() + ":" + slaveridel.getPort() + "/exectestplancase/execfunctiontest";
                        try {
                            String respon = Httphelp.doPost(ServerUrl, params, header, 30000);
                            if (respon.contains("\"code\":200")) {
                                generaltomorow(executeplanbatchList, ExecDate, MonthData, DateData);
                            } else {
                                for (Executeplanbatch ex : executeplanbatchList) {
                                    ex.setStatus("已停止");
                                    ex.setMemo("重新分配后，请求slaverservice执行任务异常：" + respon);
                                    executeplanbatchService.update(ex);
                                    dispatchMapper.updatedispatchfail("调度失败", "请求slaverservice执行任务异常：" + respon, ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                                }
                            }
                            EveryDayExecScheduleTask.log.info("【每天定时执行任务】测试定时器-============请求slaver响应结果：" + respon);
                        } catch (Exception exception) {
                            for (Executeplanbatch ex : executeplanbatchList) {
                                ex.setStatus("已停止");
                                ex.setMemo("重新分配后，请求slaverservice执行任务异常：" + exception.getMessage());
                                executeplanbatchService.update(ex);
                                dispatchMapper.updatedispatchfail("调度失败", "请求slaverservice执行任务异常：" + exception.getMessage(), ex.getSlaverid(), ex.getExecuteplanid(), ex.getId(), ex.getSceneid());
                            }
                        }
                    }
                    fixflag = true;
                    break;
                } else {
                    fixflag = false;
                }
            }
        }
        return fixflag;
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
                EveryDayExecScheduleTask.log.info("检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
            } catch (Exception e) {
                EveryDayExecScheduleTask.log.info("检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
            }
        }
        return slaverlistresult;
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

}
