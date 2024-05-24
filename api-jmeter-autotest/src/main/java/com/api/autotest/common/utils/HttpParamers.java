package com.api.autotest.common.utils;

/**
 * Created by fanseasn on 2020/10/17.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/17
*/

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求参数
 */


public class HttpParamers {
//    public enum HttpMethod {
//        GET, POST;
//    }
    private Map<String, Object> params = new HashMap<String, Object>();
    //private HttpMethod httpMethod;
    private String jsonParamer = "";

//    public HttpParamers(HttpMethod httpMethod) {
//        this.httpMethod = httpMethod;
//    }

//    public  HttpParamers Paramers() {
//        return new HttpParamers();
//    }
//
//    public static HttpParamers httpGetParamers() {
//        return new HttpParamers(HttpMethod.GET);
//    }

    public HttpParamers addParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

//    public HttpMethod getHttpMethod() {
//        return this.httpMethod;
//    }

    public String getQueryString() throws UnsupportedEncodingException {
        if ((this.params == null) || (this.params.isEmpty())) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = this.params.entrySet();

        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            Object value = entry.getValue();
            query.append("&").append(name).append("=").append(value);//.append(URLEncoder.encode(value, "UTF-8"));
        }
        return query.substring(1);
    }

    public boolean isJson() {
        return !isEmpty(this.jsonParamer);
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public String toString() {
        return "HttpParamers " + JSON.toJSONString(this);
    }

    public String getJsonParamer() {
        return this.jsonParamer;
    }

    public void setJsonParamer() {
        this.jsonParamer = JSON.toJSONString(this.params);
    }

    private  boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }
}
