package com.zoctan.api.core.service;

import cn.hutool.core.util.XmlUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
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
        if (ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("json")||ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("application/json;charset=utf-8")||ResponeResultType.replaceAll(" ", "").equalsIgnoreCase("application/json")) {
            ParseResponeHelp.log.info("接口子条件开始解析json内容-================================：" );
            Result = ParseJsonRespone(Path, Respone);
            ParseResponeHelp.log.info("接口子条件完成解析json内容-================================："+" 结果为："+Result );
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
            ParseResponeHelp.log.info("接口子条件条件报告子条件ParseJsonRespone处理变量表达式-============解析异常结果 is:"+Result);
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
            ParseResponeHelp.log.info("接口子条件条件报告子条件ParseXmlRespone处理变量表达式-============解析异常结果 is:"+Result);
            throw new Exception("变量管理中此变量值表达式XPath："+XPath+" 在接口子条件的请求响应："+ActualXml+" 中未匹配到对应的值");
        }
        return Result;
    }

    public String ParseHeader(TestResponeData testResponeData, String Path) throws Exception {
//        String Result="";
//        List<Header> headerList=testResponeData.getHeaderList();
//        for (Header header:headerList) {
//            if(header.getName().equalsIgnoreCase(Path))
//            {
//                Result=header.getValue();
//            }
//        }
//        if(Result=="")
//        {
//            throw new Exception("接口变量来源Header："+Path+" 在绑定的接口的请求响应中没有对应的值");
//        }
//        return Result;
        String Result = "";
        List<Header> headerList = testResponeData.getHeaderList();
        int index = 1;
        if (!Path.contains("[")) {
            throw new Exception("接口变量来源Header不符合表达式规范，正确例如Vary[1]");
        }
        for (Header header : headerList) {
            String Key = Path.substring(0, Path.indexOf("["));
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
        if (Result == "") {
            throw new Exception("接口变量来源Header：" + Path + " 在绑定的接口的请求响应中没有对应的值");
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

    public static void main(final String[] args)
    {
        String res="{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"id\":null,\"name\":\"全部\",\"parentId\":null,\"weight\":null,\"count\":20,\"children\":[{\"id\":27,\"name\":\"分组1\",\"parentId\":0,\"weight\":27,\"count\":2,\"children\":null},{\"id\":28,\"name\":\"营销分组0001\",\"parentId\":0,\"weight\":28,\"count\":0,\"children\":[{\"id\":30,\"name\":\"A0001\",\"parentId\":28,\"weight\":30,\"count\":0,\"children\":[{\"id\":31,\"name\":\"A00002\",\"parentId\":30,\"weight\":31,\"count\":0,\"children\":null}]}]},{\"id\":29,\"name\":\"营销分组0002\",\"parentId\":0,\"weight\":29,\"count\":1,\"children\":null},{\"id\":63,\"name\":\"收到\",\"parentId\":0,\"weight\":63,\"count\":0,\"children\":null},{\"id\":64,\"name\":\"收到1\",\"parentId\":0,\"weight\":64,\"count\":0,\"children\":null},{\"id\":65,\"name\":\"测试新建分类\",\"parentId\":0,\"weight\":65,\"count\":0,\"children\":null},{\"id\":67,\"name\":\"奥迪\",\"parentId\":0,\"weight\":67,\"count\":0,\"children\":null},{\"id\":0,\"name\":\"未分类\",\"parentId\":0,\"weight\":null,\"count\":17,\"children\":null}]}} ";

        Configuration configuration = Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();

        ObjectNode obj = (ObjectNode) configuration.jsonProvider().parse(res);


        DocumentContext ctx = JsonPath.parse(obj, configuration);

        String path = "$.data.children[?(@.length-2)]";
        //String path = "$..jsonArr[?(@.name == 'nOne')]";
        JsonPath jsonPath = JsonPath.compile(path);
        String asasa=jsonPath.read(res);
        //String asasa= ctx.read(path);

        //String Result= JsonPath.read(res,"$.data.children[(@.length-2)].id").toString();

        //String Result= JsonPath.read(res,"$.data.children[(@.length-2)]").toString();

        System.out.println(asasa);
    }
}
