package com.api.autotest.core;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.File;
import java.util.Date;

/**
 * Created by fanseasn on 2020/12/8.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/12/8
*/
public class postcondition extends AbstractJavaSamplerClient {

    // 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行，类似于LoadRunner中的init方法
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);
    }

    // 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        //定义一个参数，显示到Jmeter的参数列表中，第一个参数为参数默认的显示名称，第二个参数为默认值
        params.addArgument("testplanid", "12");
        params.addArgument("caseid", "1");
        params.addArgument("batchid", "41");
        params.addArgument("slaverid", "5");
        params.addArgument("batchname", "xxxxxxxxxxxxxxxxxxxxx");
        params.addArgument("casetype", "/opt/");
        params.addArgument("casereportfolder", "/opt/");
        params.addArgument("testclass", "/opt/");
        params.addArgument("start", "1608107091283");

        params.addArgument("mysqlurl", "jdbc:mysql://127.0.0.1:3306/testcenter?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        params.addArgument("mysqlusername", "root");
        params.addArgument("mysqlpassword", "root");
        params.addArgument("creator", "admin");
        return params;
    }

    // 测试执行的循环体，根据线程数和循环次数的不同可执行多次，类似于LoadRunner中的Action方法
    public SampleResult runTest(JavaSamplerContext ctx) {
//        SampleResult results = new SampleResult();
//        results.sampleEnd();
        return null;
    }

    //结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行，类似于LoadRunner中的end方法
    public void teardownTest(JavaSamplerContext ctx) {
        super.teardownTest(ctx);
        SampleResult results = new SampleResult();
        TestCore core = new TestCore(ctx, getLogger(),results);
        String caseid = ctx.getParameter("caseid");
        String testplanid = ctx.getParameter("testplanid");
        String batchid = ctx.getParameter("batchid");
        String batchname = ctx.getParameter("batchname");
        String slaverid = ctx.getParameter("slaverid");
        String casetype = ctx.getParameter("casetype");
        String casereportfolder = ctx.getParameter("casereportfolder").replace("Autometer", " ");
        String testclass = ctx.getParameter("testclass");
        String creator = ctx.getParameter("creator");

        String start = ctx.getParameter("start");
        getLogger().info("postcondition teardownTest 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:");

        try {
            long canceldispatch = core.PlanBatchAllDipatchCancel(testplanid, batchname);
            if (canceldispatch > 0) {
                core.UpdateReportStatics(testplanid, batchname, "已取消");
                getLogger().info(TestCaseData.logplannameandcasename + "更新计划批次表状态已取消");
            } else {
                //更新调度表状态已完成
                //core.updatedispatchcasestatus(testplanid, caseid, slaverid, batchid);
                getLogger().info(TestCaseData.logplannameandcasename + "更新调度表状态完成");
                long runningnums = core.PlanBatchAllDipatchFinish(testplanid, batchname);
                if (runningnums == 0) {
                    core.UpdateReportStatics(testplanid, batchname, "已完成");
                    getLogger().info(TestCaseData.logplannameandcasename + "更新计划批次表状态完成");
                }
            }
            //新增性能日志记录表
            String FileName = testplanid + "-" + batchid + "-" + slaverid+ "-" + caseid;
            core.generalperformancelogfile(testplanid, caseid, slaverid, batchid, FileName, "待处理");
            getLogger().info(TestCaseData.logplannameandcasename + "generalperformancelogfile完成");
            core.SendMessageDingDing(testplanid, batchname);
            core.SendMailByFinishPlanCase(testplanid, batchname);
            getLogger().info(TestCaseData.logplannameandcasename + "发送mail，dingding完成");

        } catch (Exception ex) {
            getLogger().info(TestCaseData.logplannameandcasename + "性能测试teardownTest异常：" + ex.getMessage());
        }
        finally {
            try {
                core.UpdateSlaverStatus(slaverid, "空闲");
            } catch (Exception ex) {
                getLogger().info(TestCaseData.logplannameandcasename + "性能测试teardownTest更新Slaver状态空闲异常：" + ex.getMessage());
            }
        }
        //slaver性能测试解析报告，生成数据入库
        try {
            if (casetype.equalsIgnoreCase("性能")) {
                getLogger().info(TestCaseData.logplannameandcasename + "处理性能报告出错获取的开始时间：" + start);
                long end = new Date().getTime();
                long starttime = Long.parseLong(start);
                double costtime = (double) (end - starttime) / 1000;
                core.genealperformacestaticsreport(testclass, batchname, testplanid, batchid, slaverid, caseid, casereportfolder, costtime, creator);
                getLogger().info(TestCaseData.logplannameandcasename + "保存待解析性能报告结果完成。。。。。。");
//                String property = System.getProperty("os.name");
//                File file1 =null;
//                String FilePath="";
//                if(property.toLowerCase().startsWith("win")) {
//                    FilePath = casereportfolder + "\\index.html";
//                }
//                else
//                {
//                    FilePath = casereportfolder + "/index.html";
//                }
//                getLogger().info(TestCaseData.logplannameandcasename + "处理性能统计报告目录：" + FilePath);
//                file1 = new File(FilePath);
//                if (!file1.exists()) {
//                    getLogger().error(TestCaseData.logplannameandcasename + "性能报告文件未生成。。。。。。。。。。。。。。。");
//                }
//                else
//                {
//                    getLogger().info(TestCaseData.logplannameandcasename + "处理性能报告出错获取的开始时间：" + start);
//                    long end = new Date().getTime();
//                    long starttime = Long.parseLong(start);
//                    double costtime = (double) (end - starttime) / 1000;
//                    core.genealperformacestaticsreport(testclass, batchname, testplanid, batchid, slaverid, caseid, casereportfolder, costtime, creator);
//                    getLogger().info(TestCaseData.logplannameandcasename + "保存待解析性能报告结果完成。。。。。。");
//                }
            }
        } catch (Exception e) {
            getLogger().error(TestCaseData.logplannameandcasename + "处理性能报告文件出错：" + e.getMessage());
        }
    }

    // 本地调试
    public static void main(String[] args) {

        Arguments params = new Arguments();
        params.addArgument("testplanid", "12");
        params.addArgument("caseid", "1");
        params.addArgument("batchid", "2");
        params.addArgument("slaverid", "5");
        params.addArgument("batchname", "xxxxxxxxxxxxxxxxxxxxx");

        params.addArgument("casereportfolder", "/Users/fanseasn/Desktop/testresult/13-2-x100001");
        params.addArgument("casetype", "性能");
        params.addArgument("testclass", "retrySendSmsOrFindShortUrl");
        params.addArgument("start", "1608107091283");

        params.addArgument("mysqlurl", "jdbc:mysql://127.0.0.1:3306/testcenter?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        params.addArgument("mysqlusername", "root");
        params.addArgument("mysqlpassword", "root");

        JavaSamplerContext ctx = new JavaSamplerContext(params);
        postcondition test = new postcondition();
        test.setupTest(ctx);
        test.runTest(ctx);
        test.teardownTest(ctx);
        System.exit(0);

    }
}
