package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Executeplanbatch;
import com.zoctan.api.mapper.ExecuteplanbatchMapper;
import com.zoctan.api.service.ExecuteplanbatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2020/10/22
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ExecuteplanbatchServiceImpl extends AbstractService<Executeplanbatch> implements ExecuteplanbatchService {
@Resource
private ExecuteplanbatchMapper executeplanbatchMapper;

}
