package com.zoctan.api.core.service;

import cn.hutool.core.util.XmlUtil;
import com.jayway.jsonpath.JsonPath;
import com.zoctan.api.controller.TestconditionController;
import com.zoctan.api.dto.TestResponeData;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import org.apache.http.Header;

import org.apache.http.cookie.Cookie;
import javax.xml.xpath.XPathConstants;
import java.util.List;

@Slf4j
public class ParseResponeHelp {
    public String ParseRespone(String ResponeResultType,String Respone,String Path) throws Exception {
        ParseResponeHelp.log.info("接口子条件解析json内容-============：" + Respone + " 响应数据类型" + ResponeResultType+" JsonPath is:"+Path);
        String Result="";
        if (ResponeResultType.trim().equalsIgnoreCase("json")||ResponeResultType.trim().equalsIgnoreCase("application/json;charset=utf-8")||ResponeResultType.trim().equalsIgnoreCase("application/json")) {
            Result = ParseJsonRespone(Path, Respone);
        }
        if (ResponeResultType.trim().equalsIgnoreCase("xml")||ResponeResultType.trim().equalsIgnoreCase("application/xml;charset=utf-8")||ResponeResultType.trim().equalsIgnoreCase("application/xml")) {
            Result = ParseXmlRespone(Path, Respone);
            //处理xml
        }
        return Result;
    }

    public String ParseJsonRespone(String JSPath,String JsonRespone) throws Exception {
        String Result="";
        try {
             Result= JsonPath.read(JsonRespone,JSPath).toString();
            ParseResponeHelp.log.info("接口子条件条件报告子条件处理变量表达式-============：" + JSPath + " 响应内容" + JsonRespone+" 解析结果 is:"+Result);
        }
        catch (Exception ex)
        {
            Result=ex.getMessage();
            throw new Exception("变量管理中此变量值表达式JsonPath："+JSPath+" 在接口子条件的请求响应: "+JsonRespone+" 中未匹配到对应的值");
        }
        return Result;
    }


    public String ParseXmlRespone(String  XPath,String ActualXml) throws Exception {
        String Result="";
        try {
            Document docResult= XmlUtil.readXML(ActualXml);
            Result = XmlUtil.getByXPath(XPath, docResult, XPathConstants.STRING).toString();
        }
        catch (Exception ex)
        {
            Result=ex.getMessage();
            throw new Exception("变量管理中此变量值表达式XPath："+XPath+" 在接口子条件的请求响应："+ActualXml+" 中未匹配到对应的值");
        }
        return Result;
    }

    public String ParseHeader(TestResponeData testResponeData, String Path) throws Exception {
        String Result="";
        List<Header> headerList=testResponeData.getHeaderList();
        for (Header header:headerList) {
            if(header.getName().equalsIgnoreCase(Path))
            {
                Result=header.getValue();
            }
        }
        if(Result=="")
        {
            throw new Exception("接口变量来源Header："+Path+" 在绑定的接口的请求响应中没有对应的值");
        }
        return Result;
    }

    public String ParseCookies(TestResponeData testResponeData,String Path) throws Exception {
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
            throw new Exception("接口变量来源Cookies："+Path+" 在绑定的接口的请求响应中没有对应的值");
        }
        return Result;
    }
}
