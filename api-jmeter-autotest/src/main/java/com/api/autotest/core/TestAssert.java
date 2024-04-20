package com.api.autotest.core;

import cn.hutool.Hutool.*;
import cn.hutool.core.util.XmlUtil;
import com.api.autotest.dto.*;
import com.jayway.jsonpath.JsonPath;
import net.didion.jwnl.data.Exc;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import sun.misc.FloatingDecimal;

import javax.xml.xpath.XPathConstants;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fanseasn on 2020/10/17.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/17
*/
public class TestAssert {

    private Logger logger = null;

    public TestAssert(Logger log) {
        logger = log;
    }

    //通过JsonPath获取实际值和期望值比较，返回断言信息
    public String ParseJsonResult(TestResponeData responeData, ApicasesAssert apicasesAssert )  {
        String ActualJson = "";
        String AssertInfo = "";
        if (responeData!=null)
        {
            ActualJson = responeData.getResponeContent();
        }
        //获取实际值使用JsonPath解析
        String ExpectValue = apicasesAssert.getAssertvalues();
        if (apicasesAssert.getAsserttype().equalsIgnoreCase("Json")) {
            String ActualResult=ParseJson(apicasesAssert.getExpression(),ActualJson);
            logger.info(TestCaseData.logplannameandcasename + "ParseJson  ExpectValue is:" + ExpectValue + "  ActualResult is:" + ActualResult);
            AssertInfo = AssertCondition(apicasesAssert, ExpectValue, ActualResult);
        }
        logger.info(TestCaseData.logplannameandcasename + "ParseJson AssertInfo is:" + AssertInfo );
        return AssertInfo;
    }

    public String ParseHeader(TestResponeData testResponeData, String Path) {
        String Result = "";
        Header[] headerList = testResponeData.getHeaderarray();
        int index = 1;
        try {
            if (!Path.contains("[")) {
                throw new Exception("接口变量来源Header不符合表达式规范，正确例如Vary[1]");
            }
            for (Header header : headerList) {
                String Key=Path.substring(0,Path.indexOf("["));
                if (header.getName().equalsIgnoreCase(Key)) {
                    if (Path.contains("]")) {
                        String IndexNum=Path.substring(Path.indexOf("[")+1,Path.indexOf("]"));
                        int KeyIndex = Integer.parseInt(IndexNum);
                        if (KeyIndex == index) {
                            Result = header.getValue();
                        }
                    } else {
                        throw new Exception("接口变量来源Header不符合表达式规范，正确例如Vary[1]");
                    }
                    index++;
                }
            }
        } catch (Exception ex) {
            Result = "接口变量来源Header不符合表达式规范，正确例如Vary[1]";
        }
        if (Result == "") {
            Result = "接口变量来源Header：" + Path + " 在绑定的接口的请求响应中没有对应的值";
        }
        return Result;
    }

    public String ParseCookies(TestResponeData testResponeData,String Path)  {
        String Result="";
        List<Cookie> headerList=testResponeData.getCookies();
        for (Cookie cookie:headerList) {
            if(cookie.getName().equalsIgnoreCase(Path))
            {
                Result=cookie.getValue();
            }
        }
        if(Result=="")
        {
            Result="接口变量来源Cookies："+Path+" 在绑定的接口的请求响应中没有对应的值";
        }
        return Result;
    }


    public String ParseRespone(String ResponeResultType,String Path ,String Respone)
    {
        String Result="";
        if (ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("json")||ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("application/json;charset=utf-8")||ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("application/json")) {
            Result = ParseJson(Path, Respone);
        }
        if (ResponeResultType.equalsIgnoreCase("xml")||ResponeResultType.equalsIgnoreCase("application/xml;charset=utf-8")||ResponeResultType.equalsIgnoreCase("application/xml")) {
            Result = ParseXml(Path, Respone);
            //处理xml
        }
        return Result;
    }

