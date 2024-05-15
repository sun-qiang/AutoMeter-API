package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Project;

import java.util.List;
import java.util.Map;

public interface ProjectMapper extends MyMapper<Project> {
    List<Project> findProjectWithName(final Map<String, Object> params);
    /**
     * 更新字典内容
     *
     * @param params 参数
     * @return 用户列表
     */
    void updateProject(Project params);
}