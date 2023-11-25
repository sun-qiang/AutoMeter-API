package com.api.autotest.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.autotest.common.utils.*;
import com.api.autotest.dto.*;
import com.google.common.collect.Maps;
import org.apache.http.Header;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Logger;

import java.util.*;

/**
 * Created by fanseasn on 2020/10/17.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/17
*/
public class TestCore {
    private Logger logger = null;
    private TestMysqlHelp testMysqlHelp = null;
    private TestCaseData testCaseData = null;
    private TestHttpRequestData testHttpRequestData = null;
    private TestHttp testHttp = null;
    private TestCondition testCondition = null;

    public TestCore(JavaSamplerContext context, Logger log, SampleResult results) {
        logger = log;
        Httphelp.logger = log;
        String MysqlUrl = context.getParameter("mysqlurl");
        String MysqlUserName = context.getParameter("mysqlusername");
        String MysqlPass = context.getParameter("mysqlpassword");
        String JdbcMysql = "?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai";
        logger.info("TestCore 数据库连接字 is :  " + MysqlUrl + JdbcMysql + "   " + MysqlUserName + "   " + MysqlPass);
        //GetDBConnection(MysqlUrl, MysqlUserName, MysqlPass);
        testHttp = new TestHttp(log);
        testMysqlHelp = new TestMysqlHelp(MysqlUrl + JdbcMysql, MysqlUserName, MysqlPass, log);
        testCaseData = new TestCaseData(log, testMysqlHelp);
        testHttpRequestData = new TestHttpRequestData(log, testMysqlHelp);
        testCondition = new TestCondition(log, results, context, testMysqlHelp, testCaseData, testHttpRequestData, testHttp);
    }

    //性能初始化数据根据jmeter传递下来的数据拼装用例请求的数据
    public RequestObject InitHttpDatabyJmeter(JavaSamplerContext context) throws Exception {
        RequestObject requestObject = testCaseData.InitHttpDatabyJmeter(context);
        RequestObject newob = testHttpRequestData.GetPerformanceHttpRequestData(requestObject);
        logger.info("GetPerformanceHttpRequestData 完成-============：");
        return newob;
    }

    //功能测试通过调度IDs获取请求拼装数据列表
    public List<RequestObject> GetDispatchOBList(JavaSamplerContext context) {
        List<RequestObject> FunctionROList = new ArrayList<>();
        String planid = context.getParameter("planid");
        String batchname = context.getParameter("batchname");
        String SlaverId = context.getParameter("SlaverId");
        String DispatchIds = context.getParameter("DispatchIds");
        String[] SceneArray = DispatchIds.split(",");
        for (String SceneID : SceneArray) {
            ArrayList<HashMap<String, String>> DispatchList = getcaseData("select * from dispatch where execplanid=" + planid + " and batchname='" + batchname + "' and slaverid=" + SlaverId + " and sceneid=" + SceneID + " order by caseorder asc");
            for (int i = 0; i < DispatchList.size(); i++) {
                HashMap<String, String> hs = DispatchList.get(i);
                String DispatchId = hs.get("id");
                ArrayList<HashMap<String, String>> DispatchDataList = getcaseData("select * from dispatch where id=" + DispatchId);
                String PlanId = getcaseValue("execplanid", DispatchDataList);
                String CaseId = getcaseValue("testcaseid", DispatchDataList);
                String SceneId = getcaseValue("sceneid", DispatchDataList);
                String SceneName = getcaseValue("scenename", DispatchDataList);
                //String SlaverId = getcaseValue("slaverid", DispatchList);
                String BatchId = getcaseValue("batchid", DispatchDataList);
                String BatchName = getcaseValue("batchname", DispatchDataList);
                String ExecPlanName = getcaseValue("execplanname", DispatchDataList);
                RequestObject ro = testCaseData.GetCaseRequestData(PlanId, CaseId, SlaverId, BatchName, ExecPlanName, SceneId, SceneName);
                //ro=testHttpRequestData.GetFuntionHttpRequestData(ro);
                ro = GetFuntionHttpRequestData(ro);
                ro.setLoop(Integer.parseInt(getcaseValue("loops", DispatchDataList)));
                FunctionROList.add(ro);
                logger.info("CaseId:" + CaseId + "初始化 完成-============================================================：");
            }
//            String DispatchId = getcaseValue("id", DispatchList);
//
//            for (String DispatchID : DispatchArray) {
//                ArrayList<HashMap<String, String>> DispatchList = getcaseData("select * from dispatch where id=" + DispatchID);
//                String PlanId = getcaseValue("execplanid", DispatchList);
//                String CaseId = getcaseValue("testcaseid", DispatchList);
//                //String SlaverId = getcaseValue("slaverid", DispatchList);
//                String BatchId = getcaseValue("batchid", DispatchList);
//                String BatchName = getcaseValue("batchname", DispatchList);
//                String ExecPlanName = getcaseValue("execplanname", DispatchList);
//                RequestObject ro =testCaseData.GetCaseRequestData(PlanId, CaseId, SlaverId, BatchId, BatchName, ExecPlanName);
//                //ro=testHttpRequestData.GetFuntionHttpRequestData(ro);
//                ro=GetFuntionHttpRequestData(ro);
//                ro.setLoop(Integer.parseInt(getcaseValue("loops", DispatchList)));
//                FunctionROList.add(ro);
//                logger.info("CaseId:"+CaseId+"初始化 完成-============================================================：");
//            }
        }
        return FunctionROList;
    }

