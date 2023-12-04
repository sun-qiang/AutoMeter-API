package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.DispatchMapper;
import com.zoctan.api.entity.Dispatch;
import com.zoctan.api.service.DispatchService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/11/21
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class DispatchServiceImpl extends AbstractService<Dispatch> implements DispatchService {
@Resource
private DispatchMapper dispatchMapper;

    @Override
    public List<Dispatch> findDispatchWithName(Map<String, Object> params) {
        return dispatchMapper.findDispatchWithName(params);
    }

    @Override
    public List<Dispatch> getDispatchWithstatus(long testplanid, String batchname, String status) {
        return dispatchMapper.getDispatchWithstatus(testplanid, batchname, status);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

    @Override
    public Long getdispatchnum(long testplanid, String batchname) {
        return dispatchMapper.getdispatchnum(testplanid,batchname);
    }
}
