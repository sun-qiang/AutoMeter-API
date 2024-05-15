package com.zoctan.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelp {
    public static void main(String[] args) {
        // 要解析的JSON数据
        String json = "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"zipcode\":\"10001\"}}";
        // 初始化ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 解析JSON数据
            JsonNode rootNode = objectMapper.readTree(json);

            // 获取顶层键的值
            String name = rootNode.get("name").asText();
            int age = rootNode.get("age").asInt();

            // 获取嵌套层级的键的值
            JsonNode addressNode = rootNode.get("address");
            String city = addressNode.get("city").asText();
            String zipcode = addressNode.get("zipcode").asText();

            // 打印获取到的值
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("City: " + city);
            System.out.println("Zipcode: " + zipcode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
