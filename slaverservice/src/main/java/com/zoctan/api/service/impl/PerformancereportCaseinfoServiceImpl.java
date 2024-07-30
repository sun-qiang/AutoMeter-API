package com.zoctan.api.service.impl;

import com.zoctan.api.mapper.PerformancereportCaseinfoMapper;
import com.zoctan.api.entity.PerformancereportCaseinfo;
import com.zoctan.api.service.PerformancereportCaseinfoService;
import com.zoctan.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author Season
* @date 2024/07/30
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PerformancereportCaseinfoServiceImpl extends AbstractService<PerformancereportCaseinfo> implements PerformancereportCaseinfoService {
@Resource
private PerformancereportCaseinfoMapper performancereportCaseinfoMapper;

}
