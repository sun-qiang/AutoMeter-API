package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ApicasesAssert;
import com.zoctan.api.entity.ExecuteplanTestcase;
import com.zoctan.api.entity.TestsceneTestcase;
import com.zoctan.api.service.TestsceneTestcaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2023/10/29
 */
@RestController
@RequestMapping("/testscene/testcase")
public class TestsceneTestcaseController {
    @Resource
    private TestsceneTestcaseService testsceneTestcaseService;

    @PostMapping
    public Result add(@RequestBody TestsceneTestcase testsceneTestcase) {
        testsceneTestcaseService.save(testsceneTestcase);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        testsceneTestcaseService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody TestsceneTestcase testsceneTestcase) {
        testsceneTestcaseService.update(testsceneTestcase);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        TestsceneTestcase testsceneTestcase = testsceneTestcaseService.getById(id);
        return ResultGenerator.genOkResult(testsceneTestcase);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TestsceneTestcase> list = testsceneTestcaseService.listAll();
        PageInfo<TestsceneTestcase> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/findcasebyscenenid")
    public Result searchbycaseid(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<TestsceneTestcase> list = this.testsceneTestcaseService.findCasebyscenenid(param);
        final PageInfo<TestsceneTestcase> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/addcases")
    public Result addcase(@RequestBody final List<TestsceneTestcase> testsceneTestcaseList) {
        testsceneTestcaseService.savetestscenencase(testsceneTestcaseList);
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/updatescenenCaseorder")
    public Result updatePlanCaseorder(@RequestBody final Map<String, Object> param) {
        long id= Long.parseLong(param.get("id").toString());
        long caseorder= Long.parseLong(param.get("caseorder").toString());
        this.testsceneTestcaseService.updatescenenCaseorder(id,caseorder);
        return ResultGenerator.genOkResult();
    }
}
