package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Project;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/03/27
*/
public interface ProjectService extends Service<Project> {
    List<Project> findProjectWithName(final Map<String, Object> params);
    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updateProject(Project params);


    int ifexist(Condition condition);
}
