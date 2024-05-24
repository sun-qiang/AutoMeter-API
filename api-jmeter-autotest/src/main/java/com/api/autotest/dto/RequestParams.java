package com.api.autotest.dto;

public class RequestParams {
    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }

    public String getKeyValue() {
        return KeyValue;
    }

    public void setKeyValue(String keyValue) {
        KeyValue = keyValue;
    }

    private String KeyName;
    private String KeyValue;

}
