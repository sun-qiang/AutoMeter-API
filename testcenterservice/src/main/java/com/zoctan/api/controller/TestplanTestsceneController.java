package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.entity.TestsceneTestcase;
import com.zoctan.api.service.TestplanTestsceneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2023/11/01
 */
@RestController
@RequestMapping("/testplan/testscene")
public class TestplanTestsceneController {
    @Resource
    private TestplanTestsceneService testplanTestsceneService;

    @PostMapping
    public Result add(@RequestBody TestplanTestscene testplanTestscene) {
        testplanTestsceneService.save(testplanTestscene);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        testplanTestsceneService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody TestplanTestscene testplanTestscene) {
        testplanTestsceneService.update(testplanTestscene);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        TestplanTestscene testplanTestscene = testplanTestsceneService.getById(id);
        return ResultGenerator.genOkResult(testplanTestscene);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TestplanTestscene> list = testplanTestsceneService.listAll();
        PageInfo<TestplanTestscene> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/findscenebyexecplanid")
    public Result findscenebyexecplanid(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<TestplanTestscene> list = this.testplanTestsceneService.findscenebyexecplanid(param);
        final PageInfo<TestplanTestscene> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/addplanscene")
    public Result addcase(@RequestBody final List<TestplanTestscene> testsceneTestcaseList) {
        testplanTestsceneService.savetestplanscenen(testsceneTestcaseList);
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/updateplanscenenorder")
    public Result updatePlanCaseorder(@RequestBody final Map<String, Object> param) {
        long id= Long.parseLong(param.get("id").toString());
        long caseorder= Long.parseLong(param.get("ordernum").toString());
        this.testplanTestsceneService.updateplanscenenorder(id,caseorder);
        return ResultGenerator.genOkResult();
    }


    @PostMapping("/deletescene")
    public Result deletescene(@RequestBody final Map<String, Object> param) {
        long planid= Long.parseLong(param.get("testplanid").toString());
        long testscenenid= Long.parseLong(param.get("testscenenid").toString());
        this.testplanTestsceneService.removeexecuteplantestscene(planid,testscenenid);
        return ResultGenerator.genOkResult();
    }
}
