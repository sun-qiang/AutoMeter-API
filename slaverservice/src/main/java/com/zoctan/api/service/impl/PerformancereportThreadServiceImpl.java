package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PerformancereportThreadMapper;
import com.zoctan.api.entity.PerformancereportThread;
import com.zoctan.api.service.PerformancereportThreadService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author Season
* @date 2024/07/29
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PerformancereportThreadServiceImpl extends AbstractService<PerformancereportThread> implements PerformancereportThreadService {
@Resource
private PerformancereportThreadMapper performancereportThreadMapper;

}
