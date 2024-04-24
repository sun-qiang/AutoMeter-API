package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.Casedata;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/09/21
 */
@RestController
@RequestMapping("/api/casedata")
public class ApiCasedataController {
    @Resource
    private ApiCasedataService apiCasedataService;
    @Resource
    private ApiParamsService apiParamsService;
    @Resource
    private ApicasesService apicasesService;

    @Resource
    private AccountRoleService accountRoleService;

    @PostMapping
    public Result add(@RequestBody Casedata apiCasedata) {
        apiCasedataService.save(apiCasedata);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apiCasedataService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ApiCasedata apiCasedata) {
        apiCasedataService.update(apiCasedata);
        return ResultGenerator.genOkResult();
    }

    /**
     * 更新BodyData
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ApiCasedata apiCasedata) {
        Long apicaseidid = apiCasedata.getCaseid();
        Apicases apicases = apicasesService.getById(apicaseidid);
        Long currentaccountid = apiCasedata.getMid();
        AccountRole accountRole = accountRoleService.getBy("accountId", currentaccountid);
        if (apicases != null) {
            if (currentaccountid.equals(apicases.getMid()) || accountRole.getRoleId() == 1) {
                if (apiCasedata.getId() == null) {
                    apiCasedataService.save(apiCasedata);
                } else {
                    this.apiCasedataService.update(apiCasedata);
//            if(!apiCasedata.getApiparamvalue().isEmpty())
//            {
//                this.apiCasedataService.update(apiCasedata);
//            }
                }
            } else {
                return ResultGenerator.genFailedResult("当前用例数据只有维护人或者管理员可以修改");
            }
        } else {
            return ResultGenerator.genFailedResult("当前用例不存在");
        }
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ApiCasedata apiCasedata = apiCasedataService.getById(id);
        return ResultGenerator.genOkResult(apiCasedata);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApiCasedata> list = apiCasedataService.listAll();
        PageInfo<ApiCasedata> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 根据caseid和参数类型返回参数值
     */
    @PostMapping("/casevalue")
    public Result casevalue(@RequestBody final Map<String, Object> param) {
        final List<ApiCasedata> list = this.apiCasedataService.getparamvaluebycaseidandtype(param);
        final PageInfo<ApiCasedata> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 根据用例值的保存实际是更新参数值操作
     */
    @PostMapping("/updatepropertydata")
    public Result updatepropertydata(@RequestBody List<ApiCasedata> apiCasedataList) {
        for (ApiCasedata apiCasedata : apiCasedataList) {
            Long apicaseidid = apiCasedataList.get(0).getCaseid();
            Apicases apicases = apicasesService.getById(apicaseidid);
            Long currentaccountid = apiCasedataList.get(0).getMid();
            AccountRole accountRole = accountRoleService.getBy("accountId", currentaccountid);
            if (apicases != null) {
                if (currentaccountid.equals(apicases.getMid()) || accountRole.getRoleId() == 1) {
                    apiCasedataService.update(apiCasedata);
                } else {
                    return ResultGenerator.genFailedResult("当前用例数据只有维护人或者管理员可以修改");
                }
            } else {
                return ResultGenerator.genFailedResult("当前用例不存在");
            }
        }
        return ResultGenerator.genOkResult();
    }


    /**
     * 根据caseid,apiid,参数类型处理body，如果用例数据没数据，则取参数名
     */
    @PostMapping("/casevalueforbody")
    public Result casevalueforbody(@RequestBody final Map<String, Object> param) {
        final List<ApiCasedata> list = this.apiCasedataService.getparamvaluebycaseidandtype(param);
        String BodyValue = "";
        if (list.size() == 0) {
            Long apiid = Long.parseLong(param.get("apiid").toString());
            String propertytype = param.get("propertytype").toString();
            List<ApiParams> apiParams = apiParamsService.getApiParamsbypropertytype(apiid, propertytype);
            if (apiParams.size() > 0) {
                BodyValue = apiParams.get(0).getKeyname();
            }
            return ResultGenerator.genOkResult(BodyValue);
        } else {
            BodyValue = list.get(0).getApiparamvalue();
            return ResultGenerator.genOkResult(BodyValue);
        }
    }

}
