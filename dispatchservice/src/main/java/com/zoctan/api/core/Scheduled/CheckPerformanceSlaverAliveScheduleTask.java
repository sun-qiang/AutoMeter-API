package com.zoctan.api.core.Scheduled;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.core.service.HttpHeader;
import com.zoctan.api.core.service.Httphelp;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.DispatchMapper;
import com.zoctan.api.mapper.ExecuteplanMapper;
import com.zoctan.api.mapper.SlaverMapper;
import com.zoctan.api.mapper.TestconditionMapper;
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
public class CheckPerformanceSlaverAliveScheduleTask {

    @Autowired(required = false)
    private SlaverMapper slaverMapper;

    @Autowired(required = false)
    private DispatchMapper dispatchMapper;

    @Autowired(required = false)
    private ExecuteplanMapper executeplanMapper;

    @Autowired(required = false)
    RedisUtils redisUtils;
    private String redisKey = "";

    @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            boolean lock = redisUtils.tryLock(redisKey, "CheckPerformanceSlaverAliveScheduleTask", redis_default_expire_time);
            if (lock) {
                try {
                    CheckAliveSlaver();
                } catch (Exception ex) {
                    CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver是否Alive定时器异常=======================" + ex.getMessage());
                } finally {
                    //TODO 执行任务结束后需要释放锁
                    //释放锁
                    redisUtils.deletekey(redisKey);
                    CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver是否Alive定时器-============释放分布式锁成功=======================");
                }
            } else {
                CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver是否Alive定时器-============{}机器上占用分布式锁，正在执行中=======================" + redisKey);
                return;
            }
        } catch (Exception ex) {
            CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver是否Alive定时器-异常: " + ex.getMessage());
        }
    }

    public void CheckAliveSlaver() {
        List<Slaver> SlaverList = slaverMapper.findslaverbytype("性能");
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
                    respon = Httphelp.doPost(ServerUrl, params, header, 3000);
                    CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver检测：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
                } catch (Exception e) {
                    //1.置为已下线
                    slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
                    CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver：" + slaver.getSlavername() + " 更新为已下线。。。。。。。。。。。。。。。。。。。。。。。。" );
                    List<Dispatch> dispatchList= dispatchMapper.getdispatchsbyslaverid(slaver.getId(),"已分配","性能");
                    if(dispatchList.size()>0)
                    {
                        long planid=dispatchList.get(0).getExecplanid();
                        long batchid=dispatchList.get(0).getBatchid();
                        //2.更新dispatch为已取消
                        dispatchMapper.updatedispatchcancel("已取消",planid,batchid,"性能测试执行过程中，有执行机："+slaver.getSlavername()+" 异常下线，导致性能测试集合执行计划取消");
                        CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能更新集合id： "+planid + " 计划id ：" + batchid + " 状态为已取消。。。。。。。。。。。。。。。。。。。。。。。。" );
                    }
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
        List<Slaver> slaverlist = slaverMapper.findslaveralive("性能", "已下线");
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
                CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + respon);
            } catch (Exception e) {
                CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver检测GetAliveSlaver：" + ServerUrl + "请求响应结果。。。。。。。。。。。。。。。。。。。。。。。。：" + e.getMessage());
                slaverMapper.updateSlaverStatus(slaver.getId(), "已下线");
            }
        }
        return slaverlistresult;
    }

    @PostConstruct
    public void Init() {
        redisKey = "CheckPerformanceSlaverAliveScheduleTask-RedisLock" + new Date();
        CheckPerformanceSlaverAliveScheduleTask.log.info("调度服务检查性能Slaver调度服务检查Slaver是否Alive定时器-redisKey is:" + redisKey);
    }
}
