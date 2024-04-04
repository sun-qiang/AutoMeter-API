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
        long ConidtionID =Long.parseLong(conditionApi.get("id"));
        String ConidtionType =conditionApi.get("conditiontype");

        String Batchname = requestObject.getBatchname();
        String CondionCaseID = conditionApi.get("caseid");
        requestObject.setCasename(conditionApi.get("casename"));
        String Respone = "";
        TestResponeData responeData = new TestResponeData();
        //查看条件是否已经保存过，有则返回，无则执行
        ArrayList<HashMap<String, String>> ApiConditionReportList = testMysqlHelp.Gettestconditionreport(PlanID, Batchname, ConidtionID, "接口");
        if (ApiConditionReportList.size() == 0)
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
            //根据用例是否有中间变量(多个)，如果有变量，解析（header,cookies,json，xml，html）保存变量值表，没有变量直接保存条件结果表
            FixInterfaceVariables(requestObject,ConidtionID,ConidtionType, Long.parseLong(CondionCaseID), responeData, Respone, PlanID, requestObject.getTestplanname(), Batchname);
        }
    }
    private void SaveApiSubCondition(RequestObject requestObject, TestResponeData testResponeData, HashMap<String, String> conditionApi, String Respone, String ConditionResultStatus, long CostTime) throws Exception {
        Long CaseID = Long.parseLong(conditionApi.get("caseid"));
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        String BatchName = requestObject.getBatchname();
        TestconditionReport testconditionReport = new TestconditionReport();
        testconditionReport.setTestplanid(PlanID);
        testconditionReport.setPlanname(conditionApi.get("conditionname"));
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
//        Long Sceneid = requestObject.getSceneid();
//        ArrayList<HashMap<String, String>> sceneCaseList = testMysqlHelp.GetSceneID(CaseID, Sceneid);
//        if (sceneCaseList.size() > 0) {
//            //保存报告表
//            TestCore core = new TestCore(ctx, logger, results);
//            core.FixCaseData(requestObject, ctx, results, false, testResponeData);
//        }
    }
    public void FixInterfaceVariables(RequestObject requestObject,Long ConditionID,String ConditionType, Long CaseID, TestResponeData testResponeData, String Respone, Long PlanID, String PlanName, String BatchName) {
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
                ArrayList<HashMap<String, String>> testVariablesValueList = testMysqlHelp.GetTestVariablesValue(PlanID, BatchName, Long.parseLong(Variablesid),ConditionID);
                if (testVariablesValueList.size() > 0) {
                    testMysqlHelp.testVariablesValueUpdate(PlanID, BatchName, Long.parseLong(Variablesid), ParseValue);
                    logger.info("TestCondition条件报告子条件变量值已存在则更新：-============：" + ParseValue);
                } else {
                    TestvariablesValue testvariablesValue = new TestvariablesValue();
                    testvariablesValue.setPlanid(PlanID);
                    testvariablesValue.setConditionid(ConditionID);
                    testvariablesValue.setConditiontype(ConditionType);
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
        }
    }
    //处理脚本子条件
    public void conditionscript(HashMap<String, String> conditionScript,  RequestObject requestObject) throws Exception {
        long Start = 0;
        long End = 0;
        long CostTime = 0;
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        Long CaseID = Long.parseLong(requestObject.getCaseid());
        Long ConditionID = Long.parseLong(conditionScript.get("id"));
        String conditionname = conditionScript.get("conditionname");
        String conditiontype = conditionScript.get("conditiontype");

        String Respone = "执行脚本成功";
        String ConditionResultStatus = "成功";
        try {
            Start = new Date().getTime();
            DnamicCompilerHelp dnamicCompilerHelp = new DnamicCompilerHelp();
            //数据库中获取脚本
            String JavaSource = conditionScript.get("script");
            logger.info("TestCondition条件报告脚本子条件:-============：" + JavaSource);
            String Source = dnamicCompilerHelp.GetCompeleteClass(JavaSource, CaseID);
            Object Result= dnamicCompilerHelp.CallDynamicScript(Source);
            End = new Date().getTime();
            CostTime = End - Start;
            ArrayList<HashMap<String, String>> scriptconditionVariablesList = testMysqlHelp.getscriptvariablesbyconditionid(ConditionID);
            if (scriptconditionVariablesList.size() > 0) {
                //2.获取查询结果
                for (HashMap<String, String> scriptconditionVariables : scriptconditionVariablesList) {
                    long variablesid = Long.parseLong(scriptconditionVariables.get("id"));
                    String Variablesname = scriptconditionVariables.get("scriptvariablesname");
                    String VariablesValue = Result.toString();
                    TestvariablesValue testvariablesValue = new TestvariablesValue();
                    testvariablesValue.setPlanid(Long.parseLong(requestObject.getTestplanid()));
                    testvariablesValue.setPlanname(requestObject.getTestplanname());
                    testvariablesValue.setBatchname(requestObject.getBatchname());
                    testvariablesValue.setVariablestype("脚本");
                    testvariablesValue.setCasename(conditionname);
                    testvariablesValue.setVariablesid(variablesid);
                    testvariablesValue.setVariablesname(Variablesname);
                    testvariablesValue.setVariablesvalue(VariablesValue);
                    testvariablesValue.setConditiontype(conditiontype);
                    testvariablesValue.setMemo("test");
                    testvariablesValue.setCaseid(Long.parseLong(requestObject.getCaseid()));
                    testvariablesValue.setConditionid(ConditionID);
                    testvariablesValue.setCasename(requestObject.getCasename());
                    testvariablesValue.setSlaverid(Long.parseLong(requestObject.getSlaverid()));
                    testMysqlHelp.testVariablesValueSave(testvariablesValue);
                }
            }
            SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Result.toString(), ConditionResultStatus, CostTime);
        } catch (Exception ex) {
            ConditionResultStatus = "失败";
            Respone = ex.getMessage();
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("脚本", requestObject, PlanID, ConditionID, conditionScript, Respone, ConditionResultStatus, CostTime);
            throw new Exception("脚本子条件执行异常：" + ex.getMessage());
        }
    }
    //延时子条件处理
    public void conditiondelay(HashMap<String, String> conditionDelay, RequestObject requestObject) throws Exception {
        Long PlanID=Long.parseLong(requestObject.getTestplanid());
        long ConidtionID =Long.parseLong(conditionDelay.get("id"));
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
            SaveSubCondition("延时", requestObject, PlanID, ConidtionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
        } catch (Exception ex) {
            ConditionResultStatus = "失败";
            Respone = ex.getMessage();
            End = new Date().getTime();
            CostTime = End - Start;
            SaveSubCondition("延时", requestObject, PlanID, ConidtionID, conditionDelay, Respone, ConditionResultStatus, CostTime);
            throw new Exception("延时子条件执行异常：" + ex.getMessage());
        }
    }
    private void SaveSubCondition(String SubconditionType, RequestObject requestObject, Long PlanID, Long ConditionID, HashMap<String, String> conditionScript, String Respone, String ConditionResultStatus, long CostTime) {
        //更新条件结果表
        TestconditionReport testconditionReport = new TestconditionReport();
        testconditionReport.setTestplanid(PlanID);
        testconditionReport.setPlanname(conditionScript.get("conditionname"));
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
    public void conditiondb(HashMap<String, String> conditionDb, RequestObject requestObject) throws Exception {
        long Start = 0;
        long End = 0;
        long CostTime = 0;
        String Respone = "";
        String ConditionResultStatus = "成功";
        Long PlanID = Long.parseLong(requestObject.getTestplanid());
        Long ConditionID = Long.parseLong(conditionDb.get("conditionid"));
        Long Assembleid = Long.parseLong(conditionDb.get("assembleid"));
        Long DBConditionid = Long.parseLong(conditionDb.get("id"));
        String SqlType = conditionDb.get("dbtype");
        String DBConditionName = conditionDb.get("subconditionname");
        String conditiontype = conditionDb.get("conditiontype");
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
            Respone=Rundb(conditiontype,requestObject, DBConditionid, DBConditionName, macdepunitlist, machinelist, ConnetcArray, AssembleType, deployunitvisittype, Sql, SqlType);
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
    private String GetDBResultValueByMap(List<HashMap<String, String>> DbResult, String columnname, long rownum) throws Exception {
        String Result = null;
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
        if(Result==null)
        {
            throw new Exception("未获得数据库变量值，请确认查询sql是否能正常获取数据，或者列名是否和Sql中匹配");
        }
        return Result;
    }
    //获取数据库变量值
    private String GetDBResultValueByEntity(List<Entity> DbResult, String columnname, long rownum) throws Exception {
        String Result = null;
        for (int i = 0; i < DbResult.size(); i++) {
            if (i == rownum) {
                Entity row = DbResult.get(i);
                Result = row.getStr(columnname);
            }
        }
        if(Result==null)
        {
            throw new Exception("未获得数据库变量值，请确认查询sql是否能正常获取数据，或者列名是否和Sql中匹配");
        }
        return Result;
    }

    //保存数据库变量值
    private void SaveDBTestVariablesValue(String conditiontype,RequestObject requestObject, long Conidtiondbid, String DBConditionName, long variablesid, String Variablesname, String VariablesValue) {
        TestvariablesValue testvariablesValue = new TestvariablesValue();
        testvariablesValue.setPlanid(Long.parseLong(requestObject.getTestplanid()));
        testvariablesValue.setPlanname(requestObject.getTestplanname());
        testvariablesValue.setBatchname(requestObject.getBatchname());
        testvariablesValue.setVariablestype("数据库");
        testvariablesValue.setCasename(DBConditionName);
        testvariablesValue.setVariablesid(variablesid);
        testvariablesValue.setVariablesname(Variablesname);
        testvariablesValue.setVariablesvalue(VariablesValue);
        testvariablesValue.setConditiontype(conditiontype);
        testvariablesValue.setMemo("test");
        testvariablesValue.setCaseid(new Long(requestObject.getCaseid()));
        testvariablesValue.setConditionid(Conidtiondbid);
        testvariablesValue.setCasename(requestObject.getCasename());
        testvariablesValue.setSlaverid(Long.parseLong(requestObject.getSlaverid()));
        testMysqlHelp.testVariablesValueSave(testvariablesValue);
    }

    private String Rundb(String conditiontype,RequestObject requestObject, long Conidtiondbid, String DBConditionName, ArrayList<HashMap<String, String>> macdepunitlist, ArrayList<HashMap<String, String>> machinelist, String[] ConnetcArray, String AssembleType, String deployunitvisittype, String Sql, String SqlType) throws Exception {
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
                        SaveDBTestVariablesValue(conditiontype,requestObject, Conidtiondbid, DBConditionName, variablesid, Variablesname, VariablesValue);
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
            Respone = UseHutoolDb(conditiontype,requestObject, Conidtiondbid, DBConditionName, SqlType, DBUrl, username, pass, Sql);
        }
        if (AssembleType.equalsIgnoreCase("oracle")) {
            DBUrl = GetDbUrl(AssembleType, macdepunitlist, deployunitvisittype, machinelist, dbname, port);
            Respone = UseHutoolDb(conditiontype,requestObject, Conidtiondbid, DBConditionName, SqlType, DBUrl, username, pass, Sql);
        }
        return Respone;
    }

    private String UseHutoolDb(String conditiontype,RequestObject requestObject, long Conidtiondbid, String DBConditionName, String SqlType, String DBUrl, String username, String pass, String Sql) throws Exception {
        String Respone = "";
        DataSource ds = new SimpleDataSource(DBUrl, username, pass);

        if (SqlType.equalsIgnoreCase("Select")) {
            // 1.查询数据库条件是否有变量关联
            ArrayList<HashMap<String, String>> dbconditionVariablesList = testMysqlHelp.getbyconditionid(Conidtiondbid);
            if (dbconditionVariablesList.size() > 0) {
                //2.获取查询结果
                List<Entity> result = Db.use(ds).query(Sql);
                for (HashMap<String, String> dbconditionVariables : dbconditionVariablesList) {
                    long variablesid = Long.parseLong(dbconditionVariables.get("id"));
                    String Variablesname = dbconditionVariables.get("dbvariablesname");
                    String columnname = dbconditionVariables.get("fieldname");
                    long roworder = Long.parseLong(dbconditionVariables.get("roworder"));
                    if (roworder > 0) {
                        roworder = roworder - 1;
                    }
                    String VariablesValue = GetDBResultValueByEntity(result, columnname, roworder);
                    Respone = Respone + "成功获取 数据库变量名：" + Variablesname + " 值:" + VariablesValue;
                    //保存数据库变量
                    SaveDBTestVariablesValue(conditiontype,requestObject, Conidtiondbid, DBConditionName, variablesid, Variablesname, VariablesValue);
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