    public String ParseJson(String JSPath,String JsonRespone)
    {
        String Result="";
        try {
            Result= JsonPath.read(JsonRespone,JSPath).toString();
        }
        catch (Exception ex)
        {
            Result="解析Json结果异常"+ex.getMessage()+"解析内容："+JsonRespone;
        }
        return Result;
    }

    public String ParseXml(String  XPath,String ActualXml)
    {
        String Result="";
        try {
            Document docResult= XmlUtil.readXML(ActualXml);
            Result = XmlUtil.getByXPath(XPath, docResult, XPathConstants.STRING).toString();
        }
        catch (Exception ex)
        {
            Result="解析Xml结果异常"+ex.getMessage()+"解析内容："+ActualXml;
        }
        return Result;
    }

    public String ParseXmlResult(TestResponeData responeData, ApicasesAssert apicasesAssert )  {
        String ActualXml = "";
        String AssertInfo = "";
        if (responeData!=null)
        {
            ActualXml = responeData.getResponeContent();
        }
        //获取实际值使用XPath解析
        String ExpectValue = apicasesAssert.getAssertvalues();
        if (apicasesAssert.getAsserttype().equalsIgnoreCase("Xml")) {
            String ActualResult=ParseXml(apicasesAssert.getExpression(),ActualXml);
            logger.info(TestCaseData.logplannameandcasename + "ParseXml  ExpectValue is:" + ExpectValue + "  ActualResult is:" + ActualResult);
            AssertInfo = AssertCondition(apicasesAssert, ExpectValue, ActualResult);
        }
        return AssertInfo;
    }

    public String ParseResponeResult(TestResponeData responeData, ApicasesAssert apicasesAssert )  {
        String AssertInfo = "";
        if (responeData!=null)
        {
            String ExpectValue = apicasesAssert.getAssertvalues();
            String ActualResult="";
            if (apicasesAssert.getAsserttype().equals("Respone")) {
                if (apicasesAssert.getAssertsubtype().equals("Code"))
                {
                    int Code=responeData.getResponeCode();
                    ActualResult=String.valueOf(Code);
                    logger.info(TestCaseData.logplannameandcasename + "响应断言  ExpectValue is:" + ExpectValue + "  ActualResult is:" + ActualResult);
                    AssertInfo = AssertCondition(apicasesAssert, ExpectValue, ActualResult);
                }
                if (apicasesAssert.getAssertsubtype().equals("文本"))
                {
                    ActualResult=responeData.getResponeContent();
                    logger.info(TestCaseData.logplannameandcasename + "响应断言  ExpectValue is:" + ExpectValue + "  ActualResult is:" + ActualResult);
                    AssertInfo = AssertCondition(apicasesAssert, ExpectValue, ActualResult);
                }
            }
        }
        return AssertInfo;
    }

    private String AssertCondition(ApicasesAssert apicasesAssert, String ExpectValue, String ActualResult) {
        String AssertInfo = "";
        if (apicasesAssert.getAssertcondition().equals("=")) {
            AssertInfo = CallAssertEqualFun(apicasesAssert.getAssertvaluetype(), ExpectValue, ActualResult);
        }
        if (apicasesAssert.getAssertcondition().equals(">")) {
            AssertInfo = CallAssertMoreFun(apicasesAssert.getAssertvaluetype(), ExpectValue, ActualResult);
        }
        if (apicasesAssert.getAssertcondition().equals("<")) {
            AssertInfo = CallAssertLessFun(apicasesAssert.getAssertvaluetype(), ExpectValue, ActualResult);
        }
        if (apicasesAssert.getAssertcondition().equals("Contain")) {
            AssertInfo = AssertContains(ExpectValue, ActualResult);
        }
        if (apicasesAssert.getAssertcondition().equals("NoContain")) {
            AssertInfo = AssertNoContains(ExpectValue, ActualResult);
        }
        return AssertInfo;
    }

