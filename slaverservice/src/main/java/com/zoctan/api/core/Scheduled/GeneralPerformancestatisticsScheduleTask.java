package com.zoctan.api.core.Scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zoctan.api.core.config.RedisUtils;
import com.zoctan.api.entity.ApicasesPerformancestatistics;
import com.zoctan.api.entity.Executeplan;
import com.zoctan.api.entity.Performancereportsource;
import com.zoctan.api.entity.Slaver;
import com.zoctan.api.mapper.DictionaryMapper;
import com.zoctan.api.mapper.PerformancereportsourceMapper;
import com.zoctan.api.mapper.SlaverMapper;
import com.zoctan.api.service.ApicasesPerformancestatisticsService;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.util.IPHelpUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.*;

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
public class GeneralPerformancestatisticsScheduleTask {
    @Autowired(required = false)
    private DictionaryMapper dictionaryMapper;
    @Autowired(required = false)
    RedisUtils redisUtils;
    @Autowired(required = false)
    private ApicasesPerformancestatisticsService apicasesPerformancestatisticsService;
    @Autowired(required = false)
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    private ExecuteplanService epservice;
    @Autowired(required = false)
    private PerformancereportsourceMapper performancereportsourceMapper;
    private String redisKey = "";
    private String ip = "";


    //3.添加定时任务
    @Scheduled(cron = "0/20 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            long redis_default_expire_time = 2000;
            //默认上锁时间为五小时
            //此key存放的值为任务执行的ip，
            // redis_default_expire_time 不能设置为永久，避免死锁
            boolean lock = redisUtils.tryLock(redisKey, ip, redis_default_expire_time);
            if (lock) {
                try
                {
                    List<Slaver> slaverlist = slaverMapper.findslaverbyip(ip);
                    if (slaverlist.size() == 0) {
                        GeneralPerformancestatisticsScheduleTask.log.error("性能报告解析任务-没有找到slaver。。。。。。。。" + "未找到ip为：" + ip + "的slaver，请检查调度中心-执行节点");
                        throw new Exception("性能报告解析任务-没有找到slaver。。。。。。。。未找到ip为：" + ip + "的slaver，请检查调度中心-执行节点");
                    }
                    Long SlaverId = slaverlist.get(0).getId();
                    List<Performancereportsource> performancereportsourcelist= performancereportsourceMapper.findperformancereportsource(SlaverId);
                    for (Performancereportsource per:performancereportsourcelist)
                    {
                        fixperformancestatistics(per.getTestclass(),per.getBatchname(),per.getPlanid().toString(),per.getBatchid().toString(),per.getSlaverid().toString(),per.getCaseid().toString(),per.getSource(),per.getRuntime(),per.getCreator());
                        GeneralPerformancestatisticsScheduleTask.log.info("性能报告解析任务-ID："+per.getId()+" 解析完成");
                    }
                }
                catch (Exception ex)
                {
                    GeneralPerformancestatisticsScheduleTask.log.info("GeneralPerformancestatisticsScheduleTask性能报告解析异常======================="+ex.getMessage());
                }
                finally {
                    redisUtils.deletekey(redisKey);
                }
                //TODO 执行任务结束后需要释放锁
                //释放锁
                //GeneralPerformancestatisticsScheduleTask.log.info("GeneralPerformancestatisticsScheduleTask============释放分布式锁成功=======================");
            } else {
                //GeneralPerformancestatisticsScheduleTask.log.info("GeneralPerformancestatisticsScheduleTask============获得分布式锁失败=======================");
                ip =  redisUtils.getkey(redisKey);
                //GeneralPerformancestatisticsScheduleTask.log.info("GeneralPerformancestatisticsScheduleTask============{}机器上占用分布式锁，正在执行中======================="+redisKey+" ip:"+ip);
                return;
            }
        } catch (Exception ex) {
            GeneralPerformancestatisticsScheduleTask.log.info("GeneralPerformancestatisticsScheduleTask调度定时器异常: "+ex.getMessage());
        }
    }


    public void fixperformancestatistics(String testclass,String batchname,String testplanid,String batchid,String slaverid,String caseid,String casereportfolder,Double costtime,String Creator)  {
        try {

            Executeplan executeplan=epservice.getBy("id",Long.parseLong(testplanid));
            long projectid=executeplan.getProjectid();
            String casereport="";
            String property = System.getProperty("os.name");
            if(property.toLowerCase().startsWith("win")) {
                casereport=casereportfolder+"\\content\\js\\dashboard.js";
            }else
            {
                casereport=casereportfolder+"/content/js/dashboard.js";
            }
            GeneralPerformancestatisticsScheduleTask.log.info("参数为 is:"+testclass+"，"+batchname+"，"+testplanid+"，"+batchid+","+slaverid+","+caseid+","+casereportfolder+","+costtime);
            BufferedReader reader = new BufferedReader(new FileReader(casereport));
//            try {
//                reader = new BufferedReader(new FileReader(casereport));
//            } catch (FileNotFoundException e) {
//                GeneralPerformancestatisticsScheduleTask.log.info(e.getMessage());
//            }
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                GeneralPerformancestatisticsScheduleTask.log.info("性能报告解析任务- "+e.getMessage());
            }
            try {
                reader.close();
            } catch (IOException e) {
                GeneralPerformancestatisticsScheduleTask.log.info("性能报告解析任务- "+e.getMessage());
            }
            String statisticsTableStr="";
            String sourceStr = sb.substring(sb.indexOf("{"), sb.lastIndexOf("}") + 1);
            if (sourceStr != null && sourceStr.contains("statisticsTable")) {
                statisticsTableStr = (sourceStr.substring(sourceStr.indexOf("createTable($(\"#statisticsTable\"), ") + 35, sourceStr.length())).split(", function")[0];
            } else {
                GeneralPerformancestatisticsScheduleTask.log.info("There is no statisticsTable!,未找到性能数据");
            }

            GeneralPerformancestatisticsScheduleTask.log.info("statisticsTableStr is:"+statisticsTableStr);
            statisticsTableStr=statisticsTableStr.replace("Infinity","0.0");
            statisticsTableStr=statisticsTableStr.replace("NaN","0.0");



            JSONObject statisticsTableJson = JSONObject.parseObject(statisticsTableStr);
            JSONArray titlesArr = statisticsTableJson.getJSONArray("titles");
            JSONArray dataArr = statisticsTableJson.getJSONObject("overall").getJSONArray("data");
            JSONArray itemArr = statisticsTableJson.getJSONArray("items");

            JSONArray testclassdataarray=null;
            for (int i = 0; i < itemArr.size(); i++) {
                JSONObject data=(JSONObject)itemArr.get(i);
                JSONArray itemdataArr = data.getJSONArray("data");
                for (int j = 0; j < itemdataArr.size(); j++) {
                    if(String.valueOf(itemdataArr.get(j)).equals(testclass))
                    {
                        testclassdataarray =itemdataArr;
                        break;
                    }
                }
            }
            Map<String, String> reportMap = new HashMap<>();
            for (int i = 0; i < titlesArr.size(); i++) {
                reportMap.put(String.valueOf(titlesArr.get(i)), String.valueOf(testclassdataarray.get(i)));
            }
            String Samples=reportMap.get("#Samples");
            String KO=reportMap.get("KO");
            String Error=reportMap.get("Error %");
            String Average=reportMap.get("Average");
            String Min=reportMap.get("Min");
            String Max=reportMap.get("Max");
            String Median=reportMap.get("Median");
            String nzth=reportMap.get("90th pct");
            String nfth=reportMap.get("95th pct");
            String nnth=reportMap.get("99th pct");
            String Throughput=reportMap.get("Transactions/s");
            if(Throughput.contains("."))
            {
                Throughput=Throughput.substring(0,Throughput.indexOf('.')+2);
            }
            String Received=reportMap.get("Received");
            String Sent=reportMap.get("Sent");


            ApicasesPerformancestatistics apicasesPerformancestatistics=new ApicasesPerformancestatistics();
            apicasesPerformancestatistics.setCaseid(Long.parseLong(caseid));
            apicasesPerformancestatistics.setTestplanid(Long.parseLong(testplanid));
            apicasesPerformancestatistics.setBatchname(batchname);
            apicasesPerformancestatistics.setNzpct(Double.parseDouble(nzth));
            apicasesPerformancestatistics.setNfpct(Double.parseDouble(nfth));
            apicasesPerformancestatistics.setNnpct(Double.parseDouble(nnth));
            apicasesPerformancestatistics.setAverage(Double.parseDouble(Average));
            apicasesPerformancestatistics.setErrorcount(Long.parseLong(KO));
            apicasesPerformancestatistics.setErrorrate(Double.parseDouble(Error));
            apicasesPerformancestatistics.setMax(Double.parseDouble(Max));
            apicasesPerformancestatistics.setMin(Double.parseDouble(Min));
            apicasesPerformancestatistics.setMedian(Double.parseDouble(Median));
            apicasesPerformancestatistics.setSamples(Long.parseLong(Samples));
            apicasesPerformancestatistics.setReceivekbsec(Double.parseDouble(Received));
            apicasesPerformancestatistics.setSendkbsec(Double.parseDouble(Sent));
            apicasesPerformancestatistics.setRuntime(costtime);
            apicasesPerformancestatistics.setSlaverid(Long.parseLong(slaverid));
            apicasesPerformancestatistics.setTps(Double.parseDouble(Throughput));
            apicasesPerformancestatistics.setCreator(Creator);
            apicasesPerformancestatistics.setProjectid(projectid);
            apicasesPerformancestatisticsService.save(apicasesPerformancestatistics);

            performancereportsourceMapper.updateperformancereportsourcedone(Long.parseLong(testplanid),Long.parseLong(slaverid),Long.parseLong(batchid),Long.parseLong(caseid));
            GeneralPerformancestatisticsScheduleTask.log.info("性能报告解析任务- "+testclass+" ：保存性能统计结果完成...........: ");
        }
        catch (Exception ex)
        {
            GeneralPerformancestatisticsScheduleTask.log.info("性能报告解析任务- "+testclass+" ：保存性能统计结果异常...........: "+ex.getMessage());
        }


    }

    @PostConstruct
    public void Init() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            ip = IPHelpUtils.getInet4Address();//address.getHostAddress();
        } catch (UnknownHostException e) {
            GeneralPerformancestatisticsScheduleTask.log.info("性能统计报告-UnknownHostException is:" + e.getMessage());
        }
        redisKey = "Performancestatistics"+ip+"GeneralPerformancestatistics"+ new Date();
        GeneralPerformancestatisticsScheduleTask.log.info("性能统计报告-redisKey is:" + redisKey);
    }

    public static String getInet4Address() {
        Enumeration<NetworkInterface> nis;
        String ip = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            for (; nis.hasMoreElements();) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                for (; ias.hasMoreElements();) {
                    InetAddress ia = ias.nextElement();
                    //ia instanceof Inet6Address && !ia.equals("")
                    if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            GeneralPerformancestatisticsScheduleTask.log.info("slaver-getInet4Address......................................................."+e.getMessage());
        }
        return ip;
    }


}