    public RequestObject GetFuntionHttpRequestData(RequestObject ro) {
        RequestObject roresult = testHttpRequestData.GetFuntionHttpRequestData(ro);
        return roresult;
    }

    private Map<Long, List<RequestObject>> GetGroupMap(List<RequestObject> objectList, String ObjectName) {
        Map<Long, List<RequestObject>> GroupObjectMap = new HashMap<>();
        for (RequestObject ob : objectList) {
            if (!GroupObjectMap.containsKey(ob.getSceneid())) {
                List<RequestObject> tmpList = new ArrayList<>();
                tmpList.add(ob);
                GroupObjectMap.put(ob.getSceneid(), tmpList);
            } else {
                GroupObjectMap.get(ob.getSceneid()).add(ob);
            }
        }
        return GroupObjectMap;
    }

    //初始化用例的基础数据
    public Map<Long, List<RequestObject>> InitalTestData(JavaSamplerContext ctx) {
        logger.info("根据调度ids获取请求数据列表 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:");
        List<RequestObject> objectList = GetDispatchOBList(ctx);
        Map<Long, List<RequestObject>> BatchObject = GetGroupMap(objectList, "BatchName");
        return BatchObject;
    }


    public void FinisBatchScene(String planid, String BatchName, String Sceneid) {
        UpdateBatchScene(planid, BatchName, Sceneid, "已完成");
    }

