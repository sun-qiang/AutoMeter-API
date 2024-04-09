package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Dispatch;
import com.zoctan.api.entity.Slaver;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DispatchMapper extends MyMapper<Dispatch> {

    void insertBatchDispatch(@Param("dispatchList") final List<Dispatch> dispatchList);

    Integer findbusythreadnums(@Param("slaverid") Long slaverid, @Param("status") String status);

    List<Dispatch> getcasebyslaverid(@Param("slaverid") Long slaverid, @Param("status") String status, @Param("plantype") String plantype, @Param("maxthread") Long maxthread);

    List<Dispatch> getcasebyrunmode(@Param("status") String status, @Param("usetype") String usetype, @Param("runmode") String runmode, @Param("execplanid") Long execplanid, @Param("batchname") String batchname);

    void updatedispatchstatus(@Param("status") String status, @Param("slaverid") Long slaverid, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid, @Param("testcaseid") Long testcaseid);

    Integer findbusyslavernums(@Param("slaverlist") final List<Slaver> slaverlist, @Param("status") String status);

    Dispatch getrecentdispatch(@Param("status") String status);

    Dispatch getrecentdispatchbyusetype(@Param("status") String status, @Param("plantype") String plantype);

    List<Dispatch> getdistinctslaverid(@Param("status") String status, @Param("plantype") String plantype, @Param("execplanid") Long execplanid, @Param("batchname") String batchname);

    List<Dispatch> getdistinctslaveridandcaaseid(@Param("status") String status, @Param("plantype") String plantype, @Param("execplanid") Long execplanid, @Param("batchname") String batchname, @Param("testcaseid") Long testcaseid);

    List<Dispatch> getfunctiondispatchsbyslaverid(@Param("slaverid") Long slaverid, @Param("status") String status, @Param("plantype") String plantype, @Param("execplanid") Long execplanid, @Param("batchname") String batchname);


    List<Dispatch> getdispatchsbyslaverid(@Param("slaverid") Long slaverid, @Param("status") String status, @Param("plantype") String plantype);
    List<Dispatch> getdispatchsbypbssid( @Param("slaverid") Long slaverid, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid,@Param("sceneid") Long sceneid);


    void updatedispatchstatusandmemo(@Param("status") String status, @Param("memo") String memo, @Param("slaverid") Long slaverid, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid, @Param("testcaseid") Long testcaseid);

    void updatedispatchfail(@Param("status") String status, @Param("memo") String memo, @Param("slaverid") Long slaverid, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid,@Param("sceneid") Long sceneid);

    void updatedispatchnewslaver( @Param("memo") String memo,@Param("slaverid") Long slaverid, @Param("slaverid") Long newslaverid,@Param("slavername") String slavername, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid,@Param("sceneid") Long sceneid);



    void updatedispatchstatusbyplanandbatch(@Param("status") String status, @Param("execplanid") Long execplanid, @Param("batchname") String batchname);

    void updatedispatchcancel(@Param("status") String status, @Param("execplanid") Long execplanid, @Param("batchid") Long batchid, @Param("memo") String memo);


    List<Dispatch> findnotfinishdis(@Param("slaverid") Long slaverid, @Param("status") String status);
}
