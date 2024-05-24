package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Planmail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface PlanmailMapper extends MyMapper<Planmail> {
    List<Planmail> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(Planmail params);
    void saveplanmail(@Param("planmailList")final List<Planmail> planmailList);
}