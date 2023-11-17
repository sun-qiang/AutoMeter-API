package com.api.autotest.core;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.autotest.common.utils.DnamicCompilerHelp;
import com.api.autotest.common.utils.PgsqlConnectionUtils;
import com.api.autotest.dto.RequestObject;
import com.api.autotest.dto.TestResponeData;
import com.api.autotest.dto.TestconditionReport;
import com.api.autotest.dto.TestvariablesValue;
import org.apache.http.Header;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static com.api.autotest.core.TestCaseData.logplannameandcasename;


public class TestCondition {

    private Logger logger = null;
    private JavaSamplerContext ctx = null;
    private SampleResult results = null;

    TestMysqlHelp testMysqlHelp = null;
    private TestCaseData testCaseData = null;
    private TestHttpRequestData testHttpRequestData = null;
    private TestHttp testHttp = null;

    public TestCondition(Logger log, SampleResult result, JavaSamplerContext context, TestMysqlHelp mysqlHelp, TestCaseData casedata, TestHttpRequestData httprequestdata, TestHttp ttHttp) {
        testMysqlHelp = mysqlHelp;
        testCaseData = casedata;
        testHttpRequestData = httprequestdata;
        testHttp = ttHttp;
        logger = log;
        ctx = context;
        results = result;
    }


    //接口子条件处理
    public void conditionapi(HashMap<String, String> conditionApi, RequestObject requestObject) throws Exception {
        long PlanID = Long.parseLong(requestObject.getTestplanid());
        String Batchname = requestObject.getBatchname();
        String CondionCaseID = conditionApi.get("caseid");
        requestObject.setCasename(conditionApi.get("casename"));
        String Respone = "";
        TestResponeData responeData = new TestResponeData();
        //查看条件是否已经保存过，有则返回，无则执行
        ArrayList<HashMap<String, String>> ApiConditionReportList = testMysqlHelp.Gettestconditionreport(PlanID, Batchname, Long.parseLong(conditionApi.get("id")), "接口");
        if (ApiConditionReportList.size() == 0)
//        ArrayList<HashMap<String, String>> result = testMysqlHelp.GetReportByPBST(PlanID, Batchname, Long.parseLong(CondionCaseID), Sceneid);
//        if (result.size() > 0) {
//            try {
//                //条件已经执行过，则将用例结果保存为前置结果
//                String Reportid = result.get(0).get("id");
//                ArrayList<HashMap<String, String>> resultex = testMysqlHelp.GetReportEXByReportid(Long.parseLong(Reportid));
//                //将报告扩展信息还原成TestResponeData
//                String ReportexInfo = resultex.get(0).get("responeinfo");
//                logger.info("111111111111111111111111111111111111111111111111111111111111111-============：" + ReportexInfo);
//                responeData = JSON.parseObject(ReportexInfo, TestResponeData.class);
//                String ResponeContentType = "application/json;charset=utf-8";
//                Header[] responeheaderArray = responeData.getHeaderarray();
////                for (Header head : responeheaderArray) {
////                    logger.info("44444444444444444444444444444444444444444444444444444444444444444444444444444444444-============：" );
////                    if (head.getName().equalsIgnoreCase("Content-Type")) {
////                        ResponeContentType = head.getValue();
////                        logger.info("555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555-============：" );
////                    }
////                }
//                requestObject.setResponecontenttype(ResponeContentType);
//                Respone = result.get(0).get("respone");
//                String status = result.get(0).get("status");
//                Long runtime = Long.parseLong(result.get(0).get("runtime"));
//                TestconditionReport testconditionReport = new TestconditionReport();
//                testconditionReport.setTestplanid(PlanID);
//                testconditionReport.setPlanname(requestObject.getTestplanname());
//                testconditionReport.setBatchname(Batchname);
//                testconditionReport.setConditiontype("前置条件");
//                testconditionReport.setConditionresult(Respone);
//                testconditionReport.setConditionstatus(status);
//                testconditionReport.setRuntime(runtime);
//                testconditionReport.setSubconditionid(Long.parseLong(conditionApi.get("id")));
//                testconditionReport.setSubconditionname(conditionApi.get("subconditionname"));
//                testconditionReport.setSubconditiontype("接口");
//                testconditionReport.setStatus("已完成");
//                logger.info("TestCondition条件报告保存子条件已完成状态-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname() + "|" + requestObject.getCasename());
//                //增加判断是否已经存在
//                testMysqlHelp.SubConditionReportSave(testconditionReport);
//            } catch (Exception ex) {
//                logger.info("111111111111111111111111111111111111111111111111111111111111111 xxxxxxxxxxxxxxxxxxxxxxx-============：" + ex.getMessage());
//            }
//
//        } else  //条件未执行
        {
            long Start = 0;
            long End = 0;
            long CostTime = 0;
            String ConditionResultStatus = "成功";
            RequestObject re = new RequestObject();
            //String ConditionCaseName="";
            try {
                //ConditionCaseName = conditionApi.get("casename");
                Start = new Date().getTime();
                re = testCaseData.GetCaseRequestData(requestObject.getTestplanid(), CondionCaseID, requestObject.getSlaverid(), requestObject.getBatchname(), requestObject.getTestplanname(), requestObject.getSceneid().toString(), requestObject.getScenename());
                re = testHttpRequestData.GetFuntionHttpRequestData(re);
                End = new Date().getTime();
                responeData = testHttp.doService(re, 30000);
                Respone = responeData.getResponeContent();

                String ResponeContentType = "application/json;charset=utf-8";
                Header[] responeheaderArray = responeData.getHeaderarray();
                for (Header head : responeheaderArray) {
                    if (head.getName().equalsIgnoreCase("Content-Type")) {
                        ResponeContentType = head.getValue();
                    }
                }
                requestObject.setResponecontenttype(ResponeContentType);
                re.setResponecontenttype(ResponeContentType);
                CostTime = End - Start;
                SaveApiSubCondition(re, responeData, conditionApi, Respone, ConditionResultStatus, CostTime);
            } catch (Exception ex) {
                ConditionResultStatus = "失败";
                End = new Date().getTime();
                Respone = ex.getMessage();
                CostTime = End - Start;
                SaveApiSubCondition(re, responeData, conditionApi, Respone, ConditionResultStatus, CostTime);
                throw new Exception("接口子条件执行异常：" + ex.getMessage());
            }
        }
        //根据用例是否有中间变量(多个)，如果有变量，解析（header,cookies,json，xml，html）保存变量值表，没有变量直接保存条件结果表
        FixInterfaceVariables(requestObject, Long.parseLong(CondionCaseID), responeData, Respone, PlanID, requestObject.getTestplanname(), Batchname);
    }


