package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Dispatch;
import com.zoctan.api.entity.Executeplanbatch;
import com.zoctan.api.entity.TestsceneDispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestsceneDispatchMapper extends MyMapper<TestsceneDispatch> {
    List<TestsceneDispatch> findDicWithName(@Param("execplanid") Long planid, @Param("batchid")Long batchid, @Param("slaverid")Long Slaverid);
    void insertBatchDispatch(@Param("dispatchList") final List<TestsceneDispatch> dispatchList);
    List<TestsceneDispatch> getdistinctslaver(@Param("execplanid") Long executeplanid, @Param("batchname") String batchname);

}