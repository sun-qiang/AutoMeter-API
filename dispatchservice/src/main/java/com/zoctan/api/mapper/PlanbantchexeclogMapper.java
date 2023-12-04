package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Planbantchexeclog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanbantchexeclogMapper extends MyMapper<Planbantchexeclog> {
    void SaveExecLog(@Param("batchid") Long batchid, @Param("exec_time") String exec_time, @Param("memo") String memo);

    List<Planbantchexeclog> GetPlanExecLog(@Param("batchid") Long batchid, @Param("exec_time") String exec_time);

    List<Planbantchexeclog> GetPlanExecLogToday(@Param("batchid") Long batchid, @Param("exec_time") String exec_time);


}