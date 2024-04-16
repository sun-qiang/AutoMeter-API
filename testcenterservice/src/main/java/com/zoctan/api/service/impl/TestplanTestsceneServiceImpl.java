package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.TestplanTestsceneMapper;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.service.TestplanTestsceneService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2023/11/01
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TestplanTestsceneServiceImpl extends AbstractService<TestplanTestscene> implements TestplanTestsceneService {
@Resource
private TestplanTestsceneMapper testplanTestsceneMapper;

    @Override
    public void savetestplanscenen(List<TestplanTestscene> testcase) {
        testplanTestsceneMapper.savetestplanscenen(testcase);
    }

    @Override
    public List<TestplanTestscene> findscenebyexecplanid(Map<String, Object> params) {
        return testplanTestsceneMapper.findscenebyexecplanid(params);
    }

    @Override
    public void updateplanscenenorder(long id, long ordernum) {
        testplanTestsceneMapper.updateplanscenenorder(id, ordernum);
    }

    @Override
    public void updateplanscenename(long testscenenid, String scenename) {
        testplanTestsceneMapper.updateplanscenename(testscenenid, scenename);
    }

    @Override
    public void removeexecuteplantestscene(long planid, long sceneid) {
        testplanTestsceneMapper.removeexecuteplantestscene(planid,sceneid);
    }

    @Override
    public void removeexecuteplanalltestscene(long planid) {
        testplanTestsceneMapper.removeexecuteplanalltestscene(planid);
    }

    @Override
    public List<TestplanTestscene> getstaticsplancases(long projectid) {
        return testplanTestsceneMapper.getstaticsplancases(projectid);
    }
}
