package com.api.autotest.test.httpapitestcase;

import com.alibaba.fastjson.JSONObject;
import com.api.autotest.core.TestAssert;
import com.api.autotest.core.TestCore;
import com.api.autotest.dto.ApicasesReportstatics;
import com.api.autotest.dto.RequestObject;
import com.api.autotest.dto.ResponeData;
import com.api.autotest.dto.TestResponeData;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.*;

public class HttpApiFunction extends AbstractJavaSamplerClient {
    TestCore Core = null;

    // 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行，类似于LoadRunner中的init方法
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);
    }

    // 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        //定义一个参数，显示到Jmeter的参数列表中，第一个参数为参数默认的显示名称，第二个参数为默认值
        params.addArgument("DispatchIds", "14");
        params.addArgument("SlaverId", "1");
        params.addArgument("mysqlurl", "/opt/");
        params.addArgument("mysqlusername", "/opt/");
        params.addArgument("mysqlpassword", "/opt/");
        return params;
    }

    // 测试执行的循环体，根据线程数和循环次数的不同可执行多次，类似于LoadRunner中的Action方法
    public SampleResult runTest(JavaSamplerContext ctx) {
        String SlaverId = ctx.getParameter("SlaverId");
        String planid = ctx.getParameter("planid");
        String BatchName = ctx.getParameter("batchname");
        SampleResult results = new SampleResult();
        //Jmeter java实例开始执行
        results.sampleStart();
        Core = new TestCore(ctx, getLogger(), results);
        // 初始化用例数据
        Map<Long, List<RequestObject>> requestObjectList = Core.InitalTestData(ctx);
        List<Long> sortlist = new ArrayList<>();
        for (Long Scenneid : requestObjectList.keySet()) {
            sortlist.add(Scenneid);
        }
        Collections.sort(sortlist);

        getLogger().info(" requestObject 初始化数据完成  。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。================------------------------------:");
        // 发送用例请求，并返回结果
        boolean scenestopflag = false;
        boolean scenecasestopflag = false;
        for (Long Sceneid : sortlist) {
            try {
                ArrayList<HashMap<String, String>> result = Core.GetSceneByid(Sceneid.toString());
                if (result.size() > 0) {
                    String SceneName = result.get(0).get("scenename");
                    RequestObject re = new RequestObject();
                    re.setTestplanid(planid);
                    re.setBatchname(BatchName);
                    re.setSlaverid(SlaverId);
                    re.setSceneid(Sceneid);
                    re.setScenename(SceneName);
                    re.setCaseid("0");
                    Core.FixSceneCondition(re);

                    getLogger().info(" requestObject Sceneid id is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + Sceneid + "用例个数：" + requestObjectList.get(Sceneid).size());
                    ArrayList<HashMap<String, String>> batchresult = Core.GetBatchByPBS(planid, BatchName, Sceneid.toString());
                    String status = "";
                    if (result.size() > 0) {
                        status = batchresult.get(0).get("status");
                        getLogger().info(" requestObject BatchName  is ------------------------------------------------------------------------------。:" + BatchName + " SceneName：" + SceneName + "状态：" + status);
                    }
                    // 人工暂停标记，表示停止集合运行，先刷新场景中的dispatch为已停止状态
                    if (status.equalsIgnoreCase("停止中") || scenestopflag) {
                        scenestopflag = true;
                        getLogger().info("场景id：" + Sceneid + " 用例被人工停止  。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。================------------------------------:");
                        Core.updatebatchdispatchcasestatus(planid, SlaverId, Sceneid.toString(), BatchName, "已停止");
                    } else {
                        for (RequestObject requestObject : requestObjectList.get(Sceneid)) {
                            getLogger().info(" requestObject casename id is ------------------------------------------------------------------------------。:" + requestObject.getCasename() + " 暂停标志：" + requestObject.getStopflag());
                            if (scenecasestopflag) {
                                Core.updatedispatchcasestatus(planid, requestObject.getCaseid(), SlaverId, Sceneid.toString(), BatchName, "已停止");
                            } else {
                                for (int i = 0; i < requestObject.getLoop(); i++) {
                                    try {
                                        boolean assertflag = Core.FixCase(requestObject, ctx, results);
                                        getLogger().info(" requestObject casename  is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + requestObject.getCasename() + " 断言结果：" + assertflag);
                                        if (!assertflag) //断言失败开始判断是否停止场景或者集合
                                        {
                                            String CaseStopFlag = requestObject.getStopflag();
                                            getLogger().info(" requestObject casename  is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + requestObject.getCasename() + " 暂停标志：" + CaseStopFlag);
                                            if (CaseStopFlag.equalsIgnoreCase("当前场景")) {
                                                scenecasestopflag = true;
                                            }
                                            if (CaseStopFlag.equalsIgnoreCase("当前集合")) {
                                                scenecasestopflag = true;
                                                scenestopflag = true;
                                            }
                                            if (CaseStopFlag.equalsIgnoreCase("无")) {
                                                scenecasestopflag = false;
                                                scenestopflag = false;
                                            }
                                        }
                                    } catch (Exception e) {
                                        getLogger().info(" 用例" + requestObject.getCasename() + "执行异常 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + e.getMessage());
                                    }
                                }
                            }
                        }
                        scenecasestopflag = false;
                    }
                }
            } catch (Exception e) {
                getLogger().info(" 场景id" + Sceneid + "执行前置条件失败异常 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + e.getMessage());
                //保存失败结果
            }
            //更新计划批次场景为运行完成或者已停止状态
            if (scenestopflag) {
                Core.FinisBatchScene(planid, BatchName, Sceneid.toString(), "已停止");
            } else {
                Core.FinisBatchScene(planid, BatchName, Sceneid.toString(), "已完成");
            }
        }
        //收集本次运行的功能用例统计结果
        //CollectionBatchDeployReportStatics(Core, apicasesReportstatics, BatchName, BatchDeployTotalCaseNums, BatchDeployTotalPassNums, BatchDeployTotalFailNUms, AllCostTime, SlaverId);
        Core.FinisPlanBatchAllCase(planid, BatchName, SlaverId);
        results.sampleEnd();
        return results;
    }

    //结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行，类似于LoadRunner中的end方法
    public void teardownTest(JavaSamplerContext ctx) {
        String SlaverId = ctx.getParameter("SlaverId");
        try {
            Core.UpdateSlaverStatus(SlaverId, "空闲");
        } catch (Exception exception) {
            getLogger().info("UpdateSlaverStatus 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + exception.getMessage());
        }
        super.teardownTest(ctx);
    }

    // 本地调试
    public static void main(String[] args) {
        Arguments params = new Arguments();
        params.addArgument("planid", "1");
        params.addArgument("batchname", "lai0005");
        params.addArgument("DispatchIds", "11,");
        params.addArgument("SlaverId", "5");
        params.addArgument("mysqlurl", "jdbc:mysql://127.0.0.1:3306/testcenter?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        params.addArgument("mysqlusername", "test");
        params.addArgument("mysqlpassword", "test");

        JavaSamplerContext ctx = new JavaSamplerContext(params);
        HttpApiFunction test = new HttpApiFunction();
        test.setupTest(ctx);
        test.runTest(ctx);
        test.teardownTest(ctx);
        System.exit(0);

    }
}
