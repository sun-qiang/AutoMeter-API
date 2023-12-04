package com.zoctan.api.core.Scheduled;


import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.ExecuteplanbatchMapper;
import com.zoctan.api.mapper.PlanbantchexeclogMapper;
import com.zoctan.api.service.ExecuteplanService;

import com.zoctan.api.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Component
public class SomeDayExecScheduleTask {
//    @Autowired(required = false)
//    private ExecuteplanService executeplanService;
//    @Autowired(required = false)
//    private ExecuteplanbatchMapper executeplanbatchMapper;
//    @Autowired(required = false)
//    private PlanbantchexeclogMapper planbantchexeclogMapper;
//    @Autowired(required = false)
//    RedisUtils redisUtils;
//    private String redisKey = "";
//
//
//
//    @Scheduled(cron = "0/60 * * * * ?")
//    //或直接指定时间间隔，例如：5秒
//    private void configureTasks() {
//        try {
//            long redis_default_expire_time = 2000;
//            boolean lock = redisUtils.tryLock(redisKey, "SomeDayExecScheduleTask", redis_default_expire_time);
//            if (lock) {
//                try {
//                    Calendar cal = Calendar.getInstance();
//                    int Month = cal.get(Calendar.MONTH) + 1;
//                    int DATE = cal.get(Calendar.DATE);
//                    int Hour = cal.get(Calendar.HOUR_OF_DAY);
//                    int Minitues = cal.get(Calendar.MINUTE) ;
//                    String MonthData=FinishZERO(Month);
//                    String DateData=FinishZERO(DATE);
//                    String HourData=FinishZERO(Hour);
//                    String MinitesData=FinishZERO(Minitues);
//                    String CurrentTime = cal.get(Calendar.YEAR) + "-" +MonthData+ "-" + DateData + " " + HourData+ ":" + MinitesData + ":00";
//                    SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============CurrentTime=======================" + CurrentTime);
//                    List<Executeplanbatch> executeplanbatchList = executeplanbatchMapper.getbatchbyexectype("某天定时");
//                    for (Executeplanbatch ex : executeplanbatchList) {
//                        String ExecDate = ex.getExecdate();
//                        SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============ExecDate=======================" + ExecDate);
//                        if (CurrentTime.equals(ExecDate)) {
//                            SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============ExecDate=CurrentTime======================" + ExecDate);
//                            List<Planbantchexeclog> planbantchexeclogList = planbantchexeclogMapper.GetPlanExecLog(ex.getId(), ExecDate);
//                            if (planbantchexeclogList.size() == 0)//日志表不存在,表示还没执行
//                            {
//                                List<Testplanandbatch> testplanandbatchList = new ArrayList<>();
//                                Testplanandbatch testplanandbatch = new Testplanandbatch();
//                                testplanandbatch.setBatchname(ex.getBatchname());
//                                testplanandbatch.setPlanid(ex.getExecuteplanid());
//                                //testplanandbatchList.add(testplanandbatch);
//                                String memo = "";
//                                try {
//                                    SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============开始执行某天的用例======================"+ CurrentTime);
//                                    ExecPlanCase(testplanandbatch);
//                                    SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============完成执行某天的用例======================"+ CurrentTime);
//                                } catch (Exception exp) {
//                                    memo = exp.getMessage();
//                                }
//                                planbantchexeclogMapper.SaveExecLog(ex.getId(), ExecDate, memo);
//                                SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============完成执行用例保存log记录表======================"+ CurrentTime);
//                            }
//                        }
//                    }
//                }
//                catch (Exception ex)
//                {
//                    SomeDayExecScheduleTask.log.info("【某天定时执行任务异常: " + ex.getMessage());
//                }
//                finally {
//                    //释放锁
//                    redisUtils.deletekey(redisKey);
//                    SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============释放分布式锁成功=======================");
//                }
//            } else {
//                SomeDayExecScheduleTask.log.info("【某天定时执行任务】-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
//                return;
//            }
//        } catch (Exception ex) {
//            SomeDayExecScheduleTask.log.info("【某天定时执行任务】-异常: " + ex.getMessage());
//        }
//    }
//
//    @PostConstruct
//    public void Init() {
//        redisKey = "SomeDayExec-ScheduleTask-RedisLock" + new Date();
//        SomeDayExecScheduleTask.log.info("【某天定时执行任务】-redisKey is:" + redisKey);
//    }
//
//    private String FinishZERO(int Nums)
//    {
//        String MonthDate="";
//        if(Nums<10)
//        {
//            MonthDate="0"+Nums;
//        }
//        else
//        {
//            MonthDate=String.valueOf(Nums);
//        }
//        return MonthDate;
//    }
//
//
//    private void ExecPlanCase(Testplanandbatch testplanandbatch) {
//        executeplanService.execcase(testplanandbatch);
//    }

}
