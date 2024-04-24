package com.zoctan.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.core.service.*;
import com.zoctan.api.dto.*;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.*;
import com.zoctan.api.util.RadomVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zoctan
 * @date 2020/09/11
 */
@Slf4j
@RestController
@RequestMapping("/apicases")
public class ApicasesController {
    @Resource
    private ApicasesService apicasesService;
    @Autowired
    private ApiCasedataService apiCasedataService;
    @Autowired
    private EnviromentService enviromentService;
    @Autowired
    private ApicasesAssertService apicasesAssertService;

    @Autowired
    private ApicasesDbassertService apicasesDbassertService;

    @Autowired
    private ApicasesDbassertValueService apicasesDbassertValueService;
    @Autowired(required = false)
    private ApiService apiService;
    @Autowired(required = false)
    private DeployunitService deployunitService;
    @Autowired(required = false)
    private MacdepunitService macdepunitService;
    @Autowired(required = false)
    private MachineService machineService;
    @Autowired(required = false)
    private ApiParamsService apiParamsService;
    @Autowired(required = false)
    private TestconditionService testconditionService;
    @Autowired(required = false)
    private ConditionApiService conditionApiService;
    @Autowired(required = false)
    private ConditionDbService conditionDbService;
    @Autowired(required = false)
    private ConditionScriptService conditionScriptService;

    @Autowired(required = false)
    private ConditionDelayService conditionDelayService;
    @Autowired(required = false)
    private ExecuteplanTestcaseService executeplanTestcaseService;
    @Resource
    private ConditionOrderService conditionOrderService;
    @Value("${spring.conditionserver.serverurl}")
    private String conditionserver;
    @Autowired(required = false)
    private VariablesService variablesService;
    @Autowired(required = false)
    private TestvariablesService testvariablesService;

    @Autowired(required = false)
    private DbvariablesService dbvariablesService;

    @Autowired(required = false)
    private ScriptvariablesService scriptvariablesService;

    @Autowired(required = false)
    private ApicasesDebugConditionService apicasesDebugConditionService;

    @Autowired(required = false)
    private GlobalheaderParamsService globalheaderParamsService;


    @Autowired(required = false)
    private GlobalvariablesService globalvariablesService;

    @Autowired(required = false)
    private ScenecasesDebugReportService scenecasesDebugReportService;

    @Autowired(required = false)
    private TestsceneTestcaseService testsceneTestcaseService;

    @Autowired(required = false)
    private TestsceneService testsceneService;

    @Autowired(required = false)
    private EnviromentvariablesService enviromentvariablesService;

    @Resource
    private AccountRoleService accountRoleService;

    @PostMapping
    public Result add(@RequestBody Apicases apicases) {
        Condition con = new Condition(Apicases.class);
        con.createCriteria().andCondition("projectid = " + apicases.getProjectid())
                .andCondition("deployunitname = '" + apicases.getDeployunitname() + "'")
                .andCondition("apiname = '" + apicases.getApiname() + "'").andCondition("casename = '" + apicases.getCasename().replace("'", "''") + "'");
        if (apicasesService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("用例名已存在");
        } else {
            apicasesService.save(apicases);
            //增加初始化参数值
            Long apiid = apicases.getApiid();
            Api api = apiService.getById(apiid);
            String RequestContentType = api.getRequestcontenttype();
            Map<String, Object> params = new HashMap<>();
            params.put("apiid", apiid);
            List<ApiParams> apiParamsList = apiParamsService.getApiParamsbyapiid(params);
            List<ApiCasedata> apiCasedataList = new ArrayList<>();
            for (ApiParams apiParams : apiParamsList) {
                if (apiParams.getPropertytype().equalsIgnoreCase("Header") || apiParams.getPropertytype().equalsIgnoreCase("Params")) {
                    ApiCasedata apiCasedata = GetApiCaseData(apicases, apiParams);
                    apiCasedataList.add(apiCasedata);
                } else {
                    if (RequestContentType.equalsIgnoreCase("Form表单")) {
                        ApiCasedata apiCasedata = GetApiCaseData(apicases, apiParams);
                        apiCasedataList.add(apiCasedata);
                    } else {
                        if (apiParams.getKeytype().equalsIgnoreCase(RequestContentType)) {
                            ApiCasedata apiCasedata = GetApiCaseData(apicases, apiParams);
                            apiCasedataList.add(apiCasedata);
                        }
                    }
                }
            }
            if (apiCasedataList.size() > 0) {
                apiCasedataService.save(apiCasedataList);
            }
            long casecount = api.getCasecounts();
            api.setCasecounts(casecount + 1);
            apiService.updateApi(api);
            return ResultGenerator.genOkResult();
        }
    }

    private ApiCasedata GetApiCaseData(Apicases apicases, ApiParams apiParams) {
        ApiCasedata apiCasedata = new ApiCasedata();
        apiCasedata.setMid(apicases.getMid());
        apiCasedata.setCaseid(apicases.getId());
        apiCasedata.setCasename(apicases.getCasename());
        apiCasedata.setPropertytype(apiParams.getPropertytype());
        if (apiParams.getKeydefaultvalue().equalsIgnoreCase("NoForm")) {
            apiCasedata.setApiparam("Body");
            apiCasedata.setApiparamvalue(apiParams.getKeyname());
        } else {
            apiCasedata.setApiparam(apiParams.getKeyname());
            apiCasedata.setApiparamvalue(apiParams.getKeydefaultvalue());
        }
        apiCasedata.setParamstype(apiParams.getKeytype());
        apiCasedata.setMemo("");
        apiCasedata.setCreateTime(new Date());
        apiCasedata.setLastmodifyTime(new Date());
        return apiCasedata;
    }

    @PostMapping("/copycases")
    public Result copycases(@RequestBody final Map<String, Object> param) {
        String sourcecaseid = param.get("sourcecaseid").toString();
        String sourcedeployunitid = param.get("sourcedeployunitid").toString();
        String sourcedeployunitname = param.get("sourcedeployunitname").toString();
        String newcasename = param.get("newcasename").toString();

        Condition con = new Condition(Apicases.class);
        con.createCriteria().andCondition("deployunitid = " + sourcedeployunitid)
                .andCondition("casename = '" + newcasename + "'");
        if (apicasesService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult(sourcedeployunitname + "已存在存在此用例名");
        } else {
            Apicases Sourcecase = apicasesService.getBy("id", Long.parseLong(sourcecaseid));
            if (Sourcecase != null) {
                long Apiid = Sourcecase.getApiid();
                Condition apcasedatacon = new Condition(Apicases.class);
                apcasedatacon.createCriteria().andCondition("caseid = " + Long.parseLong(sourcecaseid));
                List<ApiCasedata> SourceApicasedataList = apiCasedataService.listByCondition(apcasedatacon);
                //复制用例
                Sourcecase.setDeployunitid(Long.parseLong(sourcedeployunitid));
                Sourcecase.setDeployunitname(sourcedeployunitname);
                Sourcecase.setCasename(newcasename);
                Sourcecase.setCreateTime(new Date());
                Sourcecase.setLastmodifyTime(new Date());
                Sourcecase.setId(null);
                apicasesService.save(Sourcecase);
                Long NewCaseId = Sourcecase.getId();
                //复制用例数据
                for (ApiCasedata apiCasedata : SourceApicasedataList) {
                    apiCasedata.setCaseid(NewCaseId);
                    apiCasedata.setId(null);
                    apiCasedata.setCasename(newcasename);
                    apiCasedata.setCreateTime(new Date());
                    apiCasedata.setLastmodifyTime(new Date());
                    apiCasedataService.save(apiCasedata);
                }
                //复制断言
                Condition AssertDataCondition = new Condition(ApicasesAssert.class);
                AssertDataCondition.createCriteria().andCondition("caseid = " + Long.parseLong(sourcecaseid));
                List<ApicasesAssert> SourceAssertdataList = apicasesAssertService.listByCondition(AssertDataCondition);
                for (ApicasesAssert apicasesAssert : SourceAssertdataList) {
                    apicasesAssert.setCaseid(NewCaseId);
                    apicasesAssert.setId(null);
                    apicasesAssertService.save(apicasesAssert);
                }
                //复制数据库断言
                Condition AssertDBCondition = new Condition(ApicasesDbassert.class);
                AssertDBCondition.createCriteria().andCondition("caseid = " + Long.parseLong(sourcecaseid));
                List<ApicasesDbassert> SourceAssertdbList = apicasesDbassertService.listByCondition(AssertDBCondition);
                for (ApicasesDbassert apicasesDbassert : SourceAssertdbList) {
                    long sourcedbassertid=apicasesDbassert.getId();
                    apicasesDbassert.setCaseid(NewCaseId);
                    apicasesDbassert.setId(null);
                    apicasesDbassertService.save(apicasesDbassert);
                    long dbassertid=apicasesDbassert.getId();
                    Condition AssertDBValueCondition = new Condition(ApicasesDbassertValue.class);
                    AssertDBValueCondition.createCriteria().andCondition("dbassertid = " + sourcedbassertid);
                    List<ApicasesDbassertValue> apicasesDbassertValueList= apicasesDbassertValueService.listByCondition(AssertDBValueCondition);
                    for (ApicasesDbassertValue apicasesDbassertValue : apicasesDbassertValueList) {
                        apicasesDbassertValue.setDbassertid(dbassertid);
                        apicasesDbassertValue.setId(null);
                        apicasesDbassertValueService.save(apicasesDbassertValue);
                    }
                }

                //复制提取变量
                Condition TestvariablesCondition = new Condition(Testvariables.class);
                TestvariablesCondition.createCriteria().andCondition("caseid = " + Long.parseLong(sourcecaseid));
                List<Testvariables> TestvariablesConditionList = testvariablesService.listByCondition(TestvariablesCondition);
                for (Testvariables testvariables : TestvariablesConditionList) {
                    testvariables.setCaseid(NewCaseId);
                    testvariables.setCasename(newcasename);
                    testvariables.setTestvariablesname(testvariables.getTestvariablesname()+"-copy");
                    testvariables.setId(null);
                    testvariablesService.save(testvariables);
                }

                //复制前置条件
                SubCondition(Long.parseLong(sourcecaseid), NewCaseId, newcasename);
                //api用例数加1
                Api api = apiService.getById(Apiid);
                long casecount = api.getCasecounts();
                api.setCasecounts(casecount + 1);
                apiService.updateApi(api);

                Deployunit desdeployunit= deployunitService.getBy("id",Long.parseLong(sourcedeployunitid));
                desdeployunit.setApicounts(desdeployunit.getApicounts()+1);
                deployunitService.update(desdeployunit);
            }
            return ResultGenerator.genOkResult();
        }
    }


