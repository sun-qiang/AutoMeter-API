package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.entity.TestsceneTestcase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestplanTestsceneMapper extends MyMapper<TestplanTestscene> {
    void savetestplanscenen(@Param("casedataList")final List<TestplanTestscene> testcase);
    List<TestplanTestscene> findscenebyexecplanid(final Map<String, Object> params);
    void updateplanscenenorder(final long id,long ordernum);
    void updateplanscenename(@Param("testscenenid")final long testscenenid,@Param("scenename")String scenename);
    void removeexecuteplantestscene(@Param("testplanid")final long planid,@Param("testscenenid")final long sceneid);
    void removeexecuteplanalltestscene(final long planid);
    List<TestplanTestscene> getstaticsplancases(@Param("projectid")long projectid);

}