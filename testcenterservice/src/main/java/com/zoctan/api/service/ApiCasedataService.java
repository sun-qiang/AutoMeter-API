package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.dto.Casedata;
import com.zoctan.api.entity.ApiCasedata;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Zoctan
* @date 2020/09/21
*/
public interface ApiCasedataService extends Service<ApiCasedata> {
    /**
     *
     *
     * @param casedata 用例参数数据
     */
    void save(Casedata casedata);

    void saveCasedata(List<ApiCasedata> casedataMap);

    List<ApiCasedata> getparamvaluebycaseidandtype(final Map<String, Object> params);

    List<ApiCasedata> getparamvaluebycaseidandtype(Long caseid,String PrppertyType);

    List<ApiCasedata> getdataidbyapiidandtypeandparatype(Long apiid,String PrppertyType,String paramstype);

    List<ApiCasedata> getdataidbyapiidandtypeandapiparam(Long apiid,String PrppertyType,String apiparam);

    List<ApiCasedata> getdataidbyapiidandtype(Long apiid,String PrppertyType);

    void deletcasedatabyid(Long caseid);

    void updateparambycaseidandprotypeandapiparam(Long caseid,String propertytype,String apiparam, String oldapiparam,String paramstype,String encyptype);
}
