package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.FunctionCaseSandF;
import com.zoctan.api.dto.MyCreateInfo;
import com.zoctan.api.dto.MyFunctionRecentInfo;
import com.zoctan.api.dto.MyRunInfo;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.ApicasesReportPerformanceMapper;
import com.zoctan.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/04/18
 */
@Slf4j
@RestController
@RequestMapping("/MyInfo")
public class MyInfoController {
    @Resource
    private ApiService apiService;
    @Resource
    private ApicasesService apicasesService;
    @Resource
    private ExecuteplanService executeplanService;

    @Resource
    private TestsceneService testsceneService;
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
    private ApicasesReportPerformanceMapper apicasesReportPerformanceMapper;

    @Resource
    private ApicasesReportService apicasesReportService;

    @Resource
    private ExecuteplanbatchService executeplanbatchService;

    @Resource
    private DispatchService dispatchService;

    @Resource
    private ApicasesPerformancestatisticsService apicasesPerformancestatisticsService;




    /**
     * 输入框查询
     */
    @PostMapping("/mycreateinfo")
    public Result mycreateinfo(@RequestBody final Map<String, Object> param) {
        Long creatorid= Long.parseLong(param.get("creatorid").toString());
        long projectid=Long.parseLong(param.get("projectid").toString());

        List<MyCreateInfo> myCreateInfoList=new ArrayList<>();
        MyCreateInfo myCreateInfo=new MyCreateInfo();
        Condition con=new Condition(Api.class);
        con.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid );
        List<Api> apiList= apiService.listByCondition(con);
        int apinums=apiList.size();
        myCreateInfo.setApiNums(apinums);

        Condition casefuncon=new Condition(Apicases.class);
        casefuncon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid )
        .andCondition("casetype = '功能" + "'");
        List<Apicases>apicasesfunctionList=apicasesService.listByCondition(casefuncon);
        int apicasefunnum=apicasesfunctionList.size();
        myCreateInfo.setApiFunctionCaseNums(apicasefunnum);

        Condition caseperformancecon=new Condition(Apicases.class);
        caseperformancecon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid )
                .andCondition("casetype = '性能" + "'");
        List<Apicases>apicasesperformanceList=apicasesService.listByCondition(caseperformancecon);
        int apicaseperformancenums=apicasesperformanceList.size();
        myCreateInfo.setApiPerformanceCaseNums(apicaseperformancenums);

        Condition functionexecplancon=new Condition(Executeplan.class);
        functionexecplancon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid )
                .andCondition("usetype = '功能" + "'");
        List<Executeplan> functionexecuteplanList= executeplanService.listByCondition(functionexecplancon);
        int functionexecplannums=functionexecuteplanList.size();
        myCreateInfo.setExecplanFunnums(functionexecplannums);

        Condition performanceexecplancon=new Condition(Executeplan.class);
        performanceexecplancon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid )
                .andCondition("usetype = '性能" + "'");
        List<Executeplan> performanceexecuteplanList= executeplanService.listByCondition(performanceexecplancon);
        int performanceexecplannums=performanceexecuteplanList.size();
        myCreateInfo.setExecplanPerformancenums(performanceexecplannums);



        Condition testscenecon=new Condition(Testscene.class);
        testscenecon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid );
        List<Testscene> testsceneList= testsceneService.listByCondition(testscenecon);
        int conidtionsallnums=testsceneList.size();
        myCreateInfo.setTestConditions(conidtionsallnums);

