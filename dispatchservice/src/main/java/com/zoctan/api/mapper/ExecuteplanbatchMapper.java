package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Dispatch;
import com.zoctan.api.entity.Executeplanbatch;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

public interface ExecuteplanbatchMapper extends MyMapper<Executeplanbatch> {
    int ifexist(Condition condition);

    List<Executeplanbatch> getbatchbyplan(@Param("executeplanid") Long executeplanid);

    Executeplanbatch getbatchidbyplanidandbatchnameandsceneid(@Param("executeplanid") Long executeplanid, @Param("batchname") String batchname,@Param("sceneid") Long sceneid);

    void updatestatusbyplanandbatch(@Param("status") String status,@Param("executeplanid")Long executeplanid,@Param("batchname")String batchname);

    List<Executeplanbatch> getbatchbyexectype(@Param("exectype") String exectype);

    Executeplanbatch getrecentbatch(@Param("status") String status,@Param("exectype") String exectype,@Param("usetype") String usetype);

    Executeplanbatch getrecentsinglebatch(@Param("status") String status,@Param("exectype") String exectype,@Param("executeplanid") Long executeplanid, @Param("batchname") String batchname);

    List<Executeplanbatch> getplanbatch(@Param("status") String status,@Param("exectype") String exectype,@Param("executeplanid") Long executeplanid, @Param("batchname") String batchname);

    List<Executeplanbatch> getrecentallbatch(@Param("status") String status,@Param("exectype") String exectype,@Param("usetype") String usetype);

    List<Executeplanbatch> getbatchtestscene(@Param("status") String status,@Param("executeplanid") Long executeplanid, @Param("batchname") String batchname, @Param("exectype") String exectype);

    void updateconditionfail(@Param("executeplanid") Long executeplanid,@Param("batchname") String batchname,@Param("memo") String memo);

    void updateconditionstatus( @Param("executeplanid") Long executeplanid,@Param("batchname") String batchname,@Param("memo") String memo,@Param("status")String status);


    void saveplanbatchscenen(@Param("casedataList")final List<Executeplanbatch> testcase);


}