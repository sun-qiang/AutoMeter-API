package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.PerformancereportThread;
import com.zoctan.api.service.PerformancereportThreadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Season
* @date 2024/07/29
*/
@RestController
@RequestMapping("/performancereport/thread")
public class PerformancereportThreadController {
@Resource
private PerformancereportThreadService performancereportThreadService;

@PostMapping
public Result add(@RequestBody PerformancereportThread performancereportThread) {
performancereportThreadService.save(performancereportThread);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
performancereportThreadService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody PerformancereportThread performancereportThread) {
performancereportThreadService.update(performancereportThread);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
PerformancereportThread performancereportThread = performancereportThreadService.getById(id);
return ResultGenerator.genOkResult(performancereportThread);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<PerformancereportThread> list = performancereportThreadService.listAll();
PageInfo<PerformancereportThread> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
