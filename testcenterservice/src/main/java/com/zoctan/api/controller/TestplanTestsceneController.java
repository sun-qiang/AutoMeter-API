package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.StaticsDataForPie;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.service.ExecuteplanbatchService;
import com.zoctan.api.service.TestplanTestsceneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.service.TestsceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private ExecuteplanService executeplanService;

    @Resource
    private TestsceneService testsceneService;


    @Autowired
    private ExecuteplanbatchService executeplanbatchService;

    @PostMapping
    public Result add(@RequestBody TestplanTestscene testplanTestscene) {
        testplanTestsceneService.save(testplanTestscene);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        TestplanTestscene testplanTestscene = testplanTestsceneService.getById(id);
        if (testplanTestscene != null) {
            long planid = testplanTestscene.getTestplanid();
            long sceneid = testplanTestscene.getTestscenenid();
            Condition con = new Condition(Executeplanbatch.class);
            con.createCriteria().andCondition("executeplanid = " + planid)
                    .andCondition("sceneid = " + sceneid)
                    .andCondition("status != '已完成'");
            List<Executeplanbatch> executeplanbatchList = executeplanbatchService.listByCondition(con);
            if (executeplanbatchList.size() > 0) {
                return ResultGenerator.genFailedResult("当前测试场景有正在运行的执行计划，无法删除！！");
            } else {
                Executeplan executeplan = executeplanService.getById(planid);
                if (executeplan != null) {
                    Testscene testscene = testsceneService.getById(sceneid);
                    testplanTestsceneService.deleteById(id);
                    executeplan.setScenenums(executeplan.getScenenums() - 1);
                    executeplan.setCasecounts(executeplan.getCasecounts() - testscene.getCasenums());
                    executeplanService.update(executeplan);
                    return ResultGenerator.genOkResult();
                } else {
                    return ResultGenerator.genFailedResult("当前集合已经删除！！");
                }
            }
        } else {
            return ResultGenerator.genFailedResult("当前集合下的测试场景已经删除！！");
        }
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
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<TestplanTestscene> list = this.testplanTestsceneService.findscenebyexecplanid(param);
        final PageInfo<TestplanTestscene> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/addplanscene")
    public Result addcase(@RequestBody final List<TestplanTestscene> testsceneTestcaseList) {
        if (testsceneTestcaseList.size() > 0) {
            long planid = testsceneTestcaseList.get(0).getTestplanid();
            Executeplan executeplan = executeplanService.getById(planid);
            long plancasenums = executeplan.getCasecounts();
            long planscenenums = executeplan.getScenenums();
            for (TestplanTestscene tsc : testsceneTestcaseList) {
                long sceneid = tsc.getTestscenenid();
                Testscene testscene = testsceneService.getById(sceneid);
                if (testscene.getCasenums() == 0) {
                    return ResultGenerator.genFailedResult("添加失败，测试场景: "+testscene.getScenename()+" 中还没有用例，请先加载用例");
                } else {
                    plancasenums = plancasenums + testscene.getCasenums();
                    planscenenums = planscenenums + 1;
                }
            }
            executeplan.setCasecounts(plancasenums);
            executeplan.setScenenums(planscenenums);
            executeplanService.update(executeplan);
            testplanTestsceneService.savetestplanscenen(testsceneTestcaseList);
            return ResultGenerator.genOkResult();

        } else {
            return ResultGenerator.genFailedResult("添加失败，测试场景为空");

        }
    }

    @PostMapping("/updateplanscenenorder")
    public Result updatePlanCaseorder(@RequestBody final Map<String, Object> param) {
        long id = Long.parseLong(param.get("id").toString());
        long caseorder = Long.parseLong(param.get("ordernum").toString());
        this.testplanTestsceneService.updateplanscenenorder(id, caseorder);
        return ResultGenerator.genOkResult();
    }


    @PostMapping("/deletescene")
    public Result deletescene(@RequestBody final Map<String, Object> param) {
        long planid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        this.testplanTestsceneService.removeexecuteplantestscene(planid, testscenenid);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/getstaticsplancases")
    public Result getstaticsplancases(@RequestParam long projectid) {
        List<TestplanTestscene> list = testplanTestsceneService.getstaticsplancases(projectid);
        List<StaticsDataForPie> result=new ArrayList<>();
        for (TestplanTestscene executeplanTestcase: list) {
            StaticsDataForPie staticsDataForPie =new StaticsDataForPie();
            staticsDataForPie.setValue(executeplanTestcase.getId());
            staticsDataForPie.setName(executeplanTestcase.getPlanname());
            result.add(staticsDataForPie);
        }
        return ResultGenerator.genOkResult(result);
    }
}
