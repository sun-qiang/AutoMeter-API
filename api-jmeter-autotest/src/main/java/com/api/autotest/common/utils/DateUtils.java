package com.api.autotest.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getTimeFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String sj_str = dateFormat.format(new Date());

        return sj_str;
    }
    public static String getTimeFormat(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String sj_str = dateFormat.format(new Date());

        return sj_str;
    }

    public static String getTimeFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String sj_str = dateFormat.format(date);

        return sj_str;
    }

    public static void main(String[] args) {
        System.out.println(getTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

}

