package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.TestsceneDispatch;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/07
*/
public interface TestsceneDispatchService extends Service<TestsceneDispatch> {
List<TestsceneDispatch> findDicWithName( Long planid,Long batchid,Long Slaverid);
int ifexist(Condition condition);
    void insertBatchDispatch(@Param("dispatchList") final List<TestsceneDispatch> dispatchList);

}
