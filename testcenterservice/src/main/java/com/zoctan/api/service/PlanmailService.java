package com.zoctan.api.service;

import com.zoctan.api.entity.Planmail;
import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.TestsceneTestcase;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/05/19
 */
public interface PlanmailService extends Service<Planmail> {
    List<Planmail> findDicWithName(final Map<String, Object> params);

    int ifexist(Condition condition);

    void updateDic(Planmail params);

    void saveplanmail(final List<Planmail> planmailList);

}
