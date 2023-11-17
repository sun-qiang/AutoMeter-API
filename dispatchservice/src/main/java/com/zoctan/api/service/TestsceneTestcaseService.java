package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.entity.TestsceneTestcase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2023/10/29
*/
public interface TestsceneTestcaseService extends Service<TestsceneTestcase> {
    List<TestsceneTestcase> findCasebyscenenid(final Map<String, Object> params);
    void savetestscenencase(final List<TestsceneTestcase> testcase);
    void updatescenenCaseorder(final long id,long caseorder);
    List<TestsceneTestcase> findcasebytestscenenid(final  long executeplanid, String casetype);

}
