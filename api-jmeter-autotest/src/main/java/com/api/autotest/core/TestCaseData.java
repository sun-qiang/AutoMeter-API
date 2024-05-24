package com.api.autotest.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.autotest.common.utils.HttpHeader;
import com.api.autotest.common.utils.HttpParamers;
import com.api.autotest.dto.ApicasesAssert;
import com.api.autotest.dto.ApicasesDBAssert;
import com.api.autotest.dto.RequestObject;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestCaseData {
    public static String logplannameandcasename = "";
    private Logger logger = null;
    TestMysqlHelp testMysqlHelp = null;

    public TestCaseData(Logger log, TestMysqlHelp mysqlHelp) {
        testMysqlHelp = mysqlHelp;
        logger = log;
    }

    //性能初始化数据根据jmeter传递下来的数据拼装用例请求的数据
    public RequestObject InitHttpDatabyJmeter(JavaSamplerContext context) throws Exception {
        RequestObject newob = new RequestObject();
        try {
            String casename = context.getParameter("casename");
            String executeplanname = context.getParameter("executeplanname");
            logplannameandcasename = executeplanname + "--" + casename + " :";
            //logger.info(logplannameandcasename + "用例数据casename is :  " + casename);
            //logger.info(logplannameandcasename + "用例数据executeplanname is :  " + executeplanname);
            String ProjectID = context.getParameter("projectid");
            String testplanid = context.getParameter("testplanid");
            String caseid = context.getParameter("caseid");
            String slaverid = context.getParameter("slaverid");
//            String batchid = context.getParameter("batchid");
            String batchname = context.getParameter("batchname");
            String machineip = context.getParameter("machineip");
            String deployvisitytype = context.getParameter("deployvisitytype");
            String RequestmMthod = context.getParameter("RequestmMthod");
            //logger.info(logplannameandcasename + "用例数据 RequestmMthod is :  " + RequestmMthod);

            String resource = context.getParameter("resource");
            //logger.info(logplannameandcasename + "用例数据 resource is :  " + resource);

            String apistyle = context.getParameter("apistyle");
            //logger.info(logplannameandcasename + "用例数据 apistyle is :  " + apistyle);

            String requestcontenttype = context.getParameter("requestcontenttype");
            //logger.info(logplannameandcasename + "用例数据 requestcontenttype is :  " + requestcontenttype);

            String responecontenttype = "";//context.getParameter("responecontenttype");
            //logger.info(logplannameandcasename + "用例数据 responecontenttype is :  " + responecontenttype);

            String headjson = context.getParameter("headjson").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 headjson is :  " + headjson);

            String paramsjson = context.getParameter("paramsjson").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 paramsjson is :  " + paramsjson);

            String bodyjson = context.getParameter("bodyjson").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 bodyjson is :  " + bodyjson);

            String postdata = context.getParameter("postdata").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 postdata is :  " + postdata);

            String variablesjson = context.getParameter("variablesjson").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 variablesjson is :  " + variablesjson);

            String casetype = context.getParameter("casetype");
            //logger.info(logplannameandcasename + "用例数据 casetype is :  " + casetype);

            String protocal = context.getParameter("protocal");
            //logger.info(logplannameandcasename + "用例数据 protocal is :  " + protocal);

            newob.setProjectid(ProjectID);
            newob.setCaseid(caseid);
            newob.setCasename(casename);
            newob.setTestplanid(testplanid);
            newob.setSlaverid(slaverid);
            newob.setBatchname(batchname);
//            newob.setBatchid(batchid);
            newob.setCasetype(casetype);
            newob.setResponecontenttype(responecontenttype);
            newob.setResource(resource);
            newob.setRequestmMthod(RequestmMthod);
            newob.setRequestcontenttype(requestcontenttype);
            newob.setApistyle(apistyle);
            newob.setProtocal(protocal);
            newob.setHeadjson(headjson);
            newob.setBodyjson(bodyjson);
            newob.setParamjson(paramsjson);
            newob.setPostData(postdata);
            newob.setVariablesjson(variablesjson);
            newob.setDeployunitvisittype(deployvisitytype);
            newob.setMachineip(machineip);

            String expect = context.getParameter("expect").replace("Autometer", " ");
            //logger.info(logplannameandcasename + "用例数据 expect is :  " + expect);
            List<ApicasesAssert> apicasesAssertList = new ArrayList<>();
            if ((!expect.isEmpty()) && (expect != null)) {
                apicasesAssertList = JSONObject.parseArray(expect, ApicasesAssert.class);
            }
            newob.setApicasesAssertList(apicasesAssertList);
            expect = GetAssertInfo(apicasesAssertList);
            newob.setExpect(expect);
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "性能用例数据 InitHttpDatabyJmeter 异常 :  " + ex.getMessage());
            throw new Exception("性能用例数据获取异常：" + ex.getMessage());
        }
        return newob;
    }

    // 功能用例拼装请求需要的用例数据
    public RequestObject GetCaseRequestData(String PlanId, String TestCaseId, String SlaverId, String BatchName, String ExecPlanName, String SceneId, String SceneName) {
        RequestObject ro = new RequestObject();
        try {
            //ArrayList<HashMap<String, String>> planlist = getcaseData("select * from executeplan where id=" + PlanId);
            ArrayList<HashMap<String, String>> deployunitlist = testMysqlHelp.getcaseData("select b.protocal,b.port,b.id,b.baseurl from apicases a inner join deployunit b on a.deployunitid=b.id where a.id=" + TestCaseId);
            ArrayList<HashMap<String, String>> apilist = testMysqlHelp.getcaseData("select b.visittype,b.apistyle,b.path,b.requestcontenttype,b.responecontenttype from apicases a inner join api b on a.apiid=b.id where a.id=" + TestCaseId);
            ArrayList<HashMap<String, String>> deployunitmachineiplist = testMysqlHelp.getcaseData("select m.ip,a.domain,a.visittype from macdepunit a INNER JOIN apicases b INNER JOIN executeplan c JOIN machine m on a.depunitid=b.deployunitid and  a.envid=c.envid and  m.id=a.machineid where b.id=" + TestCaseId + " and c.id=" + PlanId);
            ArrayList<HashMap<String, String>> caselist = testMysqlHelp.getcaseData("select * from apicases where id=" + TestCaseId);
            ArrayList<HashMap<String, String>> caseassertlist = testMysqlHelp.getcaseData("select * from apicases_assert where caseid=" + TestCaseId);
            ArrayList<HashMap<String, String>> casedbassertlist = testMysqlHelp.getcaseData("select * from apicases_dbassert where caseid=" + TestCaseId);

            // url请求资源路径
            String path = testMysqlHelp.getcaseValue("path", apilist);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            //获取请求响应的数据格式
            String requestcontenttype = testMysqlHelp.getcaseValue("requestcontenttype", apilist);
            String responecontenttype = "";//testMysqlHelp.getcaseValue("responecontenttype", apilist);
            // http请求方式 get，post
            String method = testMysqlHelp.getcaseValue("visittype", apilist).toLowerCase();
            // api风格
            String apistyle = testMysqlHelp.getcaseValue("apistyle", apilist).toLowerCase();
            // 协议 http,https,rpc
            String protocal = testMysqlHelp.getcaseValue("protocal", deployunitlist).toLowerCase();
            // 微服务端口
            String port = testMysqlHelp.getcaseValue("port", deployunitlist);

            String BaseUrl = testMysqlHelp.getcaseValue("baseurl", deployunitlist);
            logger.info(logplannameandcasename + "BaseUrl  is " + BaseUrl);

            String deployunitid = testMysqlHelp.getcaseValue("id", deployunitlist);

            // 获取微服务访问方式，ip或者域名
            String deployunitvisittype = testMysqlHelp.getcaseValue("visittype", deployunitmachineiplist);

            String IP = testMysqlHelp.getcaseValue("ip", deployunitmachineiplist);
            // 根据访问方式来确定ip还是域名
            String testserver = "";
            String resource = "";
            if (deployunitvisittype.equalsIgnoreCase("ip")) {
                testserver = IP;
                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = protocal + "://" + testserver + ":" + port + path;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = protocal + "://" + testserver + ":" + port + BaseUrl + path;
                    } else {
                        resource = protocal + "://" + testserver + ":" + port + "/" + BaseUrl + path;
                    }
                }
                logger.info(logplannameandcasename + "resource ip is " + resource);
            } else {
                testserver = testMysqlHelp.getcaseValue("domain", deployunitmachineiplist);
                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = protocal + "://" + testserver + path;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = protocal + "://" + testserver + BaseUrl + path;
                    } else {
                        resource = protocal + "://" + testserver + "/" + BaseUrl + path;
                    }
                }
                logger.info(logplannameandcasename + "resource domain is " + resource);
            }
            //获取断言记录
            TestAssert testAssert = new TestAssert(logger);
            List<ApicasesAssert> apicasesAssertList = testAssert.GetApicasesAssertList(caseassertlist);
            ro.setApicasesAssertList(apicasesAssertList);
            //获取数据库断言记录
            List<ApicasesDBAssert> apicasesDBAssertList = testAssert.GetApicasesdbAssertList(casedbassertlist);
            ro.setApicasesDBAssertList(apicasesDBAssertList);
            String expect = GetAssertInfo(apicasesAssertList);
            logger.info(logplannameandcasename + "用例数据 接口断言期望 is :  " + expect );
            expect=GetDBAssertInfo(expect,apicasesDBAssertList);
            logger.info(logplannameandcasename + "用例数据 断言期望 is :  " + expect );

            String casetype = testMysqlHelp.getcaseValue("casetype", caselist);
            String CaseName = testMysqlHelp.getcaseValue("casename", caselist);
            String ProjectID = testMysqlHelp.getcaseValue("projectid", caselist);
            logger.info(logplannameandcasename + "用例数据 resource is :  " + resource + "   protocal  is:   " + protocal + "  expect is :      " + expect + "  visittype is: " + method + "   path is: " + path + " casetype is: " + casetype);

            ro.setProjectid(ProjectID);
            ro.setSceneid(Long.parseLong(SceneId));
            ro.setScenename(SceneName);
            ro.setCaseid(TestCaseId);
            ro.setCasename(CaseName);
            ro.setDeployunitid(deployunitid);
            ro.setTestplanid(PlanId);
            ro.setSlaverid(SlaverId);
            ro.setBatchname(BatchName);
            ro.setTestplanname(ExecPlanName);
