package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/05/20
 */
@Slf4j
@RestController
@RequestMapping("/api/params")
public class ApiParamsController {
    @Resource
    private ApiParamsService apiParamsService;
    @Resource
    private ApiCasedataService apiCasedataService;
    @Resource
    private ApicasesService apicasesService;
    @Resource
    private ApiService apiService;
    @Resource
    private AccountRoleService accountRoleService;


    @PostMapping
    public Result add(@RequestBody ApiParams apiParams) {
        if(apiParams.getPropertytype().equalsIgnoreCase("Body"))
        {
            Long apiid = apiParams.getApiid();
            Api apiexist= apiService.getById(apiid);
            Long currentaccountid = apiParams.getCreatorid();
            AccountRole accountRole= accountRoleService.getBy("accountId",currentaccountid);
            if (apiexist != null) {
                if (currentaccountid.equals(apiexist.getMid()) || accountRole.getRoleId()==1) {
                    String RequestContentType=apiexist.getRequestcontenttype();
                    String Property = apiParams.getPropertytype();
                    Condition con = new Condition(ApiParams.class);
                    con.createCriteria().andCondition("keytype = '" + apiParams.getKeytype() + "'").andCondition("apiid = " + apiParams.getApiid());
//            List<ApiCasedata> apiCasedataList = apiCasedataService.getdataidbyapiidandtype(apiid, Property);
//            String IDS = "";
//            if (apiCasedataList.size() > 0) {
//                for (ApiCasedata apicasedata : apiCasedataList) {
//                    IDS = IDS + "," + apicasedata.getId();
//                }
//                IDS = IDS.substring(1);
//                ApiParamsController.log.info("删除用例数据表的id为：" + IDS);
//            }
//            List<Apicases>apicasesList=apicasesService.getcasebyapiid(apiid);
                    //存在此类型的body参数
                    if (apiParamsService.ifexist(con) > 0) {
                        //更新参数表
                        apiParamsService.updateApiParams(apiParams);
                        //参数值是空，做删除操作
//                if(apiParams.getKeyname().isEmpty())
//                {
//                    //Body值为空，则当做删除类型为keytype的Body，并且删除用例参数值
//                    apiParamsService.deleteByCondition(con);
////                    if(!IDS.isEmpty())
////                    {
////                        apiCasedataService.deleteByIds(IDS);
////                    }
//                }
//                //更新操作
//                else
//                {
//                    //更新参数表
//                    apiParamsService.updateApiParams(apiParams);
//                    //判断用例数据表中类型是否和当前相同，如果不同，先删除，保存当前类型的默认值
////                    if(!IDS.isEmpty())
////                    {
////                        apiCasedataService.deleteByIds(IDS);
////                    }
////                    //保存新的Body参数值
////                    List< ApiCasedata> apiCasedataList1=new ArrayList<>();
////                    for (Apicases apicases:apicasesList) {
////                        ApiCasedata apiCasedata=new ApiCasedata();
////                        apiCasedata.setCaseid(apicases.getId());
////                        apiCasedata.setCasename(apicases.getCasename());
////                        apiCasedata.setApiparam("Body");
////                        apiCasedata.setApiparamvalue(apiParams.getKeyname());
////                        apiCasedata.setParamstype(apiParams.getKeytype());
////                        apiCasedata.setPropertytype("Body");
////                        apiCasedata.setMemo("");
////                        apiCasedataList1.add(apiCasedata);
////                    }
////                    if(apiCasedataList1.size()>0)
////                    {
////                        apiCasedataService.saveCasedata(apiCasedataList1);
////                    }
//                }
                    }
                    //不存在此类型的body参数，新增，删除老的Body用例数据，同步新的Body用例数据
                    else
                    {
                        apiParamsService.SaveApiParams(apiParams);
                        //保存Body参数
//                if(!apiParams.getKeyname().isEmpty())
//                {
//                    apiParamsService.SaveApiParams(apiParams);
//                    //同步删除已存在的用例数据
////                    if(!IDS.isEmpty())
////                    {
////                        apiCasedataService.deleteByIds(IDS);
////                    }
////                    //保存新的Body参数值
////                    List< ApiCasedata> apiCasedataList1=new ArrayList<>();
////                    for (Apicases apicases:apicasesList) {
////                        ApiCasedata apiCasedata=new ApiCasedata();
////                        apiCasedata.setCaseid(apicases.getId());
////                        apiCasedata.setCasename(apicases.getCasename());
////                        apiCasedata.setApiparam("Body");
////                        apiCasedata.setApiparamvalue(apiParams.getKeyname());
////                        apiCasedata.setParamstype(apiParams.getKeytype());
////                        apiCasedata.setPropertytype("Body");
////                        apiCasedata.setMemo("");
////                        apiCasedataList1.add(apiCasedata);
////                    }
////                    if(apiCasedataList1.size()>0)
////                    {
////                        apiCasedataService.saveCasedata(apiCasedataList1);
////                    }
//                }
                    }
                } else {
                    return ResultGenerator.genFailedResult("当前api参数只有api维护人或者管理员可以修改");
                }
            } else
            {
                return ResultGenerator.genFailedResult("当前api不存在");
            }
        }
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/addapiallparams")
    public Result addallparams(@RequestBody List<ApiParams> apiParamsList) {
        //如果是Body类型的参数，先删除用例值中的Body类型的数据
        if(apiParamsList.size()>0)
        {
            String Property=apiParamsList.get(0).getPropertytype();
            Long apiid=apiParamsList.get(0).getApiid();
            Api api= apiService.getById(apiid);
            Long currentaccountid = apiParamsList.get(0).getCreatorid();
            AccountRole accountRole= accountRoleService.getBy("accountId",currentaccountid);
            if (api != null) {
                if (currentaccountid.equals(api.getMid()) || accountRole.getRoleId()==1) {
                    String requesttype=api.getRequestcontenttype();
                    if(Property.equalsIgnoreCase("Body"))
                    {
                        if(requesttype.equalsIgnoreCase("Form表单"))
                        {
                            updateparams(apiParamsList);
                        }
                    }
                    else
                    {
                        updateparams(apiParamsList);
                    }
                } else {
                    return ResultGenerator.genFailedResult("当前api参数只有api维护人或者管理员可以修改");
                }
            } else
            {
                return ResultGenerator.genFailedResult("当前api不存在");
            }
        }
        return ResultGenerator.genOkResult();
    }

    private  void updateparams(List<ApiParams> apiParamsList)
    {
        for (ApiParams apiparam: apiParamsList) {
            Long apiidnew = apiparam.getApiid();
            String Protype = apiparam.getPropertytype();
            String KeyName = apiparam.getKeyname();
            String KeyValue = apiparam.getKeydefaultvalue();
            String ParamType = apiparam.getKeytype();
            List<Apicases> apicasesList = apicasesService.getcasebyapiid(apiidnew);
            if (apiparam.getId() == null) {
                if ((!apiparam.getKeyname().isEmpty()) || (!apiparam.getKeytype().isEmpty()) || (!apiparam.getKeydefaultvalue().isEmpty())) {
                    apiParamsService.SaveApiParams(apiparam);
                    //新增所有用例值的参数名，参数值为默认值
                    if (apicasesList.size() > 0) {
                        List<ApiCasedata> apiCasedataList = new ArrayList<>();
                        for (Apicases apicase : apicasesList) {
                            ApiCasedata apiCasedata = new ApiCasedata();
                            apiCasedata.setCaseid(apicase.getId());
                            apiCasedata.setCasename(apicase.getCasename());
                            apiCasedata.setApiparam(KeyName);
                            apiCasedata.setApiparamvalue(KeyValue);
                            apiCasedata.setPropertytype(Protype);
                            apiCasedata.setParamstype(ParamType);
                            apiCasedata.setMemo("");
                            apiCasedataList.add(apiCasedata);
                        }
                        if(apiCasedataList.size()>0)
                        {
                            apiCasedataService.saveCasedata(apiCasedataList);
                        }
                    }
                }
            }
            else
            {
                ApiParams oldparam=apiParamsService.getById(apiparam.getId());
                String OldParamName=oldparam.getKeyname();
                apiParamsService.updateApiParams(apiparam);
                //更新所有用例值的参数名，（参数值先不刷新成默认值）
                for (Apicases apicase : apicasesList) {
                    apiCasedataService.updateparambycaseidandprotypeandapiparam(apicase.getId(),Protype,KeyName,OldParamName,ParamType);
                }
            }
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        //删除使用此api参数的用例数据
        ApiParams apiParams= apiParamsService.getById(id);
        Long Apiid=apiParams.getApiid();
        String apiparam =apiParams.getKeyname();
        String Porperty=apiParams.getPropertytype();
        List<ApiCasedata>apiCasedataList= apiCasedataService.getdataidbyapiidandtypeandapiparam(Apiid,Porperty,apiparam);
        if(apiCasedataList.size()>0)
        {
            String IDS="";
            for (ApiCasedata apicasedata: apiCasedataList) {
                IDS=IDS+","+apicasedata.getId();
            }
            IDS=IDS.substring(1);
            ApiParamsController.log.info("删除用例数据表的id为："+IDS);
            if(!IDS.isEmpty())
            {
                apiCasedataService.deleteByIds(IDS);
            }
        }
        apiParamsService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ApiParams apiParams) {
        apiParamsService.update(apiParams);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ApiParams apiParams = apiParamsService.getById(id);
        return ResultGenerator.genOkResult(apiParams);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApiParams> list = apiParamsService.listAll();
        PageInfo<ApiParams> pageInfo = PageInfo.of(list);
        System.out.println(ResultGenerator.genOkResult(pageInfo));
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ApiParams apiParams) {
        Condition con=new Condition(ApiParams.class);
        con.createCriteria().andCondition("deployunitname = '" + apiParams.getDeployunitname() + "'")
                .andCondition("apiname = '" + apiParams.getApiname() + "'")
                .andCondition("propertytype = '" + apiParams.getPropertytype() + "'")
                .andCondition("id <> " + apiParams.getId());
        if(apiParamsService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("微服务下已经存在此类型的API参数");
        }

        else
        {
            this.apiParamsService.updateApiParams(apiParams);
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
        PageHelper.startPage(page, size);
        final List<ApiParams> list = this.apiParamsService.findApiParamsWithName(param);
        final PageInfo<ApiParams> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 输入APIID获取参数列表
     */
    @PostMapping("/searchparamsbyapiid")
    public Result searchparamsbyapiid(@RequestBody Map<String, Object> param) {
        final List<ApiParams> list = this.apiParamsService.getApiParamsbyapiid(param);
        final PageInfo<ApiParams> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 根据APIid和propertype获取参数
     */
    @PostMapping("/searchbyidandproperty")
    public Result searchbyidandproperty(@RequestBody Map<String, Object> param) {
        Long apiid= Long.parseLong(param.get("apiid").toString());
        String propertytype= param.get("propertytype").toString();
        final List<ApiParams> list = this.apiParamsService.getApiParamsbypropertytype(apiid,propertytype);
        return ResultGenerator.genOkResult(list);
    }

    @PostMapping("/getBodyNoFormbyapiid")
    public Result getBodyNoFormbyapiid(@RequestBody Map<String, Object> param) {
        Long apiid= Long.parseLong(param.get("apiid").toString());
        String propertytype= param.get("propertytype").toString();
        String KeyType=param.get("keytype").toString();
        ApiParams apiParams = this.apiParamsService.getBodyNoFormbyapiid(apiid,propertytype,"NoForm",KeyType);
        if(apiParams==null)
        {
            apiParams=new ApiParams();
        }
        return ResultGenerator.genOkResult(apiParams);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/searchid")
    public Result searchid(@RequestBody Map<String, Object> param) {
        final List<ApiParams> list = this.apiParamsService.getApiParamsbyname(param);
        final PageInfo<ApiParams> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);    }
}