    //接口条件入口
    public void APICondition(long ConditionID, RequestObject requestObject) throws Exception {
        ArrayList<HashMap<String, String>> conditionApiList = testMysqlHelp.GetApiConditionByConditionID(ConditionID);
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        logger.info("TestCondition条件报告API子条件数量-============：" + conditionApiList.size());
        for (HashMap<String, String> conditionApi : conditionApiList) {
            String ConditionCaseType = conditionApi.get("runtype");
            //只处理集合外接口子条件
            if (ConditionCaseType.equalsIgnoreCase("测试集合外")) {
                //集合外
                //conditionapi(conditionApi,ConditionID,requestObject,PlanID);
            } else {
                //集合内
                String planid = requestObject.getTestplanid();
                String BatchName = requestObject.getBatchname();
                //根据当前用例获取接口子条件用例id，再根据集合id，批次id，用例id到报告中获取用例的响应结果
                String CondionCaseID = conditionApi.get("caseid");
                String ConditionCaseName = conditionApi.get("casename");

                ArrayList<HashMap<String, String>> ApiCaseReportList = testMysqlHelp.GetApiCaseReportByPBCID(planid, BatchName, CondionCaseID);
                for (HashMap<String, String> apicaserep : ApiCaseReportList) {
                    String Reportid = apicaserep.get("id");
                    String respone = apicaserep.get("respone");
                    String status = apicaserep.get("status");
                    Long runtime = Long.parseLong(apicaserep.get("runtime"));

                    TestconditionReport testconditionReport = new TestconditionReport();
                    testconditionReport.setTestplanid(PlanID);
                    testconditionReport.setPlanname(ConditionCaseName);
                    testconditionReport.setBatchname(BatchName);
                    testconditionReport.setConditionid(new Long(ConditionID));
                    testconditionReport.setConditiontype("前置条件");
                    testconditionReport.setConditionresult(respone);
                    testconditionReport.setConditionstatus(status);
                    testconditionReport.setRuntime(runtime);
                    testconditionReport.setSubconditionid(Long.parseLong(conditionApi.get("id")));
                    testconditionReport.setSubconditionname(conditionApi.get("subconditionname"));
                    testconditionReport.setSubconditiontype("接口");
                    testconditionReport.setStatus("已完成");
                    logger.info("TestCondition条件报告保存子条件已完成状态-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname() + "|" + requestObject.getCasename());
                    testMysqlHelp.SubConditionReportSave(testconditionReport);
                    //从响应中提取变量值
                    ArrayList<HashMap<String, String>> ApiCaseReportExtList = testMysqlHelp.GetApiCaseReportExtByID(Reportid);
                    for (HashMap<String, String> apicasereportext : ApiCaseReportExtList) {
                        String ExtInfo = apicasereportext.get("responeinfo");
                        TestResponeData testResponeData = (TestResponeData) JSONObject.parse(ExtInfo);
                    }
                }
            }
//            long Start = 0;
//            long End = 0;
//            long CostTime = 0;
//            String Respone = "";
//            String ConditionResultStatus = "成功";
//            RequestObject re = new RequestObject();
//            String CondionCaseID = "";
//            try {
//                CondionCaseID = conditionApi.get("caseid");
//                Start = new Date().getTime();
//                re = testCaseData.GetCaseRequestData(requestObject.getTestplanid(), CondionCaseID, requestObject.getSlaverid(), requestObject.getBatchid(), requestObject.getBatchname(), requestObject.getTestplanname());
//                re = testHttpRequestData.GetFuntionHttpRequestData(re);
//                End = new Date().getTime();
//                TestResponeData responeData = testHttp.doService(re,30000);
//                Respone = responeData.getResponeContent();
//
//                String ResponeContentType = "application/json;charset=utf-8";
//                List<Header> responeheaderlist = responeData.getHeaderList();
//                for (Header head : responeheaderlist) {
//                    if (head.getName().equalsIgnoreCase("Content-Type")) {
//                        ResponeContentType = head.getValue();
//                    }
//                }
//                requestObject.setResponecontenttype(ResponeContentType);
//                CostTime = End - Start;
//                SaveApiSubCondition(re, requestObject.getCasename(), PlanID, requestObject.getTestplanname(), requestObject.getBatchname(), Long.parseLong(CondionCaseID), ConditionID, conditionApi, Respone, ConditionResultStatus, CostTime);
//            } catch (Exception ex) {
//                ConditionResultStatus = "失败";
//                End = new Date().getTime();
//                Respone = ex.getMessage();
//                CostTime = End - Start;
//                SaveApiSubCondition(re, requestObject.getCasename(), PlanID, requestObject.getTestplanname(), requestObject.getBatchname(), Long.parseLong(CondionCaseID), ConditionID, conditionApi, Respone, ConditionResultStatus, CostTime);
//                throw new Exception("接口子条件执行异常：" + ex.getMessage());
//            }
        }
    }

    private void SaveApiSubCondition(RequestObject requestObject, TestResponeData testResponeData, HashMap<String, String> conditionApi, String Respone, String ConditionResultStatus, long CostTime) throws Exception {
        Long CaseID = Long.parseLong(conditionApi.get("caseid"));
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        String BatchName = requestObject.getBatchname();
        TestconditionReport testconditionReport = new TestconditionReport();
        testconditionReport.setTestplanid(PlanID);
        testconditionReport.setPlanname(requestObject.getTestplanname());
        testconditionReport.setBatchname(BatchName);
        testconditionReport.setConditiontype("前置条件");
        testconditionReport.setConditionresult(Respone);
        testconditionReport.setConditionstatus(ConditionResultStatus);
        testconditionReport.setRuntime(CostTime);
        testconditionReport.setSubconditionid(Long.parseLong(conditionApi.get("id")));
        testconditionReport.setSubconditionname(conditionApi.get("subconditionname"));
        testconditionReport.setSubconditiontype("接口");
        testconditionReport.setStatus("已完成");
        logger.info("TestCondition条件报告保存子条件已完成状态-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname() + "|" + requestObject.getCasename());
        //增加判断是否已经存在
        testMysqlHelp.SubConditionReportSave(testconditionReport);
        //判断此前置用例在测试场景中是否存在，如果存在则保存报告表，如果不存在则不保存报告表
        Long Sceneid = requestObject.getSceneid();
        ArrayList<HashMap<String, String>> sceneCaseList = testMysqlHelp.GetSceneID(CaseID, Sceneid);
        if (sceneCaseList.size() > 0) {
            //保存报告表
            TestCore core = new TestCore(ctx, logger, results);
            core.FixCaseData(requestObject, ctx, results, false, testResponeData);
        }
    }

    public void FixInterfaceVariables(RequestObject requestObject, Long CaseID, TestResponeData testResponeData, String Respone, Long PlanID, String PlanName, String BatchName) {
        ArrayList<HashMap<String, String>> apicasesVariablesList = testMysqlHelp.GetApiCaseVaribales(CaseID);
        if (apicasesVariablesList.size() > 0) {
            for (HashMap<String, String> map : apicasesVariablesList) {
                String Variablesid = map.get("id");
                String variablesname = map.get("testvariablesname");
                String VariablesPath = map.get("variablesexpress");
                String VariablesResoruce = map.get("testvariablestype");
                logger.info("TestCondition条件报告子条件处理变量-============：" + variablesname + " Respone:" + Respone);
//                        if (VariablesList.size() > 0) {
                String ParseValue = "";
                logger.info("TestCondition条件报告子条件处理变量表达式-============：" + VariablesPath + " 响应数据类型:" + requestObject.getResponecontenttype());
                TestAssert testAssert = new TestAssert(logger);
                switch (VariablesResoruce) {
                    case "Body":
                        ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
                        break;
                    case "Header":
                        ParseValue = testAssert.ParseHeader(testResponeData, VariablesPath);
                        break;
                    case "Cookies":
                        ParseValue = testAssert.ParseCookies(testResponeData, VariablesPath);
                        break;
                    default:
                        ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
                }
                //String ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
                ArrayList<HashMap<String, String>> testVariablesValueList = testMysqlHelp.GetTestVariablesValue(PlanID, BatchName, Long.parseLong(Variablesid));
                if (testVariablesValueList.size() > 0) {
                    testMysqlHelp.testVariablesValueUpdate(PlanID, BatchName, Long.parseLong(Variablesid), ParseValue);
                    logger.info("TestCondition条件报告子条件变量值已存在则更新：-============：" + ParseValue);
                } else {
                    TestvariablesValue testvariablesValue = new TestvariablesValue();
                    testvariablesValue.setPlanid(PlanID);
                    testvariablesValue.setPlanname(PlanName);
                    testvariablesValue.setBatchname(BatchName);
                    testvariablesValue.setCaseid(CaseID);
                    testvariablesValue.setVariablestype("接口");
                    testvariablesValue.setCasename(requestObject.getCasename());
                    testvariablesValue.setVariablesid(Long.parseLong(Variablesid));
                    testvariablesValue.setVariablesname(variablesname);
                    testvariablesValue.setVariablesvalue(ParseValue);
                    testvariablesValue.setMemo("test");
                    testvariablesValue.setSlaverid(Long.parseLong(requestObject.getSlaverid()));
                    //增加判断是否已经存在
                    testMysqlHelp.testVariablesValueSave(testvariablesValue);
                    logger.info("TestCondition条件报告子条件处理变量不存在则新增-============：" + ParseValue);
                }
                //}
            }
//                for (String Key : map.keySet()) {
//                    if (Key.equalsIgnoreCase("variablesid")) {
//                        logger.info("TestCondition条件报告子条件处理变量-============：" + map.get("variablesname"));
//                        String Variablesid = map.get(Key);
//                        String VariablesPath = map.get("variablesexpress");
//                        String VariablesResoruce = map.get("testvariablestype");
////                        if (VariablesList.size() > 0) {
//                        String ParseValue = "";
//                        logger.info("TestCondition条件报告子条件处理变量表达式-============：" + VariablesPath + " 响应数据类型" + requestObject.getResponecontenttype());
//                        TestAssert testAssert = new TestAssert(logger);
//                        switch (VariablesResoruce) {
//                            case "Body":
//                                ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
//                                break;
//                            case "Header":
//                                ParseValue = testAssert.ParseHeader(testResponeData, VariablesPath);
//                                break;
//                            case "Cookies":
//                                ParseValue = testAssert.ParseCookies(testResponeData, VariablesPath);
//                                break;
//                            default:
//                                ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
//                        }
//                        //String ParseValue = testAssert.ParseRespone(requestObject.getResponecontenttype(), VariablesPath, Respone);
//                        logger.info("TestCondition条件报告子条件处理变量取值-============：" + ParseValue);
//                        TestvariablesValue testvariablesValue = new TestvariablesValue();
//                        testvariablesValue.setPlanid(PlanID);
//                        testvariablesValue.setPlanname(PlanName);
//                        testvariablesValue.setBatchname(BatchName);
//                        testvariablesValue.setCaseid(CaseID);
//                        testvariablesValue.setVariablestype("接口");
//                        testvariablesValue.setCasename(requestObject.getCasename());
//                        testvariablesValue.setVariablesid(Long.parseLong(Variablesid));
//                        testvariablesValue.setVariablesname(map.get("variablesname"));
//                        testvariablesValue.setVariablesvalue(ParseValue);
//                        testvariablesValue.setMemo("test");
//                        //增加判断是否已经存在
//                        testMysqlHelp.testVariablesValueSave(testvariablesValue);
//                        //}
//                    }
//                }
        }
    }

    //处理脚本子条件
    public void conditionscript(HashMap<String, String> conditionScript, long ConditionID, RequestObject requestObject, Long PlanID, Long CaseID) throws Exception {
        long Start = 0;
        long End = 0;
        long CostTime = 0;
        String Respone = "执行脚本成功";
        String ConditionResultStatus = "成功";
        try {
            Start = new Date().getTime();
            DnamicCompilerHelp dnamicCompilerHelp = new DnamicCompilerHelp();
            //数据库中获取脚本
            String JavaSource = conditionScript.get("script");
            logger.info("TestCondition条件报告脚本子条件:-============：" + JavaSource);
            String Source = dnamicCompilerHelp.GetCompeleteClass(JavaSource, CaseID);
            dnamicCompilerHelp.CallDynamicScript(Source);
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Respone, ConditionResultStatus, CostTime);
        } catch (Exception ex) {
            ConditionResultStatus = "失败";
            Respone = ex.getMessage();
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Respone, ConditionResultStatus, CostTime);
            throw new Exception("脚本子条件执行异常：" + ex.getMessage());
        }
    }

