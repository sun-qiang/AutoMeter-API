package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.TestsceneDispatch;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/07
*/
public interface TestsceneDispatchService extends Service<TestsceneDispatch> {
List<TestsceneDispatch> findDicWithName(final Map<String, Object> params);
    List<TestsceneDispatch> findscenebypbs(long planid,String batchname,long slaverid);


int ifexist(Condition condition);
void updateDic(TestsceneDispatch params);
}
