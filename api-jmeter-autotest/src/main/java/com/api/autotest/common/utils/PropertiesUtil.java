package com.api.autotest.common.utils;

/**
 * Created by fanseasn on 2020/10/16.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/10/16
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Map<String, PropertiesUtil> propsMap;
    Properties properties;

    public static synchronized PropertiesUtil getInstance(String propName) {
        PropertiesUtil instance = null;
        if (propsMap == null) {
            propsMap = new HashMap<String, PropertiesUtil>();
        }
        instance = propsMap.get(propName);
        if (instance == null) {
            instance = new PropertiesUtil(propName);
            propsMap.put(propName, instance);
        }
        return instance;
    }

    private PropertiesUtil(String propName) {
        ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
        InputStream inStream = classLoader.getResourceAsStream(propName);
        properties = new Properties();
        try {
            properties.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("There's no resource file named [" + propName + "]", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        return Integer.valueOf(getProperty(key));
    }

    public int getInt(String key, int defaultValue) {
        return getProperty(key) == null ? defaultValue : Integer.valueOf(getProperty(key));
    }

    public long getLong(String key) {
        return Long.valueOf(getProperty(key));
    }

    public long getLong(String key, long defaultValue) {
        return getProperty(key) == null ? defaultValue : Long.valueOf(getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getProperty(key) == null ? defaultValue : Boolean.parseBoolean(getProperty(key));
    }

    public Properties getProperties() {
        return properties;
    }

    public static void main(String[] args) {
        PropertiesUtil pUtil = PropertiesUtil.getInstance("app.properties");
        String reString = pUtil.getProperty("username");
        System.out.println(reString);
        logger.info(reString);
    }
}
