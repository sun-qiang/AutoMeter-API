package com.zoctan.api.service;

import com.zoctan.api.entity.ApicasesAssert;
import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.entity.TestsceneTestcase;
import com.zoctan.api.core.service.Service;

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
    void updatescenecaselogic(final long id,long loopnums,String stopflag);
    List<TestsceneTestcase> finddeployunitbyscenenid(long sceneid);
    List<TestsceneTestcase> findcasebyscenenid(long sceneid);


}
