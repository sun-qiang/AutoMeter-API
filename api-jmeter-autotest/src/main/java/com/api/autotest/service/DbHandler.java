package com.api.autotest.service;

import com.api.autotest.common.utils.HttpUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by harvey.xu on 2017/8/11.
 */
public class DbHandler {

//    private static String requestUrl = "http://192.168.1.192:8090/autotest/";
    private static String requestUrl = "http://47.100.31.205:8090/autotest/";

    public static String saveAutoTestTaskInfo(Map requestMap) {

        requestMap.put("createTime", new Date());
        String result = HttpUtils.postRestTemplate(requestUrl + "saveAutoTestTaskInfo", requestMap);
        return result;
    }

    public static String updateAutoTestTaskResult(int batchStatus, String batchNo) {

        Map requestMap = new HashMap();
        requestMap.put("batchNo", batchNo);
        requestMap.put("batchStatus", batchStatus);
        String result = HttpUtils.postRestTemplate(requestUrl + "updateAutoTestTaskResult", requestMap);
        return result;
    }
}