//        Condition parentcondition=new Condition(Testcondition.class);
//        parentcondition.createCriteria().andCondition("projectid="+projectid)
//                .andCondition("creator = '" + creator + "'");
//        List<Testcondition>testconditionList= testconditionService.listByCondition(parentcondition);
//        int parentconditions=testconditionList.size();
//        conidtionsallnums=conidtionsallnums+parentconditions;
//
//        Condition conditionapi=new Condition(ConditionApi.class);
//        conditionapi.createCriteria().andCondition("projectid="+projectid)
//                .andCondition("creator = '" + creator + "'");
//        List<ConditionApi>conditionApiList= conditionApiService.listByCondition(conditionapi);
//        int conditionsapinums=conditionApiList.size();
//        conidtionsallnums=conidtionsallnums+conditionsapinums;
//
//        Condition conditiondb=new Condition(ConditionDb.class);
//        conditiondb.createCriteria().andCondition("projectid="+projectid)
//                .andCondition("creator = '" + creator + "'");
//        List<ConditionDb>conditionDbList= conditionDbService.listByCondition(conditiondb);
//        int conditionsdbnums=conditionDbList.size();
//        conidtionsallnums=conidtionsallnums+conditionsdbnums;
//
//
//        Condition conditionscript=new Condition(ConditionScript.class);
//        conditionscript.createCriteria().andCondition("projectid="+projectid)
//                .andCondition("creator = '" + creator + "'");
//        List<ConditionScript>conditionScriptList= conditionScriptService.listByCondition(conditionscript);
//        int conditionsscriptnums=conditionScriptList.size();
//        conidtionsallnums=conidtionsallnums+conditionsscriptnums;
//
//        Condition conditiondelay=new Condition(ConditionDelay.class);
//        conditiondelay.createCriteria().andCondition("projectid="+projectid)
//                .andCondition("creator = '" + creator + "'");
//        List<ConditionDelay>conditionDelayList= conditionDelayService.listByCondition(conditiondelay);
//        int conditionsdelaynums=conditionDelayList.size();
//        conidtionsallnums=conidtionsallnums+conditionsdelaynums;
        myCreateInfoList.add(myCreateInfo);
        return ResultGenerator.genOkResult(myCreateInfoList);
    }

    @PostMapping("/myruninfo")
    public Result myruninfo(@RequestBody final Map<String, Object> param) {
        Long creatorid= Long.parseLong(param.get("creatorid").toString());
        long projectid=Long.parseLong(param.get("projectid").toString());
        MyRunInfo myRunInfo=new MyRunInfo();

        Condition functionexecplancon=new Condition(Executeplan.class);
        functionexecplancon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid)
                .andCondition("usetype = '功能" + "'");
        List<Executeplan> functionexecuteplanList= executeplanService.listByCondition(functionexecplancon);
        MyInfoController.log.info("myruninfo..........功能测试集合数："+functionexecuteplanList.size());
        long functionallruncase=0;
        long functionallrunsuccesscase=0;
        long functionallrunfailcase=0;
        float functionallruncasetime=0;

        for (Executeplan ex:functionexecuteplanList) {
            long execplanid=ex.getId();
            Condition execplancon=new Condition(ApicasesReportstatics.class);
            execplancon.createCriteria().andCondition("testplanid = " + execplanid );
            List<ApicasesReportstatics>apicasesReportstaticsList= apicasesReportstaticsService.listByCondition(execplancon);
            MyInfoController.log.info("测试集合："+ex.getExecuteplanname()+" myruninfo..........功能测试报告统计结果条数："+apicasesReportstaticsList.size());


            for (ApicasesReportstatics ap:apicasesReportstaticsList) {
                functionallruncase=functionallruncase+ap.getTotalcases();
                MyInfoController.log.info("测试集合："+ex.getExecuteplanname()+" myruninfo..........功能测试运行循环用例数："+functionallruncase);

                functionallrunsuccesscase=functionallrunsuccesscase+ap.getTotalpasscases();
                MyInfoController.log.info("测试集合："+ex.getExecuteplanname()+" myruninfo..........功能测试运行循环成功用例数："+functionallrunsuccesscase);

                functionallrunfailcase=functionallrunfailcase+ap.getTotalfailcases();
                MyInfoController.log.info("测试集合："+ex.getExecuteplanname()+" myruninfo..........功能测试运行循环失败用例数："+functionallrunfailcase);

                functionallruncasetime=functionallruncasetime+ap.getRuntime();
                MyInfoController.log.info("测试集合："+ex.getExecuteplanname()+" myruninfo..........功能测试运行循环时间："+functionallruncasetime);
            }
        }
        myRunInfo.setFuntotalcasenums(functionallruncase);
        MyInfoController.log.info(" myruninfo..........功能测试运行总用例数结果："+functionallruncase);

        myRunInfo.setFuntotalpasscasenums(functionallrunsuccesscase);
        MyInfoController.log.info(" myruninfo..........功能测试运行总用例数结果："+functionallrunsuccesscase);

        myRunInfo.setFuntotalfailcasenums(functionallrunfailcase);
        MyInfoController.log.info(" myruninfo..........功能测试运行总用例数结果："+functionallrunfailcase);

        myRunInfo.setFuntotalcosttime(functionallruncasetime/1000);
        MyInfoController.log.info(" myruninfo..........功能测试运行总用例数结果："+functionallruncasetime);


        Condition performanceexecplancon=new Condition(Executeplan.class);
        performanceexecplancon.createCriteria().andCondition("creatorid = " + creatorid)
                .andCondition("usetype = '性能" + "'");
        List<Executeplan> performanceexecuteplanList= executeplanService.listByCondition(performanceexecplancon);
        long perallruncase=0;
        long perallrunsuccesscase=0;
        long perallrunfailcase=0;
        float perallruncasetime=0;

        for (Executeplan ex:performanceexecuteplanList) {
            long execplanid=ex.getId();
            List<ApicasesReportPerformance>apicasesReportstaticsList= apicasesReportPerformanceMapper.listallresultbyplanid(execplanid);
            for (ApicasesReportPerformance ap:apicasesReportstaticsList) {
                perallruncase=perallruncase+1;
                if(ap.getStatus().equalsIgnoreCase("成功"))
                {
                    perallrunsuccesscase=perallrunsuccesscase+1;
                }
                if(ap.getStatus().equalsIgnoreCase("失败"))
                {
                    perallrunfailcase=perallrunfailcase+1;
                }
                perallruncasetime=perallruncasetime+ap.getRuntime();
            }
        }

        myRunInfo.setPertotalcasenums(perallruncase);
        myRunInfo.setPertotalpasscasenums(perallrunsuccesscase);
        myRunInfo.setPertotalfailcasenums(perallrunfailcase);
        myRunInfo.setPertotalcosttime(perallruncasetime/1000);
        List<MyRunInfo> myRunInfoList=new ArrayList<>();
        myRunInfoList.add(myRunInfo);
        return ResultGenerator.genOkResult(myRunInfoList);
    }

    @PostMapping("/myrecentfunctioninfo")
    public Result myrecentfunctioninfo(@RequestBody final Map<String, Object> param) {
        Long creatorid= Long.parseLong(param.get("creatorid").toString());
        long projectid=Long.parseLong(param.get("projectid").toString());
        Condition functionexecplancon=new Condition(Executeplan.class);
        functionexecplancon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid)
                .andCondition("usetype = '功能" + "'");
        List<Executeplan> functionexecuteplanList= executeplanService.listByCondition(functionexecplancon);
        List<MyFunctionRecentInfo> myFunctionRecentInfoList=new ArrayList<>();
        for (Executeplan ex:functionexecuteplanList) {

            String Execplanname=ex.getExecuteplanname();
            long execplanid=ex.getId();
            List<Executeplanbatch> executeplanbatchList=executeplanbatchService.getrecentbatchbyid(execplanid);

            for (Executeplanbatch epb:executeplanbatchList) {
                String batchname=epb.getBatchname();

                MyFunctionRecentInfo myFunctionRecentInfo=new MyFunctionRecentInfo();
                myFunctionRecentInfo.setExecplanname(Execplanname);
                myFunctionRecentInfo.setBatchname(batchname);

                long batchid=epb.getId();
                param.put("testplanid",execplanid);
                param.put("batchname",batchname);

                Long casetotals = this.apicasesReportService.getApicasetotalsWithName(param);
                myFunctionRecentInfo.setTotalcasenums(casetotals);

                List<FunctionCaseSandF>functionCaseSandFList=new ArrayList<>();
                List<ApicasesReport> apicasesReportSuccessList = apicasesReportService.getreportbyplanandbatchstatus(execplanid, "成功", batchname);
                myFunctionRecentInfo.setTotalsuccessnums(apicasesReportSuccessList.size());
                List<ApicasesReport> apicasesReportFailList = apicasesReportService.getreportbyplanandbatchstatus(execplanid, "失败", batchname);
                myFunctionRecentInfo.setTotalfailnums(apicasesReportFailList.size());
                Condition dispatchnotexeccon = new Condition(Dispatch.class);
                dispatchnotexeccon.createCriteria()
                        .andCondition("execplanid = " + execplanid)
                        .andCondition("batchid = " + batchid)
                        .andCondition("status != '" + "已完成" + "'");
                List<Dispatch> dispatchnotexecList = dispatchService.listByCondition(dispatchnotexeccon);
                myFunctionRecentInfo.setTotalunexecnums(dispatchnotexecList.size());
                myFunctionRecentInfoList.add(myFunctionRecentInfo);
            }
        }
        return ResultGenerator.genOkResult(myFunctionRecentInfoList);
    }

    @PostMapping("/myrecentperfermanceinfo")
    public Result myrecentperfermanceinfo(@RequestBody final Map<String, Object> param) {
        Long creatorid= Long.parseLong(param.get("creatorid").toString());
        long projectid=Long.parseLong(param.get("projectid").toString());

        List<ApicasesPerformancestatistics> allresult=new ArrayList<>();

        Condition perexecplancon=new Condition(Executeplan.class);
        perexecplancon.createCriteria().andCondition("projectid="+projectid)
                .andCondition("projectid="+projectid)
                .andCondition("creatorid = " + creatorid)
                .andCondition("usetype = '性能" + "'");
        List<Executeplan> perctionexecuteplanList= executeplanService.listByCondition(perexecplancon);

        for (Executeplan ex:perctionexecuteplanList) {
            String planname=ex.getExecuteplanname();
            long execplanid=ex.getId();
            List<Executeplanbatch> executeplanbatchList=executeplanbatchService.getrecentbatchbyid(execplanid);

            for (Executeplanbatch epb:executeplanbatchList) {
                String batchname=epb.getBatchname();

                Condition percon=new Condition(ApicasesPerformancestatistics.class);
                percon.createCriteria().andCondition("testplanid = " + execplanid )
                        .andCondition("batchname = '"+batchname + "'");
                List<ApicasesPerformancestatistics> apicasesPerformancestatisticsList= apicasesPerformancestatisticsService.getresultbyidandname(execplanid,batchname,projectid);

                for (ApicasesPerformancestatistics apf:apicasesPerformancestatisticsList) {
                    allresult.add(apf);
                }
            }
        }

        return ResultGenerator.genOkResult(allresult);
    }
}
