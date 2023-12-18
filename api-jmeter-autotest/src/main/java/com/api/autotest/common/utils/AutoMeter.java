package com.api.autotest.common.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoMeter {

    public static Long caseid;

    public static String GetRequestValue(String Param, String Property) {
        String Value = "";
        try {
            String sql = "SELECT a.* FROM api_casedata a where a.caseid =" + caseid + " and a.apiparam ='" + Param + "' and a.propertytype = '" + Property + "'";
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                HashMap<String, String> hs = result.get(0);
                Value = hs.get("apiparamvalue");
            }
        } catch (Exception ex) {
        }
        return Value;
    }

    public static void SetRequestValue(String Param, String Property, String Value) {
        String sql = "update api_casedata a set a.apiparamvalue ='" + Value + "' where a.caseid =" + caseid + " and a.apiparam ='" + Param + "' and a.propertytype = '" + Property + "'";
        try {
            MysqlConnectionUtils.update(sql);
        } catch (Exception exception) {
        }
    }
}
