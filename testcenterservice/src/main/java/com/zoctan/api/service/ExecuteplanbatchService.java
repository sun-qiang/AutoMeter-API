package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Executeplanbatch;
import com.zoctan.api.entity.TestplanTestscene;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/10/22
*/
public interface ExecuteplanbatchService extends Service<Executeplanbatch> {
    int ifexist(Condition condition);

    List<Executeplanbatch> getbatchbyplan(Long executeplanid);

    List<Executeplanbatch> getallexplanbatch();

    List<Executeplanbatch> findexplanbatchWithName(final Map<String, Object> params);

    List<Executeplanbatch> getstopplanbatchList(Long executeplanid);

    List<Executeplanbatch> getrecentbatchbyid(Long executeplanid);

    void saveplanbatchscenen(final List<Executeplanbatch> testcase);

    void updatebatchstatus(Long id,String batchname);



}
