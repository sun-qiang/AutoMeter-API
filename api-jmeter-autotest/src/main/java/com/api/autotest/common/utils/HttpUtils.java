package com.api.autotest.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtils {

    public static String sendGet(String url, Map<String, String> parameters) {
        String result = "";
        BufferedReader in = null;// 读取响应输入流
        StringBuffer sb = new StringBuffer();// 存储参数
        String params;// 编码之后的参数
        String requestUrl;
        try {
            if (parameters.size() < 1) {
                requestUrl = url;
            } else if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name),
                                    "UTF-8"));
                }
                params = sb.toString();
                requestUrl = url + "?" + params;
            } else {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name),
                                    "UTF-8")).append("&");
                }
                String tempParams = sb.toString();
                params = tempParams.substring(0, tempParams.length() - 1);
                requestUrl = url + "?" + params;
            }

            URL realUrl = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();

//            Map<String, List<String>> map = connection.getHeaderFields();
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常!");
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String httpUrl, Map sPara) {
        String response = "";
        List keys = new ArrayList(sPara.keySet());
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();
        method.setRequestHeader("Content-type", "text/xml; charset=GB2312");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(2000));
        try {
            URL url = new URL(httpUrl);
            method.setPath(url.getPath());
            client.getHostConfiguration().setHost(url.getHost(), url.getPort(), url.getProtocol());
            NameValuePair[] nameValuePairs = new NameValuePair[sPara.size()];
            for (int i = 0; i < sPara.size(); i++) {
                String name = (String) keys.get(i);
                String value = (String) sPara.get(name);
                nameValuePairs[i] = new NameValuePair(name, value);
            }
            method.setQueryString(nameValuePairs);
            int rescode = client.executeMethod(method);
            if (rescode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8"));
                String curline;
                while ((curline = reader.readLine()) != null) {
                    response += curline;
                }
            } else if(rescode==302){
                Header header = method.getResponseHeader("Location");
                response = header.getValue();
            }else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return response;
    }

    public static String postWithRequestBody(String httpUrl, Map sPara) {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();
        method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(60000));
        try {
            URL url = new URL(httpUrl);
            method.setPath(url.getPath());
            client.getHostConfiguration().setHost(url.getHost(), url.getPort(), url.getProtocol());
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(sPara);
            String body = jsonObject.toString();
            method.setRequestBody(body);
            int rescode = client.executeMethod(method);
            if (rescode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8"));
                String curline;
                while ((curline = reader.readLine()) != null) {
                    response += curline;
                }
                System.out.println(httpUrl + " response:" + response);
            } else {
                System.out.println(httpUrl + " error:" + rescode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return response;
    }

    public static String postWithBody(String httpUrl, Map sPara) {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();
        method.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(60000));
        try {
            URL url = new URL(httpUrl);
            method.setPath(url.getPath());
            client.getHostConfiguration().setHost(url.getHost(), url.getPort(), url.getProtocol());
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(sPara);
            String body = jsonObject.toString();
            method.setRequestEntity(new StringRequestEntity(body));
            int rescode = client.executeMethod(method);
            if (rescode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8"));
                String curline;
                while ((curline = reader.readLine()) != null) {
                    response += curline;
                }
//                System.out.println(httpUrl + " response:" + response);
            } else {
                System.out.println(httpUrl + " error:" + rescode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return response;
    }

    public static String postWithBodyContenttype(String httpUrl, Map sPara,String Contenttype) {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();
        method.setRequestHeader("Content-type", Contenttype);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(60000));
        try {
            URL url = new URL(httpUrl);
            method.setPath(url.getPath());
            client.getHostConfiguration().setHost(url.getHost(), url.getPort(), url.getProtocol());
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(sPara);
            String body = jsonObject.toString();
            method.setRequestEntity(new StringRequestEntity(body,"application/json","UTF-8"));
            //method.setRequestEntity(new StringRequestEntity(body));
            int rescode = client.executeMethod(method);
            if (rescode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8"));
                String curline;
                while ((curline = reader.readLine()) != null) {
                    response += curline;
                }
//                System.out.println(httpUrl + " response:" + response);
            } else {
                System.out.println(httpUrl + " error:" + rescode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return response;
    }

    public static String postRestTemplate(String httpUrl, Map requestMap) {
        String requestBody = JsonUtils.toJson(requestMap);
        String responseBody = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            stringHttpMessageConverter.setWriteAcceptCharset(false);
            for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
                if (restTemplate.getMessageConverters().get(i) instanceof StringHttpMessageConverter) {
                    restTemplate.getMessageConverters().remove(i);
                    restTemplate.getMessageConverters().add(i, stringHttpMessageConverter);
                    break;
                }
            }
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    RequestEntity.post(java.net.URI.create(httpUrl)).body(requestBody), String.class);
            responseBody = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBody;
    }

}