    //批量复制微服务的用例
    @PostMapping("/copydeployunitcases")
    public Result copydeployunitcases(@RequestBody final Map<String, Object> param) {
        Long sourcedeployunitid = Long.parseLong(param.get("sourcedeployunitid").toString());
        String sourcedeployunitname = param.get("sourcedeployunitname").toString();
        Long destinationdeployunitid = Long.parseLong(param.get("destinationdeployunitid").toString());
        String destinationdeployunitname = param.get("destinationdeployunitname").toString();

        if (sourcedeployunitid.equals(destinationdeployunitid)) {
            return ResultGenerator.genFailedResult("源微服务和目标微服务相同，请选择不同的微服务进行批量复制用例");
        } else {
            Condition apicon = new Condition(Api.class);
            apicon.createCriteria().andCondition("deployunitid = " + sourcedeployunitid);
            if (apiService.ifexist(apicon) == 0) {
                return ResultGenerator.genFailedResult(sourcedeployunitname + "源微服务不存在任何API接口，请先完善API");
            } else {
                List<Api> SourceapiList = apiService.listByCondition(apicon);
                for (Api SourceApi : SourceapiList) {
                    long SourceApiid = SourceApi.getId();
                    //1.复制api
                    Api DestinationApi = new Api();
                    long DestinationApiid = 0;
                    String DestinationApiName = "";
                    Condition desapicon = new Condition(Api.class);
                    desapicon.createCriteria().andCondition("deployunitid = " + destinationdeployunitid)
                            .andCondition("apiname='" + SourceApi.getApiname() + "-批量复制用例'");

                    List<Api> apiList=apiService.listByCondition(desapicon);
                    if (apiList.size()== 0) {
                        DestinationApi.setMid(SourceApi.getMid());
                        DestinationApi.setMnickname(SourceApi.getMnickname());
                        DestinationApi.setProjectid(SourceApi.getProjectid());
                        DestinationApi.setModelid(SourceApi.getModelid());
                        DestinationApi.setModelname(SourceApi.getModelname());
                        DestinationApi.setVisittype(SourceApi.getVisittype());
                        DestinationApi.setPath(SourceApi.getPath());
                        DestinationApi.setRequestcontenttype(SourceApi.getRequestcontenttype());
                        DestinationApi.setCreator(SourceApi.getCreator());
                        DestinationApi.setCreatorid(SourceApi.getCreatorid());
                        DestinationApi.setMemo(SourceApi.getMemo());
                        DestinationApi.setCreateTime(new Date());
                        DestinationApi.setLastmodifyTime(new Date());
                        DestinationApi.setApistyle(SourceApi.getApistyle());
                        DestinationApi.setResponecontenttype(SourceApi.getResponecontenttype());
                        DestinationApi.setDeployunitname(destinationdeployunitname);
                        DestinationApi.setDeployunitid(destinationdeployunitid);
                        DestinationApi.setId(null);
                        DestinationApi.setCasecounts(SourceApi.getCasecounts());
                        DestinationApi.setApiname(SourceApi.getApiname() + "-批量复制用例");
                        apiService.save(DestinationApi);
                        DestinationApiid=DestinationApi.getId();
                        DestinationApiName=DestinationApi.getApiname();

                        //2.复制api参数
                        Condition apiparamcon = new Condition(ApiParams.class);
                        apiparamcon.createCriteria().andCondition("apiid = " + SourceApiid);
                        List<ApiParams> apiParamsList = apiParamsService.listByCondition(apiparamcon);
                        for (ApiParams SourceParam : apiParamsList) {
                            ApiParams DestinationParam =new ApiParams();
                            DestinationParam.setCreator(SourceParam.getCreator());
                            DestinationParam.setCreatorid(SourceParam.getCreatorid());
                            DestinationParam.setLastmodifyTime(new Date());
                            DestinationParam.setCreateTime(new Date());
                            DestinationParam.setKeytype(SourceParam.getKeytype());
                            DestinationParam.setKeyname(SourceParam.getKeyname());
                            DestinationParam.setKeydefaultvalue(SourceParam.getKeydefaultvalue());
                            DestinationParam.setPropertytype(SourceParam.getPropertytype());
                            DestinationParam.setApiid(DestinationApiid);
                            DestinationParam.setApiname(DestinationApiName);
                            DestinationParam.setDeployunitname(destinationdeployunitname);
                            DestinationParam.setDeployunitid(destinationdeployunitid);
                            DestinationParam.setId(null);
                            apiParamsService.save(DestinationParam);
                        }
                    } else
                    {
                         DestinationApiid = apiList.get(0).getId();
                         DestinationApiName = apiList.get(0).getApiname();
                    }

                    //3.复制api用例
                    Condition apicasecon = new Condition(Apicases.class);
                    apicasecon.createCriteria().andCondition("apiid = " + SourceApiid);
                    List<Apicases> apicasesList = apicasesService.listByCondition(apicasecon);
                    for (Apicases SourceCase : apicasesList) {
                        long SourceCaseID = SourceCase.getId();
                        Apicases DesitionApicase = new Apicases() ;
                        DesitionApicase.setMid(SourceCase.getMid());
                        DesitionApicase.setMnickname(SourceCase.getMnickname());
                        DesitionApicase.setProjectid(SourceCase.getProjectid());
                        DesitionApicase.setModelid(SourceCase.getModelid());
                        DesitionApicase.setModelname(SourceCase.getModelname());
                        DesitionApicase.setCasecontent(SourceCase.getCasecontent());
                        DesitionApicase.setCasejmxname(SourceCase.getCasejmxname());
                        DesitionApicase.setCasetype(SourceCase.getCasetype());
                        DesitionApicase.setCreateTime(new Date());
                        DesitionApicase.setLastmodifyTime(new Date());
                        DesitionApicase.setExpect(SourceCase.getExpect());
                        DesitionApicase.setLevel(SourceCase.getLevel());
                        DesitionApicase.setLoops(SourceCase.getLoops());
                        DesitionApicase.setCreator(SourceCase.getCreator());
                        DesitionApicase.setThreadnum(SourceCase.getThreadnum());
                        DesitionApicase.setMemo(SourceCase.getMemo());
                        DesitionApicase.setMiddleparam(SourceCase.getMiddleparam());
                        DesitionApicase.setDeployunitname(destinationdeployunitname);
                        DesitionApicase.setDeployunitid(destinationdeployunitid);
                        DesitionApicase.setApiid(DestinationApiid);
                        DesitionApicase.setApiname(DestinationApiName);
                        DesitionApicase.setMid(SourceCase.getMid());
                        DesitionApicase.setMnickname(SourceCase.getMnickname());
                        DesitionApicase.setCreatorid(SourceCase.getCreatorid());
                        DesitionApicase.setId(null);

                        Condition desapicasecon = new Condition(Apicases.class);
                        desapicasecon.createCriteria().andCondition("deployunitid = " + destinationdeployunitid)
                                .andCondition("casename='" + SourceCase.getCasename()+"-批量复制用例'");
                        if (apicasesService.ifexist(desapicasecon) == 0) {
                            DesitionApicase.setCasename(SourceCase.getCasename()+"-批量复制用例");
                            apicasesService.save(DesitionApicase);

                            long DestinationCaseID = DesitionApicase.getId();
                            String DestinationCaseName = DesitionApicase.getCasename();

                            //4.复制用例数据
                            Condition apicasedatacon = new Condition(ApiCasedata.class);
                            apicasedatacon.createCriteria().andCondition("caseid = " + SourceCaseID);
                            List<ApiCasedata> apiCasedataList = apiCasedataService.listByCondition(apicasedatacon);
                            for (ApiCasedata SourceCaseData : apiCasedataList) {
                                ApiCasedata DestinationData = SourceCaseData;
                                DestinationData.setCaseid(DestinationCaseID);
                                DestinationData.setId(null);
                                apiCasedataService.save(DestinationData);
                            }

                            //5.复制用例断言
                            Condition CaseAssertCondition = new Condition(ApicasesAssert.class);
                            CaseAssertCondition.createCriteria().andCondition("caseid = " + SourceCaseID);
                            List<ApicasesAssert> SourceAssertdataList = apicasesAssertService.listByCondition(CaseAssertCondition);
                            for (ApicasesAssert apicasesAssert : SourceAssertdataList) {
                                apicasesAssert.setCaseid(DestinationCaseID);
                                apicasesAssert.setId(null);
                                apicasesAssertService.save(apicasesAssert);
                            }
                            //复制数据库断言
                            Condition AssertDBCondition = new Condition(ApicasesDbassert.class);
                            AssertDBCondition.createCriteria().andCondition("caseid = " + SourceCaseID);
                            List<ApicasesDbassert> SourceAssertdbList = apicasesDbassertService.listByCondition(AssertDBCondition);
                            for (ApicasesDbassert apicasesDbassert : SourceAssertdbList) {
                                long sourcedbassertid=apicasesDbassert.getId();
                                apicasesDbassert.setCaseid(DestinationCaseID);
                                apicasesDbassert.setId(null);
                                apicasesDbassertService.save(apicasesDbassert);
                                long dbassertid=apicasesDbassert.getId();
                                Condition AssertDBValueCondition = new Condition(ApicasesDbassertValue.class);
                                AssertDBValueCondition.createCriteria().andCondition("dbassertid = " + sourcedbassertid);
                                List<ApicasesDbassertValue> apicasesDbassertValueList= apicasesDbassertValueService.listByCondition(AssertDBValueCondition);
                                for (ApicasesDbassertValue apicasesDbassertValue : apicasesDbassertValueList) {
                                    apicasesDbassertValue.setDbassertid(dbassertid);
                                    apicasesDbassertValue.setId(null);
                                    apicasesDbassertValueService.save(apicasesDbassertValue);
                                }
                            }

                            //6.复制提取变量
                            Condition TestvariablesCondition = new Condition(Testvariables.class);
                            TestvariablesCondition.createCriteria().andCondition("caseid = " + SourceCaseID);
                            List<Testvariables> TestvariablesConditionList = testvariablesService.listByCondition(TestvariablesCondition);
                            for (Testvariables testvariables : TestvariablesConditionList) {
                                testvariables.setCaseid(DestinationCaseID);
                                testvariables.setCasename(DestinationCaseName);
                                testvariables.setTestvariablesname(testvariables.getTestvariablesname()+"-batchcopy");
                                testvariables.setId(null);
                                testvariablesService.save(testvariables);
                            }

                            //7.复制前置调试条件
                            SubCondition(SourceCaseID, DestinationCaseID, DestinationCaseName);
                        }

                    }
                }
                Deployunit desdeployunit= deployunitService.getBy("id",destinationdeployunitid);
                desdeployunit.setApicounts(Long.valueOf(SourceapiList.size()));
                deployunitService.update(desdeployunit);
                return ResultGenerator.genOkResult();
            }
        }
    }

