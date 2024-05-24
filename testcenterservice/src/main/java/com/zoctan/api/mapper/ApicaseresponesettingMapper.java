package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Apicaseresponesetting;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

public interface ApicaseresponesettingMapper extends MyMapper<Apicaseresponesetting> {
    List<Apicaseresponesetting> findDicWithName(final Map<String, Object> params);
    int ifexist(Condition condition);
    void updateDic(Apicaseresponesetting params);
}