    //处理脚本条件入口
    public void ScriptCondition(long ConditionID, RequestObject requestObject) throws Exception {
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        Long CaseID = Long.parseLong(requestObject.getCaseid());
        ArrayList<HashMap<String, String>> conditionScriptList = testMysqlHelp.GetScriptConditionByConditionID(ConditionID);
        for (HashMap<String, String> conditionScript : conditionScriptList) {
            conditionscript(conditionScript, ConditionID, requestObject, PlanID, CaseID);
//            long Start = 0;
//            long End = 0;
//            long CostTime = 0;
//            String Respone = "执行脚本成功";
//            String ConditionResultStatus = "成功";
//            try {
//                Start = new Date().getTime();
//                DnamicCompilerHelp dnamicCompilerHelp = new DnamicCompilerHelp();
//                //数据库中获取脚本
//                String JavaSource = conditionScript.get("script");
//                logger.info("TestCondition条件报告脚本子条件:-============：" + JavaSource);
//                String Source = dnamicCompilerHelp.GetCompeleteClass(JavaSource, CaseID);
//                dnamicCompilerHelp.CallDynamicScript(Source);
//                End = new Date().getTime();
//                CostTime = End - Start;
//                SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Respone, ConditionResultStatus, CostTime);
//            } catch (Exception ex) {
//                ConditionResultStatus = "失败";
//                Respone = ex.getMessage();
//                End = new Date().getTime();
//                CostTime = End - Start;
//                SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Respone, ConditionResultStatus, CostTime);
//                throw new Exception("脚本子条件执行异常：" + ex.getMessage());
//            }
        }
    }


