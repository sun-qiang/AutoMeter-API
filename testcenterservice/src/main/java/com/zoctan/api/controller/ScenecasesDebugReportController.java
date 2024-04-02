package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ApicasesReport;
import com.zoctan.api.entity.ScenecasesDebugReport;
import com.zoctan.api.service.ScenecasesDebugReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/03/31
 */
@RestController
@RequestMapping("/scenecases/debug/report")
public class ScenecasesDebugReportController {
    @Resource
    private ScenecasesDebugReportService scenecasesDebugReportService;

    @PostMapping
    public Result add(@RequestBody ScenecasesDebugReport scenecasesDebugReport) {
        scenecasesDebugReportService.save(scenecasesDebugReport);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        scenecasesDebugReportService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ScenecasesDebugReport scenecasesDebugReport) {
        scenecasesDebugReportService.update(scenecasesDebugReport);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ScenecasesDebugReport scenecasesDebugReport = scenecasesDebugReportService.getById(id);
        return ResultGenerator.genOkResult(scenecasesDebugReport);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ScenecasesDebugReport> list = scenecasesDebugReportService.listAll();
        PageInfo<ScenecasesDebugReport> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<ScenecasesDebugReport> list = this.scenecasesDebugReportService.findscenecasedebugreportWithName(param);
        final PageInfo<ScenecasesDebugReport> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