    public String AssertDBCondition(ApicasesDBAssertValue apicasesDBAssertValue, String ExpectValue, String ActualResult) {
        String AssertInfo = "";
        if (apicasesDBAssertValue.getAssertcondition().equals("=")) {
            AssertInfo = CallAssertEqualFun(apicasesDBAssertValue.getValuetype(), ExpectValue, ActualResult);
        }
        if (apicasesDBAssertValue.getAssertcondition().equals(">")) {
            AssertInfo = CallAssertMoreFun(apicasesDBAssertValue.getValuetype(), ExpectValue, ActualResult);
        }
        if (apicasesDBAssertValue.getAssertcondition().equals("<")) {
            AssertInfo = CallAssertLessFun(apicasesDBAssertValue.getValuetype(), ExpectValue, ActualResult);
        }
        if (apicasesDBAssertValue.getAssertcondition().equals("Contain")) {
            AssertInfo = AssertContains(ExpectValue, ActualResult);
        }
        if (apicasesDBAssertValue.getAssertcondition().equals("NoContain")) {
            AssertInfo = AssertNoContains(ExpectValue, ActualResult);
        }
        return AssertInfo;
    }

    private String CallAssertEqualFun(String AssertValueType, String ExpectValue, String ActualResult) {
        String AssertInfo = "";
        try {
            if (AssertValueType.equals(new String("int"))) {
                AssertInfo = AssertEqual(Integer.parseInt(ExpectValue), Integer.parseInt(ActualResult));
            }
            if (AssertValueType.equals(new String("Long"))) {
                AssertInfo = AssertEqual(Long.parseLong(ExpectValue), Long.parseLong(ActualResult));
            }
            if (AssertValueType.equals(new String("Float"))) {
                AssertInfo = AssertEqual(Float.parseFloat(ExpectValue), Float.parseFloat((ActualResult)));
            }
            if (AssertValueType.equals(new String("Double"))) {
                AssertInfo = AssertEqual(Double.parseDouble(ExpectValue), Double.parseDouble(ActualResult));
            }
            if (AssertValueType.equals(new String("Decimal"))) {
                AssertInfo = AssertEqual(FloatingDecimal.parseDouble(ExpectValue), FloatingDecimal.parseDouble(ActualResult));
            }
            if (AssertValueType.equals(new String("String"))) {
                AssertInfo = AssertEqual(ExpectValue, ActualResult);
            }
        } catch (Exception ex) {
            caseresult = false;
            AssertInfo = "1,接口实际响应中无期望值类型的数据 2,期望值和配置的期望值类型不匹配,期望值类型：" + AssertValueType + " 期望值：" + ExpectValue + " 实际值：" + ActualResult;
        }
        return AssertInfo;
    }

    private String CallAssertMoreFun(String AssertValueType, String ExpectValue, String ActualResult) {
        String AssertInfo = "";
        try {
            if (AssertValueType.equals(new String("int"))) {
                AssertInfo = AssertMore(Integer.parseInt(ExpectValue), Integer.parseInt(ActualResult));
            }
            if (AssertValueType.equals(new String("Long"))) {
                AssertInfo = AssertMore(Long.parseLong(ExpectValue), Long.parseLong(ActualResult));
            }
            if (AssertValueType.equals(new String("Float"))) {
                AssertInfo = AssertMore(Float.parseFloat(ExpectValue), Float.parseFloat((ActualResult)));
            }
            if (AssertValueType.equals(new String("Double"))) {
                AssertInfo = AssertMore(Double.parseDouble(ExpectValue), Double.parseDouble(ActualResult));
            }
            if (AssertValueType.equals(new String("Decimal"))) {
                AssertInfo = AssertMore(FloatingDecimal.parseDouble(ExpectValue), FloatingDecimal.parseDouble(ActualResult));
            }
        } catch (Exception ex) {
            caseresult = false;
            AssertInfo = "1,接口实际响应中无期望值类型的数据 2,期望值和配置的期望值类型不匹配,期望值类型：" + AssertValueType + " 期望值：" + ExpectValue + " 实际值：" + ActualResult;
        }
        return AssertInfo;
    }

