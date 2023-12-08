package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.CaseReportStatics;
import com.zoctan.api.dto.FunctionCaseSandF;
import com.zoctan.api.dto.FunctionCaseStatis;
import com.zoctan.api.dto.FunctionConditionStatis;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.ExecuteplanTestcaseMapper;
import com.zoctan.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2020/10/16
 */
@Slf4j
@RestController
@RequestMapping("/apicases/report")
public class ApicasesReportController {
    @Resource
    private ApicasesReportService apicasesReportService;

    @Resource
    private ExecuteplanTestcaseService executeplanTestcaseService;

    @Resource
    private DispatchService dispatchService;

    @Resource
    private TestconditionService testconditionService;

    @Resource
    private ConditionApiService conditionApiService;

    @Resource
    private ConditionDbService conditionDbService;

    @Resource
    private ConditionScriptService conditionScriptService;

    @Resource
    private ConditionDelayService conditionDelayService;

    @Resource
    private ApicasesReportstaticsService apicasesReportstaticsService;

    @Resource
    private TestplanTestsceneService testplanTestsceneService;

    @Resource
    private TestsceneTestcaseService testsceneTestcaseService;


    @PostMapping
    public Result add(@RequestBody ApicasesReport apicasesReport) {
        apicasesReportService.save(apicasesReport);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apicasesReportService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ApicasesReport apicasesReport) {
        apicasesReportService.update(apicasesReport);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ApicasesReport apicasesReport = apicasesReportService.getById(id);
        return ResultGenerator.genOkResult(apicasesReport);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size, @RequestParam long projectid) {
        PageHelper.startPage(page, size);
        List<ApicasesReport> list = apicasesReportService.listallresult(projectid);
        PageInfo<ApicasesReport> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<ApicasesReport> list = this.apicasesReportService.findApicasereportWithName(param);
        final PageInfo<ApicasesReport> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/findApicasereportWithNameandStatus")
    public Result findApicasereportWithNameandStatus(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
        String status = param.get("caseststus").toString();
        String batchname = param.get("batchname").toString();
        String scenename = param.get("scenename").toString();
        String casename = param.get("casename").toString();
        Long projectid = Long.parseLong(param.get("projectid").toString());
        PageHelper.startPage(page, size);
        final List<ApicasesReport> list = this.apicasesReportService.findApicasereportWithNameandStatus(executeplanid, status, batchname, projectid, scenename, casename);
        final PageInfo<ApicasesReport> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

//    @PostMapping("/findconditionreport")
//    public Result findconditionreport(@RequestBody final Map<String, Object> param) {
//        Integer page = Integer.parseInt(param.get("page").toString());
//        Integer size = Integer.parseInt(param.get("size").toString());
//        Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
//        String batchname = param.get("batchname").toString();
//        PageHelper.startPage(page, size);
//        final List<TestconditionReport> list = this.testconditionReportService.findconditionreport(executeplanid,batchname);
//        final PageInfo<TestconditionReport> pageInfo = new PageInfo<>(list);
//        return ResultGenerator.genOkResult(pageInfo);
//    }


    /**
     * 输入框查询
     */
    @PostMapping("/getstaticsreport")
    public Result getstaticsreport(@RequestBody final Map<String, Object> param) {
        //ApicasesReportController.log.info(param);
        if (param.get("batchname") == null || param.get("testplanname") == null) {
            return ResultGenerator.genOkResult("请选中测试计划和批次");
        }

        Long planid = Long.parseLong(param.get("executeplanid").toString());
        String batchname = param.get("batchname").toString();

        CaseReportStatics caseReportStatics = new CaseReportStatics();

        Long casetotals = dispatchService.getdispatchnum(planid, batchname);
//        Long casetotals = this.apicasesReportService.getApicasetotalsWithName(param);
        Map<String, Object> statusparams = param;
        statusparams.put("status", "成功");
        Long passcasetotals = this.apicasesReportService.getApicasenumbystatus(statusparams);

        statusparams.put("status", "失败");
        Long failcasetotals = this.apicasesReportService.getApicasenumbystatus(statusparams);


        Long costtimes = this.apicasesReportService.getApicasecosttimes(param);
        caseReportStatics.setBatchname(param.get("batchname").toString());
        caseReportStatics.setPlanname(param.get("testplanname").toString());
        caseReportStatics.setTestcasenums(casetotals);
        caseReportStatics.setPassnums(passcasetotals);
        caseReportStatics.setFailnums(failcasetotals);
        caseReportStatics.setCosttimes(costtimes);

        final List<CaseReportStatics> list = new ArrayList<>();
        list.add(caseReportStatics);
        final PageInfo<CaseReportStatics> pageInfo = new PageInfo<>(list);

        return ResultGenerator.genOkResult(pageInfo);
    }


    @PostMapping("/getfunctioncasestatics")
    public Result getfunctioncasestatics(@RequestBody final Map<String, Object> param) {
        if (param.get("batchid").toString().isEmpty() || param.get("executeplanid").toString().isEmpty()) {
            return ResultGenerator.genFailedResult("请选择测试集合，执行计划");
        } else {
            Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
            Long batchid = Long.parseLong(param.get("batchid").toString());
            String batchname = param.get("batchname").toString();

            FunctionCaseStatis functionCaseStatis = new FunctionCaseStatis();

            Condition dispatchccon = new Condition(Dispatch.class);
            dispatchccon.createCriteria().andCondition("execplanid = " + executeplanid)
                    .andCondition("batchname = '" + batchname + "'");
            List<Dispatch> dispatchList = dispatchService.listByCondition(dispatchccon);
            functionCaseStatis.setCaseNum(dispatchList.size());

//            Long casetotals= dispatchService.getdispatchnum(executeplanid,batchname);
//            functionCaseStatis.setCaseNum(casetotals);


            Condition planscenecon = new Condition(TestplanTestscene.class);
            planscenecon.createCriteria().andCondition("testplanid = " + executeplanid);
            List<TestplanTestscene> testplanTestsceneList = testplanTestsceneService.listByCondition(planscenecon);
            functionCaseStatis.setSceneNums(testplanTestsceneList.size());

            int ExecCaseNums = 0;
            for (Dispatch dis : dispatchList) {
                if (dis.getStatus().equalsIgnoreCase("已分配")) {
                    ExecCaseNums = ExecCaseNums + 1;
                }
            }
            functionCaseStatis.setExecCaseNums(ExecCaseNums);

            int NotExecCaseNums = 0;
            for (Dispatch dis : dispatchList) {
                if (dis.getStatus().equalsIgnoreCase("待分配")) {
                    NotExecCaseNums = NotExecCaseNums + 1;
                }
            }
            functionCaseStatis.setNotExecCaseNums(NotExecCaseNums);


            int StopCaseNums = 0;
            for (Dispatch dis : dispatchList) {
                if (dis.getStatus().equalsIgnoreCase("已停止")) {
                    StopCaseNums = StopCaseNums + 1;
                }
            }
            functionCaseStatis.setStopExecCaseNums(StopCaseNums);

            List<ApicasesReport> apicasesReportSuccessList = apicasesReportService.getreportbyplanandbatchstatus(executeplanid, "成功", batchname);
            functionCaseStatis.setSuccessCaseNums(apicasesReportSuccessList.size());

            List<ApicasesReport> apicasesReportFailList = apicasesReportService.getreportbyplanandbatchstatus(executeplanid, "失败", batchname);
            functionCaseStatis.setFailCaseNums(apicasesReportFailList.size());

            int totalrunnums = apicasesReportSuccessList.size() + apicasesReportFailList.size();

            float successrate = Float.valueOf(apicasesReportSuccessList.size()) / Float.valueOf(totalrunnums);
            float failrate = Float.valueOf(apicasesReportFailList.size()) / Float.valueOf(totalrunnums);
            String sresultrate = "";
            String fresultrate = "";
            DecimalFormat decimalFormat = new DecimalFormat(".00");

            if (successrate == 0.0) {
                sresultrate = "0%";
            } else {
                sresultrate = decimalFormat.format(successrate * 100) + "%";
            }
            if (failrate == 0.0) {
                fresultrate = "0%";
            } else {
                fresultrate = decimalFormat.format(failrate * 100) + "%";
            }

            functionCaseStatis.setFailrate(fresultrate);
            functionCaseStatis.setSuccessrate(sresultrate);

            float costalltime = 0;
            for (ApicasesReport ap : apicasesReportSuccessList) {
                costalltime = costalltime + ap.getRuntime();
            }
            for (ApicasesReport ap : apicasesReportFailList) {
                costalltime = costalltime + ap.getRuntime();
            }

//            List<ApicasesReportstatics> apicasesReportstaticsList = apicasesReportstaticsService.getapicasesreportstaticsbypandb(executeplanid, batchname);
//
//            float costtime = 0;
//            for (ApicasesReportstatics apicasesReportstatics : apicasesReportstaticsList) {
//                costtime = costtime + apicasesReportstatics.getRuntime();
//            }
            functionCaseStatis.setCosttime(costalltime / 1000);

            List<FunctionCaseStatis> functionCaseStatisList = new ArrayList<>();
            functionCaseStatisList.add(functionCaseStatis);

            return ResultGenerator.genOkResult(functionCaseStatisList);
        }
    }

    @PostMapping("/getfunctionconditionstatics")
    public Result getfunctionconditionstatics(@RequestBody final Map<String, Object> param) {
        FunctionConditionStatis functionConditionStatis = new FunctionConditionStatis();
        if (param.get("batchid").toString().isEmpty() || param.get("executeplanid").toString().isEmpty()) {
            return ResultGenerator.genFailedResult("请选择测试集合，执行计划");
        } else {
            Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
            Long batchid = Long.parseLong(param.get("batchid").toString());
            String batchname = param.get("batchname").toString();

            long totalconditionnums = 0;

            Condition apiconditioncon = new Condition(ConditionApi.class);
            apiconditioncon.createCriteria().andCondition("conditionid = " + executeplanid).andCondition("conditiontype = 'execplan'");
            List<ConditionApi> conditionApiList = conditionApiService.listByCondition(apiconditioncon);
            if (conditionApiList.size() > 0) {
                totalconditionnums = totalconditionnums + conditionApiList.size();
            }

            Condition delayconditioncon = new Condition(ConditionDelay.class);
            delayconditioncon.createCriteria().andCondition("conditionid = " + executeplanid).andCondition("conditiontype = 'execplan'");
            List<ConditionDelay> conditionDelayList = conditionDelayService.listByCondition(delayconditioncon);
            if (conditionApiList.size() > 0) {
                totalconditionnums = totalconditionnums + conditionDelayList.size();
            }
            Condition dbconditioncon = new Condition(ConditionDb.class);
            dbconditioncon.createCriteria().andCondition("conditionid = " + executeplanid).andCondition("conditiontype = 'execplan'");
            List<ConditionDb> conditionDBList = conditionDbService.listByCondition(dbconditioncon);
            if (conditionDBList.size() > 0) {
                totalconditionnums = totalconditionnums + conditionDBList.size();
            }
//
//                Condition scriptconditioncon = new Condition(ConditionScript.class);
//                scriptconditioncon.createCriteria().andCondition("conditionid = " + executeplanid).andCondition("conditiontype = 'execplan'" );
//                List<ConditionScript> conditionScriptList = conditionScriptService.listByCondition(scriptconditioncon);
//                if (conditionScriptList.size() >0) {
//                    totalconditionnums = totalconditionnums + conditionScriptList.size();
//                }

            //}
            functionConditionStatis.setTestCollectionConditionsNUms(totalconditionnums);


            //

            long Sceneconditionnums = 0;
            long caseconditionnums = 0;

            List<ConditionApi> conditionApiList1 = conditionApiService.listAll();
            List<ConditionDelay> conditionDelayList1 = conditionDelayService.listAll();
            List<ConditionDb> conditionDbList = conditionDbService.listAll();

            Condition planscenecon = new Condition(TestplanTestscene.class);
            planscenecon.createCriteria().andCondition("testplanid = " + executeplanid);
            List<TestplanTestscene> testplanTestsceneList = testplanTestsceneService.listByCondition(planscenecon);

            for (TestplanTestscene tpts : testplanTestsceneList) {
                long sceneid = tpts.getTestscenenid();
                Condition sceneapiconditioncon = new Condition(ConditionApi.class);
                sceneapiconditioncon.createCriteria().andCondition("conditionid = " + sceneid).andCondition("conditiontype = 'scene'");
                List<ConditionApi> sceneconditionApiList = conditionApiService.listByCondition(sceneapiconditioncon);
                if (sceneconditionApiList.size() > 0) {
                    Sceneconditionnums = Sceneconditionnums + sceneconditionApiList.size();
                }
                Condition scenedelayconditioncon = new Condition(ConditionDelay.class);
                scenedelayconditioncon.createCriteria().andCondition("conditionid = " + sceneid).andCondition("conditiontype = 'scene'");
                List<ConditionDelay> sceneconditionDelayList = conditionDelayService.listByCondition(scenedelayconditioncon);
                if (sceneconditionDelayList.size() > 0) {
                    Sceneconditionnums = Sceneconditionnums + sceneconditionDelayList.size();
                }

                Condition scenedbconditioncon = new Condition(ConditionDb.class);
                scenedbconditioncon.createCriteria().andCondition("conditionid = " + sceneid).andCondition("conditiontype = 'scene'");
                List<ConditionDb> sceneconditionDbList = conditionDbService.listByCondition(scenedbconditioncon);
                if (sceneconditionDbList.size() > 0) {
                    Sceneconditionnums = Sceneconditionnums + sceneconditionDelayList.size();
                }

                Condition scenecasecon = new Condition(TestsceneTestcase.class);
                scenecasecon.createCriteria().andCondition("testscenenid = " + sceneid);
                List<TestsceneTestcase> testsceneTestcaseList = testsceneTestcaseService.listByCondition(scenecasecon);

                for (TestsceneTestcase tstc : testsceneTestcaseList) {
                    Long sccencaseid = tstc.getId();
                    for (ConditionApi ca : conditionApiList1) {
                        if (sccencaseid.equals(ca.getConditionid()) && ca.getConditiontype().equalsIgnoreCase("scencecase")) {
                            caseconditionnums = caseconditionnums + 1;
                        }
                    }
                    for (ConditionDelay cd : conditionDelayList1) {
                        if (sccencaseid.equals(cd.getConditionid()) && cd.getConditiontype().equalsIgnoreCase("scencecase")) {
                            caseconditionnums = caseconditionnums + 1;
                        }
                    }

                    for (ConditionDb db : conditionDbList) {
                        if (sccencaseid.equals(db.getConditionid()) && db.getConditiontype().equalsIgnoreCase("scencecase")) {
                            caseconditionnums = caseconditionnums + 1;
                        }
                    }
                }
            }
//            List<ExecuteplanTestcase> executeplanTestcaseList = executeplanTestcaseService.getplancasesbyplanid(executeplanid);
//
//            for (ExecuteplanTestcase ec : executeplanTestcaseList) {
//                long caseid = ec.getTestcaseid();
//
//                Condition testcaseconditioncon = new Condition(Testcondition.class);
//                testcaseconditioncon.createCriteria().andCondition("objecttype = '" + "测试用例" + "'")
//                        .andCondition("objectid = " + caseid);
//                List<Testcondition> testcaseconditionList = testconditionService.listByCondition(testcaseconditioncon);
//
//                if (testcaseconditionList.size() > 0) {
//                    long conditionid = testcaseconditionList.get(0).getId();
//
//                    Condition conditionApi = new Condition(ConditionApi.class);
//                    conditionApi.createCriteria().andCondition("conditionid = " + conditionid);
//                    List<ConditionApi> conditionApiList = conditionApiService.listByCondition(conditionApi);
//                    caseconditionnums = caseconditionnums + conditionApiList.size();
//
//                    Condition conditionDB = new Condition(ConditionDb.class);
//                    conditionDB.createCriteria().andCondition("conditionid = " + conditionid);
//                    List<ConditionDb> conditionDbList = conditionDbService.listByCondition(conditionDB);
//                    caseconditionnums = caseconditionnums + conditionDbList.size();
//
//                    Condition conditionScript = new Condition(ConditionScript.class);
//                    conditionScript.createCriteria().andCondition("conditionid = " + conditionid);
//                    List<ConditionScript> conditionScriptList = conditionScriptService.listByCondition(conditionDB);
//                    caseconditionnums = caseconditionnums + conditionScriptList.size();
//
//
//                    Condition delayconditioncon = new Condition(ConditionDelay.class);
//                    delayconditioncon.createCriteria().andCondition("conditionid = " + conditionid);
//                    List<ConditionDelay> conditionDelayList = conditionDelayService.listByCondition(delayconditioncon);
//                    caseconditionnums = caseconditionnums + conditionDelayList.size();
//                }
//            }
            functionConditionStatis.setSceneConditionNums(Sceneconditionnums);
            functionConditionStatis.setCaseConditionNums(caseconditionnums);
        }
        List<FunctionConditionStatis> functionConditionStatisList = new ArrayList<>();
        functionConditionStatisList.add(functionConditionStatis);
        return ResultGenerator.genOkResult(functionConditionStatisList);
    }

    @PostMapping("/getfunctionCaseSandF")
    public Result getfunctionCaseSandF(@RequestBody final Map<String, Object> param) {
        if (param.get("batchid").toString().isEmpty() || param.get("executeplanid").toString().isEmpty()) {
            return ResultGenerator.genFailedResult("请选择测试集合，执行计划");
        } else {
            Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
            String batchname = param.get("batchname").toString();

            List<FunctionCaseSandF> functionCaseSandFList = new ArrayList<>();
            List<ApicasesReport> apicasesReportSuccessList = apicasesReportService.getreportbyplanandbatchstatus(executeplanid, "成功", batchname);
            FunctionCaseSandF functionCaseSandF = new FunctionCaseSandF();
            functionCaseSandF.setName("成功");
            functionCaseSandF.setValue(apicasesReportSuccessList.size());
            functionCaseSandFList.add(functionCaseSandF);

            List<ApicasesReport> apicasesReportFailList = apicasesReportService.getreportbyplanandbatchstatus(executeplanid, "失败", batchname);
            FunctionCaseSandF functionCaseSandFail = new FunctionCaseSandF();
            functionCaseSandFail.setName("失败");
            functionCaseSandFail.setValue(apicasesReportFailList.size());
            functionCaseSandFList.add(functionCaseSandFail);
            return ResultGenerator.genOkResult(functionCaseSandFList);
        }
    }

}