    public void FinisBatchCase(String planid, String BatchName, String SlaverId) {
        logger.info("SlaverId 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + SlaverId);
        logger.info("功能用例统计收集信息 完成。。。。。。。。。。。。。。。。");
        //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
        long DispatchNotFinishNums = PlanBatchAllDipatchFinish(planid, BatchName);

        if (DispatchNotFinishNums > 0) {
            logger.info("查询计划下的批次调度未完成数量：" + DispatchNotFinishNums);
        } else {
            UpdateReportStatics(planid, BatchName, "已完成");
            SendMessageDingDing(planid, BatchName);
            SendMailByFinishPlanCase(planid, BatchName);
        }
        //增加邮件通知
    }

    private String CaseException(TestAssert testAssert, String exceptionMessage) {
        // 断言用例运行结果为失败
        testAssert.setCaseresult(false);
        String ErrorInfo = exceptionMessage.replace("'", "");
        logger.error("用例执行发生异常，请检查!" + exceptionMessage);
        return ErrorInfo;
    }


    private void CaseFinish(SampleResult results, TestAssert testAssert, String assertInfo, long time, String ErrorInfo, String ActualResult, JavaSamplerContext ctx, RequestObject requestObject, TestResponeData testResponeData) {
        //jmeter java实例执行完成，记录结果
        try {
            results.setSuccessful(testAssert.isCaseresult());
            ActualResult = ActualResult.replace("'", "");
            assertInfo = assertInfo.replace("'", "");
            ErrorInfo = ErrorInfo.replace("'", "");
            //保存报告
            savetestcaseresult(testAssert.isCaseresult(), time, ActualResult, assertInfo, ErrorInfo, requestObject, ctx);
            //查询自己是不是条件，如果是并且条件报告中没有结果，则保存条件报告，解析变量保存
            FixCaseFinishCondition(requestObject, testResponeData);
            //转换成json存数据库新报告扩展表
//            String testResponeDatarResult = JSONObject.toJSONString(testResponeData);
//            logger.error("用例testResponeDatarResult，请检查.....................................................!-- " + key + " ---" + testResponeDatarResult);
//
//            String projectid = ctx.getParameter("projectid");
//            savetestcaseextresult(String.valueOf(key), testResponeDatarResult, projectid);
            //更新调度状态
            updatedispatchcasestatus(requestObject.getTestplanid(), requestObject.getCaseid(), requestObject.getSlaverid(), requestObject.getSceneid().toString(), requestObject.getBatchname());
        } catch (Exception ex) {
            logger.error("用例运行结束保存记录CaseFinish发生异常，请检查!" + ex.getMessage());
        }
    }

    public void FixCaseFinishCondition(RequestObject requestObject, TestResponeData testResponeData) {
        try {
            Long PlanID = Long.parseLong(requestObject.getTestplanid());
            String BatchName = requestObject.getBatchname();
            Long SceneID = requestObject.getSceneid();
            ArrayList<HashMap<String, String>> ScenceCaseList = GetSceneCaseByID(SceneID);
            Long ConditionCaseID = Long.parseLong(requestObject.getCaseid());
            ArrayList<HashMap<String, String>> ApiConditionList = GetConditionApiByCaseIDAndType(ConditionCaseID, "scencecase");
            boolean flag = false;
            for (int i = 0; i < ApiConditionList.size(); i++) {
                HashMap<String, String> hs = ApiConditionList.get(i);
                Long Conditionid = Long.parseLong(hs.get("id"));
                Long ConditionTestScenecaseid = Long.parseLong(hs.get("conditionid"));
                for (int ii = 0; ii < ScenceCaseList.size(); ii++) {
                    HashMap<String, String> hscase = ScenceCaseList.get(ii);
                    Long TestScenecaseid = Long.parseLong(hscase.get("id"));
                    if (ConditionTestScenecaseid.equals(TestScenecaseid)) {
                        flag = true;
                    }
                    if (flag) {
                        //需要判断是否已经存条件报告，不存在则新增条件报告
                        ArrayList<HashMap<String, String>> ApiConditionReportList = testMysqlHelp.Gettestconditionreport(PlanID, BatchName, Long.parseLong(hs.get("id")), "接口");
                        if (ApiConditionReportList.size() == 0) {
                            String Respone = testResponeData.getResponeContent();
                            String status = "成功";
                            Long runtime = testResponeData.getResponeTime();
                            TestconditionReport testconditionReport = new TestconditionReport();
                            testconditionReport.setTestplanid(Long.parseLong(requestObject.getTestplanid()));
                            testconditionReport.setPlanname(requestObject.getTestplanname());
                            testconditionReport.setBatchname(requestObject.getBatchname());
                            testconditionReport.setConditiontype("前置条件");
                            testconditionReport.setConditionresult(Respone);
                            testconditionReport.setConditionstatus(status);
                            testconditionReport.setRuntime(runtime);
                            testconditionReport.setSubconditionid(Long.parseLong(hs.get("id")));
                            testconditionReport.setSubconditionname(hs.get("subconditionname"));
                            testconditionReport.setSubconditiontype("接口");
                            testconditionReport.setStatus("已完成");
                            logger.info("TestCondition条件报告保存子条件已完成状态-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname() + "|" + requestObject.getCasename());
                            //增加判断是否已经存在
                            testMysqlHelp.SubConditionReportSave(testconditionReport);
                            testCondition.FixInterfaceVariables(requestObject, Conditionid,hs.get("conditiontype"), Long.parseLong(requestObject.getCaseid()), testResponeData, Respone, Long.parseLong(requestObject.getTestplanid()), requestObject.getTestplanname(), requestObject.getBatchname());
                            flag = false;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("FixCaseFinishCondition用例是否为条件处理异常：" + requestObject.getCasename() + ex.getMessage());
        }
    }

    public void FixCase(RequestObject requestObject, JavaSamplerContext ctx, SampleResult results) throws Exception {
        FixCaseCondition(requestObject);
        logger.info(" requestObject casename id is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + requestObject.getCasename()+"条件处理完成");
        FixCaseData(requestObject, ctx, results, true, null);
        logger.info(" requestObject casename id is 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" + requestObject.getCasename()+"数据处理完成");
    }

    public void FixCaseData(RequestObject requestObject, JavaSamplerContext ctx, SampleResult results, boolean flag, TestResponeData responeData1) throws Exception {
        long Start = new Date().getTime();
        //断言信息汇总
        String AssertInfo = "";
        String ErrorInfo = "";
        String ActualResult = "";
        TestResponeData responeData = new TestResponeData();
        TestAssert TestAssert = new TestAssert(logger);
        try {
            //增加条件处理逻辑，bug用例前置api还未执行，变量未产生，用例的参数值是错的
            if (flag) {
                requestObject = GetFuntionHttpRequestData(requestObject);
                responeData = request(requestObject);// SendCaseRequest(requestObject, Core);
                logger.info("用例：" + requestObject.getCasename() + " 请求完成 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:");
            } else {
                responeData = responeData1;
            }
            ActualResult = responeData.getResponeContent();
            //断言
            AssertInfo = FixAssert(TestAssert, requestObject.getApicasesAssertList(), responeData);
        } catch (Exception ex) {
            logger.error("CaseException start。。。。。。。。。。。。。!" + ex.getMessage());
            String ExceptionMess = ex.getMessage();
            if (ExceptionMess.contains("Illegal character in path at")) {
                ExceptionMess = "Url不合法，请检查是否有无法替换的变量，或者有相关非法字符：" + ex.getMessage();
            }
            ErrorInfo = CaseException(TestAssert, ExceptionMess);
        } finally {
            // 保存用例运行结果，Jmeter的sample运行结果
            long End = new Date().getTime();
            long CostTime = End - Start;
            CaseFinish(results, TestAssert, AssertInfo, CostTime, ErrorInfo, ActualResult, ctx, requestObject, responeData);
        }
    }


    //处理用例条件入口
    public void FixCaseCondition(RequestObject requestObject) throws Exception {
        Long testcaseid = Long.parseLong(requestObject.getCaseid());
        Long secneid = requestObject.getSceneid();
        ArrayList<HashMap<String, String>> testscenecaseList = GetSceneID(testcaseid, secneid);
        if (testscenecaseList.size() > 0) {
            long SceneCaseID = Long.parseLong(testscenecaseList.get(0).get("id"));
            //接口条件
            ArrayList<HashMap<String, String>> ConditionApiList = GetConditionApiByObjectIDAndType(SceneCaseID, "scencecase");
            logger.info("TestCore 开始处理用例前置条件-接口子条件-============："+ConditionApiList.size());
            for (int i = 0; i < ConditionApiList.size(); i++) {
                HashMap<String, String> hs = ConditionApiList.get(i);
                testCondition.conditionapi(hs, requestObject);
            }
            logger.info("TestCore 结束处理用例前置条件-接口子条件-============："+ConditionApiList.size());
            //延时条件
            ArrayList<HashMap<String, String>> ConditionDelayList = GetConditionDelayByObjectIDAndType(SceneCaseID, "scencecase");
            logger.info("TestCore 开始处理用例前置条件-延时子条件-============："+ConditionDelayList.size());
            if (ConditionDelayList.size() > 0) {
                for (int i = 0; i < ConditionDelayList.size(); i++) {
                    HashMap<String, String> hs = ConditionDelayList.get(i);
                    testCondition.conditiondelay(hs, requestObject);
                }
            }
            logger.info("TestCore 完成处理用例前置条件-延时子条件-============：");
        }
//        Long ObjectID = Long.parseLong(requestObject.getCaseid());
//        ArrayList<HashMap<String, String>> testconditionList = GetConditionByPlanIDAndConditionType(ObjectID, "前置条件", "测试用例");
//        if (testconditionList.size() > 0) {
//            long ConditionID = Long.parseLong(testconditionList.get(0).get("id"));
//            ArrayList<HashMap<String, String>> conditionorderList =GetConditionOrderByID(ConditionID);
//            if(conditionorderList.size()>0)
//            {
//                for (HashMap<String, String> conditionorder:conditionorderList) {
//                    long subconditionid=Long.parseLong(conditionorder.get("subconditionid"));
//                    if(conditionorder.get("subconditiontype").equals("接口"))
//                    {
//                        ArrayList<HashMap<String, String>> result= testMysqlHelp.GetApiConditionByID(subconditionid);
//                        if(result.size()>0)
//                        {
//                            logger.info("TestCore 开始处理用例前置条件顺序-API子条件-============：");
//                            testCondition.conditionapi(result.get(0),ConditionID, requestObject,Long.parseLong(requestObject.getTestplanid()));
//                            logger.info("TestCore 完成处理用例前置条件顺序-API子条件-============：");
//                        }
//                    }
//                    if(conditionorder.get("subconditiontype").equals("数据库"))
//                    {
//                        ArrayList<HashMap<String, String>> result= testMysqlHelp.GetDBConditionByID(subconditionid);
//                        if(result.size()>0)
//                        {
//                            logger.info("TestCore 开始处理用例前置条件顺序-数据库子条件-============：");
//                            testCondition.conditiondb(result.get(0),ConditionID, requestObject,Long.parseLong(requestObject.getTestplanid()));
//                            logger.info("TestCore 完成处理用例前置条件顺序-数据库子条件-============：");
//                        }
//                    }
//                    if(conditionorder.get("subconditiontype").equals("脚本"))
//                    {
//                        ArrayList<HashMap<String, String>> result= testMysqlHelp.GetScriptConditionByID(subconditionid);
//                        if(result.size()>0)
//                        {
//                            logger.info("TestCore 开始处理用例前置条件顺序-脚本子条件-============：");
//                            testCondition.conditionscript(result.get(0),ConditionID, requestObject,Long.parseLong(requestObject.getTestplanid()),Long.parseLong(requestObject.getCaseid()));
//                            logger.info("TestCore 完成处理用例前置条件顺序-脚本子条件-============：");
//                        }
//                    }
//                    if(conditionorder.get("subconditiontype").equals("延时"))
//                    {
//                        ArrayList<HashMap<String, String>> result= testMysqlHelp.GetDelayConditionByID(subconditionid);
//                        if(result.size()>0) {
//                            logger.info("TestCore 开始处理用例前置条件顺序-延时子条件-============：");
//                            testCondition.conditiondelay(result.get(0),ConditionID, requestObject,Long.parseLong(requestObject.getTestplanid()));
//                            logger.info("TestCore 完成处理用例前置条件顺序-延时子条件-============：");
//                        }
//                    }
//                }
//            }
//            else
//            {
//                //处理数据库条件
//                logger.info("TestCore 开始处理用例前置条件-数据库子条件-============：");
//                testCondition.DBCondition(ConditionID, requestObject);
//                logger.info("TestCore 完成处理用例前置条件-数据库条件-============：");
//                //处理接口条件
//                logger.info("TestCore 开始处理用例前置条件-API子条件-============：");
//                testCondition.APICondition(ConditionID, requestObject);
//                logger.info("TestCore 完成处理用例前置条件-API子条件-============：");
//                //处理脚本条件
//                logger.info("TestCore 开始处理用例前置条件-脚本子条件-============：");
//                testCondition.ScriptCondition(ConditionID, requestObject);
//                logger.info("TestCore 完成处理用例前置条件-脚本子条件-============：");
//                //处理脚本条件
//                logger.info("TestCore 开始处理用例前置条件-延时子条件-============：");
//                testCondition.DelayCondition(ConditionID, requestObject);
//                logger.info("TestCore 完成处理用例前置条件-延时子条件-============：");
//            }
//        }
    }


    //处理场景条件入口
    public void FixSceneCondition(RequestObject requestObject) throws Exception {
        ArrayList<HashMap<String, String>> ConditionApiList = GetConditionApiByObjectIDAndType(requestObject.getSceneid(), "scence");
        for (int i = 0; i < ConditionApiList.size(); i++) {
            HashMap<String, String> hs = ConditionApiList.get(i);
            testCondition.conditionapi(hs, requestObject);
        }
    }

    // 发送http请求
    public TestResponeData request(RequestObject requestObject) throws Exception {
        TestResponeData result = testHttp.doService(requestObject, 30000);
        logger.info("用例:" + requestObject.getCasename() + " doService完成-============================================================：");
        String ResponeContentType = "application/json;charset=utf-8";
        List<Header> responeheaderlist = Arrays.asList(result.getHeaderarray());
        for (Header head : responeheaderlist) {
            if (head.getName().equalsIgnoreCase("Content-Type")) {
                ResponeContentType = head.getValue();
            }
        }
        requestObject.setResponecontenttype(ResponeContentType);
//        testCondition.FixInterfaceVariables(requestObject,Long.parseLong(requestObject.getCaseid()),result,result.getResponeContent(),Long.parseLong(requestObject.getTestplanid()),requestObject.getTestplanname(),requestObject.getBatchname());
//        logger.info("用例:"+requestObject.getCasename()+" 处理变量完成-============================================================：");
        return result;
    }

    //断言
    public String FixAssert(TestAssert TestAssert, List<ApicasesAssert> apicasesAssertList, TestResponeData responeData) throws Exception {
        String AssertInfo = "";
        for (ApicasesAssert apicasesAssert : apicasesAssertList) {

            if (apicasesAssert.getAsserttype().equalsIgnoreCase("Respone")) {
                AssertInfo = TestAssert.ParseResponeResult(responeData, apicasesAssert);
            }
            if (apicasesAssert.getAsserttype().equalsIgnoreCase("Json")) {
                AssertInfo = TestAssert.ParseJsonResult(responeData, apicasesAssert);
            }
            if (apicasesAssert.getAsserttype().equalsIgnoreCase("Xml")) {
                AssertInfo = TestAssert.ParseXmlResult(responeData, apicasesAssert);
            }
        }
        return AssertInfo;
    }

    //发送邮件
    public void SendMailByFinishPlanCase(String PlanID, String BatchName) {
        try {
            ArrayList<HashMap<String, String>> dicNameValueWithCode = findDicNameValueWithCode("Mail");
            if (dicNameValueWithCode.size() > 0) {
                String MailInfo = dicNameValueWithCode.get(0).get("dicitmevalue");
                String[] MailArray = MailInfo.split(",");
                if (MailArray.length > 4) {
                    String Smtp = MailArray[0];
                    int port = Integer.parseInt(MailArray[1]);
                    String from = MailArray[2];
                    String mailuser = MailArray[3];
                    String pass = MailArray[4];

                    MailAccount account = new MailAccount();
                    account.setHost(Smtp);
                    account.setPort(port);
                    account.setAuth(true);
                    account.setFrom(from);
                    account.setUser(mailuser);
                    account.setPass(pass);

                    ArrayList<HashMap<String, String>> list = GetplanBatchCreator(PlanID, BatchName);
                    if (list.size() > 0) {
                        String PlanName = list.get(0).get("executeplanname");
                        String Creator = list.get(0).get("creator");
                        ArrayList<HashMap<String, String>> listaccount = findWithUsername(Creator);
                        if (listaccount.size() > 0) {
                            String mailto = listaccount.get(0).get("email");
                            String Subject = PlanName + "|" + BatchName + " 执行完成！";
                            ArrayList<HashMap<String, String>> liststatics = GetStatic(PlanID, BatchName);
                            long tc = 0;
                            long tpc = 0;
                            long tfc = 0;
                            if (liststatics.size() > 0) {
                                tc = Long.parseLong(liststatics.get(0).get("tc"));
                                tpc = Long.parseLong(liststatics.get(0).get("tpc"));
                                tfc = Long.parseLong(liststatics.get(0).get("tfc"));
                            }
                            String Content = "测试集合运行完成结果总计用例数：" + tc + "， 成功数：" + tpc + "， 失败数：" + tfc;
                            MailUtil.send(account, CollUtil.newArrayList(mailto), Subject, Content, false);
                            logger.info("TestCore 发送邮件成功-============：" + mailto);
                        }
                    }
                }
            } else {
                logger.info("TestCore发送邮件未找到字典表邮件配置信息-============：");
            }
        } catch (Exception ex) {
            logger.info("发送邮件异常-============：" + ex.getMessage());
        }
    }

    private String GetSendContent(String PlanID, String BatchName) {
        String Content = "";
        ArrayList<HashMap<String, String>> list = GetplanBatchCreator(PlanID, BatchName);
        if (list.size() > 0) {
            String PlanName = list.get(0).get("executeplanname");
            String Subject = "测试集合：" + PlanName + " |  执行计划：" + BatchName + " 完成！";
            ArrayList<HashMap<String, String>> liststatics = GetStatic(PlanID, BatchName);
            long tc = 0;
            long tpc = 0;
            long tfc = 0;
            if (liststatics.size() > 0) {
                tc = Long.parseLong(liststatics.get(0).get("tc"));
                tpc = Long.parseLong(liststatics.get(0).get("tpc"));
                tfc = Long.parseLong(liststatics.get(0).get("tfc"));
            }
            Content = Subject + "-------------------------------------------------总计用例数：" + tc + "， 成功数：" + tpc + "， 失败数：" + tfc + " ，请登陆AutoMeter-报告中心查看详情";
        }
        return Content;
    }

    //发送钉钉
    public void SendMessageDingDing(String PlanID, String BatchName) {
        try {
            String MessageContent = GetSendContent(PlanID, BatchName);
            String Token = "";
            //先获取测试集合是否配置了钉钉token
            ArrayList<HashMap<String, String>> list = Getplan(PlanID);
            if (list.size() > 0) {
                Token = list.get(0).get("dingdingtoken");
                logger.info("测试集合中的token：-============：" + Token);
                //如果计划中的token位空，则找字典表中的token
                if (Token.isEmpty() || Token == null) {
                    ArrayList<HashMap<String, String>> dicNameValueWithCode = findDicNameValueWithCode("DingDing");
                    if (dicNameValueWithCode.size() > 0) {
                        Token = dicNameValueWithCode.get(0).get("dicitmevalue");
                        logger.info("字典中的钉钉的token：-============：" + Token);
                        DingdingPost(MessageContent, Token);
                        logger.info("字典中的钉钉的token：-============：发送完成");
                    } else {
                        logger.info("发送钉钉信息未找到字典表钉钉配置token,取消发送钉钉：-============：");
                    }
                } else {
                    DingdingPost(MessageContent, Token);
                    logger.info("测试集合中的钉钉的token：-============：发送完成");
                }
            }
        } catch (Exception ex) {
            logger.info("发送钉钉信息异常：-============：" + ex.getMessage());
        }
    }

    private void DingdingPost(String Content, String Token) {
        //消息内容
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", "AutoMeter自动化测试平台执行" + Content);
        //通知人
        Map<String, Object> atMap = new HashMap<>();
        ;
        //1.是否通知所有人
        atMap.put("isAtAll", true);
        Map<String, Object> reqMap = new HashMap<>(); //Maps.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        String RequestContent = JSON.toJSONString(reqMap);
        String Respone = HttpRequest.post(Token).body(RequestContent).timeout(10000).execute().body();
        logger.info("发送钉钉信息响应：-============：" + Respone);
    }

    //获取计划批次的数据统计
    public ArrayList<HashMap<String, String>> GetStatic(String planid, String Batchname) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.GetStatic(planid, Batchname);
        return list;
    }


    //获取账号数据
    public ArrayList<HashMap<String, String>> findWithUsername(String username) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.findWithUsername(username);
        return list;
    }


    //获取数据库用例相关数据
    public ArrayList<HashMap<String, String>> findDicNameValueWithCode(String DicCode) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.findDicNameValueWithCode(DicCode);
        return list;
    }

    //获取测试集合
    public ArrayList<HashMap<String, String>> Getplan(String planid) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.Getplan(planid);
        return list;
    }

    //获取计划批次
    public ArrayList<HashMap<String, String>> GetplanBatchCreator(String planid, String BatchName) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.GetplanBatchCreator(planid, BatchName);
        return list;
    }

    //获取数据库用例相关数据
    public ArrayList<HashMap<String, String>> getcaseData(String Sql) {
        ArrayList<HashMap<String, String>> list = testMysqlHelp.getcaseData(Sql);
        return list;
    }

    // 获取用例期望值
    public String getcaseValue(String key, ArrayList<HashMap<String, String>> list) {
        String value = testMysqlHelp.getcaseValue(key, list);
        return value;
    }


    //获取变量值类型
//    private String GetVariablesDataType(String VariablesName) {
//        String ValueType=testMysqlHelp.GetVariablesDataType(VariablesName);
//        return ValueType;
//    }
//
//    //根据变量名获取caseid
//    private String GetCaseIdByVariablesName(String VariablesName,String Caseid) {
//        String CaseID=testMysqlHelp.GetCaseIdByVariablesName(VariablesName);
//        return CaseID;
//    }
//
//    //获取变量值
//    private String GetVariablesValues(String PlanID, String TestCaseId, String BatchName, String VariablesName) {
//        String VariablesResult=testMysqlHelp.GetVariablesValues(PlanID,TestCaseId,BatchName,VariablesName);
//        return VariablesResult;
//    }


    //获取条件
    private ArrayList<HashMap<String, String>> GetConditionByPlanIDAndConditionType(Long Caseid, String ConditionType, String ObjectType) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetConditionByPlanIDAndConditionType(Caseid, ConditionType, ObjectType);
        return result;
    }


    //获取场景用例id
    private ArrayList<HashMap<String, String>> GetSceneID(Long Caseid, Long Sceneid) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetSceneID(Caseid, Sceneid);
        return result;
    }

