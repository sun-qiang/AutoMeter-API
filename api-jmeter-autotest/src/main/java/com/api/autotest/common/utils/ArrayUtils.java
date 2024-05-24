package com.api.autotest.common.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harvey.xu on 2017/10/17.
 */
public class ArrayUtils {

    public static String arrayList2String(ArrayList<HashMap<String, String>> arrayList) {
        if (arrayList.size() < 1) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (int i = 0; i < arrayList.size(); i++) {
            String key = String.valueOf(arrayList.get(i).keySet()).replace("[","").replace("]","");
            sb.append(arrayList.get(i).get(key));
            sb.append(",");
        }
        String str = sb.substring(0, sb.length() - 1) + "}";
        return str;
    }
}
