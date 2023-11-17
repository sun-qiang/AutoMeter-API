package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.entity.TestsceneTestcase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestsceneTestcaseMapper extends MyMapper<TestsceneTestcase> {
    List<TestsceneTestcase> findCasebyscenenid(final Map<String, Object> params);
    void savetestscenencase(@Param("casedataList")final List<TestsceneTestcase> testcase);
    void updatescenenCaseorder(@Param("id") long id, @Param("caseorder") long caseorder);

    List<TestsceneTestcase> findcasebytestscenenid(final @Param("testscenenid") long executeplanid, @Param("casetype") String casetype);

}