    private String CallAssertLessFun(String AssertValueType, String ExpectValue, String ActualResult) {
        String AssertInfo = "";
        try {
            if (AssertValueType.equals(new String("int"))) {
                AssertInfo = AssertLess(Integer.parseInt(ExpectValue), Integer.parseInt(ActualResult));
            }
            if (AssertValueType.equals(new String("Long"))) {
                AssertInfo = AssertLess(Long.parseLong(ExpectValue), Long.parseLong(ActualResult));
            }
            if (AssertValueType.equals(new String("Float"))) {
                AssertInfo = AssertLess(Float.parseFloat(ExpectValue), Float.parseFloat((ActualResult)));
            }
            if (AssertValueType.equals(new String("Double"))) {
                AssertInfo = AssertLess(Double.parseDouble(ExpectValue), Double.parseDouble(ActualResult));
            }
            if (AssertValueType.equals(new String("Decimal"))) {
                AssertInfo = AssertLess(FloatingDecimal.parseDouble(ExpectValue), FloatingDecimal.parseDouble(ActualResult));
            }
        } catch (Exception ex) {
            caseresult = false;
            AssertInfo = "1,接口实际响应中无期望值类型的数据 2,期望值和配置的期望值类型不匹配,期望值类型：" + AssertValueType + " 期望值：" + ExpectValue + " 实际值：" + ActualResult;
        }
        return AssertInfo;
    }

    public List<ApicasesAssert> GetApicasesAssertList(ArrayList<HashMap<String, String>> list) {
        List<ApicasesAssert> apicasesAssertList = new ArrayList<>();
        if (list.size() > 0) {
            for (HashMap<String, String> map : list) {
                ApicasesAssert apicasesAssert = new ApicasesAssert();
                for (String Key : map.keySet()) {
                    if (Key.equals(new String("asserttype"))) {
                        apicasesAssert.setAsserttype(map.get(Key));
                    }
                    if (Key.equals(new String("assertsubtype"))) {
                        apicasesAssert.setAssertsubtype(map.get(Key));
                    }
                    if (Key.equals(new String("assertvalues"))) {
                        apicasesAssert.setAssertvalues(map.get(Key));
                    }
                    if (Key.equals(new String("expression"))) {
                        apicasesAssert.setExpression(map.get(Key));
                    }
                    if (Key.equals(new String("assertcondition"))) {
                        apicasesAssert.setAssertcondition(map.get(Key));
                    }
                    if (Key.equals(new String("assertvaluetype"))) {
                        apicasesAssert.setAssertvaluetype(map.get(Key));
                    }
                }
                apicasesAssertList.add(apicasesAssert);
            }
        }
        return apicasesAssertList;
    }

    public List<ApicasesDBAssert> GetApicasesdbAssertList(ArrayList<HashMap<String, String>> list) {
        List<ApicasesDBAssert> apicasesdbAssertList = new ArrayList<>();
        if (list.size() > 0) {
            for (HashMap<String, String> map : list) {
                ApicasesDBAssert apicasesAssert = new ApicasesDBAssert();
                for (String Key : map.keySet()) {
                    if (Key.equals(new String("caseid"))) {
                        apicasesAssert.setCaseid(Long.parseLong(map.get(Key)));
                    }
                    if (Key.equals(new String("id"))) {
                        apicasesAssert.setId(Long.parseLong(map.get(Key)));
                    }
                    if (Key.equals(new String("assembleid"))) {
                        apicasesAssert.setAssembleid(Long.parseLong(map.get(Key)));
                    }
                    if (Key.equals(new String("assemblename"))) {
                        apicasesAssert.setAssemblename(map.get(Key));
                    }
                    if (Key.equals(new String("envid"))) {
                        apicasesAssert.setEnvid(Long.parseLong(map.get(Key)));
                    }
                    if (Key.equals(new String("enviroment"))) {
                        apicasesAssert.setEnviroment(map.get(Key));
                    }
                    if (Key.equals(new String("expression"))) {
                        apicasesAssert.setExpression(map.get(Key));
                    }
                    if (Key.equals(new String("expectrecordsnums"))) {
                        apicasesAssert.setExpectrecordsnums(Long.parseLong(map.get(Key)));
                    }
                }
                apicasesdbAssertList.add(apicasesAssert);
            }
        }
        return apicasesdbAssertList;
    }

