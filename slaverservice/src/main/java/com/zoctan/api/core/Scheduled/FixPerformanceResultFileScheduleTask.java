package com.zoctan.api.core.Scheduled;

import com.sun.corba.se.impl.orb.ParserTable;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.SlaverMapper;
import com.zoctan.api.service.ApicasesReportPerformanceService;
import com.zoctan.api.service.PerformancereportfilelogService;
import com.zoctan.api.service.PerformancereportsourceService;
import com.zoctan.api.service.RouteperformancereportService;
import com.zoctan.api.util.IPHelpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
public class FixPerformanceResultFileScheduleTask {
    @Autowired(required = false)
    RedisUtils redisUtils;
    @Autowired(required = false)
    private ApicasesReportPerformanceService apicasesReportPerformanceService;
    @Autowired(required = false)
    private PerformancereportfilelogService performancereportfilelogService;
    @Autowired(required = false)
    private PerformancereportsourceService performancereportsourceService;
    @Autowired(required = false)
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    private RouteperformancereportService routeperformancereportService;

    private String ip = "";


    //3.添加定时任务
    @Scheduled(cron = "0/3 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    private void configureTasks() {
        //TODO 执行任务结束后需要释放锁
        List<Slaver> slaverlist = slaverMapper.findslaverbyip(ip);
        if (slaverlist.size() == 0) {
            FixPerformanceResultFileScheduleTask.log.error("性能报告解析任务-没有找到slaver。。。。。。。。" + "未找到ip为：" + ip + "的slaver，请检查调度中心-执行节点");
        }
        Long SlaverId = slaverlist.get(0).getId();
        BufferedReader reader = null;
        try {
            List<Performancereportfilelog> performancereportfilelogList = performancereportfilelogService.findrecentperformancereportfile(SlaverId);
            if (performancereportfilelogList.size() > 0) {
                Performancereportfilelog performancereportfilelog = performancereportfilelogList.get(0);
                long planid = performancereportfilelog.getExecplanid();
                long batchid = performancereportfilelog.getBatchid();
                long slaverid = performancereportfilelog.getSlaverid();
                long caseid = performancereportfilelog.getCaseid();
                String property = System.getProperty("os.name");
                String ProjectPath = System.getProperty("user.dir");
                String JmeterPerformanceReportLogFilePath = "";
                if (ProjectPath.contains("slaverservice")) {
                    if(property.toLowerCase().startsWith("win")) {
                        JmeterPerformanceReportLogFilePath = ProjectPath + "\\performancereportlogfile"+ "\\" +SlaverId+"\\"+ planid + "\\" + performancereportfilelog.getFilename()+".txt";
                    }
                    else
                    {
                        JmeterPerformanceReportLogFilePath = ProjectPath + "/performancereportlogfile"+ "/" +SlaverId+"/"+ planid + "/" + performancereportfilelog.getFilename()+".txt";
                    }
                } else
                {
                    if(property.toLowerCase().startsWith("win")) {
                        JmeterPerformanceReportLogFilePath = ProjectPath + "\\slaverservice\\performancereportlogfile"+ "\\" +SlaverId+"\\"+ planid + "\\" + performancereportfilelog.getFilename()+".txt";
                    }
                    else
                    {
                        JmeterPerformanceReportLogFilePath = ProjectPath + "/slaverservice/performancereportlogfile"+ "/" +SlaverId+"/"+ planid + "/" + performancereportfilelog.getFilename()+".txt";
                    }
                }
                reader = new BufferedReader(new FileReader(JmeterPerformanceReportLogFilePath ));
                FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-开始读文件=======================" + JmeterPerformanceReportLogFilePath);
                performancereportfilelog.setStatus("处理中");
                performancereportfilelogService.update(performancereportfilelog);
                String line = reader.readLine();
                FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-文件行：=======================" + line);

                //保存到性能报告分表
                Routeperformancereport routeperformancereport = routeperformancereportService.getBy("executeplanid", planid);
                if (routeperformancereport != null) {
                    String TableName = routeperformancereport.getTablename();

                    long totalcasenums = 0;
                    long totalcasepassnums = 0;
                    long totalcasefailnums = 0;
                    while (line != null) {
                        boolean resultstatues = SavePerformanceReport(line, TableName);
                        totalcasenums = totalcasenums + 1;
                        //增加记录读到第几行，如果slaver崩溃，或者重启，还需要从第几行重新开始
                        if (resultstatues) {
                            totalcasepassnums = totalcasepassnums + 1;
                        } else {
                            totalcasefailnums = totalcasefailnums + 1;
                        }
                        line = reader.readLine();
                    }
                    FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-文件：" + performancereportfilelog.getFilename() + " 总行数：=======================" + totalcasenums);
                    FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-文件：" + performancereportfilelog.getFilename() + " 总成功行数：=======================" + totalcasepassnums);
                    FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-文件：" + performancereportfilelog.getFilename() + " 总失败行数：=======================" + totalcasefailnums);

                    //更新性能统计表
                    List<Performancereportsource> performancereportsourceList = performancereportsourceService.findperformancereportsourcebyids(caseid, slaverid, planid, batchid);
                    if (performancereportsourceList.size() > 0) {
                        Performancereportsource performancereportsource = performancereportsourceList.get(0);
                        performancereportsource.setTotalcasenums(totalcasenums);
                        performancereportsource.setTotalcasepassnums(totalcasepassnums);
                        performancereportsource.setTotalcasefailnums(totalcasefailnums);
                        performancereportsourceService.update(performancereportsource);
                    }
                    //更新性能结果日志表
                    performancereportfilelog.setStatus("已完成");
                    performancereportfilelogService.update(performancereportfilelog);
                }

            }
        } catch (Exception ex) {
            FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-异常=======================" + ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据关闭文件异常=======================" + ex.getMessage());
            }
        }
    }

    private boolean SavePerformanceReport(String Content, String TableName) throws Exception {
        String[] array = Content.split("\\$\\$");
        FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据文件每行长度：=======================" + array.length);
        boolean caseresultstatus = true;
        if (array.length == 19) {
            ApicasesReportPerformance apicasesReportPerformance = new ApicasesReportPerformance();
            long caseid = Long.parseLong(array[0]);
            apicasesReportPerformance.setCaseid(caseid);
            long planid = Long.parseLong(array[1]);
            apicasesReportPerformance.setTestplanid(planid);
            String batchname = array[2];
            apicasesReportPerformance.setBatchname(batchname);
            long slaverid = Long.parseLong(array[3]);
            String status = "";
            apicasesReportPerformance.setSlaverid(slaverid);
            if (array[4].equalsIgnoreCase("true")) {
                status = "成功";
                caseresultstatus = true;
            } else {
                status = "失败";
                caseresultstatus = false;
            }
            apicasesReportPerformance.setStatus(status);

            String respone = array[5];
            apicasesReportPerformance.setRespone(respone);
            String Assertvalue = array[6];
            apicasesReportPerformance.setAssertvalue(Assertvalue);
            long runtime = Long.parseLong(array[7]);
            apicasesReportPerformance.setRuntime(runtime);
            String Expect = array[8];
            apicasesReportPerformance.setExpect(Expect);
            String Errorinfo = array[9];
            apicasesReportPerformance.setErrorinfo(array[9]);

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateNowStr = sdf.parse(array[10]);
            apicasesReportPerformance.setCreateTime(dateNowStr);
            apicasesReportPerformance.setLastmodifyTime(dateNowStr);

            String Creator = array[12];
            apicasesReportPerformance.setCreator(Creator);
            String Requestheader = array[13];
            apicasesReportPerformance.setRequestheader(Requestheader);
            String Requestdatas = array[14];
            apicasesReportPerformance.setRequestdatas(Requestdatas);
            String Url = array[15];
            apicasesReportPerformance.setUrl(Url);
            String Requestmethod = array[16];
            apicasesReportPerformance.setRequestmethod(Requestmethod);
            long peojectid=Long.parseLong(array[17]);
            apicasesReportPerformance.setProjectid(peojectid);
            long sceneid=Long.parseLong(array[18]);
            apicasesReportPerformance.setSceneid(sceneid);
            apicasesReportPerformanceService.adddynamiccaseperformancereport(caseid, planid, batchname, slaverid, status, respone, Assertvalue, runtime, Expect, Errorinfo, dateNowStr, dateNowStr, Creator, Requestheader, Requestdatas, Url, Requestmethod, TableName,peojectid,sceneid);
        }
        return caseresultstatus;
    }

    @PostConstruct
    public void Init() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            ip = IPHelpUtils.getInet4Address();//address.getHostAddress();
        } catch (UnknownHostException e) {
            FixPerformanceResultFileScheduleTask.log.info("收集性能报告数据-UnknownHostException is:" + e.getMessage());
        }
    }
}