//            ro.setBatchid(BatchId);
            ro.setExpect(expect);
            ro.setCasetype(casetype);
            ro.setProtocal(protocal);
            ro.setApistyle(apistyle);
            ro.setRequestcontenttype(requestcontenttype);
            ro.setRequestmMthod(method);
            ro.setResource(resource);
            ro.setResponecontenttype(responecontenttype);
            ro.setDeployunitvisittype(deployunitvisittype);
            ro.setMachineip(IP);
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "功能用例数据GetCaseRequestData异常 :  " + ex);
        }
        return ro;
    }


    //获取断言信息
    private String GetAssertInfo(List<ApicasesAssert> apicasesAssertList) {
        String expectValue = "";
        if (apicasesAssertList.size() > 0) {
            for (ApicasesAssert apicasesAssert : apicasesAssertList) {
                if (apicasesAssert.getAsserttype().equals(new String("Respone"))) {
                    expectValue = expectValue + "【断言类型：" + apicasesAssert.getAsserttype() + "， 断言子类型：" + apicasesAssert.getAssertsubtype() + "， 断言条件：" + apicasesAssert.getAssertcondition() + "， 断言值：" + apicasesAssert.getAssertvalues() + "， 断言值类型：" + apicasesAssert.getAssertvaluetype() + "】";
                } else {
                    expectValue = expectValue + "【断言类型：" + apicasesAssert.getAsserttype() + "， 断言表达式：" + apicasesAssert.getExpression() + "， 断言条件：" + apicasesAssert.getAssertcondition() + "， 断言值：" + apicasesAssert.getAssertvalues() + "， 断言值类型：" + apicasesAssert.getAssertvaluetype() + "】";
                }
            }
        }
        return expectValue;
    }

    //获取数据库断言信息
    private String GetDBAssertInfo(String expectValue,List<ApicasesDBAssert> apicasesDBAssertList) {
        if (apicasesDBAssertList.size() > 0) {
            for (ApicasesDBAssert apicasesDBAssert : apicasesDBAssertList) {
               long dbassertid= apicasesDBAssert.getId();
                ArrayList<HashMap<String, String>> casedbassertvaluelist = testMysqlHelp.getcaseData("select * from apicases_dbassert_value where dbassertid=" + dbassertid);
                String fieldname = "";
                String valuetype = "";
                String assertcondition = "";
                String expectvalue = "";
                long roworder = 0;
                for (HashMap<String, String> map : casedbassertvaluelist) {
                    for (String Key : map.keySet()) {
                        if (Key.equals(new String("fieldname"))) {
                            fieldname = map.get(Key);
                        }
                        if (Key.equals(new String("valuetype"))) {
                            valuetype = map.get(Key);
                        }
                        if (Key.equals(new String("assertcondition"))) {
                            assertcondition = map.get(Key);
                        }
                        if (Key.equals(new String("expectvalue"))) {
                            expectvalue = map.get(Key);
                        }
                        if (Key.equals(new String("roworder"))) {
                            roworder = Long.parseLong(map.get(Key));
                        }
                    }
                    expectValue = expectValue + "【数据库断言字段名：" + fieldname + "， 行号：" + roworder+ "， 断言条件：" + assertcondition + "， 断言期望值：" + expectvalue + "， 断言值类型：" + valuetype + "】";
                }
            }
        }
        return expectValue;
    }
}
