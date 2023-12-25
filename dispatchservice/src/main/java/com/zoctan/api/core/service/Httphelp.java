package com.zoctan.api.core.service;

/**
 * Created by fanseasn on 2020/10/17.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/17
*/

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Httphelp {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String JSON_CONTENT_FORM = "application/json;charset=UTF-8";
    public static final String CONTENT_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    public static String doService(String url,String method, String paramers, HttpHeader header, int connectTimeout) throws Exception {
        switch (method) {
            case "get":
                //return doGet(url, paramers, header, connectTimeout, readTimeout);
            case "post":
                return doPost(url, paramers, header, connectTimeout);
        }
        return null;
    }

    /**
     * post方法
     *
     * @param url
     * @param paramers
     * @param header
     * @param connectTimeout
     * @return
     * @throws IOException
     */
    public static String doPost(String url, String paramers, HttpHeader header, int connectTimeout) throws Exception {
        String responseData = "";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout)
                    .setSocketTimeout(connectTimeout).build();
            String query = null;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if(header.getParams().size()>0)
            {
                setHeader(httpPost, header);
            }
            //json数据
            httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_FORM);
            query = paramers;
            if (query != null) {
                HttpEntity reqEntity = new StringEntity(query,"utf-8");
                httpPost.setEntity(reqEntity);
            }
            httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            responseData = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            responseData=e.getMessage();
            throw new Exception(responseData);
        } finally {
            if(httpResponse!=null)
            {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    responseData=e.getMessage();
                }
            }
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    responseData=e.getMessage();
                }
            }
        }
        System.out.println("post responseData is :"+responseData);
        return responseData;
    }


    /**
     * get方法
     *
     * @param url
     * @param params
     * @param header
     * @param connectTimeout
     * @param readTimeout
     * @return
     * @throws IOException
     */
    public static String doGet(String url, HttpParamers params, HttpHeader header, int connectTimeout, int readTimeout) throws IOException {
        String responseData = "";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            String query = params.getQueryString(DEFAULT_CHARSET);
            url = buildGetUrl(url, query);
            HttpGet httpGet = new HttpGet(url);
            if(header.getParams().size()>0)
            {
                setHeader(httpGet, header);
            }
            httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpGet);
            HttpEntity resEntity = httpResponse.getEntity();
            responseData = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(httpResponse!=null)
            {
                httpResponse.close();
            }
            if(httpClient!=null) {
                httpClient.close();
            };
        }
        return responseData;
    }

    private static void setHeader(HttpRequestBase httpRequestBase, HttpHeader header) {
        if (header != null) {
            Map<String, String> headerMap = header.getParams();
            if (headerMap != null && !headerMap.isEmpty()) {
                Set<Map.Entry<String, String>> entries = headerMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    httpRequestBase.setHeader(name, value);
                }
            }
        }
    }

    private static String buildGetUrl(String url, String query) throws IOException {
        if (query == null || query.equals("")) {
            return url;
        }
        StringBuilder newUrl = new StringBuilder(url);
        boolean hasQuery = url.contains("?");
        boolean hasPrepend = (url.endsWith("?")) || (url.endsWith("&"));
        if (!hasPrepend) {
            if (hasQuery) {
                newUrl.append("&");
            } else {
                newUrl.append("?");
                hasQuery = true;
            }
        }
        newUrl.append(query);
        hasPrepend = false;
        return newUrl.toString();
    }
}
