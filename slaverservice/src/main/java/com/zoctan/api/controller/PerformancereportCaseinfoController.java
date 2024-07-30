package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.PerformancereportCaseinfo;
import com.zoctan.api.service.PerformancereportCaseinfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Season
* @date 2024/07/30
*/
@RestController
@RequestMapping("/performancereport/caseinfo")
public class PerformancereportCaseinfoController {
@Resource
private PerformancereportCaseinfoService performancereportCaseinfoService;

@PostMapping
public Result add(@RequestBody PerformancereportCaseinfo performancereportCaseinfo) {
performancereportCaseinfoService.save(performancereportCaseinfo);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
performancereportCaseinfoService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody PerformancereportCaseinfo performancereportCaseinfo) {
performancereportCaseinfoService.update(performancereportCaseinfo);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
PerformancereportCaseinfo performancereportCaseinfo = performancereportCaseinfoService.getById(id);
return ResultGenerator.genOkResult(performancereportCaseinfo);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<PerformancereportCaseinfo> list = performancereportCaseinfoService.listAll();
PageInfo<PerformancereportCaseinfo> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
