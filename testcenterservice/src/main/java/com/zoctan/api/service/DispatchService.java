package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Dispatch;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/11/21
*/
public interface DispatchService extends Service<Dispatch> {
    List<Dispatch> findDispatchWithName(final Map<String, Object> params);

    List<Dispatch> getDispatchWithstatus(long testplanid,String batchname,String status);

    int ifexist(Condition condition);
    Long getdispatchnum(long testplanid,String batchname);

}