    //延时子条件处理
    public void conditiondelay(HashMap<String, String> conditionDelay, long ConditionID, RequestObject requestObject, Long PlanID) throws Exception {
        long Start = 0;
        long End = 0;
        long CostTime = 0;
        String Respone = "执行延时条件成功";
        String ConditionResultStatus = "成功";
        try {
            Start = new Date().getTime();
            long delaytime = Long.parseLong(conditionDelay.get("delaytime")) * 1000;
            logger.info("TestCondition条件报告延时子条件，延时时间为（毫秒）:-============：" + delaytime);
            Thread.sleep(delaytime);
            Respone = Respone + "（毫秒）:" + delaytime;
            logger.info("TestCondition条件报告延时子条件，延时时间为（毫秒）:-============：" + Respone);
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("延时", requestObject, PlanID, ConditionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
        } catch (Exception ex) {
            ConditionResultStatus = "失败";
            Respone = ex.getMessage();
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("延时", requestObject, PlanID, ConditionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
            throw new Exception("延时子条件执行异常：" + ex.getMessage());
        }
    }

    //延时子条件入口
    public void DelayCondition(long ConditionID, RequestObject requestObject) throws Exception {
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        ArrayList<HashMap<String, String>> conditionDelayList = testMysqlHelp.GetDelayConditionByConditionID(ConditionID);
        for (HashMap<String, String> conditionDelay : conditionDelayList) {
            conditiondelay(conditionDelay, ConditionID, requestObject, PlanID);
//            long Start = 0;
//            long End = 0;
//            long CostTime = 0;
//            String Respone = "执行延时条件成功";
//            String ConditionResultStatus = "成功";
//            try {
//                Start = new Date().getTime();
//                long delaytime = Long.parseLong(conditionDelay.get("delaytime"))*1000;
//                logger.info("TestCondition条件报告延时子条件，延时时间为（毫秒）:-============：" + delaytime);
//                Thread.sleep(delaytime);
//                Respone = Respone + "（毫秒）:" + delaytime;
//                logger.info("TestCondition条件报告延时子条件，延时时间为（毫秒）:-============：" + Respone);
//                End = new Date().getTime();
//                CostTime = End - Start;
//                SaveSubCondition("延时", requestObject, PlanID, ConditionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
//            } catch (Exception ex) {
//                ConditionResultStatus = "失败";
//                Respone = ex.getMessage();
//                End = new Date().getTime();
//                CostTime = End - Start;
//                SaveSubCondition("延时", requestObject, PlanID, ConditionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
//                throw new Exception("延时子条件执行异常：" + ex.getMessage());
//            }
        }
    }


    private void SaveSubCondition(String SubconditionType, RequestObject requestObject, Long PlanID, Long ConditionID, HashMap<String, String> conditionScript, String Respone, String ConditionResultStatus, long CostTime) {
        //更新条件结果表
        TestconditionReport testconditionReport = new TestconditionReport();
        testconditionReport.setTestplanid(PlanID);
        testconditionReport.setPlanname(requestObject.getCasename());
        testconditionReport.setBatchname(requestObject.getBatchname());
        testconditionReport.setConditionid(new Long(ConditionID));
        testconditionReport.setConditiontype("前置条件");
        testconditionReport.setSubconditionid(Long.parseLong(conditionScript.get("id")));
        testconditionReport.setSubconditionname(conditionScript.get("subconditionname"));
        testconditionReport.setSubconditiontype(SubconditionType);
        logger.info("TestCondition " + SubconditionType + "条件报告保存子条件进行中状态-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname());

        testconditionReport.setConditionresult(Respone.replace("'", "''"));
        testconditionReport.setConditionstatus(ConditionResultStatus);
        testconditionReport.setRuntime(CostTime);
        testconditionReport.setStatus("已完成");
        testMysqlHelp.SubConditionReportSave(testconditionReport);
        logger.info("TestCondition " + SubconditionType + "条件报告更新子条件结果-============：" + testconditionReport.getPlanname() + "|" + testconditionReport.getBatchname());

    }


    //处理数据库子条件
    public void conditiondb(HashMap<String, String> conditionDb, long ConditionID, RequestObject requestObject, Long PlanID) throws Exception {
        long Start = 0;
        long End = 0;
        long CostTime = 0;
        String Respone = "";
        String ConditionResultStatus = "成功";
        Long Assembleid = Long.parseLong(conditionDb.get("assembleid"));
        Long DBConditionid = Long.parseLong(conditionDb.get("id"));
        String SqlType = conditionDb.get("dbtype");
        String DBConditionName = conditionDb.get("subconditionname");
        try {
            ArrayList<HashMap<String, String>> enviromentAssemblelist = testMysqlHelp.getcaseData("select * from enviroment_assemble where id=" + Assembleid);
            if (enviromentAssemblelist.size() == 0) {
                Respone = "未找到环境组件，请检查是否存在或已被删除";
                ConditionResultStatus = "失败";
                SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
            }
            String AssembleType = enviromentAssemblelist.get(0).get("assembletype");
            Long Envid = Long.parseLong(conditionDb.get("enviromentid"));
            String Sql = conditionDb.get("dbcontent");
            logger.info(logplannameandcasename + "TestCondition数据库子条件完整的sql ....." + Sql);
            String ConnnectStr = enviromentAssemblelist.get(0).get("connectstr");
            ArrayList<HashMap<String, String>> macdepunitlist = testMysqlHelp.getcaseData("select * from macdepunit where envid=" + Envid + " and assembleid=" + Assembleid);
            if (macdepunitlist.size() == 0) {
                Respone = "未找到环境组件部署，请检查是否存在或已被删除";
                ConditionResultStatus = "失败";
                SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
            }

            Long MachineID = Long.parseLong(macdepunitlist.get(0).get("machineid"));
            ArrayList<HashMap<String, String>> machinelist = testMysqlHelp.getcaseData("select * from machine where id=" + MachineID);
            if (machinelist.size() == 0) {
                Respone = "未找到环境组件部署的服务器，请检查是否存在或已被删除";
                ConditionResultStatus = "失败";
                SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
            }
            String deployunitvisittype = macdepunitlist.get(0).get("visittype");
            String[] ConnetcArray = ConnnectStr.split(",");
            if (ConnetcArray.length < 4) {
                Respone = "数据库连接字填写不规范，请按规则填写";
                ConditionResultStatus = "失败";
                SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
            }
            Long planid = Long.parseLong(requestObject.getTestplanid());
            Start = new Date().getTime();
            Rundb(planid, requestObject.getTestplanname(), requestObject.getBatchname(), DBConditionid, DBConditionName, macdepunitlist, machinelist, ConnetcArray, AssembleType, deployunitvisittype, Sql, SqlType);
        } catch (Exception ex) {
            ConditionResultStatus = "失败";
            Respone = ex.getMessage();
            throw new Exception("数据库子条件执行异常：" + ex.getMessage());
        } finally {
            End = new Date().getTime();
            CostTime = End - Start;
            //更新条件结果表
            SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
        }
    }

    //数据库条件入口
    public void DBCondition(long ConditionID, RequestObject requestObject) throws Exception {
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        ArrayList<HashMap<String, String>> conditionDbListList = testMysqlHelp.GetDBConditionByConditionID(ConditionID);
        for (HashMap<String, String> conditionDb : conditionDbListList) {
            conditiondb(conditionDb, ConditionID, requestObject, PlanID);
//            long Start = 0;
//            long End = 0;
//            long CostTime = 0;
//            String Respone = "";
//            String ConditionResultStatus = "成功";
//            Long Assembleid = Long.parseLong(conditionDb.get("assembleid"));
//            Long DBConditionid = Long.parseLong(conditionDb.get("id"));
//            String SqlType = conditionDb.get("dbtype");
//            String DBConditionName = conditionDb.get("subconditionname");
//            try {
//                ArrayList<HashMap<String, String>> enviromentAssemblelist = testMysqlHelp.getcaseData("select * from enviroment_assemble where id=" + Assembleid);
//                if (enviromentAssemblelist.size() == 0) {
//                    Respone = "未找到环境组件，请检查是否存在或已被删除";
//                    ConditionResultStatus = "失败";
//                    SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
//                    break;
//                }
//                String AssembleType = enviromentAssemblelist.get(0).get("assembletype");
//                Long Envid = Long.parseLong(conditionDb.get("enviromentid"));
//                String Sql = conditionDb.get("dbcontent");
//                logger.info(logplannameandcasename + "TestCondition数据库子条件完整的sql ....." + Sql);
//                String ConnnectStr = enviromentAssemblelist.get(0).get("connectstr");
//                ArrayList<HashMap<String, String>> macdepunitlist = testMysqlHelp.getcaseData("select * from macdepunit where envid=" + Envid + " and assembleid=" + Assembleid);
//                if (macdepunitlist.size() == 0) {
//                    Respone = "未找到环境组件部署，请检查是否存在或已被删除";
//                    ConditionResultStatus = "失败";
//                    SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
//                    break;
//                }
//
//                Long MachineID = Long.parseLong(macdepunitlist.get(0).get("machineid"));
//                ArrayList<HashMap<String, String>> machinelist = testMysqlHelp.getcaseData("select * from machine where id=" + MachineID);
//                if (machinelist.size() == 0) {
//                    Respone = "未找到环境组件部署的服务器，请检查是否存在或已被删除";
//                    ConditionResultStatus = "失败";
//                    SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
//                    break;
//                }
//                String deployunitvisittype = macdepunitlist.get(0).get("visittype");
//                String[] ConnetcArray = ConnnectStr.split(",");
//                if (ConnetcArray.length < 4) {
//                    Respone = "数据库连接字填写不规范，请按规则填写";
//                    ConditionResultStatus = "失败";
//                    SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
//                    break;
//                }
//                Long planid = Long.parseLong(requestObject.getTestplanid());
//                Rundb(planid, requestObject.getTestplanname(), requestObject.getBatchname(), DBConditionid, DBConditionName, macdepunitlist, machinelist, ConnetcArray, AssembleType, deployunitvisittype, Sql, SqlType);
//                Start = new Date().getTime();
//            } catch (Exception ex) {
//                ConditionResultStatus = "失败";
//                Respone = ex.getMessage();
//                throw new Exception("数据库子条件执行异常：" + ex.getMessage());
//            } finally {
//                End = new Date().getTime();
//                CostTime = End - Start;
//                //更新条件结果表
//                SaveSubCondition("数据库", requestObject, PlanID, ConditionID, conditionDb, Respone, ConditionResultStatus, CostTime);
//            }
        }
    }

    //获取数据库连接字
    private String GetDbUrl(String AssembleType, ArrayList<HashMap<String, String>> macdepunitlist, String deployunitvisittype, ArrayList<HashMap<String, String>> machinelist, String dbname, String port) {
        String DBUrl = "";
        if (AssembleType.equalsIgnoreCase("pgsql")) {
            DBUrl = "jdbc:postgresql://";
            // 根据访问方式来确定ip还是域名
            if (deployunitvisittype.equalsIgnoreCase("ip")) {
                String IP = machinelist.get(0).get("ip");
                DBUrl = DBUrl + IP + ":" + port + "/" + dbname;
            } else {
                String Domain = macdepunitlist.get(0).get("domain");
                DBUrl = DBUrl + Domain + "/" + dbname;
            }
        }
        if (AssembleType.equalsIgnoreCase("mysql")) {
            DBUrl = "jdbc:mysql://";
            // 根据访问方式来确定ip还是域名
            if (deployunitvisittype.equalsIgnoreCase("ip")) {
                String IP = machinelist.get(0).get("ip");
                DBUrl = DBUrl + IP + ":" + port + "/" + dbname + "?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC";
            } else {
                String Domain = macdepunitlist.get(0).get("domain");
                DBUrl = DBUrl + Domain + "/" + dbname + "?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC";
            }
        }
        if (AssembleType.equalsIgnoreCase("oracle")) {
            DBUrl = "jdbc:oracle:thin:@";
            // 根据访问方式来确定ip还是域名
            if (deployunitvisittype.equalsIgnoreCase("ip")) {
                String IP = machinelist.get(0).get("ip");
                DBUrl = DBUrl + IP + ":" + port + ":" + dbname;
            } else {
                String Domain = macdepunitlist.get(0).get("domain");
                DBUrl = DBUrl + Domain + ":" + dbname;
            }
        }
        return DBUrl;
    }

    //获取数据库变量值
    private String GetDBResultValueByMap(List<HashMap<String, String>> DbResult, String columnname, long rownum) {
        String Result = "未获得数据库变量值，请确认查询sql是否能正常获取数据，或者列名是否和Sql中匹配";
        for (int i = 0; i < DbResult.size(); i++) {
            if (i == rownum) {
                HashMap<String, String> rowdata = DbResult.get(i);
                for (String Cloumn : rowdata.keySet()) {
                    if (Cloumn.equalsIgnoreCase(columnname)) {
                        Result = rowdata.get(Cloumn);
                    }
                }
            }
        }
        return Result;
    }

    //获取数据库变量值
    private String GetDBResultValueByEntity(List<Entity> DbResult, String columnname, long rownum) {
        String Result = "未获得数据库变量值，请确认查询sql是否能正常获取数据，或者列名是否和Sql中匹配";
        for (int i = 0; i < DbResult.size(); i++) {
            if (i == rownum) {
                Entity row = DbResult.get(i);
                Result = row.getStr(columnname);
            }
        }
        return Result;
    }

    //保存数据库变量值
    private void SaveDBTestVariablesValue(long planid, String planname, String batchname, long Conidtiondbid, String DBConditionName, long variablesid, String Variablesname, String VariablesValue) {
        TestvariablesValue testvariablesValue = new TestvariablesValue();
        testvariablesValue.setPlanid(planid);
        testvariablesValue.setPlanname(planname);
        testvariablesValue.setBatchname(batchname);
        testvariablesValue.setCaseid(Conidtiondbid);
        testvariablesValue.setVariablestype("数据库");
        testvariablesValue.setCasename(DBConditionName);
        testvariablesValue.setVariablesid(variablesid);
        testvariablesValue.setVariablesname(Variablesname);
        testvariablesValue.setVariablesvalue(VariablesValue);
        testvariablesValue.setMemo("test");
        testMysqlHelp.testVariablesValueSave(testvariablesValue);
    }

    private String Rundb(long planid, String planname, String batchname, long Conidtiondbid, String DBConditionName, ArrayList<HashMap<String, String>> macdepunitlist, ArrayList<HashMap<String, String>> machinelist, String[] ConnetcArray, String AssembleType, String deployunitvisittype, String Sql, String SqlType) throws Exception {
        String Respone = "";
        String username = ConnetcArray[0];
        String pass = ConnetcArray[1];
        String port = ConnetcArray[2];
        String dbname = ConnetcArray[3];
        String DBUrl = "";
        if (AssembleType.equalsIgnoreCase("pgsql")) {
            DBUrl = GetDbUrl(AssembleType, macdepunitlist, deployunitvisittype, machinelist, dbname, port);
            PgsqlConnectionUtils.initDbResource(DBUrl, username, pass);
            if (SqlType.equalsIgnoreCase("Select")) {
                //查询语句结果解析到数据库变量中
                // 1.查询数据库条件是否有变量关联
                ArrayList<HashMap<String, String>> dbconditionVariablesList = testMysqlHelp.getbyconditionid(Conidtiondbid);
                if (dbconditionVariablesList.size() > 0) {
                    //2.获取查询结果
                    List<HashMap<String, String>> result = PgsqlConnectionUtils.query(Sql);
                    for (HashMap<String, String> dbconditionVariables : dbconditionVariablesList) {
                        long variablesid = Long.parseLong(dbconditionVariables.get("variablesid"));
                        String Variablesname = dbconditionVariables.get("variablesname");
                        String columnname = dbconditionVariables.get("fieldname");
                        long roworder = Long.parseLong(dbconditionVariables.get("roworder"));
                        if (roworder > 0) {
                            roworder = roworder - 1;
                        }
                        String VariablesValue = GetDBResultValueByMap(result, columnname, roworder);
                        //保存数据库变量
                        Respone = Respone + "成功获取 数据库变量名：" + Variablesname + " 值:" + VariablesValue;
                        SaveDBTestVariablesValue(planid, planname, batchname, Conidtiondbid, DBConditionName, variablesid, Variablesname, VariablesValue);
                    }
                }
            } else {
                String[] SqlArr = Sql.split(";");
                for (String ExecSql : SqlArr) {
                    int nums = PgsqlConnectionUtils.execsql(ExecSql);
                    Respone = Respone + " 成功执行Sql:" + Sql + " 影响条数：" + nums;
                }
            }
        }

        if (AssembleType.equalsIgnoreCase("mysql")) {
            DBUrl = GetDbUrl(AssembleType, macdepunitlist, deployunitvisittype, machinelist, dbname, port);
            Respone = UseHutoolDb(planid, planname, batchname, Conidtiondbid, DBConditionName, SqlType, DBUrl, username, pass, Sql);
        }
        if (AssembleType.equalsIgnoreCase("oracle")) {
            DBUrl = GetDbUrl(AssembleType, macdepunitlist, deployunitvisittype, machinelist, dbname, port);
            Respone = UseHutoolDb(planid, planname, batchname, Conidtiondbid, DBConditionName, SqlType, DBUrl, username, pass, Sql);
        }
        return Respone;
    }

    private String UseHutoolDb(long planid, String planname, String batchname, long Conidtiondbid, String DBConditionName, String SqlType, String DBUrl, String username, String pass, String Sql) throws SQLException {
        String Respone = "";
        DataSource ds = new SimpleDataSource(DBUrl, username, pass);

        if (SqlType.equalsIgnoreCase("Select")) {
            // 1.查询数据库条件是否有变量关联
            ArrayList<HashMap<String, String>> dbconditionVariablesList = testMysqlHelp.getbyconditionid(Conidtiondbid);
            if (dbconditionVariablesList.size() > 0) {
                //2.获取查询结果
                List<Entity> result = Db.use(ds).query(Sql);
                for (HashMap<String, String> dbconditionVariables : dbconditionVariablesList) {
                    long variablesid = Long.parseLong(dbconditionVariables.get("variablesid"));
                    String Variablesname = dbconditionVariables.get("variablesname");
                    String columnname = dbconditionVariables.get("fieldname");
                    long roworder = Long.parseLong(dbconditionVariables.get("roworder"));
                    if (roworder > 0) {
                        roworder = roworder - 1;
                    }
                    String VariablesValue = GetDBResultValueByEntity(result, columnname, roworder);
                    Respone = Respone + "成功获取 数据库变量名：" + Variablesname + " 值:" + VariablesValue;
                    //保存数据库变量
                    SaveDBTestVariablesValue(planid, planname, batchname, Conidtiondbid, DBConditionName, variablesid, Variablesname, VariablesValue);
                }
            }
        } else {
            String[] SqlArr = Sql.split(";");
            for (String ExecSql : SqlArr) {
                int nums = Db.use(ds).execute(ExecSql);
                Respone = Respone + " 成功执行Sql:" + Sql + " 影响条数：" + nums;
            }
        }
        return Respone;
    }

}
