package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.service.TestplanTestsceneService;
import com.zoctan.api.service.TestsceneService;
import com.zoctan.api.service.TestsceneTestcaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

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

    @Resource
    private TestsceneService testsceneService;

    @Resource
    private ExecuteplanService executeplanService;

    @Resource
    private TestplanTestsceneService testplanTestsceneService;


    @PostMapping
    public Result add(@RequestBody TestsceneTestcase testsceneTestcase) {
        testsceneTestcaseService.save(testsceneTestcase);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        TestsceneTestcase testcase= testsceneTestcaseService.getById(id);
        if(testcase!=null)
        {
            long sceneid=testcase.getTestscenenid();
            Testscene testscene= testsceneService.getById(sceneid);
            testscene.setCasenums(testscene.getCasenums()-1);

            Condition plansencecon =new Condition(TestplanTestscene.class);
            plansencecon.createCriteria().andCondition("testscenenid = " + sceneid);
            List<TestplanTestscene> testplanTestsceneList= testplanTestsceneService.listByCondition(plansencecon);

            for (TestplanTestscene te:testplanTestsceneList) {
                long planid=te.getTestplanid();
                Executeplan executeplan= executeplanService.getById(planid);
                executeplan.setCasecounts(executeplan.getCasecounts()-1);
                executeplanService.update(executeplan);
            }
            testsceneService.update(testscene);
            testsceneTestcaseService.deleteById(id);
        }
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

    @PostMapping("/findscenecasebyid")
    public Result findscenecasebyid(@RequestBody final Map<String, Object> param) {
        long id= Long.parseLong(param.get("id").toString());
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
        if(testsceneTestcaseList.size()>0)
        {
            Long sceneid=testsceneTestcaseList.get(0).getTestscenenid();
            Testscene testscene= testsceneService.getById(sceneid);
            testscene.setCasenums(testscene.getCasenums()+testsceneTestcaseList.size());

            Condition plansencecon =new Condition(TestplanTestscene.class);
            plansencecon.createCriteria().andCondition("testscenenid = " + sceneid);
            List<TestplanTestscene> testplanTestsceneList= testplanTestsceneService.listByCondition(plansencecon);

            for (TestplanTestscene te:testplanTestsceneList) {
                long planid=te.getTestplanid();
                Executeplan executeplan= executeplanService.getById(planid);
                executeplan.setCasecounts(executeplan.getCasecounts()+testsceneTestcaseList.size());
                executeplanService.update(executeplan);
            }
            testsceneService.update(testscene);
        }
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/updatescenenCaseorder")
    public Result updatePlanCaseorder(@RequestBody final Map<String, Object> param) {
        long id= Long.parseLong(param.get("id").toString());
        long caseorder= Long.parseLong(param.get("caseorder").toString());
        this.testsceneTestcaseService.updatescenenCaseorder(id,caseorder);
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/updatescenecaselogic")
    public Result updatescenecaselogic(@RequestBody final Map<String, Object> param) {
        long id= Long.parseLong(param.get("id").toString());
        long loopnums= Long.parseLong(param.get("loopnums").toString());
        String stopflag= param.get("stopflag").toString();
        this.testsceneTestcaseService.updatescenecaselogic(id,loopnums,stopflag);
        return ResultGenerator.genOkResult();
    }
}
