package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.TestsceneDispatch;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface TestsceneDispatchMapper extends MyMapper<TestsceneDispatch> {
    List<TestsceneDispatch> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(TestsceneDispatch params);
    List<TestsceneDispatch> findscenebypbs(@Param("execplanid") long planid, @Param("batchname")String batchname, @Param("slaverid")long slaverid);

}