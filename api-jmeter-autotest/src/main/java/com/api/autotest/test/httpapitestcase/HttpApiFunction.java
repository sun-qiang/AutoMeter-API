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
        Core = new TestCore(ctx, getLogger(),results);
        // 初始化用例数据
        Map<Long, List<RequestObject>> requestObjectList = Core.InitalTestData(ctx);
        // 发送用例请求，并返回结果
        for (Long Sceneid : requestObjectList.keySet()) {
            //fixSceneCondition
            getLogger().info(" requestObject Sceneid id is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + Sceneid);
            for (RequestObject requestObject : requestObjectList.get(Sceneid)) {
                for (int i = 0; i < requestObject.getLoop(); i++) {
                    try {
                        Core.FixCase(requestObject, ctx, results);
                    } catch (Exception e) {
                        getLogger().info(" 用例" + requestObject.getCasename() + "执行异常 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + e.getMessage());
                    }
//                    long Start = new Date().getTime();
//                    //断言信息汇总
//                    String AssertInfo = "";
//                    String ErrorInfo = "";
//                    String ActualResult = "";
//                    TestResponeData responeData = new TestResponeData();
//                    TestAssert TestAssert = new TestAssert(getLogger());
//                    try {
//                        //增加条件处理逻辑，bug用例前置api还未执行，变量未产生，用例的参数值是错的
//                        Core.FixCaseCondition(requestObject);
//                        requestObject = Core.GetFuntionHttpRequestData(requestObject);
//                        responeData = Core.request(requestObject);// SendCaseRequest(requestObject, Core);
//                        getLogger().info("用例：" + requestObject.getCasename() + " 请求完成 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:");
//                        ActualResult = responeData.getResponeContent();
//                        //断言
//                        AssertInfo = Core.FixAssert(TestAssert, requestObject.getApicasesAssertList(), responeData);
//                    } catch (Exception ex) {
//                        getLogger().error("CaseException start。。。。。。。。。。。。。!" + ex.getMessage());
//                        String ExceptionMess = ex.getMessage();
//                        if (ExceptionMess.contains("Illegal character in path at")) {
//                            ExceptionMess = "Url不合法，请检查是否有无法替换的变量，或者有相关非法字符：" + ex.getMessage();
//                        }
//                        ErrorInfo = CaseException(results, TestAssert, ExceptionMess);
//                    } finally {
//                        // 保存用例运行结果，Jmeter的sample运行结果
//                        long End = new Date().getTime();
//                        long CostTime = End - Start;
//                        CaseFinish(Core, results, TestAssert, AssertInfo, CostTime, ErrorInfo, ActualResult, ctx, requestObject, responeData);
//                    }
                }
            }
            //更新计划批次场景为运行完成状态
            Core.FinisBatchScene(planid, BatchName, Sceneid.toString());
        }
        //收集本次运行的功能用例统计结果
        //CollectionBatchDeployReportStatics(Core, apicasesReportstatics, BatchName, BatchDeployTotalCaseNums, BatchDeployTotalPassNums, BatchDeployTotalFailNUms, AllCostTime, SlaverId);
        Core.FinisBatchCase(planid, BatchName, SlaverId);
//    Map<String, List<RequestObject>> BatchRequestObjectMap = InitalTestData(Core, ctx);
//
//
//        for (String BatchName : BatchRequestObjectMap.keySet()) {
//            getLogger().info("BatchName 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + BatchName + " size is" + BatchRequestObjectMap.get(BatchName).size());
//            String TestPlanID = "";
//            if (BatchRequestObjectMap.get(BatchName).size() > 0) {
//                TestPlanID = BatchRequestObjectMap.get(BatchName).get(0).getTestplanid();
//                getLogger().info("TestPlanID 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + TestPlanID);
//                Core.UpdateReportStatics(TestPlanID, BatchName, "运行中");
//            }
//            Map<String, List<RequestObject>> DeployUnitrequestObjectMap = GetGroupMap(BatchRequestObjectMap.get(BatchName), "DeployID");
//            for (String DeployUnitID : DeployUnitrequestObjectMap.keySet()) {
//                ApicasesReportstatics apicasesReportstatics = new ApicasesReportstatics();
//                getLogger().info("DeployUnitID 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + DeployUnitID + " size is:" + DeployUnitrequestObjectMap.get(DeployUnitID).size());
//                apicasesReportstatics.setDeployunitid(DeployUnitID);
//                int BatchDeployTotalCaseNums = DeployUnitrequestObjectMap.get(DeployUnitID).size();
//                int BatchDeployTotalPassNums = 0;
//                int BatchDeployTotalFailNUms = 0;
//                long AllCostTime = 0;
//                // 发送用例请求，并返回结果
//                for (RequestObject requestObject : DeployUnitrequestObjectMap.get(DeployUnitID)) {
//                    for (int i = 0; i < requestObject.getLoop(); i++) {
//                        getLogger().info("Deployid:" + DeployUnitID + " requestObject case id is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + requestObject.getCaseid());
//                        apicasesReportstatics.setTestplanid(requestObject.getTestplanid());
//                        apicasesReportstatics.setTestplanname(requestObject.getTestplanname());
//                        apicasesReportstatics.setSlaverid(requestObject.getSlaverid());
//                        long Start = new Date().getTime();
//                        //断言信息汇总
//                        String AssertInfo = "";
//                        String ErrorInfo = "";
//                        String ActualResult = "";
//                        TestResponeData responeData=new TestResponeData();
//                        TestAssert TestAssert = new TestAssert(getLogger());
//                        try {
//                            //增加条件处理逻辑，bug用例前置api还未执行，变量未产生，用例的参数值是错的
//                            Core.FixCondition(requestObject);
//                            requestObject=Core.GetFuntionHttpRequestData(requestObject);
//                            responeData = Core.request(requestObject);// SendCaseRequest(requestObject, Core);
//                            getLogger().info("用例："+requestObject.getCasename()+" 请求完成 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" );
//                            ActualResult = responeData.getResponeContent();
//                            //断言
//                            AssertInfo = Core.FixAssert(TestAssert, requestObject.getApicasesAssertList(), responeData);
//                        } catch (Exception ex) {
//                            getLogger().error("CaseException start。。。。。。。。。。。。。!" + ex.getMessage());
//                            String ExceptionMess = ex.getMessage();
//                            if (ExceptionMess.contains("Illegal character in path at")) {
//                                ExceptionMess = "Url不合法，请检查是否有无法替换的变量，或者有相关非法字符：" + ex.getMessage();
//                            }
//                            ErrorInfo = CaseException(results, TestAssert, ExceptionMess);
//                        } finally {
//                            // 保存用例运行结果，Jmeter的sample运行结果
//                            long End = new Date().getTime();
//                            long CostTime = End - Start;
//                            AllCostTime = AllCostTime + CostTime;
//                            if (TestAssert.isCaseresult()) {
//                                BatchDeployTotalPassNums = BatchDeployTotalPassNums + 1;
//                            } else {
//                                BatchDeployTotalFailNUms = BatchDeployTotalFailNUms + 1;
//                            }
//
//                            CaseFinish(Core, results, TestAssert, AssertInfo, CostTime, ErrorInfo, ActualResult, ctx, requestObject,responeData);
//                        }
//                    }
//                }
//                //收集本次运行的功能用例统计结果
//                CollectionBatchDeployReportStatics(Core, apicasesReportstatics, BatchName, BatchDeployTotalCaseNums, BatchDeployTotalPassNums, BatchDeployTotalFailNUms, AllCostTime, SlaverId);
//            }
//            FinisBatchCase(Core, TestPlanID, BatchName, SlaverId);
//        }
        //Jmeter事务，表示这是事务的结束点
        results.sampleEnd();
        return results;
    }

    //初始化用例的基础数据
//    private Map<Long, List<RequestObject>> InitalTestData(TestCore core, JavaSamplerContext ctx) {
//        getLogger().info("根据调度ids获取请求数据列表 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:");
//        List<RequestObject> objectList = core.GetDispatchOBList(ctx);
//        Map<Long, List<RequestObject>> BatchObject = GetGroupMap(objectList, "BatchName");
//        return BatchObject;
//    }
//
//    private Map<Long, List<RequestObject>> GetGroupMap(List<RequestObject> objectList, String ObjectName) {
//        Map<Long, List<RequestObject>> GroupObjectMap = new HashMap<>();
//        for (RequestObject ob : objectList) {
//            if (!GroupObjectMap.containsKey(ob.getSceneid())) {
//                List<RequestObject> tmpList = new ArrayList<>();
//                tmpList.add(ob);
//                GroupObjectMap.put(ob.getSceneid(), tmpList);
//            } else {
//                GroupObjectMap.get(ob.getSceneid()).add(ob);
//            }
//        }
//        return GroupObjectMap;
//    }
//
//    //用例运行过程中的异常信息处理
//    private String CaseException(SampleResult results, TestAssert testAssert, String exceptionMessage) {
//        // 断言用例运行结果为失败
//        testAssert.setCaseresult(false);
//        String ErrorInfo = exceptionMessage.replace("'", "");
//        getLogger().error("用例执行发生异常，请检查!" + exceptionMessage);
//        return ErrorInfo;
//    }
//
//    //用例运行结束保存记录，并且更新dispatch状态为完成
//    private void CaseFinish(TestCore core, SampleResult results, TestAssert testAssert, String assertInfo, long time, String ErrorInfo, String ActualResult, JavaSamplerContext ctx, RequestObject requestObject, TestResponeData testResponeData) {
//        //jmeter java实例执行完成，记录结果
//        try {
//            results.setSuccessful(testAssert.isCaseresult());
//            ActualResult = ActualResult.replace("'", "");
//            assertInfo = assertInfo.replace("'", "");
//            ErrorInfo = ErrorInfo.replace("'", "");
//            //保存报告
//            int key = core.savetestcaseresult(testAssert.isCaseresult(), time, ActualResult, assertInfo, ErrorInfo, requestObject, ctx);
//            //转换成json存数据库新报告扩展表
//            String testResponeDatarResult = JSONObject.toJSONString(testResponeData);
//            String projectid = ctx.getParameter("projectid");
//            core.savetestcaseextresult(String.valueOf(key), testResponeDatarResult, projectid);
//            //更新调度状态
//            core.updatedispatchcasestatus(requestObject.getTestplanid(), requestObject.getCaseid(), requestObject.getSlaverid(), requestObject.getSceneid().toString(), requestObject.getBatchname());
//        } catch (Exception ex) {
//            getLogger().error("用例运行结束保存记录CaseFinish发生异常，请检查!" + ex.getMessage());
//        }
//    }
//
//    //功能用例统计批次微服务用例执行信息
//    private void CollectionBatchDeployReportStatics(TestCore core, ApicasesReportstatics apicasesReportstatics, String BatchName, int TotalCaseNums, int TotalPassNums, int TotalFailNUms, long AllCostTime, String SlaverId) {
//        apicasesReportstatics.setBatchname(BatchName);
//        apicasesReportstatics.setTotalcases(String.valueOf(TotalCaseNums));
//        apicasesReportstatics.setTotalpasscases(String.valueOf(TotalPassNums));
//        apicasesReportstatics.setTotalfailcases(String.valueOf(TotalFailNUms));
//        apicasesReportstatics.setRuntime(String.valueOf(AllCostTime));
//        core.SaveReportStatics(apicasesReportstatics);
//    }
//
//
//    private void FinisBatchScene(TestCore core, String planid, String BatchName, String Sceneid) {
//        core.UpdateBatchScene(planid, BatchName, Sceneid, "运行完成");
//    }
//
//    private void FinisBatchCase(TestCore core, String planid, String BatchName, String SlaverId) {
//        getLogger().info("SlaverId 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + SlaverId);
//        getLogger().info("功能用例统计收集信息 完成。。。。。。。。。。。。。。。。");
//        //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
//        long DispatchNotFinishNums = core.PlanBatchAllDipatchFinish(planid, BatchName);
//
//        if (DispatchNotFinishNums > 0) {
//            getLogger().info("查询计划下的批次调度未完成数量：" + DispatchNotFinishNums);
//        } else {
//            core.UpdateReportStatics(planid, BatchName, "已完成");
//            core.SendMessageDingDing(planid, BatchName);
//            core.SendMailByFinishPlanCase(planid, BatchName);
//        }
//        //增加邮件通知
//    }


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
        params.addArgument("batchname", "zxzxzxzxzxzx1111");
        params.addArgument("DispatchIds", "3");
        params.addArgument("SlaverId", "2");
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
