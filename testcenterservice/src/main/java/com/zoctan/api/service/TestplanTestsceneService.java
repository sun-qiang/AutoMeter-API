package com.zoctan.api.service;

import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.TestsceneTestcase;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2023/11/01
*/
public interface TestplanTestsceneService extends Service<TestplanTestscene> {
    void savetestplanscenen(final List<TestplanTestscene> testcase);
    List<TestplanTestscene> findscenebyexecplanid(final Map<String, Object> params);
    void updateplanscenenorder(final long id,long ordernum);

    void updateplanscenename(final long testscenenid,String scenename);

    void removeexecuteplantestscene(final long planid,final long sceneid);

    void removeexecuteplanalltestscene(final long planid);

    List<TestplanTestscene> getstaticsplancases(long projectid);

}
