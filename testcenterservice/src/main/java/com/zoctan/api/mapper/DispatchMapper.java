package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Dispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DispatchMapper extends MyMapper<Dispatch> {
    List<Dispatch> findDispatchWithName(final Map<String, Object> params);
    List<Dispatch> getDispatchWithstatus(@Param("execplanid") long testplanid, @Param("batchname") String batchname,@Param("status") String status);
    Long getdispatchnum(@Param("execplanid") long testplanid, @Param("batchname") String batchname);

}