    //获取场景用例id
    private ArrayList<HashMap<String, String>> GetSceneCaseByID(Long Sceneid) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetSceneCaseByID(Sceneid);
        return result;
    }


    //根据目标id和类型获取接口条件
    private ArrayList<HashMap<String, String>> GetConditionDelayByObjectIDAndType(Long Objectid, String Objecttype) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetConditionDelayByObjectIDAndType(Objectid, Objecttype);
        return result;
    }

    //根据目标id和类型获取接口条件
    private ArrayList<HashMap<String, String>> GetConditionApiByObjectIDAndType(Long Objectid, String Objecttype) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetConditionApiByObjectIDAndType(Objectid, Objecttype);
        return result;
    }

    //根据目标id和类型获取接口条件
    private ArrayList<HashMap<String, String>> GetConditionApiByCaseIDAndType(Long caseid, String Objecttype) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetConditionApiByCaseIDAndType(caseid, Objecttype);
        return result;
    }

    //获取条件顺序
    private ArrayList<HashMap<String, String>> GetConditionOrderByID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetConditionOrderByID(ConditionID);
        return result;
    }

    //获取接口条件
    private ArrayList<HashMap<String, String>> GetApiConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetApiConditionByConditionID(ConditionID);
        return result;
    }

    //获取脚本条件
    private ArrayList<HashMap<String, String>> GetScriptConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetScriptConditionByConditionID(ConditionID);
        return result;
    }

    //获取数据库条件
    private ArrayList<HashMap<String, String>> GetDBConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetDBConditionByConditionID(ConditionID);
        return result;
    }


    //保存条件结果
    public void SubConditionReportSave(TestconditionReport testconditionReport) {
        testMysqlHelp.SubConditionReportSave(testconditionReport);
    }

    //保存变量结果
    public void testVariablesValueSave(TestvariablesValue testvariablesValue) {
        testMysqlHelp.testVariablesValueSave(testvariablesValue);
    }

    //查询用例变量
    public ArrayList<HashMap<String, String>> GetApiCaseVaribales(Long CaseID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetApiCaseVaribales(CaseID);
        return result;
    }

    //查询变量
    public ArrayList<HashMap<String, String>> GetVaribales(String VaribaleID) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetVaribales(VaribaleID);
        return result;
    }

    // 获取用例Header，params，Body，Dubbo数据
    public HashMap<String, String> fixhttprequestdatas(String MapType, ArrayList<HashMap<String, String>> casedatalist) {
        HashMap<String, String> DataMap = testMysqlHelp.fixhttprequestdatas(MapType, casedatalist);
        return DataMap;
    }

    public HashMap<String, String> getparamsdatabytype(String MapType, ArrayList<HashMap<String, String>> casedatalist) {
        HashMap<String, String> DataMap = testMysqlHelp.getparamsdatabytype(MapType, casedatalist);
        return DataMap;
    }

    // 记录用例测试结果
    public int savetestcaseresult(boolean status, long time, String respone, String assertvalue, String errorinfo, RequestObject requestObject, JavaSamplerContext context) {
        int key = testMysqlHelp.savetestcaseresult(status, time, respone, assertvalue, errorinfo, requestObject, context);
        return key;
    }

    // 记录用例测试结果
    public void savetestcaseextresult(String reportid, String responeext, String projectid) {
        testMysqlHelp.savetestcaseextresult(reportid, responeext, projectid);
    }

    // 记录用例测试结果
    public void SaveReportStatics(ApicasesReportstatics apicasesReportstatics) {
        testMysqlHelp.SaveReportStatics(apicasesReportstatics);
    }


    //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
    public long PlanBatchAllDipatchFinish(String Testplanid, String batchname) {
        long DispatchNotFinishNums = testMysqlHelp.PlanBatchAllDipatchFinish(Testplanid, batchname);
        return DispatchNotFinishNums;
    }

    //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
    public long PlanBatchAllDipatchCancel(String Testplanid, String batchname) {
        long DispatchCancel = testMysqlHelp.PlanBatchAllDipatchCancel(Testplanid, batchname);
        return DispatchCancel;
    }

    // 根据id获取场景
    public ArrayList<HashMap<String, String>> GetSceneByid(String Sceneid) {
        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetSceneByID(Sceneid);
        return result;
    }

    // 更新计划批次场景状态
    public void UpdateBatchScene(String planid, String batchname, String sceneid, String status) {
        testMysqlHelp.UpdateBatchScene(planid, batchname, sceneid, status);
    }

    // 更新计划批次状态
    public void UpdateReportStatics(String Planid, String BatchName, String status) {
        testMysqlHelp.UpdateReportStatics(Planid, BatchName, status);
    }

    // 更新Slaver状态
    public void UpdateSlaverStatus(String Slaverid, String status) throws Exception {
        testMysqlHelp.UpdateSlaverStatus(Slaverid, status);
    }

    // 更新用例调度结果
    public void updatedispatchcasestatus(String testplanid, String caseid, String slaverid, String sceneid, String batchname) {
        testMysqlHelp.updatedispatchcasestatus(testplanid, caseid, slaverid, sceneid, batchname);
    }

    // 更新用例调度结果
    public void generalperformancelogfile(String testplanid, String caseid, String slaverid, String batchid, String Filename, String status) {
        testMysqlHelp.generalperformancelogfile(testplanid, caseid, slaverid, batchid, Filename, status);
    }

    //生成性能报告目录
    public void genealperformacestaticsreport(String testclass, String batchname, String testplanid, String batchid, String slaverid, String caseid, String casereportfolder, double costtime, String Creator) throws Exception {
        testMysqlHelp.genealperformacestaticsreport(testclass, batchname, testplanid, batchid, slaverid, caseid, casereportfolder, costtime, Creator);
    }
}
