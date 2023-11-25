package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.ConditionApiService;
import com.zoctan.api.service.TestsceneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.service.TestsceneTestcaseService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2023/10/29
 */
@RestController
@RequestMapping("/testscene")
public class TestsceneController {
    @Resource
    private TestsceneService testsceneService;

    @Resource
    private TestsceneTestcaseService testsceneTestcaseService;

    @Resource
    private ConditionApiService conditionApiService;


    @PostMapping
    public Result add(@RequestBody Testscene testscene) {

        Condition con = new Condition(Testscene.class);
        con.createCriteria().andCondition("projectid = " + testscene.getProjectid())
                .andCondition("scenename = '" + testscene.getScenename().replace("'", "''") + "'");
        if (testsceneService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("测试场景名已经存在");
        } else {
            testsceneService.save(testscene);
            return ResultGenerator.genOkResult("测试场景名创建成功");
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        testsceneService.deleteById(id);
        return ResultGenerator.genOkResult();
    }


    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Testscene testscene = testsceneService.getById(id);
        return ResultGenerator.genOkResult(testscene);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Testscene> list = testsceneService.listAll();
        PageInfo<Testscene> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/scenes")
    public Result listall(@RequestParam long projectid) {
        Condition con=new Condition(Testscene.class);
        con.createCriteria().andCondition("projectid = "+projectid);
        List<Testscene> list = testsceneService.listByCondition(con);
        return ResultGenerator.genOkResult(list);
    }


    @PutMapping("/detail")
    public Result update(@RequestBody Testscene testscene) {
        Condition con = new Condition(Testscene.class);
        con.createCriteria().andCondition("projectid = " + testscene.getProjectid())
                .andCondition("scenename = '" + testscene.getScenename().replace("'", "''") + "'")
                .andCondition("id <> " + testscene.getId());
        if (testsceneService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("测试场景名已经存在");
        } else {
            testsceneService.updatescene(testscene);
            return ResultGenerator.genOkResult();
        }
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Testscene> list = this.testsceneService.findtestsceneWithName(param);
        final PageInfo<Testscene> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/copyscene")
    public Result copyscene(@RequestBody final Map<String, Object> param) {
        String sourcesceneid = param.get("sourcesceneid").toString();
        String sourcescenename = param.get("sourcescenename").toString();
        String newscenename = param.get("newscenename").toString();
        Testscene testscene = testsceneService.getBy("id", Long.parseLong(sourcesceneid));
        if (testscene == null) {
            return ResultGenerator.genFailedResult("测试场景：" + sourcescenename + "不存在");
        }
        Condition con = new Condition(Testscene.class);
        con.createCriteria().andCondition("scenename = '" + newscenename + "'").andCondition("usetype = '" + testscene.getUsetype() + "'");
        if (testsceneService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult(newscenename + "已存在存在此场景名");
        } else {
            if (testscene != null) {
                String SceneType = testscene.getUsetype();
                Testscene newtestscene = new Testscene();
                newtestscene.setId(null);
                newtestscene.setCreateTime(new Date());
                newtestscene.setLastmodifyTime(new Date());
                newtestscene.setScenename(newscenename);
                newtestscene.setProjectid(testscene.getProjectid());
                newtestscene.setCreator(testscene.getCreator());
                newtestscene.setUsetype(SceneType);
                testsceneService.save(newtestscene);
                Long newtestsceneid = newtestscene.getId();
                //复制场景用例
                Condition concase = new Condition(TestsceneTestcase.class);
                concase.createCriteria().andCondition("testscenenid = " + sourcesceneid);
                List<TestsceneTestcase> testsceneTestcaseList = testsceneTestcaseService.listByCondition(concase);
                for (TestsceneTestcase tts : testsceneTestcaseList) {
                    TestsceneTestcase newtestscenecase=new TestsceneTestcase();
                    newtestscenecase.setId(null);
                    newtestscenecase.setTestscenenid(newtestsceneid);
                    newtestscenecase.setScenename(newscenename);
                    newtestscenecase.setApiid(tts.getApiid());
                    newtestscenecase.setTestcaseid(tts.getTestcaseid());
                    newtestscenecase.setCasename(tts.getCasename());
                    newtestscenecase.setCreator(tts.getCreator());
                    newtestscenecase.setCreateTime(new Date());
                    newtestscenecase.setLastmodifyTime(new Date());
                    newtestscenecase.setApiname(tts.getApiname());
                    newtestscenecase.setCaseorder(tts.getCaseorder());
                    newtestscenecase.setDeployunitid(tts.getDeployunitid());
                    newtestscenecase.setDeployunitname(tts.getDeployunitname());
                    newtestscenecase.setModelid(tts.getModelid());
                    newtestscenecase.setModelname(tts.getModelname());
                    testsceneTestcaseService.save(newtestscenecase);
                    Long newtestscenecaseid=newtestscenecase.getId();

                    //复制前置条件
                    Long ttid=tts.getId();
                    Condition apicon = new Condition(ConditionApi.class);
                    apicon.createCriteria().andCondition("conditionid = " + ttid).andCondition("conditiontype = 'scencecase'" );
                    List<ConditionApi> conditionApiList = conditionApiService.listByCondition(apicon);
                    for (ConditionApi condiapi:conditionApiList) {
                        condiapi.setId(null);
                        condiapi.setConditionid(newtestscenecaseid);
                        condiapi.setConditionname(condiapi.getConditionname());
                        condiapi.setSubconditionname("复制"+condiapi.getSubconditionname());
                        conditionApiService.save(condiapi);
                    }
                }
            }
        }
        return ResultGenerator.genOkResult();
    }

}