    private void SubCondition(long SourceCaseID, long DesCaseid, String DesCaseName) {
        long subconditiondbid = 0;
        long subconditionscriptid = 0;
//        long subconditiondelayid = 0;

        //复制前置接口条件
        Condition APISubCondition = new Condition(ConditionApi.class);
        APISubCondition.createCriteria().andCondition("conditionid = " + SourceCaseID)
                .andCondition("conditiontype='case'");
        List<ConditionApi> conditionApiList = conditionApiService.listByCondition(APISubCondition);
        for (ConditionApi SourceConditionApi : conditionApiList) {
            SourceConditionApi.setId(null);
            SourceConditionApi.setConditionid(DesCaseid);
            SourceConditionApi.setConditionname(DesCaseName);
            SourceConditionApi.setSubconditionname(SourceConditionApi.getSubconditionname() + "-复制");
            conditionApiService.save(SourceConditionApi);

            ConditionOrder conditionOrder = new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(SourceConditionApi.getConditiontype());
            conditionOrder.setConditionid(SourceConditionApi.getId());
            conditionOrder.setConditionname(SourceConditionApi.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(SourceConditionApi.getConditionid());
            conditionOrder.setSubconditionname(SourceConditionApi.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置接口条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(SourceConditionApi.getCreator());
            conditionOrderService.save(conditionOrder);

        }

        //复制前置数据库条件
        Condition DBSubCondition = new Condition(ConditionDb.class);
        DBSubCondition.createCriteria().andCondition("conditionid = " + SourceCaseID)
                .andCondition("conditiontype='case'");
        List<ConditionDb> conditionDbList = conditionDbService.listByCondition(DBSubCondition);
        for (ConditionDb SourceConditionDB : conditionDbList) {
            SourceConditionDB.setId(null);
            SourceConditionDB.setConditionid(DesCaseid);
            SourceConditionDB.setConditionname(DesCaseName);
            SourceConditionDB.setSubconditionname(SourceConditionDB.getSubconditionname() + "-复制");
            conditionDbService.save(SourceConditionDB);
            subconditiondbid = SourceConditionDB.getId();
            String SubConditionname = SourceConditionDB.getSubconditionname();

            //复制数据库变量
            Condition DbvariablesCondition = new Condition(Dbvariables.class);
            DbvariablesCondition.createCriteria().andCondition("conditionid = " + subconditiondbid);
            List<Dbvariables> DbvariablesList = dbvariablesService.listByCondition(DbvariablesCondition);

            for (Dbvariables dbvariables : DbvariablesList) {
                dbvariables.setId(null);
                dbvariables.setConditionid(subconditiondbid);
                dbvariables.setConditionname(SubConditionname);
                dbvariablesService.save(dbvariables);
            }

            ConditionOrder conditionOrder = new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(SourceConditionDB.getConditiontype());
            conditionOrder.setConditionid(SourceConditionDB.getId());
            conditionOrder.setConditionname(SourceConditionDB.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(SourceConditionDB.getConditionid());
            conditionOrder.setSubconditionname(SourceConditionDB.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置数据库条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(SourceConditionDB.getCreator());
            conditionOrderService.save(conditionOrder);
        }

        //复制前置脚本条件
        Condition ScriptSubCondition = new Condition(ConditionScript.class);
        ScriptSubCondition.createCriteria().andCondition("conditionid = " + SourceCaseID)
                .andCondition("conditiontype='case'");
        List<ConditionScript> conditionScriptList = conditionScriptService.listByCondition(ScriptSubCondition);
        for (ConditionScript SourceConditionScript : conditionScriptList) {
            SourceConditionScript.setId(null);
            SourceConditionScript.setConditionid(DesCaseid);
            SourceConditionScript.setConditionname(DesCaseName);
            SourceConditionScript.setSubconditionname(SourceConditionScript.getSubconditionname() + "-复制");
            conditionScriptService.save(SourceConditionScript);
            subconditionscriptid = SourceConditionScript.getId();
            String SubConditionname = SourceConditionScript.getSubconditionname();

            //复制脚本变量
            Condition ScriptvariablesCondition = new Condition(Scriptvariables.class);
            ScriptvariablesCondition.createCriteria().andCondition("conditionid = " + subconditionscriptid);
            List<Scriptvariables> scriptvariablesList = scriptvariablesService.listByCondition(ScriptvariablesCondition);
            for (Scriptvariables scriptvariables : scriptvariablesList) {
                scriptvariables.setId(null);
                scriptvariables.setConditionid(subconditiondbid);
                scriptvariables.setConditionname(SubConditionname);
                scriptvariablesService.save(scriptvariables);
            }

            ConditionOrder conditionOrder = new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(SourceConditionScript.getConditiontype());
            conditionOrder.setConditionid(SourceConditionScript.getId());
            conditionOrder.setConditionname(SourceConditionScript.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(SourceConditionScript.getConditionid());
            conditionOrder.setSubconditionname(SourceConditionScript.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置脚本条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(SourceConditionScript.getCreator());
            conditionOrderService.save(conditionOrder);
        }

        //复制前置延时条件
        Condition DelaySubCondition = new Condition(ConditionDelay.class);
        DelaySubCondition.createCriteria().andCondition("conditionid = " + SourceCaseID)
                .andCondition("conditiontype='case'");
        List<ConditionDelay> conditionDelayList = conditionDelayService.listByCondition(DelaySubCondition);
        for (ConditionDelay SourceConditionDelay : conditionDelayList) {
            SourceConditionDelay.setId(null);
            SourceConditionDelay.setConditionid(DesCaseid);
            SourceConditionDelay.setConditionname(DesCaseName);
            conditionDelayService.save(SourceConditionDelay);

            ConditionOrder conditionOrder = new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(SourceConditionDelay.getConditiontype());
            conditionOrder.setConditionid(SourceConditionDelay.getId());
            conditionOrder.setConditionname(SourceConditionDelay.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(SourceConditionDelay.getConditionid());
            conditionOrder.setSubconditionname(SourceConditionDelay.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置延时条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(SourceConditionDelay.getCreator());
            conditionOrderService.save(conditionOrder);
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        Apicases apicases = apicasesService.getById(id);
        apicasesService.deleteById(id);
        //删除用例值数据
        apiCasedataService.deletcasedatabyid(id);
        //删除用例断言
        Condition caseassertcon = new Condition(ApicasesAssert.class);
        caseassertcon.createCriteria().andCondition("caseid = " + id);
        apicasesAssertService.deleteByCondition(caseassertcon);
        //删除用例条件，子条件
        Condition con = new Condition(Testcondition.class);
        con.createCriteria().andCondition("objecttype = '测试用例'").andCondition("objectid = " + id).andCondition("conditiontype = '" + "前置条件'");
        List<Testcondition> testconditionList = testconditionService.listByCondition(con);
        if (testconditionList.size() > 0) {
            Long ConditionID = testconditionList.get(0).getId();
            conditionApiService.deleteBy("conditionid", ConditionID);
            conditionDbService.deleteBy("conditionid", ConditionID);
            conditionScriptService.deleteBy("conditionid", ConditionID);
            conditionDelayService.deleteBy("conditionid", ConditionID);
            conditionOrderService.deleteBy("conditionid", ConditionID);
            testconditionService.deleteByCondition(con);
        }
        //删除测试集合中的用例
        executeplanTestcaseService.removetestcase(id);
        //删除调试集合中的用例
        long ConditionID = 0;
        ApicasesDebugCondition apicasesDebugCondition = apicasesDebugConditionService.getBy("caseid", id);
        if (apicasesDebugCondition != null) {
            ConditionID = apicasesDebugCondition.getConditionid();
        }
        apicasesDebugConditionService.deleteBy("caseid", id);
        Condition ApicasesDebugCondition = new Condition(ApicasesDebugCondition.class);
        ApicasesDebugCondition.createCriteria().andCondition("conditionid = " + ConditionID);
        List<ApicasesDebugCondition> apicasesDebugConditionList = apicasesDebugConditionService.listByCondition(ApicasesDebugCondition);
        if (apicasesDebugConditionList.size() == 0) {
            testconditionService.deleteBy("id", ConditionID);
        }

        Api api = apiService.getById(apicases.getApiid());
        long casecount = api.getCasecounts();
        api.setCasecounts(casecount - 1);
        apiService.updateApi(api);
        return ResultGenerator.genOkResult();
    }


    @PostMapping("/removebatchapicase")
    public Result removebatchapicase(@RequestBody List<Apicases> apicasesList) {
        try {
            for (Apicases apicases : apicasesList) {
                long id = apicases.getId();
                apicasesService.deleteById(id);
                //删除用例值数据
                apiCasedataService.deletcasedatabyid(id);
                //删除用例断言
                Condition caseassertcon = new Condition(ApicasesAssert.class);
                caseassertcon.createCriteria().andCondition("caseid = " + id);
                apicasesAssertService.deleteByCondition(caseassertcon);
                //删除用例条件，子条件
                Condition con = new Condition(Testcondition.class);
                con.createCriteria().andCondition("objecttype = '测试用例'").andCondition("objectid = " + id).andCondition("conditiontype = '" + "前置条件'");
                List<Testcondition> testconditionList = testconditionService.listByCondition(con);
                if (testconditionList.size() > 0) {
                    Long ConditionID = testconditionList.get(0).getId();
                    conditionApiService.deleteBy("conditionid", ConditionID);
                    conditionDbService.deleteBy("conditionid", ConditionID);
                    conditionScriptService.deleteBy("conditionid", ConditionID);
                    conditionDelayService.deleteBy("conditionid", ConditionID);
                    conditionOrderService.deleteBy("conditionid", ConditionID);
                    testconditionService.deleteByCondition(con);
                }
                //删除测试集合中的用例
                executeplanTestcaseService.removetestcase(id);
                //删除调试集合中的用例
                long ConditionID = 0;
                ApicasesDebugCondition apicasesDebugCondition = apicasesDebugConditionService.getBy("caseid", id);
                if (apicasesDebugCondition != null) {
                    ConditionID = apicasesDebugCondition.getConditionid();
                }
                apicasesDebugConditionService.deleteBy("caseid", id);
                Condition ApicasesDebugCondition = new Condition(ApicasesDebugCondition.class);
                ApicasesDebugCondition.createCriteria().andCondition("conditionid = " + ConditionID);
                List<ApicasesDebugCondition> apicasesDebugConditionList = apicasesDebugConditionService.listByCondition(ApicasesDebugCondition);
                if (apicasesDebugConditionList.size() == 0) {
                    testconditionService.deleteBy("id", ConditionID);
                }
                Api api = apiService.getById(apicases.getApiid());
                long casecount = api.getCasecounts();
                api.setCasecounts(casecount - 1);
                apiService.updateApi(api);
            }
        } catch (Exception ex) {
            ApicasesController.log.info("removebatchapicase Exception: " + ex.getMessage());
        }

        return ResultGenerator.genOkResult();
    }


    @PostMapping("/batchassertapicase")
    public Result batchassertapicase(@RequestBody List<ApicasesAssert> apicasesAssertList) {
        try {
            for (ApicasesAssert apicasesAssert : apicasesAssertList) {
                Condition con = new Condition(Apicases.class);
                con.createCriteria().andCondition("caseid = " + apicasesAssert.getCaseid())
                        .andCondition("asserttype = '" + apicasesAssert.getAsserttype() + "'");
                if (apicasesAssertService.ifexist(con) > 0) {
                    List<ApicasesAssert> apicasesAssertList1 = apicasesAssertService.listByCondition(con);
                    apicasesAssert.setId(apicasesAssertList1.get(0).getId());
                    apicasesAssertService.updateAssert(apicasesAssert);
                } else {
                    apicasesAssertService.save(apicasesAssert);
                }
            }
        } catch (Exception ex) {
            ApicasesController.log.info("batchassertapicase Exception: " + ex.getMessage());
        }
        return ResultGenerator.genOkResult();
    }


    @PatchMapping
    public Result update(@RequestBody Apicases apicases) {
        apicasesService.update(apicases);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Apicases apicases = apicasesService.getById(id);
        return ResultGenerator.genOkResult(apicases);
    }

    @GetMapping("/getcasenum")
    public Result getcasenum(@RequestParam String casetype, @RequestParam long projectid) {
        Integer apicasesnum = apicasesService.getcasenum(casetype, projectid);
        return ResultGenerator.genOkResult(apicasesnum);
    }

    @GetMapping("/getperformancecasenum")
    public Result getperformancecasenum(@RequestParam String casetype, @RequestParam long projectid) {
        Integer apicasesnum = apicasesService.getcasenum(casetype, projectid);
        return ResultGenerator.genOkResult(apicasesnum);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Apicases> list = apicasesService.listAll();
        PageInfo<Apicases> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getstaticsdeployunitcases")
    public Result getstaticsdeployunitcases(@RequestParam long projectid) {
        List<Apicases> list = apicasesService.getstaticsdeployunitcases(projectid);
        List<StaticsDataForPie> result = new ArrayList<>();
        for (Apicases ac : list) {
            StaticsDataForPie staticsDataForPie = new StaticsDataForPie();
            staticsDataForPie.setValue(ac.getApiid());
            staticsDataForPie.setName(ac.getDeployunitname());
            result.add(staticsDataForPie);
        }
        return ResultGenerator.genOkResult(result);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Apicases apicases) {
        long apicaseid = apicases.getId();
        Long creatorid = apicases.getCreatorid();
        AccountRole accountRole= accountRoleService.getBy("accountId",creatorid);
        if(accountRole==null)
        {
            return ResultGenerator.genFailedResult("当前用户不存在");
        }
        Apicases apicaseexist = apicasesService.getBy("id", apicaseid);
        if (apicaseexist != null) {
            if (creatorid.equals(apicaseexist.getMid()) || accountRole.getRoleId() == 1) {
                if (apicasesService.forupdateifexist(apicases).size() > 0) {
                    return ResultGenerator.genFailedResult("用例名已存在");
                } else {
                    //限制下类型变化，如果已经在对应类型的场景中，就不能改变类型
                    Apicases existapicases = apicasesService.getBy("id", apicases.getId());
                    if (existapicases != null) {
                        if (!apicases.getCasetype().equalsIgnoreCase(existapicases.getCasetype())) {
                            Condition con = new Condition(TestsceneTestcase.class);
                            con.createCriteria().andCondition("projectid = " + apicases.getProjectid())
                                    .andCondition("testcaseid = " + apicases.getId());
                            List<TestsceneTestcase> testsceneTestcaseList = testsceneTestcaseService.listByCondition(con);
                            boolean flag = false;
                            String SceneName = "";
                            for (TestsceneTestcase testcase : testsceneTestcaseList) {
                                Long Sceneid = testcase.getTestscenenid();
                                Testscene testscene = testsceneService.getBy("id", Sceneid);
                                if (testscene.getUsetype().equalsIgnoreCase(existapicases.getCasetype())) {
                                    SceneName = testscene.getScenename();
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                this.apicasesService.updateApicase(apicases);
                                //增加更新条件管理，子条件管理中的用例名
                                testconditionService.updatecasename(apicases.getId(), "测试用例", apicases.getCasename());
                                conditionApiService.updatecasename(apicases.getId(), apicases.getCasename());
                                return ResultGenerator.genOkResult();
                            } else {
                                return ResultGenerator.genFailedResult("当前用例类型为：" + existapicases.getCasetype() + ",已在相同类型的测试场景:" + SceneName + " 中存在，请先到测试场景中移除该用例后再尝试修改类型");
                            }
                        } else {
                            this.apicasesService.updateApicase(apicases);
                            //增加更新条件管理，子条件管理中的用例名
                            testconditionService.updatecasename(apicases.getId(), "测试用例", apicases.getCasename());
                            conditionApiService.updatecasename(apicases.getId(), apicases.getCasename());
                            return ResultGenerator.genOkResult();
                        }
                    } else {
                        return ResultGenerator.genFailedResult("当前用例已被删除");
                    }
                }
            } else {
                return ResultGenerator.genFailedResult("当前api只有维护人或者管理员可以修改");
            }
        }else {
            return ResultGenerator.genFailedResult("当前用例不存在");
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
        final List<Apicases> list = this.apicasesService.findApiCaseWithName(param);
        final PageInfo<Apicases> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/searchleftcase")
    public Result searchleftcase(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Apicases> list = this.apicasesService.findApiCaseleft(param);
        final PageInfo<Apicases> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 根据微服务id获取用例
     */
    @PostMapping("/getcasebydeployunitid")
    public Result getcasebydeployunitid(@RequestBody final Map<String, Object> param) {
        String deployunitid = param.get("sourcedeployunitid").toString();
        final List<Apicases> list = this.apicasesService.getcasebydeployunitid(Long.parseLong(deployunitid));
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/searchbyname")
    public Result searchbyname(@RequestBody final Map<String, Object> param) {
        long deployunitid = Long.parseLong(param.get("deployunitid").toString());
        long apiid = Long.parseLong(param.get("apiid").toString());
        final List<Apicases> list = this.apicasesService.getapicasebyName(deployunitid, apiid);
        return ResultGenerator.genOkResult(list);
    }

    private String getSubConditionRespone(String Url, String params, HttpHeader header) throws Exception {
        //请求API条件
        TestHttp testHttp = new TestHttp();
        header.addParam("Content-Type", "application/json;charset=utf-8");
        TestResponeData testResponeData = testHttp.doService("http", "", Url, header, new HttpParamers(), params, "POST", "", 30000);
        String Respone = testResponeData.getResponeContent();
        if (Respone.contains("条件执行异常")) {
            JSONObject object = JSON.parseObject(Respone);
            throw new Exception(object.getString("message"));
        }
        return Respone;
    }


    private ConditionResult FixCondition(List<Testcondition> testconditionList, Map<String, Object> param, long Caseid, String objecttype) throws Exception {
        //List<Testcondition> testconditionList = testconditionService.GetConditionByPlanIDAndConditionType(Caseid, "前置条件", objecttype);
        ConditionResult conditionResult = new ConditionResult();
        String APIRespone = "";
        String DBRespone = "";
        conditionResult.setAPIRespone(APIRespone);
        conditionResult.setDBRespone(DBRespone);
        if (testconditionList.size() > 0) {
            String ScriptConditionServerurl = conditionserver + "/testcondition/execcasecondition/script";
            String DBConditionServerurl = conditionserver + "/testcondition/execcasecondition/db";
            String APIConditionServerurl = conditionserver + "/testcondition/execcasecondition/api";

            Long ConditionID = testconditionList.get(0).getId();
            Map<String, Object> conditionmap = new HashMap<>();
            conditionmap.put("conditionid", ConditionID);
            List<ConditionOrder> conditionOrderList = conditionOrderService.findconditionorderWithid(conditionmap);
            param.put("ConditionID", ConditionID);

            HttpHeader header = new HttpHeader();
            try {
                if (conditionOrderList.size() > 0) {
                    for (ConditionOrder conditionOrder : conditionOrderList) {
                        ApicasesController.log.info("条件顺序接口前置子条件名===================：" + conditionOrder.getSubconditionname() + " 子条件名" + conditionOrder.getConditionorder());
                        param.put("dbvariablesvalue", DBRespone);
                        String params = "";
                        if (conditionOrder.getSubconditiontype().equals("接口")) {
                            long subconditionid = conditionOrder.getSubconditionid();
                            ConditionApi conditionApi = conditionApiService.getBy("id", subconditionid);
                            long apicaseid = conditionApi.getCaseid();
                            param.put("apicaseid", apicaseid);
                            ApicasesController.log.info("条件顺序接口前置子条件名===================：" + conditionApi.getCasename());
                            params = JSON.toJSONString(param);
                            ApicasesController.log.info("。。。。。。。。接口前置子条件请求数据：" + params);
                            APIRespone = getSubConditionRespone(APIConditionServerurl, params, header);
                            param.put("apivariablesvalues", APIRespone);
                            params = JSON.toJSONString(param);
                            ApicasesController.log.info("条件顺序接口前置子条件请求结果===================：" + APIRespone);
                        }
                        if (conditionOrder.getSubconditiontype().equals("数据库")) {
                            DBRespone = getSubConditionRespone(DBConditionServerurl, params, header);
                            param.put("dbvariablesvalue", DBRespone);
                            params = JSON.toJSONString(param);
                        }
                        if (conditionOrder.getSubconditiontype().equals("脚本")) {
                            getSubConditionRespone(ScriptConditionServerurl, params, header);
                        }
                    }
                } else {
                    String params = JSON.toJSONString(param);
                    Condition dbcon = new Condition(ConditionDb.class);
                    dbcon.createCriteria().andCondition("conditionid=" + ConditionID);
                    List<ConditionDb> conditionDbList = conditionDbService.listByCondition(dbcon);
                    if (conditionDbList.size() > 0) {
                        ApicasesController.log.info("。。。。。。。。数据库前置子条件非顺序请求数据：" + params);
                        DBRespone = getSubConditionRespone(DBConditionServerurl, params, header);
                    }
                    param.put("dbvariablesvalue", DBRespone);
                    ApicasesController.log.info("。。。。。。。。数据库前置子条件非顺序结果：" + DBRespone);
                    Condition apicon = new Condition(ConditionApi.class);
                    apicon.createCriteria().andCondition("conditionid=" + ConditionID);
                    List<ConditionApi> conditionApiList = conditionApiService.listByCondition(apicon);
                    //需要增加多个接口子条件的处理逻辑
                    if (conditionApiList.size() > 0) {
                        long apicaseid = conditionApiList.get(0).getCaseid();
                        param.put("apicaseid", apicaseid);
                        params = JSON.toJSONString(param);
                        ApicasesController.log.info("。。。。。。。。接口前置子条件非顺序请求数据：" + params);
                        APIRespone = getSubConditionRespone(APIConditionServerurl, params, header);
                    }

                    Condition scriptcon = new Condition(ConditionScript.class);
                    scriptcon.createCriteria().andCondition("conditionid=" + ConditionID);
                    List<ConditionScript> conditionScriptList = conditionScriptService.listByCondition(scriptcon);

                    if (conditionScriptList.size() > 0) {
                        long apicaseid = conditionApiList.get(0).getCaseid();
                        param.put("apicaseid", apicaseid);
                        ApicasesController.log.info("。。。。。。。。脚本前置子条件非顺序请求数据：" + params);
                        getSubConditionRespone(ScriptConditionServerurl, params, header);
                    }
                }
                conditionResult.setAPIRespone(APIRespone);
                conditionResult.setDBRespone(DBRespone);
            } catch (Exception ex) {
                if (ex.getMessage().contains("Connection refused")) {
                    throw new Exception("无法连接条件服务器，请检查ConditionService是否正常启动！");
                } else {
                    throw new Exception(ex.getMessage());
                }
            }
        }
        return conditionResult;
    }

    private HashMap<String, String> GetResponeMap(String Respone, HashMap<String, String> ResponeMap, String KeyName) throws Exception {
        if (!Respone.isEmpty()) {
            try {
                JSONObject jsonObject = JSON.parseObject(Respone);
                for (Map.Entry<String, Object> objectEntry : jsonObject.getJSONObject("data").entrySet()) {
                    String key = objectEntry.getKey();
                    if (KeyName.equalsIgnoreCase(key)) {
                        String value = objectEntry.getValue().toString();
                        JSONObject jsonObject1 = JSON.parseObject(value);
                        for (Map.Entry<String, Object> apiobjectEntry : jsonObject1.entrySet()) {
                            String keyapi = apiobjectEntry.getKey();
                            String apivalue = apiobjectEntry.getValue().toString();
                            ResponeMap.put(keyapi, apivalue);
                        }
                    }
                }
            } catch (Exception ex) {
                throw new Exception("执行前置条件结果异常：" + Respone);
            }
        }
        return ResponeMap;
    }

    /**
     * 运行测试
     */
    @PostMapping("/runscenetest")
    public Result runscenetest(@RequestBody final Map<String, Object> param) throws Exception {
        String enviromentid = param.get("enviromentid").toString();
        Long sceneid = Long.parseLong(param.get("sceneid").toString());
        String scenename = param.get("scenename").toString();

        Long projectid = Long.parseLong(param.get("projectid").toString());
        Long globalheaderid = Long.parseLong(param.get("globalheaderid").toString());


        Condition scenecon = new Condition(Apicases.class);
        scenecon.createCriteria().andCondition("projectid = " + projectid)
                .andCondition("testscenenid = " + sceneid);
        List<TestsceneTestcase> testsceneTestcaseList = testsceneTestcaseService.listByCondition(scenecon);

        if (testsceneTestcaseList.size() == 0) {
            return ResultGenerator.genFailedResult("当前测试场景下无测试用例，请先装载用例后再调试！");
        }

        //先获取场景的前置结果
        HashMap<String, String> SceneParamsValuesMap = new HashMap<>();
        HashMap<String, String> SceneDBParamsValuesMap = new HashMap<>();
        HashMap<String, String> SceneScriptParamsValuesMap = new HashMap<>();

        String SceneRespone = "";
        HashMap<String, Object> scenedebugmap = new HashMap<>();
        scenedebugmap.put("conditionid", sceneid);
        scenedebugmap.put("enviromentid", Long.parseLong(enviromentid));
        scenedebugmap.put("conditiontype", "scene");

        String SceneConditionServerurl = conditionserver + "/testcondition/execdebugcondition";
        HttpHeader sceneheader1 = new HttpHeader();
        String scenedebugparams = JSON.toJSONString(scenedebugmap);
        try {
            SceneRespone = getSubConditionRespone(SceneConditionServerurl, scenedebugparams, sceneheader1);
        } catch (Exception ex) {
            return ResultGenerator.genFailedResult("前置条件处理异常：！" + ex.getMessage());
        }
        SceneParamsValuesMap = GetResponeMap(SceneRespone, SceneParamsValuesMap, "api");
        SceneDBParamsValuesMap = GetResponeMap(SceneRespone, SceneDBParamsValuesMap, "db");
        SceneScriptParamsValuesMap = GetResponeMap(SceneRespone, SceneScriptParamsValuesMap, "script");

        /////////////////////////////////////////////////////////////////////////////////////////
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String batchname = scenename + "-" + dateFormat.format(date);

        for (TestsceneTestcase te : testsceneTestcaseList) {
            Long Caseid = te.getTestcaseid();
            String Casename = te.getCasename();
            ScenecasesDebugReport scenecasesDebugReport = new ScenecasesDebugReport();
            String Status = "运行完成";
            HashMap<String, String> ParamsValuesMap = new HashMap<>();
            HashMap<String, String> DBParamsValuesMap = new HashMap<>();
            HashMap<String, String> ScriptParamsValuesMap = new HashMap<>();

            String Respone = "";
            HashMap<String, Object> debugmap = new HashMap<>();
            debugmap.put("conditionid", Caseid);
            debugmap.put("conditiontype", "case");
            debugmap.put("enviromentid", Long.parseLong(enviromentid));

            String ConditionServerurl = conditionserver + "/testcondition/execdebugcondition";
            HttpHeader header1 = new HttpHeader();
            String debugparams = JSON.toJSONString(debugmap);
            try {
                Respone = getSubConditionRespone(ConditionServerurl, debugparams, header1);
            } catch (Exception ex) {
                return ResultGenerator.genFailedResult("前置条件处理异常：！" + ex.getMessage());
            }
            ParamsValuesMap = GetResponeMap(Respone, ParamsValuesMap, "api");
            DBParamsValuesMap = GetResponeMap(Respone, DBParamsValuesMap, "db");
            ScriptParamsValuesMap = GetResponeMap(Respone, ScriptParamsValuesMap, "script");


            ParamsValuesMap.putAll(SceneParamsValuesMap);
            DBParamsValuesMap.putAll(SceneDBParamsValuesMap);
            ScriptParamsValuesMap.putAll(SceneScriptParamsValuesMap);

            Apicases apicases = apicasesService.getBy("id", Caseid);
            if (apicases == null) {
                return ResultGenerator.genFailedResult("当前用例不存在，请检查是否被删除！");
            }
            Long Apiid = apicases.getApiid();
            Api api = apiService.getBy("id", Apiid);
            if (api == null) {
                return ResultGenerator.genFailedResult("当前用例的API不存在，请检查是否被删除！");
            }
            String Method = api.getVisittype();
            String ApiStyle = api.getApistyle();
            Deployunit deployunit = deployunitService.getBy("id", api.getDeployunitid());
            if (deployunit == null) {
                return ResultGenerator.genFailedResult("当前用例的API所在的微服务不存在，请检查是否被删除！");
            }
            String Protocal = deployunit.getProtocal();
            String BaseUrl = deployunit.getBaseurl();
            Macdepunit macdepunit = macdepunitService.getmacdepbyenvidanddepid(Long.parseLong(enviromentid), deployunit.getId());
            if (macdepunit != null) {
                String testserver = "";
                String resource = "";
                String ApiUrl = api.getPath();
                if (!ApiUrl.startsWith("/")) {
                    ApiUrl = "/" + ApiUrl;
                }
                if (macdepunit.getVisittype().equals("ip")) {
                    Long MachineID = macdepunit.getMachineid();
                    Machine machine = machineService.getBy("id", MachineID);
                    if (machine == null) {
                        return ResultGenerator.genFailedResult("当前环境中的服务器不存在，请检查是否被删除！");
                    }
                    Enviroment enviroment = enviromentService.getBy("id", Long.parseLong(enviromentid));
                    if (enviroment == null) {
                        return ResultGenerator.genFailedResult("当前用例调试的环境不存在，请检查是否被删除！");
                    }
                    testserver = machine.getIp();

                    if (BaseUrl == null || BaseUrl.isEmpty()) {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + ApiUrl;
                    } else {
                        if (BaseUrl.startsWith("/")) {
                            resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + BaseUrl + ApiUrl;
                        } else {
                            resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + "/" + BaseUrl + ApiUrl;
                        }
                    }
                } else {
                    testserver = macdepunit.getDomain();
                    if (BaseUrl == null || BaseUrl.isEmpty()) {
                        resource = deployunit.getProtocal() + "://" + testserver + ApiUrl;
                    } else {
                        if (BaseUrl.startsWith("/")) {
                            resource = deployunit.getProtocal() + "://" + testserver + BaseUrl + ApiUrl;
                        } else {
                            resource = deployunit.getProtocal() + "://" + testserver + "/" + BaseUrl + ApiUrl;
                        }
                    }
                }

                Condition rdcon = new Condition(Variables.class);
                rdcon.createCriteria().andCondition("projectid = " + projectid);
                List<Variables> variablesList = variablesService.listByCondition(rdcon);
                HashMap<String, String> RadomHashMap = new HashMap<>();
                for (Variables va : variablesList) {
                    RadomHashMap.put(va.getVariablesname(), va.getVariablestype());
                }

                Condition envcon = new Condition(Enviromentvariables.class);
                rdcon.createCriteria().andCondition("projectid = " + projectid);
                List<Enviromentvariables> envvariablesList = enviromentvariablesService.listByCondition(envcon);
                HashMap<String, HashMap<Long, String>> EnvVariablesHashMap = new HashMap<>();
                for (Enviromentvariables va : envvariablesList) {
                    HashMap<Long, String> envidvaluemap = new HashMap<>();
                    if (!EnvVariablesHashMap.containsKey(va.getVariablesname())) {
                        envidvaluemap.put(va.getEnvid(), va.getVariablesvalue());
                        EnvVariablesHashMap.put(va.getVariablesname(), envidvaluemap);
                    } else {
                        EnvVariablesHashMap.get(va.getVariablesname()).put(va.getEnvid(), va.getVariablesvalue());
                    }
                }
                ApicasesController.log.info("。。。。。。。。处理前的resource Url：" + resource);
                //0.环境变量替换
                for (Enviromentvariables envvariables : envvariablesList) {
                    String VariableName = "#" + envvariables.getVariablesname() + "#";
                    if (resource.contains(VariableName)) {
                        if (envvariables.getEnvid().equals(Long.parseLong(enviromentid))) {
                            Object VariableValue = envvariables.getVariablesvalue();
                            resource = resource.replace(VariableName, VariableValue.toString());
                        }
                    }
                }
                //url中的变量替换
                //1.随机变量替换
                for (Variables variables : variablesList) {
                    String VariableName = "[" + variables.getVariablesname() + "]";
                    if (resource.contains(VariableName)) {
                        Object VariableValue = GetRadomValue(variables.getVariablesname());
                        resource = resource.replace(VariableName, VariableValue.toString());
                    }
                }
                //2.接口变量替换
                for (String Interfacevariables : ParamsValuesMap.keySet()) {
                    String UseInterfacevariables = "<" + Interfacevariables + ">";
                    if (resource.contains(UseInterfacevariables)) {
                        String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                        int index = VariableValue.indexOf(",");
                        resource = resource.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                    }
                }
                //3.数据库变量替换
                for (String DBvariables : DBParamsValuesMap.keySet()) {
                    String UseDBvariables = "<<" + DBvariables + ">>";
                    if (resource.contains(UseDBvariables)) {
                        String VariableValue = DBParamsValuesMap.get(DBvariables);
                        int index = VariableValue.indexOf(",");
                        resource = resource.replace(UseDBvariables, VariableValue.substring(index + 1));
                    }
                }
                //4.全局变量替换
                Condition gvcon = new Condition(Globalvariables.class);
                gvcon.createCriteria().andCondition("projectid = " + projectid);
                List<Globalvariables> globalvariablesList = globalvariablesService.listByCondition(gvcon);

                HashMap<String, String> GlobalVariablesHashMap = new HashMap<>();
                for (Globalvariables va : globalvariablesList) {
                    GlobalVariablesHashMap.put(va.getKeyname(), va.getKeyvalue());
                }
                for (Globalvariables variables : globalvariablesList) {
                    String VariableName = "$" + variables.getKeyname() + "$";
                    if (resource.contains(VariableName)) {
                        Object VariableValue = variables.getKeyvalue();
                        resource = resource.replace(VariableName, VariableValue.toString());
                    }
                }

                //5.脚本变量替换
                for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                    String UseScriptvariables = "{" + Scriptvariables + "}";
                    if (resource.contains(UseScriptvariables)) {
                        String VariableValue = ScriptParamsValuesMap.get(UseScriptvariables);
                        int index = VariableValue.indexOf(",");
                        resource = resource.replace(UseScriptvariables, VariableValue.substring(index + 1));
                    }
                }
                ApicasesController.log.info("。。。。。。。。处理后的resource Url：" + resource);

                List<ApiCasedata> HeaderApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Header");
                List<ApiCasedata> ParamsApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Params");
                List<ApiCasedata> BodyApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Body");

                //获取全局Header
                Condition con = new Condition(GlobalheaderParams.class);
                con.createCriteria().andCondition("globalheaderid = " + globalheaderid);
                List<GlobalheaderParams> globalheaderParamsList = globalheaderParamsService.listByCondition(con);
                String requestcontenttype = api.getRequestcontenttype();

                //Header用例值
                HttpHeader header = new HttpHeader();
                try {
                    header = GetHttpHeader(globalheaderParamsList, HeaderApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
                } catch (Exception exception) {
                    //return ResultGenerator.genFailedResult(exception.getMessage());
                }
                header = AddHeaderByRequestContentType(header, requestcontenttype);
                //参数用例值
                HttpParamers paramers = new HttpParamers();
                try {
                    paramers = GetHttpParamers(ParamsApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
                } catch (Exception exception) {
                    //return ResultGenerator.genFailedResult(exception.getMessage());
                }
                //Body用例值
                String PostData = "";
                HttpParamers Bodyparamers = new HttpParamers();
                if (requestcontenttype.equalsIgnoreCase("Form表单")) {
                    try {
                        Bodyparamers = GetHttpParamers(BodyApiCasedataList, ParamsValuesMap, ScriptParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
                    } catch (Exception exception) {
                        return ResultGenerator.genFailedResult(exception.getMessage());
                    }
                    if (Bodyparamers.getParams().size() > 0) {
                        PostData = Bodyparamers.getQueryString();
                    }
                } else {
                    for (ApiCasedata Paramdata : BodyApiCasedataList) {
                        //Body文本内容变量替换
                        PostData = Paramdata.getApiparamvalue();
                        //1.替换随机变量
                        for (Variables variables : variablesList) {
                            String VariableName = "[" + variables.getVariablesname() + "]";
                            if (PostData.contains(VariableName)) {
                                Object VariableValue = GetRadomValue(variables.getVariablesname());
                                PostData = PostData.replace(VariableName, VariableValue.toString());
                            }
                        }
                        //2.替换接口变量
                        for (String Interfacevariables : ParamsValuesMap.keySet()) {
                            String UseInterfacevariables = "<" + Interfacevariables + ">";
                            if (PostData.contains(UseInterfacevariables)) {
                                String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                                int index = VariableValue.indexOf(",");
                                PostData = PostData.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                            }
                        }
                        //3.替换数据库变量
                        for (String DBvariables : DBParamsValuesMap.keySet()) {
                            String UseDBvariables = "<<" + DBvariables + ">>";
                            if (PostData.contains(UseDBvariables)) {
                                String VariableValue = DBParamsValuesMap.get(DBvariables);
                                int index = VariableValue.indexOf(",");
                                PostData = PostData.replace(UseDBvariables, VariableValue.substring(index + 1));
                            }
                        }
                        //4.替换全局变量
                        for (Globalvariables variables : globalvariablesList) {
                            String VariableName = "$" + variables.getKeyname() + "$";
                            if (PostData.contains(VariableName)) {
                                Object VariableValue = GlobalVariablesHashMap.get(variables.getKeyname());
                                PostData = PostData.replace(VariableName, VariableValue.toString());
                            }
                        }
                        //5.替换脚本变量
                        for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                            String UseScriptvariables = "{" + Scriptvariables + "}";
                            if (PostData.contains(UseScriptvariables)) {
                                String VariableValue = ScriptParamsValuesMap.get(Scriptvariables);
                                int index = VariableValue.indexOf(",");
                                PostData = PostData.replace(UseScriptvariables, VariableValue.substring(index + 1));
                            }
                        }
                        //6.替换环境变量
                        for (Enviromentvariables variables : envvariablesList) {
                            String VariableName = "#" + variables.getVariablesname() + "#";
                            if (PostData.contains(VariableName)) {
                                if (variables.getEnvid().equals(Long.parseLong(enviromentid))) {
                                    Object VariableValue = variables.getVariablesvalue();
                                    PostData = PostData.replace(VariableName, VariableValue.toString());
                                }
                            }
                        }
                    }
                }
                try {
                    long Start = new Date().getTime();
                    TestHttp testHttp = new TestHttp();
                    String VisitType = api.getVisittype();
                    TestResponeData respon = testHttp.doService(Protocal, ApiStyle, resource, header, paramers, PostData, VisitType, requestcontenttype, 2000);
                    long End = new Date().getTime();
                    long CostTime = End - Start;

                    //断言


                    String headervalue = "";
                    for (String Key : header.getParams().keySet()) {
                        headervalue = headervalue + Key + ":" + header.getParams().get(Key).toString() + " ";
                    }
                    String paramervalue = "";
                    for (String Key : paramers.getParams().keySet()) {
                        paramervalue = paramervalue + Key + ":" + paramers.getParams().get(Key).toString() + " ";

                    }
                    scenecasesDebugReport.setCaseid(Caseid);
                    scenecasesDebugReport.setBatchname(batchname);
                    scenecasesDebugReport.setCasename(Casename);
                    scenecasesDebugReport.setCreateTime(new Date());
                    scenecasesDebugReport.setLastmodifyTime(new Date());
                    scenecasesDebugReport.setProjectid(projectid);
                    scenecasesDebugReport.setRespone(respon.getResponeContent());
                    scenecasesDebugReport.setUrl(respon.getRequestUrl());
                    scenecasesDebugReport.setRequestdatas(PostData);
                    scenecasesDebugReport.setRequestmethod(VisitType);
                    scenecasesDebugReport.setAssertvalue("");
                    scenecasesDebugReport.setCreator("");
                    scenecasesDebugReport.setErrorinfo("");
                    scenecasesDebugReport.setRequestheader(headervalue);
                    scenecasesDebugReport.setExpect("");
                    scenecasesDebugReport.setSceneid(sceneid);
                    scenecasesDebugReport.setScenename(scenename);
                    scenecasesDebugReport.setStatus(Status);
                    scenecasesDebugReport.setRuntime(CostTime);
                    scenecasesDebugReport.setTestplanid(new Long(0));

                } catch (Exception exception) {
                    Status = "失败";
                    String ExceptionMess = exception.getMessage();
                    scenecasesDebugReport.setErrorinfo(ExceptionMess);
                } finally {
                    scenecasesDebugReportService.save(scenecasesDebugReport);
                }
            } else {
                return ResultGenerator.genFailedResult("当前环境未配置用例:" + Casename + " 所属的微服务，请先完成环境下的配置！");
            }
        }
        return ResultGenerator.genOkResult();
    }


    /**
     * 运行测试
     */
    @PostMapping("/runscenecasetest")
    public Result runscenecasetest(@RequestBody final Map<String, Object> param) throws Exception {
        String enviromentid = param.get("enviromentid").toString();
        Long sceneid = Long.parseLong(param.get("sceneid").toString());
        String scenename = param.get("scenename").toString();

        Long projectid = Long.parseLong(param.get("projectid").toString());
        Long globalheaderid = Long.parseLong(param.get("globalheaderid").toString());
        Long Caseid = Long.parseLong(param.get("caseid").toString());
        String Casename = param.get("casename").toString();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String batchname = scenename + "-" + Casename + "-" + dateFormat.format(date);

        List<ScenecasesDebugReport> scenecasesDebugReportList = new ArrayList<>();
        ScenecasesDebugReport scenecasesDebugReport = new ScenecasesDebugReport();
        String Status = "运行完成";
        HashMap<String, String> ParamsValuesMap = new HashMap<>();
        HashMap<String, String> DBParamsValuesMap = new HashMap<>();
        HashMap<String, String> ScriptParamsValuesMap = new HashMap<>();

        String Respone = "";
        HashMap<String, Object> debugmap = new HashMap<>();
        debugmap.put("conditionid", Caseid);
        debugmap.put("enviromentid", Long.parseLong(enviromentid));
        debugmap.put("conditiontype", "case");


        String ConditionServerurl = conditionserver + "/testcondition/execdebugcondition";
        HttpHeader header1 = new HttpHeader();
        String debugparams = JSON.toJSONString(debugmap);
        try {
            Respone = getSubConditionRespone(ConditionServerurl, debugparams, header1);
        } catch (Exception ex) {
            return ResultGenerator.genFailedResult("前置条件处理异常：！" + ex.getMessage());
        }
        ParamsValuesMap = GetResponeMap(Respone, ParamsValuesMap, "api");
        DBParamsValuesMap = GetResponeMap(Respone, DBParamsValuesMap, "db");
        ScriptParamsValuesMap = GetResponeMap(Respone, ScriptParamsValuesMap, "script");

        Apicases apicases = apicasesService.getBy("id", Caseid);
        if (apicases == null) {
            return ResultGenerator.genFailedResult("当前用例不存在，请检查是否被删除！");
        }
        Long Apiid = apicases.getApiid();
        Api api = apiService.getBy("id", Apiid);
        if (api == null) {
            return ResultGenerator.genFailedResult("当前用例的API不存在，请检查是否被删除！");
        }
        String Method = api.getVisittype();
        String ApiStyle = api.getApistyle();
        Deployunit deployunit = deployunitService.getBy("id", api.getDeployunitid());
        if (deployunit == null) {
            return ResultGenerator.genFailedResult("当前用例的API所在的微服务不存在，请检查是否被删除！");
        }
        String Protocal = deployunit.getProtocal();
        String BaseUrl = deployunit.getBaseurl();
        Macdepunit macdepunit = macdepunitService.getmacdepbyenvidanddepid(Long.parseLong(enviromentid), deployunit.getId());
        if (macdepunit != null) {
            String testserver = "";
            String resource = "";
            String ApiUrl = api.getPath();
            if (!ApiUrl.startsWith("/")) {
                ApiUrl = "/" + ApiUrl;
            }
            if (macdepunit.getVisittype().equals("ip")) {
                Long MachineID = macdepunit.getMachineid();
                Machine machine = machineService.getBy("id", MachineID);
                if (machine == null) {
                    return ResultGenerator.genFailedResult("当前环境中的服务器不存在，请检查是否被删除！");
                }
                Enviroment enviroment = enviromentService.getBy("id", Long.parseLong(enviromentid));
                if (enviroment == null) {
                    return ResultGenerator.genFailedResult("当前用例调试的环境不存在，请检查是否被删除！");
                }
                testserver = machine.getIp();

                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + BaseUrl + ApiUrl;
                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + "/" + BaseUrl + ApiUrl;
                    }
                }
            } else {
                testserver = macdepunit.getDomain();
                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + BaseUrl + ApiUrl;
                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + "/" + BaseUrl + ApiUrl;
                    }
                }
            }

            Condition rdcon = new Condition(Variables.class);
            rdcon.createCriteria().andCondition("projectid = " + projectid);
            List<Variables> variablesList = variablesService.listByCondition(rdcon);
            HashMap<String, String> RadomHashMap = new HashMap<>();
            for (Variables va : variablesList) {
                RadomHashMap.put(va.getVariablesname(), va.getVariablestype());
            }

            Condition envcon = new Condition(Enviromentvariables.class);
            rdcon.createCriteria().andCondition("projectid = " + projectid);
            List<Enviromentvariables> envvariablesList = enviromentvariablesService.listByCondition(envcon);
            HashMap<String, HashMap<Long, String>> EnvVariablesHashMap = new HashMap<>();
            for (Enviromentvariables va : envvariablesList) {
                HashMap<Long, String> envidvaluemap = new HashMap<>();
                if (!EnvVariablesHashMap.containsKey(va.getVariablesname())) {
                    envidvaluemap.put(va.getEnvid(), va.getVariablesvalue());
                    EnvVariablesHashMap.put(va.getVariablesname(), envidvaluemap);
                } else {
                    EnvVariablesHashMap.get(va.getVariablesname()).put(va.getEnvid(), va.getVariablesvalue());
                }
            }
            ApicasesController.log.info("。。。。。。。。处理前的resource Url：" + resource);
            //0.环境变量替换
            for (Enviromentvariables envvariables : envvariablesList) {
                String VariableName = "#" + envvariables.getVariablesname() + "#";
                if (resource.contains(VariableName)) {
                    if (envvariables.getEnvid().equals(Long.parseLong(enviromentid))) {
                        Object VariableValue = envvariables.getVariablesvalue();
                        resource = resource.replace(VariableName, VariableValue.toString());
                    }
                }
            }
            //url中的变量替换
            //1.随机变量替换
            for (Variables variables : variablesList) {
                String VariableName = "[" + variables.getVariablesname() + "]";
                if (resource.contains(VariableName)) {
                    Object VariableValue = GetRadomValue(variables.getVariablesname());
                    resource = resource.replace(VariableName, VariableValue.toString());
                }
            }
            //2.接口变量替换
            for (String Interfacevariables : ParamsValuesMap.keySet()) {
                String UseInterfacevariables = "<" + Interfacevariables + ">";
                if (resource.contains(UseInterfacevariables)) {
                    String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                }
            }
            //3.数据库变量替换
            for (String DBvariables : DBParamsValuesMap.keySet()) {
                String UseDBvariables = "<<" + DBvariables + ">>";
                if (resource.contains(UseDBvariables)) {
                    String VariableValue = DBParamsValuesMap.get(DBvariables);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseDBvariables, VariableValue.substring(index + 1));
                }
            }
            //4.全局变量替换
            Condition gvcon = new Condition(Globalvariables.class);
            gvcon.createCriteria().andCondition("projectid = " + projectid);
            List<Globalvariables> globalvariablesList = globalvariablesService.listByCondition(gvcon);

            HashMap<String, String> GlobalVariablesHashMap = new HashMap<>();
            for (Globalvariables va : globalvariablesList) {
                GlobalVariablesHashMap.put(va.getKeyname(), va.getKeyvalue());
            }
            for (Globalvariables variables : globalvariablesList) {
                String VariableName = "$" + variables.getKeyname() + "$";
                if (resource.contains(VariableName)) {
                    Object VariableValue = variables.getKeyvalue();
                    resource = resource.replace(VariableName, VariableValue.toString());
                }
            }

            //5.脚本变量替换
            for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                String UseScriptvariables = "{" + Scriptvariables + "}";
                if (resource.contains(UseScriptvariables)) {
                    String VariableValue = ScriptParamsValuesMap.get(UseScriptvariables);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseScriptvariables, VariableValue.substring(index + 1));
                }
            }
            ApicasesController.log.info("。。。。。。。。处理后的resource Url：" + resource);

            List<ApiCasedata> HeaderApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Header");
            List<ApiCasedata> ParamsApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Params");
            List<ApiCasedata> BodyApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Body");

            //获取全局Header
            Condition con = new Condition(GlobalheaderParams.class);
            con.createCriteria().andCondition("globalheaderid = " + globalheaderid);
            List<GlobalheaderParams> globalheaderParamsList = globalheaderParamsService.listByCondition(con);
            String requestcontenttype = api.getRequestcontenttype();

            //Header用例值
            HttpHeader header = new HttpHeader();
            try {
                header = GetHttpHeader(globalheaderParamsList, HeaderApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
            } catch (Exception exception) {
                return ResultGenerator.genFailedResult(exception.getMessage());
            }
            header = AddHeaderByRequestContentType(header, requestcontenttype);

            //参数用例值
            HttpParamers paramers = new HttpParamers();
            try {
                paramers = GetHttpParamers(ParamsApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
            } catch (Exception exception) {
                return ResultGenerator.genFailedResult(exception.getMessage());
            }

            //Body用例值
            String PostData = "";
            HttpParamers Bodyparamers = new HttpParamers();
            if (requestcontenttype.equalsIgnoreCase("Form表单")) {
                try {
                    Bodyparamers = GetHttpParamers(BodyApiCasedataList, ParamsValuesMap, ScriptParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
                } catch (Exception exception) {
                    return ResultGenerator.genFailedResult(exception.getMessage());
                }
                if (Bodyparamers.getParams().size() > 0) {
                    PostData = Bodyparamers.getQueryString();
                }
            } else {
                for (ApiCasedata Paramdata : BodyApiCasedataList) {
                    //Body文本内容变量替换
                    PostData = Paramdata.getApiparamvalue();
                    //1.替换随机变量
                    for (Variables variables : variablesList) {
                        String VariableName = "[" + variables.getVariablesname() + "]";
                        if (PostData.contains(VariableName)) {
                            Object VariableValue = GetRadomValue(variables.getVariablesname());
                            PostData = PostData.replace(VariableName, VariableValue.toString());
                        }
                    }
                    //2.替换接口变量
                    for (String Interfacevariables : ParamsValuesMap.keySet()) {
                        String UseInterfacevariables = "<" + Interfacevariables + ">";
                        if (PostData.contains(UseInterfacevariables)) {
                            String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                        }
                    }
                    //3.替换数据库变量
                    for (String DBvariables : DBParamsValuesMap.keySet()) {
                        String UseDBvariables = "<<" + DBvariables + ">>";
                        if (PostData.contains(UseDBvariables)) {
                            String VariableValue = DBParamsValuesMap.get(DBvariables);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseDBvariables, VariableValue.substring(index + 1));
                        }
                    }
                    //4.替换全局变量
                    for (Globalvariables variables : globalvariablesList) {
                        String VariableName = "$" + variables.getKeyname() + "$";
                        if (PostData.contains(VariableName)) {
                            Object VariableValue = GlobalVariablesHashMap.get(variables.getKeyname());
                            PostData = PostData.replace(VariableName, VariableValue.toString());
                        }
                    }
                    //5.替换脚本变量
                    for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                        String UseScriptvariables = "{" + Scriptvariables + "}";
                        if (PostData.contains(UseScriptvariables)) {
                            String VariableValue = ScriptParamsValuesMap.get(Scriptvariables);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseScriptvariables, VariableValue.substring(index + 1));
                        }
                    }
                    //6.替换环境变量
                    for (Enviromentvariables variables : envvariablesList) {
                        String VariableName = "#" + variables.getVariablesname() + "#";
                        if (PostData.contains(VariableName)) {
                            if (variables.getEnvid().equals(Long.parseLong(enviromentid))) {
                                Object VariableValue = variables.getVariablesvalue();
                                PostData = PostData.replace(VariableName, VariableValue.toString());
                            }
                        }
                    }
                }
            }
            try {
                long Start = new Date().getTime();
                TestHttp testHttp = new TestHttp();
                String VisitType = api.getVisittype();
                TestResponeData respon = testHttp.doService(Protocal, ApiStyle, resource, header, paramers, PostData, VisitType, requestcontenttype, 2000);
                long End = new Date().getTime();
                long CostTime = End - Start;

                //断言

                String headervalue = "";
                for (String Key : header.getParams().keySet()) {
                    headervalue = headervalue + Key + ":" + header.getParams().get(Key).toString() + " ";
                }
                String paramervalue = "";
                for (String Key : paramers.getParams().keySet()) {
                    paramervalue = paramervalue + Key + ":" + paramers.getParams().get(Key).toString() + " ";

                }
                scenecasesDebugReport.setCaseid(Caseid);
                scenecasesDebugReport.setCasename(Casename);
                scenecasesDebugReport.setBatchname(batchname);
                scenecasesDebugReport.setCreateTime(new Date());
                scenecasesDebugReport.setLastmodifyTime(new Date());
                scenecasesDebugReport.setProjectid(projectid);
                scenecasesDebugReport.setRespone(respon.getResponeContent());
                scenecasesDebugReport.setUrl(respon.getRequestUrl());
                scenecasesDebugReport.setRequestdatas(PostData);
                scenecasesDebugReport.setRequestmethod(VisitType);
                scenecasesDebugReport.setAssertvalue("");
                scenecasesDebugReport.setCreator("");
                scenecasesDebugReport.setErrorinfo("");
                scenecasesDebugReport.setRequestheader(headervalue);
                scenecasesDebugReport.setExpect("");
                scenecasesDebugReport.setSceneid(sceneid);
                scenecasesDebugReport.setScenename(scenename);
                scenecasesDebugReport.setStatus(Status);
                scenecasesDebugReport.setRuntime(CostTime);
                scenecasesDebugReport.setTestplanid(new Long(0));

            } catch (Exception exception) {
                Status = "失败";
                String ExceptionMess = exception.getMessage();
                scenecasesDebugReport.setErrorinfo(ExceptionMess);
            } finally {
                scenecasesDebugReportList.add(scenecasesDebugReport);
                scenecasesDebugReportService.save(scenecasesDebugReport);
                return ResultGenerator.genOkResult(scenecasesDebugReportList);
            }
        } else {
            return ResultGenerator.genFailedResult("当前环境未配置用例:" + Casename + " 所属的微服务，请先完成环境下的配置！");
        }
    }

    /**
     * 运行测试
     */
    @PostMapping("/runtest")
    public Result runtest(@RequestBody final Map<String, Object> param) throws Exception {
        String enviromentid = param.get("enviromentid").toString();
        Long Caseid = Long.parseLong(param.get("caseid").toString());
        Long globalheaderid = Long.parseLong(param.get("globalheaderid").toString());
        Long projectid = Long.parseLong(param.get("projectid").toString());
        HashMap<String, String> ParamsValuesMap = new HashMap<>();
        HashMap<String, String> DBParamsValuesMap = new HashMap<>();
        HashMap<String, String> ScriptParamsValuesMap = new HashMap<>();

        String Respone = "";
        HashMap<String, Object> debugmap = new HashMap<>();
        debugmap.put("conditionid", Caseid);
        debugmap.put("enviromentid", Long.parseLong(enviromentid));
        debugmap.put("conditiontype", "case");


        String ConditionServerurl = conditionserver + "/testcondition/execdebugcondition";
        HttpHeader header1 = new HttpHeader();
        String debugparams = JSON.toJSONString(debugmap);
        try {
            Respone = getSubConditionRespone(ConditionServerurl, debugparams, header1);
        } catch (Exception ex) {
            return ResultGenerator.genFailedResult("前置条件处理异常：！" + ex.getMessage());
        }
        ParamsValuesMap = GetResponeMap(Respone, ParamsValuesMap, "api");
        DBParamsValuesMap = GetResponeMap(Respone, DBParamsValuesMap, "db");
        ScriptParamsValuesMap = GetResponeMap(Respone, ScriptParamsValuesMap, "script");

        Apicases apicases = apicasesService.getBy("id", Caseid);
        if (apicases == null) {
            return ResultGenerator.genFailedResult("当前用例不存在，请检查是否被删除！");
        }
        Long Apiid = apicases.getApiid();
        Api api = apiService.getBy("id", Apiid);
        if (api == null) {
            return ResultGenerator.genFailedResult("当前用例的API不存在，请检查是否被删除！");
        }
        String Method = api.getVisittype();
        String ApiStyle = api.getApistyle();
        Deployunit deployunit = deployunitService.getBy("id", api.getDeployunitid());
        if (deployunit == null) {
            return ResultGenerator.genFailedResult("当前用例的API所在的微服务不存在，请检查是否被删除！");
        }
        String Protocal = deployunit.getProtocal();
        String BaseUrl = deployunit.getBaseurl();
        Macdepunit macdepunit = macdepunitService.getmacdepbyenvidanddepid(Long.parseLong(enviromentid), deployunit.getId());
        if (macdepunit != null) {
            String testserver = "";
            String resource = "";
            String ApiUrl = api.getPath();
            if (!ApiUrl.startsWith("/")) {
                ApiUrl = "/" + ApiUrl;
            }
            if (macdepunit.getVisittype().equals("ip")) {
                Long MachineID = macdepunit.getMachineid();
                Machine machine = machineService.getBy("id", MachineID);
                if (machine == null) {
                    return ResultGenerator.genFailedResult("当前环境中的服务器不存在，请检查是否被删除！");
                }
                Enviroment enviroment = enviromentService.getBy("id", Long.parseLong(enviromentid));
                if (enviroment == null) {
                    return ResultGenerator.genFailedResult("当前用例调试的环境不存在，请检查是否被删除！");
                }
                testserver = machine.getIp();

                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + BaseUrl + ApiUrl;
                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + "/" + BaseUrl + ApiUrl;
                    }
                }
            } else {
                testserver = macdepunit.getDomain();
                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + BaseUrl + ApiUrl;
                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + "/" + BaseUrl + ApiUrl;
                    }
                }
            }

            Condition rdcon = new Condition(Variables.class);
            rdcon.createCriteria().andCondition("projectid = " + projectid);
            List<Variables> variablesList = variablesService.listByCondition(rdcon);
            HashMap<String, String> RadomHashMap = new HashMap<>();
            for (Variables va : variablesList) {
                RadomHashMap.put(va.getVariablesname(), va.getVariablestype());
            }

            Condition envcon = new Condition(Enviromentvariables.class);
            rdcon.createCriteria().andCondition("projectid = " + projectid);
            List<Enviromentvariables> envvariablesList = enviromentvariablesService.listByCondition(envcon);
            HashMap<String, HashMap<Long, String>> EnvVariablesHashMap = new HashMap<>();
            for (Enviromentvariables va : envvariablesList) {
                HashMap<Long, String> envidvaluemap = new HashMap<>();
                if (!EnvVariablesHashMap.containsKey(va.getVariablesname())) {
                    envidvaluemap.put(va.getEnvid(), va.getVariablesvalue());
                    EnvVariablesHashMap.put(va.getVariablesname(), envidvaluemap);
                } else {
                    EnvVariablesHashMap.get(va.getVariablesname()).put(va.getEnvid(), va.getVariablesvalue());
                }
            }

            ApicasesController.log.info("。。。。。。。。处理前的resource Url：" + resource);
            //0.环境变量替换
            for (Enviromentvariables envvariables : envvariablesList) {
                String VariableName = "#" + envvariables.getVariablesname() + "#";
                if (resource.contains(VariableName)) {
                    if (envvariables.getEnvid().equals(Long.parseLong(enviromentid))) {
                        Object VariableValue = envvariables.getVariablesvalue();
                        resource = resource.replace(VariableName, VariableValue.toString());
                    }
                }
            }
            //url中的变量替换
            //1.随机变量替换
            for (Variables variables : variablesList) {
                String VariableName = "[" + variables.getVariablesname() + "]";
                if (resource.contains(VariableName)) {
                    Object VariableValue = GetRadomValue(variables.getVariablesname());
                    resource = resource.replace(VariableName, VariableValue.toString());
                }
            }
            //2.接口变量替换
            for (String Interfacevariables : ParamsValuesMap.keySet()) {
                String UseInterfacevariables = "<" + Interfacevariables + ">";
                if (resource.contains(UseInterfacevariables)) {
                    String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                }
            }
            //3.数据库变量替换
            for (String DBvariables : DBParamsValuesMap.keySet()) {
                String UseDBvariables = "<<" + DBvariables + ">>";
                if (resource.contains(UseDBvariables)) {
                    String VariableValue = DBParamsValuesMap.get(DBvariables);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseDBvariables, VariableValue.substring(index + 1));
                }
            }
            //4.全局变量替换
            Condition gvcon = new Condition(Globalvariables.class);
            gvcon.createCriteria().andCondition("projectid = " + projectid);
            List<Globalvariables> globalvariablesList = globalvariablesService.listByCondition(gvcon);

            HashMap<String, String> GlobalVariablesHashMap = new HashMap<>();
            for (Globalvariables va : globalvariablesList) {
                GlobalVariablesHashMap.put(va.getKeyname(), va.getKeyvalue());
            }
            for (Globalvariables variables : globalvariablesList) {
                String VariableName = "$" + variables.getKeyname() + "$";
                if (resource.contains(VariableName)) {
                    Object VariableValue = variables.getKeyvalue();
                    resource = resource.replace(VariableName, VariableValue.toString());
                }
            }

            //5.脚本变量替换
            for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                String UseScriptvariables = "{" + Scriptvariables + "}";
                if (resource.contains(UseScriptvariables)) {
                    String VariableValue = ScriptParamsValuesMap.get(UseScriptvariables);
                    int index = VariableValue.indexOf(",");
                    resource = resource.replace(UseScriptvariables, VariableValue.substring(index + 1));
                }
            }
            ApicasesController.log.info("。。。。。。。。处理后的resource Url：" + resource);

            List<ApiCasedata> HeaderApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Header");
            List<ApiCasedata> ParamsApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Params");
            List<ApiCasedata> BodyApiCasedataList = apiCasedataService.getparamvaluebycaseidandtype(Caseid, "Body");

            //获取全局Header
            Condition con = new Condition(GlobalheaderParams.class);
            con.createCriteria().andCondition("globalheaderid = " + globalheaderid);
            List<GlobalheaderParams> globalheaderParamsList = globalheaderParamsService.listByCondition(con);
            String requestcontenttype = api.getRequestcontenttype();

            //Header用例值
            HttpHeader header = new HttpHeader();
            try {
                header = GetHttpHeader(globalheaderParamsList, HeaderApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
            } catch (Exception exception) {
                return ResultGenerator.genFailedResult(exception.getMessage());
            }
            header = AddHeaderByRequestContentType(header, requestcontenttype);

            //参数用例值
            HttpParamers paramers = new HttpParamers();
            try {
                paramers = GetHttpParamers(ParamsApiCasedataList, ScriptParamsValuesMap, ParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
            } catch (Exception exception) {
                return ResultGenerator.genFailedResult(exception.getMessage());
            }

            //Body用例值
            String PostData = "";
            HttpParamers Bodyparamers = new HttpParamers();
            if (requestcontenttype.equalsIgnoreCase("Form表单")) {
                try {
                    Bodyparamers = GetHttpParamers(BodyApiCasedataList, ParamsValuesMap, ScriptParamsValuesMap, RadomHashMap, DBParamsValuesMap, GlobalVariablesHashMap, EnvVariablesHashMap, Long.parseLong(enviromentid), projectid);
                } catch (Exception exception) {
                    return ResultGenerator.genFailedResult(exception.getMessage());
                }
                if (Bodyparamers.getParams().size() > 0) {
                    PostData = Bodyparamers.getQueryString();
                }
            } else {
                for (ApiCasedata Paramdata : BodyApiCasedataList) {
                    //Body文本内容变量替换
                    PostData = Paramdata.getApiparamvalue();
                    //1.替换随机变量
                    for (Variables variables : variablesList) {
                        String VariableName = "[" + variables.getVariablesname() + "]";
                        if (PostData.contains(VariableName)) {
                            Object VariableValue = GetRadomValue(variables.getVariablesname());
                            PostData = PostData.replace(VariableName, VariableValue.toString());
                        }
                    }
                    //2.替换接口变量
                    for (String Interfacevariables : ParamsValuesMap.keySet()) {
                        String UseInterfacevariables = "<" + Interfacevariables + ">";
                        if (PostData.contains(UseInterfacevariables)) {
                            String VariableValue = ParamsValuesMap.get(Interfacevariables);// GetVariablesObjectValues("$" +Interfacevariables, ParamsValuesMap);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseInterfacevariables, VariableValue.substring(index + 1));
                        }
                    }
                    //3.替换数据库变量
                    for (String DBvariables : DBParamsValuesMap.keySet()) {
                        String UseDBvariables = "<<" + DBvariables + ">>";
                        if (PostData.contains(UseDBvariables)) {
                            String VariableValue = DBParamsValuesMap.get(DBvariables);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseDBvariables, VariableValue.substring(index + 1));
                        }
                    }
                    //4.替换全局变量
                    for (Globalvariables variables : globalvariablesList) {
                        String VariableName = "$" + variables.getKeyname() + "$";
                        if (PostData.contains(VariableName)) {
                            Object VariableValue = GlobalVariablesHashMap.get(variables.getKeyname());
                            PostData = PostData.replace(VariableName, VariableValue.toString());
                        }
                    }
                    //5.替换脚本变量
                    for (String Scriptvariables : ScriptParamsValuesMap.keySet()) {
                        String UseScriptvariables = "{" + Scriptvariables + "}";
                        if (PostData.contains(UseScriptvariables)) {
                            String VariableValue = ScriptParamsValuesMap.get(Scriptvariables);
                            int index = VariableValue.indexOf(",");
                            PostData = PostData.replace(UseScriptvariables, VariableValue.substring(index + 1));
                        }
                    }
                    //6.替换环境变量
                    for (Enviromentvariables variables : envvariablesList) {
                        String VariableName = "#" + variables.getVariablesname() + "#";
                        if (PostData.contains(VariableName)) {
                            if (variables.getEnvid().equals(Long.parseLong(enviromentid))) {
                                Object VariableValue = variables.getVariablesvalue();
                                PostData = PostData.replace(VariableName, VariableValue.toString());
                            }
                        }
                    }
                }
            }
            try {
                long Start = new Date().getTime();
                TestHttp testHttp = new TestHttp();
                String VisitType = api.getVisittype();
                TestResponeData respon = testHttp.doService(Protocal, ApiStyle, resource, header, paramers, PostData, VisitType, requestcontenttype, 2000);
                long End = new Date().getTime();
                long CostTime = End - Start;
                respon.setResponeTime(CostTime);
                ResponeGeneral responeGeneral = new ResponeGeneral();
                responeGeneral.setApistyle(ApiStyle);
                responeGeneral.setPostData(PostData);
                responeGeneral.setMethod(Method);
                responeGeneral.setProtocal(Protocal);
                responeGeneral.setUrl(respon.getRequestUrl());
                List<RequestHead> requestHeadList = new ArrayList<>();
                for (String Key : header.getParams().keySet()) {
                    RequestHead requestHead = new RequestHead();
                    requestHead.setKeyName(Key);
                    requestHead.setKeyValue(header.getParams().get(Key).toString());
                    requestHeadList.add(requestHead);
                }
                List<RequestParams> requestParamsList = new ArrayList<>();
                for (String Key : paramers.getParams().keySet()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.setKeyName(Key);
                    requestParams.setKeyValue(paramers.getParams().get(Key).toString());
                    requestParamsList.add(requestParams);
                }
                respon.setRequestHeadList(requestHeadList);
                respon.setRequestParamsList(requestParamsList);
                respon.setResponeGeneral(responeGeneral);
                return ResultGenerator.genOkResult(respon);

            } catch (Exception exception) {
                String ExceptionMess = exception.getMessage();
                if (ExceptionMess.contains("Illegal character in path at")) {
                    ExceptionMess = "Url不合法，请检查是否有无法替换的变量，或者有相关非法字符：" + exception.getMessage();
                }
                return ResultGenerator.genFailedResult(ExceptionMess);
            }
        } else {
            return ResultGenerator.genFailedResult("当前环境未部署此用例API所在的微服务，请先完成环境下的部署！");
        }
    }


    //获取HttpHeader
    private HttpHeader GetHttpHeader(List<GlobalheaderParams> globalheaderParamsList, List<ApiCasedata> HeaderApiCasedataList, HashMap<String, String> ScriptParamsValuesMap, HashMap<String, String> ParamsValuesMap, HashMap<String, String> RadomMap, HashMap<String, String> DBMap, HashMap<String, String> GlobalVariablesHashMap, HashMap<String, HashMap<Long, String>> EnvVariablesHashMap, Long envid, long projectid) throws Exception {
        HashMap<String, String> globalheaderParamsHashMap = new HashMap<>();
        for (ApiCasedata Headdata : HeaderApiCasedataList) {
            if (!globalheaderParamsHashMap.containsKey(Headdata.getApiparam())) {
                globalheaderParamsHashMap.put(Headdata.getApiparam(), Headdata.getApiparamvalue());
            }
        }
        //全局Header，有相同则覆盖，无相同则增加
        for (GlobalheaderParams ghp : globalheaderParamsList) {
            globalheaderParamsHashMap.put(ghp.getKeyname(), ghp.getKeyvalue());
        }
        HttpHeader header = new HttpHeader();
        for (String HeaderName : globalheaderParamsHashMap.keySet()) {
            String HeaderValue = globalheaderParamsHashMap.get(HeaderName);
            Object Result = HeaderValue;
            if ((HeaderValue.contains("#") && HeaderValue.contains("#")) || (HeaderValue.contains("{") && HeaderValue.contains("}")) || (HeaderValue.contains("<") && HeaderValue.contains(">")) || (HeaderValue.contains("<<") && HeaderValue.contains(">>")) || (HeaderValue.contains("[") && HeaderValue.contains("]")) || (HeaderValue.contains("$") && HeaderValue.contains("$"))) {
                try {
                    Result = GetVaraibaleValue(HeaderValue, RadomMap, ScriptParamsValuesMap, ParamsValuesMap, DBMap, GlobalVariablesHashMap, EnvVariablesHashMap, envid, projectid);
                } catch (Exception ex) {
                    Result = ex.getMessage();
                    //throw new Exception("当前用例的Header中参数名：" + HeaderName + "-对应的参数值：" + ex.getMessage());
                }
            }
            header.addParam(HeaderName, Result);
        }
        return header;
    }

    //获取HttpParams
    private HttpParamers GetHttpParamers(List<ApiCasedata> ParamsApiCasedataList, HashMap<String, String> ScriptParamsValuesMap, HashMap<String, String> ParamsValuesMap, HashMap<String, String> RadomMap, HashMap<String, String> DBMap, HashMap<String, String> GlobalVariablesHashMap, HashMap<String, HashMap<Long, String>> EnvVariablesHashMap, Long envid, long projectid) throws Exception {
        HttpParamers paramers = new HttpParamers();
        for (ApiCasedata Paramdata : ParamsApiCasedataList) {
            String ParamName = Paramdata.getApiparam();
            String ParamValue = Paramdata.getApiparamvalue();
            String DataType = Paramdata.getParamstype();
            Object ObjectResult = ParamValue;
            if ((ParamValue.contains("#") && ParamValue.contains("#")) || (ParamValue.contains("{") && ParamValue.contains("}")) || (ParamValue.contains("<") && ParamValue.contains(">")) || (ParamValue.contains("<<") && ParamValue.contains(">>")) || (ParamValue.contains("[") && ParamValue.contains("]")) || (ParamValue.contains("$") && ParamValue.contains("$"))) {
                try {
                    ObjectResult = GetVaraibaleValue(ParamValue, RadomMap, ScriptParamsValuesMap, ParamsValuesMap, DBMap, GlobalVariablesHashMap, EnvVariablesHashMap, envid, projectid);
                } catch (Exception ex) {
                    ObjectResult = ex.getMessage();
                    //throw new Exception("当前用例的Params或者Body中参数名：" + ParamName + "-对应的参数值：" + ex.getMessage());
                }
            }
            Object Result = GetDataByType(ObjectResult.toString(), DataType);
            paramers.addParam(ParamName, Result);
        }
        return paramers;
    }

    private Object GetVaraibaleValue(String Value, HashMap<String, String> RadomMap, HashMap<String, String> ScriptMap, HashMap<String, String> InterfaceMap, HashMap<String, String> DBMap, HashMap<String, String> globalvariablesMap, HashMap<String, HashMap<Long, String>> envvariablesMap, long envid, long projectid) throws Exception {
        Object ObjectValue = Value;
        boolean exist = false; //标记是否Value有变量处理，false表示没有对应的子条件处理过
        //参数值替换脚本变量
        for (String scriptvariablesName : ScriptMap.keySet()) {
            boolean flag = GetSubOrNot(ScriptMap, Value, "{", "}");
            if (Value.contains("{" + scriptvariablesName + "}")) {
                exist = true;
                String ActualValueCom = ScriptMap.get(scriptvariablesName);
                int index = ActualValueCom.indexOf(",");
                long conditionid = Long.parseLong(ActualValueCom.substring(0, index));
                String ActualValue = ActualValueCom.substring(index + 1);
                if (flag) {
                    //有拼接认为是字符串
                    Value = Value.replace("{" + scriptvariablesName + "}", ActualValue);
                    ObjectValue = Value;
                } else {
                    //无拼接则转换成具体类型,根据变量名获取变量类型
                    Condition tvcon = new Condition(Testvariables.class);
                    tvcon.createCriteria().andCondition("projectid = " + projectid).andCondition("conditionid= " + conditionid)
                            .andCondition("scriptvariablesname= '" + scriptvariablesName + "'");
                    List<Scriptvariables> variablesList = scriptvariablesService.listByCondition(tvcon);
                    Scriptvariables testvariables = variablesList.get(0);// testvariablesService.getBy("testvariablesname", interfacevariablesName);//  testMysqlHelp.GetVariablesDataType(interfacevariablesName);
                    if (testvariables == null) {
                        ObjectValue = "未找到变量：" + Value + "请检查脚本前置条件是否有配置该提取变量";
                    } else {
                        ObjectValue = GetDataByType(ActualValue, testvariables.getValuetype());
                    }
                }
            }
        }
        //参数值替换接口变量
        for (String interfacevariablesName : InterfaceMap.keySet()) {
            boolean flag = GetSubOrNot(InterfaceMap, Value, "<", ">");
            if (Value.contains("<" + interfacevariablesName + ">")) {
                exist = true;
                String ActualValueCom = InterfaceMap.get(interfacevariablesName);
                int index = ActualValueCom.indexOf(",");
                long conditionid = Long.parseLong(ActualValueCom.substring(0, index));
                String ActualValue = ActualValueCom.substring(index + 1);

                if (flag) {
                    //有拼接认为是字符串
                    Value = Value.replace("<" + interfacevariablesName + ">", ActualValue);
                    ObjectValue = Value;
                } else {
                    //无拼接则转换成具体类型,根据变量名获取变量类型
                    Condition tvcon = new Condition(Testvariables.class);
                    tvcon.createCriteria().andCondition("projectid = " + projectid).andCondition("caseid= " + conditionid).andCondition("testvariablesname= '" + interfacevariablesName + "'");
                    List<Testvariables> variablesList = testvariablesService.listByCondition(tvcon);
                    Testvariables testvariables = variablesList.get(0);// testvariablesService.getBy("testvariablesname", interfacevariablesName);//  testMysqlHelp.GetVariablesDataType(interfacevariablesName);
                    if (testvariables == null) {
                        ObjectValue = "未找到变量：" + Value + "请检查前置接口用例是否有配置提取变量";
                    } else {
                        ObjectValue = GetDataByType(ActualValue, testvariables.getValuetype());
                    }
                }
            }
        }
        //参数值替换数据库变量
        for (String DBvariablesName : DBMap.keySet()) {
            boolean flag = GetSubOrNot(DBMap, Value, "<<", ">>");
            if (Value.contains("<<" + DBvariablesName + ">>")) {
                exist = true;
                String ActualValueCom = DBMap.get(DBvariablesName);
                int index = ActualValueCom.indexOf(",");
                long conditionid = Long.parseLong(ActualValueCom.substring(0, index));
                String ActualValue = ActualValueCom.substring(index + 1);
                if (flag) {
                    //有拼接认为是字符串
                    Value = Value.replace("<<" + DBvariablesName + ">>", ActualValue);
                    ObjectValue = Value;
                } else {
                    //无拼接则转换成具体类型,根据变量名获取变量类型
                    Condition dbcon = new Condition(Dbvariables.class);
                    dbcon.createCriteria().andCondition("projectid = " + projectid).andCondition("dbvariablesname= '" + DBvariablesName + "'").andCondition("conditionid= " + conditionid);
                    List<Dbvariables> variablesList = dbvariablesService.listByCondition(dbcon);
                    Dbvariables dbvariables = variablesList.get(0);// dbvariablesService.getBy("dbvariablesname", DBvariablesName);//  testMysqlHelp.GetVariablesDataType(interfacevariablesName);
                    if (dbvariables == null) {
                        ObjectValue = "未找到变量：" + Value + "请检查数据库前置操作是否有配置提取数据库变量";
                    } else {
                        ObjectValue = GetDataByType(ActualValue, dbvariables.getValuetype());
                    }
                }
            }
        }
        //参数值替换随机变量
        for (String variables : RadomMap.keySet()) {
            boolean flag = GetSubOrNot(RadomMap, Value, "[", "]");
            if (Value.contains("[" + variables + "]")) {
                exist = true;
                if (flag) {
                    Object RadomValue = GetRadomValue(variables);
                    Value = Value.replace("[" + variables + "]", RadomValue.toString());
                    ObjectValue = Value;
                } else {
                    ObjectValue = GetRadomValue(variables);
                }
            }
        }
        //参数值替换全局变量
        for (String variables : globalvariablesMap.keySet()) {
            boolean flag = GetSubOrNot(globalvariablesMap, Value, "$", "$");
            if (Value.contains("$" + variables + "$")) {
                exist = true;
                if (flag) {
                    Object GlobalVariableValue = globalvariablesMap.get(variables);
                    Value = Value.replace("$" + variables + "$", GlobalVariableValue.toString());
                    ObjectValue = Value;
                } else {
                    ObjectValue = globalvariablesMap.get(variables);
                }
            }
        }
        //参数值替换环境变量
        HashMap<String, String> Last = new HashMap<>();
        for (String variables : envvariablesMap.keySet()) {
            if (Value.contains("#" + variables + "#")) {
                for (Long envromentid : envvariablesMap.get(variables).keySet()) {
                    if (envromentid.equals(envid)) {
                        Last.put(variables, envvariablesMap.get(variables).get(envromentid));
                    }
                }
            }
        }
        for (String variables : Last.keySet()) {
            boolean flag = GetSubOrNot(Last, Value, "#", "#");
            if (Value.contains("#" + variables + "#")) {
                exist = true;
                if (flag) {
                    Object GlobalVariableValue = Last.get(variables);
                    Value = Value.replace("#" + variables + "#", GlobalVariableValue.toString());
                    ObjectValue = Value;
                } else {
                    ObjectValue = Last.get(variables);
                }
            }
        }
        if (!exist) {
            throw new Exception(Value + " 未能成功获取该变量实际值，请检查是否配置对应的前置条件来提取此变量值");
        }
        return ObjectValue;
    }

    //判断是否有拼接
    private boolean GetSubOrNot(HashMap<String, String> VariablesMap, String Value, String prefix, String profix) {
        boolean flag = false;
        for (String Key : VariablesMap.keySet()) {
            String ActualValue = prefix + Key + profix;
            if (Value.contains(ActualValue)) {
                String LeftValue = Value.replace(ActualValue, "");
                if (LeftValue.length() > 0) {
                    //表示有拼接
                    return true;
                } else {
                    return false;
                }
            }
        }
        return flag;
    }

    private Object GetEnvValue(String EnvVariables, long Envid, List<Enviromentvariables> enviromentvariablesList) {
        Object Result = null;
        for (Enviromentvariables envva : enviromentvariablesList) {
            if (envva.equals(EnvVariables) && envva.getEnvid().equals(Envid)) {
                Result = envva.getVariablesvalue();
            }
        }
        return Result;
    }


    //获取随机变量值
    private Object GetRadomValue(String Value) {
        Object Result = Value;
        String FunctionName = Value;
        List<Variables> variablesList = variablesService.listAll();
        for (Variables variables : variablesList) {
            if (variables.getVariablesname().equalsIgnoreCase(FunctionName)) {
                String Params = variables.getVariablecondition();
                String Variablestype = variables.getVariablestype();
                RadomVariables radomVariables = new RadomVariables();
                if (Variablestype.equalsIgnoreCase("随机字符串")) {
                    try {
                        Integer length = Integer.parseInt(Params);
                        Result = radomVariables.GetRadmomStr(length);
                    } catch (Exception ex) {
                        Result = "随机变量GetRadmomStr输入参数不合法，请填写参数为数字类型表示字符串长度";
                    }
                }
                if (Variablestype.equalsIgnoreCase("随机数组值")) {
                    try {
                        String[] array = Params.split(",");
                        long length = array.length;
                        Long ResultIndex = radomVariables.GetRadmomNum(new Long(0), length - 1);
                        Result = array[ResultIndex.intValue()];
                    } catch (Exception ex) {
                        Result = "随机数组输入参数不合法，请填写使用英文逗号分隔的内容";
                    }
                }
                if (Variablestype.equalsIgnoreCase("随机整数")) {
                    String ParamsArray[] = Params.split(",");
                    if (ParamsArray.length < 2) {
                        Result = "随机变量GetRadmomStr输入参数不合法，请填写需要的字符串长度";
                    } else {
                        try {
                            Long Start = Long.parseLong(ParamsArray[0]);
                            Long End = Long.parseLong(ParamsArray[1]);
                            Result = radomVariables.GetRadmomNum(Start, End);
                        } catch (Exception exception) {
                            Result = "随机变量GetRadmomNum输入参数不合法，请填写最小和最大值数字范围";
                        }
                    }
                }
                if (Variablestype.equalsIgnoreCase("随机小数")) {
                    String ParamsArray[] = Params.split(",");
                    if (ParamsArray.length < 2) {
                        Result = "随机变量GetRadmomStr输入参数不合法，请填写需要的字符串长度";
                    } else {
                        try {
                            Long Start = Long.parseLong(ParamsArray[0]);
                            Long End = Long.parseLong(ParamsArray[1]);
                            Result = radomVariables.GetRadmomDouble(Start, End);
                        } catch (Exception exception) {
                            Result = "随机变量GetRadmomNum输入参数不合法，请填写最小和最大值数字范围";
                        }
                    }
                }
                if (Variablestype.equalsIgnoreCase("Guid")) {
                    Result = radomVariables.GetGuid();
                }
                if (Variablestype.equalsIgnoreCase("随机IP")) {
                    Result = radomVariables.GetRadmonIP();
                }
                if (Variablestype.equalsIgnoreCase("当前时间")) {
                    Result = radomVariables.GetCurrentTime();
                }
                if (Variablestype.equalsIgnoreCase("当前日期")) {
                    Result = radomVariables.GetCurrentDate(Params);
                }
                if (Variablestype.equalsIgnoreCase("当前时间戳")) {
                    Result = radomVariables.GetCurrentTimeMillis();
                }
            }
        }
        return Result;
    }

    //根据数据类型转换
    private Object GetDataByType(String Data, String ValueType) throws Exception {
        Object Result = new Object();
        if (ValueType.equalsIgnoreCase("Number")) {
            try {
                Result = Long.parseLong(Data);
            } catch (Exception ex) {
                Result = "参数值  " + Data + " 不是数字Number类型，请检查修改！";
                throw new Exception(Result.toString());
            }
        }
        if (ValueType.equalsIgnoreCase("Json")) {
            try {
                Result = JSON.parse(Data);
            } catch (Exception ex) {
                Result = "参数值  " + Data + " 不是Json类型，请检查修改！";
                throw new Exception(Result.toString());
            }
        }
        if (ValueType.equalsIgnoreCase("String") || ValueType.isEmpty()) {
            Result = Data;
        }
        if (ValueType.equalsIgnoreCase("Array")) {
            String[] Array = Data.split(",");
            Result = Array;
        }
        if (ValueType.equalsIgnoreCase("Bool")) {
            try {
                Result = Boolean.parseBoolean(Data);
            } catch (Exception ex) {
                Result = "参数值  " + Data + " 不是布尔Bool类型，请检查修改！";
                throw new Exception(Result.toString());
            }
        }
        return Result;
    }

    //根据请求数据类型增加header
    private HttpHeader AddHeaderByRequestContentType(HttpHeader httpHeader, String RequestContentType) {
        if (RequestContentType.equalsIgnoreCase("json")) {
            httpHeader.addParam("Content-Type", "application/json;charset=utf-8");
        }
        if (RequestContentType.equalsIgnoreCase("xml")) {
            httpHeader.addParam("Content-Type", "application/xml;charset=utf-8");
        }
        if (RequestContentType.equalsIgnoreCase("Form表单")) {
            httpHeader.addParam("Content-Type", "application/x-www-form-urlencoded");
        }
        return httpHeader;
    }
}
