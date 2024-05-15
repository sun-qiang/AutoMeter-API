package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Project;
import com.zoctan.api.mapper.ProjectMapper;
import com.zoctan.api.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2022/03/27
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl extends AbstractService<Project> implements ProjectService {
@Resource
private ProjectMapper projectMapper;

    @Override
    public List<Project> findProjectWithName(Map<String, Object> params) {
        return projectMapper.findProjectWithName(params);
    }

    @Override
    public void updateProject(Project params) {
        projectMapper.updateProject(params);
    }

    @Override
    public int ifexist(Condition con) {
        return countByCondition(con);
    }

}
