package com.api.autotest.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.*;

public class JsonUtils {

    public static final ObjectMapper MAPPER = initObjectMapper();

    public static String toJson(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    public static ObjectMapper initObjectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    public static Object jsonArray2String(String inString) {

        List<Map<String, Object>> clist = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(inString);
        for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            Map<String, Object> cmp = new HashMap<>();
            if(jsonObject.containsKey("commandType")) {
                String commandType = jsonObject.get("commandType").toString().trim();
                cmp.put("commandType", commandType);
            }
            if(jsonObject.containsKey("fromAccountNo")) {
                String fromAccountNo = jsonObject.get("fromAccountNo").toString().trim();
                cmp.put("fromAccountNo", fromAccountNo);
            }
            if(jsonObject.containsKey("toAccountNo")) {
                String toAccountNo = jsonObject.get("toAccountNo").toString().trim();
                cmp.put("toAccountNo", toAccountNo);
            }
            if(jsonObject.containsKey("amount")) {
                Object amount = jsonObject.get("amount");
                cmp.put("amount", amount);
            }
            if(jsonObject.containsKey("transferType")) {
                String transferType = jsonObject.get("transferType").toString().trim();
                cmp.put("transferType", transferType);
            }
            String tradeNo = "";
            if (jsonObject.get("tradeNo").toString().isEmpty()) {
                tradeNo = "99" + DateUtils.getTimeFormat() + RandomUtils.getNumber(4);
            } else if (jsonObject.get("tradeNo").toString().equals("null")) {
                tradeNo = null;
            }
            if(StringUtils.isNotBlank(tradeNo)) {
                cmp.put("tradeNo", tradeNo);
            }
            cmp.put("subjict", null);
            String outTradeNo = null;
            if (jsonObject.get("outTradeNo").toString().isEmpty()) {
                outTradeNo = "88" + DateUtils.getTimeFormat() + RandomUtils.getNumber(4);
            } else if (jsonObject.get("outTradeNo").toString().equals("null")) {
                outTradeNo = null;
            }
            if(StringUtils.isNotBlank(outTradeNo)) {
                cmp.put("outTradeNo", outTradeNo);
            }
            clist.add(cmp);
        }
        return clist;
    }

    public static void main(String[] args) {
        String commandLs = "[{\"amount\":\"\",\"commandType\":\"transfer\",\"fromAccountNo\":\"\",\"notes\":\"\",\"outTradeNo\":\"88201707271005164095\",\"toAccountNo\":\"\",\"tradeNo\":\"99201707271005169165\",\"transferType\":\"\"}{\"amount\":\"\",\"commandType\":\"freeze\",\"fromAccountNo\":\"\",\"notes\":\"\",\"outTradeNo\":\"88201707271005160857\",\"tradeNo\":\"99201707271005169156\",\"transferType\":\"\"}{\"amount\":\"\",\"commandType\":\"unfreeze\",\"fromAccountNo\":\"\",\"notes\":\"\",\"outTradeNo\":\"88201707271005169898\",\"tradeNo\":\"99201707271005167635\",\"transferType\":\"\"}]";
        System.out.println(jsonArray2String(commandLs));
    }

}