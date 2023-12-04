package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.entity.Planbantchexeclog;
import com.zoctan.api.mapper.PlanbantchexeclogMapper;
import com.zoctan.api.service.PlanbantchexeclogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author SeasonFan
* @date 2021/12/26
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PlanbantchexeclogServiceImpl extends AbstractService<Planbantchexeclog> implements PlanbantchexeclogService {
@Resource
private PlanbantchexeclogMapper planbantchexeclogMapper;

}
