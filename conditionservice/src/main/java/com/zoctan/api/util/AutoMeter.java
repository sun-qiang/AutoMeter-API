package com.zoctan.api.util;

import com.zoctan.api.core.service.ParseResponeHelp;
import com.zoctan.api.entity.ApiCasedata;
import com.zoctan.api.service.ApiCasedataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.timestamp.TSRequest;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class AutoMeter {
    @Autowired
    private ApiCasedataService apiCasedataService1;
    private static ApiCasedataService apiCasedataService;

    public static Long caseid;

    @PostConstruct
    public void init() {
        apiCasedataService = apiCasedataService1;
    }

    public static String GetRequestValue(String Param, String Property) {
        String Value = "";
        ApiCasedata apiCasedata = apiCasedataService.GetCaseDatasByCaseIDAndApiparamAndType(caseid, Param, Property);
        if (apiCasedata != null) {
            Value = apiCasedata.getApiparamvalue();
        }
        AutoMeter.log.info("GetRequestValue Param:" + Param + " Property:" + Property + " Value:" + Value);
        return Value;
    }

    public static String GetRequestJsonValue(String JsonPath) throws Exception {
        String Value = "";
        ApiCasedata apiCasedata = apiCasedataService.GetCaseDatasByCaseIDAndApiparamAndType(caseid, "Body", "Body");
        if (apiCasedata != null) {
            String JsonValue = apiCasedata.getApiparamvalue();
            ParseResponeHelp parseResponeHelp = new ParseResponeHelp();
            try {
                Value = parseResponeHelp.ParseJsonRespone(JsonPath, JsonValue);
            } catch (Exception exception) {
                Value = exception.getMessage();
                AutoMeter.log.info("GetRequestJsonValue :" + Value );
                throw new Exception("GetRequestJsonValue方法内参数，JsonPath："+Value);
            }
        }
        AutoMeter.log.info("GetRequestJsonValue JsonPath:" + JsonPath + " Value:" + Value);
        return Value;
    }

    public static void SetRequestJsonValue(String JsonPath, String NewValue) throws Exception {
        ApiCasedata apiCasedata = apiCasedataService.GetCaseDatasByCaseIDAndApiparamAndType(caseid, "Body", "Body");
        if (apiCasedata != null) {
            String Value = apiCasedata.getApiparamvalue();
            AutoMeter.log.info("SetRequestJsonValue JsonPath:" + JsonPath + " NewValue:" + NewValue );
            ParseResponeHelp parseResponeHelp = new ParseResponeHelp();
            try {
                String NewJson= parseResponeHelp.SetJsonRespone(JsonPath, Value, NewValue);
                AutoMeter.log.info("SetRequestJsonValue JsonPath:" + JsonPath + " NewValue:" + NewValue + " NewJson:" + NewJson );
                apiCasedataService.UpdateByCaseIDAndApiparam(caseid, "Body", "Body", NewJson);
            } catch (Exception exception) {
                AutoMeter.log.info("SetRequestJsonValue JsonPath:" + JsonPath + " NewValue:" + NewValue + " 当前Body的Json串：" + Value + " 中用此jsonpath:" + JsonPath + " 未能提取到值，请检查表达式是否正确 "+exception.getMessage());
                throw new Exception("SetRequestJsonValue方法内参数 JsonPath:" +exception.getMessage());
            }
        }
    }

    public static void SetRequestValue(String Param, String Property, String Value) {
        apiCasedataService.UpdateByCaseIDAndApiparam(caseid, Param, Property, Value);
    }
}
