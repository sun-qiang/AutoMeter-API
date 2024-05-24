package com.api.autotest.common.utils;

/**
 * Created by fanseasn on 2020/10/17.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/17
*/

import java.util.HashMap;
import java.util.Map;

/**
 * 请求头
 */
public class HttpHeader {
    private Map<String, Object> params = new HashMap<String, Object>();

    public HttpHeader addParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }
}