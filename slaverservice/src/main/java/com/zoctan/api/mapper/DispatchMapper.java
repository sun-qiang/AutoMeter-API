package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Dispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DispatchMapper extends MyMapper<Dispatch> {

    void insertBatchDispatch(@Param("dispatchList") final List<Dispatch> dispatchList);

    Integer findbusythreadnums(@Param("slaverid") Long slaverid ,@Param("status") String status);

    List<Dispatch> getcasebyslaverid(@Param("slaverid") Long slaverid , @Param("status") String status , @Param("plantype") String plantype, @Param("maxthread")Long maxthread);

    List<Dispatch> getcasebyslaveridandrunmode(@Param("slaverid") Long slaverid , @Param("status") String status , @Param("plantype") String plantype, @Param("runmode") String runmode, @Param("maxthread")Long maxthread);

    List<Dispatch> getdoingcasebyslaverid(@Param("slaverid") Long slaverid , @Param("status") String status , @Param("plantype") String plantype);

    void updatedispatchstatus(@Param("status") String status,@Param("slaverid")Long slaverid,@Param("execplanid")Long execplanid,@Param("batchid")Long batchid, @Param("sceneid")Long Sceneid);

    void updatedispatchstatusandmemo(@Param("status") String status,@Param("memo") String memo,@Param("slaverid")Long slaverid,@Param("execplanid")Long execplanid,@Param("batchid")Long batchid,@Param("testcaseid")Long testcaseid);


}