package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.exception.ServiceException;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.ExecuteplanParamsMapper;
import com.zoctan.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2020/09/27
 */
@Slf4j
@RestController
@RequestMapping("/executeplan")
@Validated
public class ExecuteplanController {
    @Resource
    private ExecuteplanService executeplanService;
    @Autowired
    private ExecuteplanTestcaseService execplantestcaseService;
    @Autowired
    private ExecuteplanParamsService executeplanParamsService;
    @Autowired
    private MacdepunitService macdepunitService;
    @Autowired
    private RouteperformancereportService routeperformancereportService;

    @Autowired
    private TestplanTestsceneService testplanTestsceneService;

    @Autowired
    private TestsceneTestcaseService testsceneTestcaseService;

    @PostMapping
    public Result add(@RequestBody Executeplan executeplan) {
        Condition con=new Condition(Executeplan.class);
        con.createCriteria().andCondition("projectid = "+executeplan.getProjectid())
                .andCondition("executeplanname = '" + executeplan.getExecuteplanname().replace("'","''") + "'")
                .andCondition("enviromentname = '" + executeplan.getEnviromentname() + "'");
        if(executeplanService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("此环境下执行计划已经存在");
        }
        else {
            executeplanService.save(executeplan);
            if(executeplan.getUsetype().equalsIgnoreCase("性能"))
            {
                //增加动态表
                String TableName="apicases_report_performance"+executeplan.getId();
                executeplanService.createNewTable(TableName);
                //如果是性能测试集合，新增路由表
                Routeperformancereport routeperformancereport=new Routeperformancereport();
                routeperformancereport.setExecuteplanid(executeplan.getId());
                routeperformancereport.setTablename(TableName);
                routeperformancereportService.save(routeperformancereport);
            }
            return ResultGenerator.genOkResult();
        }
    }

    @PostMapping("/execplancases")
    public Result execcases(@RequestBody final List<Executeplanbatch> planbatchList) {
        //暂时支持单计划执行
        try {
            executeplanService.executeplancase(planbatchList,"立即执行");
            return ResultGenerator.genOkResult();
        }
        catch (ServiceException se)
        {
            return ResultGenerator.genFailedResult(se.getMessage());
        }
    }

    @PostMapping("/checkcondition")
    public Result checkcondition(@RequestBody Executeplan executeplan) {
        try {
            // 检查此计划下是否有装载用例
            Long planid= executeplan.getId();
            Long envid= executeplan.getEnvid();
            String enviromentname= executeplan.getEnviromentname();

            Condition con=new Condition(TestplanTestscene.class);
            con.createCriteria().andCondition("projectid = "+executeplan.getProjectid())
                    .andCondition("testplanid = " +planid );
            List<TestplanTestscene> testplanTestsceneList=testplanTestsceneService.listByCondition(con);
            Integer casenum=0;
            for (TestplanTestscene tes:testplanTestsceneList) {
                Condition scenecon=new Condition(TestsceneTestcase.class);
                scenecon.createCriteria().andCondition("testscenenid = "+tes.getTestscenenid());
                List<TestsceneTestcase> testsceneTestcaseList= testsceneTestcaseService.listByCondition(scenecon);
                casenum=casenum+testsceneTestcaseList.size();
            }
            if(casenum.intValue()==0)
            {
                return ResultGenerator.genFailedResult("该执行计划下还未装载测试用例！");
            }
//            else
//            {
//                List<ExecuteplanTestcase> deployidlist = execplantestcaseService.finddeployunitbyplanid(planid);
//                if(deployidlist.size()==0)
//                {
//                    return ResultGenerator.genFailedResult("该执行计划下用例所在的所有微服务不存在，请检查是否被删除！");
//                }
//                else
//                {
//                    for (ExecuteplanTestcase ect: deployidlist) {
//                        Long deployid=ect.getDeployunitid();
//                        String deployname=ect.getDeployunitname();
//                        Integer machinenum= macdepunitService.findmachinenumbyenvidanddeployid(envid,deployid);
//                        if(machinenum.intValue()==0)
//                        {
//                            return ResultGenerator.genFailedResult("该执行计划的用例所在的微服务: "+deployname+" 在环境: "+enviromentname+" 中未完成部署！");
//                        }
//                    }
//                    return ResultGenerator.genOkResult();
//                }
//            }
        }
        catch (ServiceException se)
        {
            return ResultGenerator.genFailedResult(se.getMessage());
        }
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        executeplanService.deleteById(id);
        //删除集合用例
        execplantestcaseService.removeplancase(id);
        //删除集合全局参数
        executeplanParamsService.removeplanparams(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Executeplan executeplan) {
        executeplanService.update(executeplan);
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/updatestatus")
    public Result updatestatus(@PathVariable final List<Executeplan> executeplanList) {
        for (Executeplan ep:executeplanList) {
            executeplanService.updatetestplanstatus(ep.getId(),ep.getStatus());
        }
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Executeplan executeplan = executeplanService.getById(id);
        return ResultGenerator.genOkResult(executeplan);
    }


    @GetMapping("/getexecuteplannum")
    public Result detail(@RequestParam long projectid) {
        Integer executeplannum = executeplanService.getexecuteplannum(projectid);
        return ResultGenerator.genOkResult(executeplannum);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Executeplan> list = executeplanService.listAll();
        PageInfo<Executeplan> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getallexplan")
    public Result getallexplan(@RequestParam long projectid) {
        List<Executeplan> list = executeplanService.getallexplan(projectid);
        return ResultGenerator.genOkResult(list);
    }

    @GetMapping("/getstaticsplan")
    public Result getstaticsplan(@RequestParam long projectid) {
        List<String> list = executeplanService.getstaticsplan(projectid);
        return ResultGenerator.genOkResult(list);
    }

    @GetMapping("/getallexplanbytype")
    public Result getallexplanbytype(@RequestParam String usetype,@RequestParam long projectid) {
        List<Executeplan> list = executeplanService.getallexplanbytype(usetype,projectid);
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateExecuteplan(@RequestBody final Executeplan executeplan) {
        Condition con=new Condition(Executeplan.class);
        con.createCriteria().andCondition("projectid = "+executeplan.getProjectid())
                .andCondition("executeplanname = '" + executeplan.getExecuteplanname().replace("'","''") + "'")
                .andCondition("id <> " + executeplan.getId())
                .andCondition("enviromentname = '" + executeplan.getEnviromentname() + "'");
        if(executeplanService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("此环境下执行计划已经存在");
        }
        else {
            this.executeplanService.updateexecuteplanname(executeplan);
            return ResultGenerator.genOkResult();
        }
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        String creator = param.get("creator").toString();
        if(creator.equalsIgnoreCase("admin"))
        {
            param.put("creator",null);
        }
        PageHelper.startPage(page, size);
        final List<Executeplan> list = this.executeplanService.findexplanWithName(param);
        final PageInfo<Executeplan> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
