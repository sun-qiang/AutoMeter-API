package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Executeplanbatch;
import com.zoctan.api.mapper.ExecuteplanbatchMapper;
import com.zoctan.api.service.ExecuteplanbatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/10/22
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ExecuteplanbatchServiceImpl extends AbstractService<Executeplanbatch> implements ExecuteplanbatchService {
@Resource
private ExecuteplanbatchMapper executeplanbatchMapper;

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public List<Executeplanbatch> getbatchbyplan(Long executeplanid) {
        return executeplanbatchMapper.getbatchbyplan(executeplanid);
    }

    @Override
    public void saveplanbatchscenen(List<Executeplanbatch> testcase) {
        executeplanbatchMapper.saveplanbatchscenen(testcase);
    }

    @Override
    public void updateconditionfail(Long executeplanid, String batchname, String memo) {
        executeplanbatchMapper.updateconditionfail(executeplanid, batchname, memo);
    }


}
