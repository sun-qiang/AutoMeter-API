package com.zoctan.api.core.service;

import cn.hutool.core.util.XmlUtil;
import com.jayway.jsonpath.JsonPath;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;

public class ParseResponeHelp {
    public String ParseRespone(String ResponeResultType,String Respone,String Path) throws Exception {
        String Result="";
        if (ResponeResultType.trim().equalsIgnoreCase("json")||ResponeResultType.trim().equalsIgnoreCase("application/json;charset=utf-8")||ResponeResultType.trim().equalsIgnoreCase("application/json")) {
            Result = ParseJsonRespone(Path, Respone);
        }
        if (ResponeResultType.equalsIgnoreCase("xml")||ResponeResultType.equalsIgnoreCase("application/xml;charset=utf-8")||ResponeResultType.equalsIgnoreCase("application/xml")) {
            Result = ParseXmlRespone(Path, Respone);
            //处理xml
        }
        return Result;
    }

    public String ParseJsonRespone(String JSPath,String JsonRespone) throws Exception {
        String Result="";
        try {
             Result= JsonPath.read(JsonRespone,JSPath).toString();
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
}