    private boolean flag = true;

    public boolean isCaseresult() {
        if (flag) {
            return caseresult;
        } else {
            return flag;
        }
    }

    public void setCaseresult(boolean caseresult) {
        this.caseresult = caseresult;
    }

    private boolean caseresult = true;
    public String assertinfo = "";


    private String collectioninfo(String expect, String actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值】：" + expect + " , 【实际值】：" + actual + " 【断言结果】: " + ResultDesc + " || ";
    }

    private String collectioninfomore(int expect, int actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值大于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + ResultDesc + " || ";
    }

    private String collectioninfomore(long expect, long actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值大于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + ResultDesc + " || ";
    }

    private String collectioninfomore(float expect, float actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值大于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + ResultDesc + " || ";
    }

    private String collectioninfomore(double expect, double actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值大于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + ResultDesc + " || ";
    }


    private String collectioninfoless(int expect, int actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值小于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + result + " || ";
    }

    private String collectioninfoless(long expect, long actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值小于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + result + " || ";
    }

    private String collectioninfoless(float expect, float actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值小于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + result + " || ";
    }

    private String collectioninfoless(double expect, double actual, boolean result) {
        String ResultDesc = "通过";
        if (!result) {
            ResultDesc = "失败";
        }
        return "【期望值小于】 ：" + expect + " 【实际值】 ：" + actual + " 【断言结果】: " + result + " || ";
    }


    public String AssertContains(String expect, String actual) {
        try {
            if (actual.contains(expect)) {
                caseresult = true;
            } else {
                caseresult = false;
            }
            assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        } catch (Exception ex) {
            caseresult = false;
            assertinfo = assertinfo + " 期望值：" + expect + " 实际值：" + actual + ex.getMessage();
        }
        return assertinfo;
    }

    public String AssertNoContains(String expect, String actual) {
        try {
            if (!actual.contains(expect)) {
                caseresult = true;
            } else {
                caseresult = false;
            }
            assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        } catch (Exception ex) {
            caseresult = false;
            assertinfo = assertinfo + " 期望值：" + expect + " 实际值：" + actual + ex.getMessage();
        }
        return assertinfo;
    }


    public String AssertEqual(String expect, String actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
            flag = false;
        }
        assertinfo = assertinfo + collectioninfo(expect, actual, caseresult);
        return assertinfo;
    }

    public String AssertEqual(int expect, int actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(double expect, double actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(long expect, long actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(float expect, float actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(Integer expect, Integer actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(Long expect, Long actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(Float expect, Float actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(Double expect, Double actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(boolean expect, boolean actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(Boolean expect, Boolean actual) {
        if (expect.equals(actual)) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }

    public String AssertEqual(char expect, char actual) {
        if (expect == actual) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfo(String.valueOf(expect), String.valueOf(actual), caseresult);
        return assertinfo;
    }


    public String AssertMore(int expect, int actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfomore(expect, actual, caseresult);
        return assertinfo;
    }


    public String AssertMore(long expect, long actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfomore(expect, actual, caseresult);
        return assertinfo;
    }

    public String AssertMore(float expect, float actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfomore(expect, actual, caseresult);
        return assertinfo;
    }

    public String AssertMore(double expect, double actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfomore(expect, actual, caseresult);
        return assertinfo;
    }


    public String AssertLess(int expect, int actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfoless(expect, actual, caseresult);
        return assertinfo;
    }


    public String AssertLess(long expect, long actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfoless(expect, actual, caseresult);
        return assertinfo;
    }

    public String AssertLess(float expect, float actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfoless(expect, actual, caseresult);
        return assertinfo;
    }

    public String AssertLess(double expect, double actual) {
        if (actual > expect) {
            caseresult = true;
        } else {
            caseresult = false;
        }
        assertinfo = assertinfo + collectioninfoless(expect, actual, caseresult);
        return assertinfo;
    }
}
