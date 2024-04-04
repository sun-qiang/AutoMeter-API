package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionOrder;
import com.zoctan.api.entity.Testcondition;
import com.zoctan.api.service.ConditionApiService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.service.ConditionOrderService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2021/05/30
 */
@RestController
@RequestMapping("/condition/api")
public class ConditionApiController {
    @Resource
    private ConditionApiService conditionApiService;
    @Resource
    private ConditionOrderService conditionOrderService;



    @PostMapping
    public Result add(@RequestBody ConditionApi conditionApi) {
        Condition con=new Condition(ConditionApi.class);
        con.createCriteria().andCondition("projectid = "+conditionApi.getProjectid())
                .andCondition("conditionname = '" + conditionApi.getConditionname()+ "'")
                .andCondition("subconditionname = '" + conditionApi.getSubconditionname().replace("'","''")+ "'")
                .andCondition("deployunitname = '" + conditionApi.getDeployunitname()+ "'")
                .andCondition("apiname = '" + conditionApi.getApiname()+ "'")
                .andCondition("casename = '" + conditionApi.getCasename()+ "'");
        if(conditionApiService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已存在该前置条件名");
        }
        else {
            conditionApiService.save(conditionApi);
            ConditionOrder conditionOrder=new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(conditionApi.getConditiontype());
            conditionOrder.setConditionid(conditionApi.getId());
            conditionOrder.setConditionname(conditionApi.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(conditionApi.getConditionid());
            conditionOrder.setSubconditionname(conditionApi.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置接口条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(conditionApi.getCreator());
            conditionOrderService.save(conditionOrder);
        }
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        ConditionApi conditionApi=conditionApiService.getBy("id",id);
        conditionOrderService.deleteconditionorderbysubconid(conditionApi.getId(),conditionApi.getConditiontype(),"前置接口条件");
        conditionApiService.deleteBy("id",id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ConditionApi conditionApi) {
        conditionApiService.update(conditionApi);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ConditionApi conditionApi = conditionApiService.getById(id);
        return ResultGenerator.genOkResult(conditionApi);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ConditionApi> list = conditionApiService.listAll();
        PageInfo<ConditionApi> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ConditionApi conditionApi) {
        Condition con=new Condition(ConditionApi.class);
        con.createCriteria().andCondition("projectid = "+conditionApi.getProjectid())
                .andCondition("conditionname = '" + conditionApi.getConditionname()+ "'")
                .andCondition("subconditionname = '" + conditionApi.getSubconditionname().replace("'","''")+ "'")
                .andCondition("deployunitname = '" + conditionApi.getDeployunitname()+ "'")
                .andCondition("apiname = '" + conditionApi.getApiname()+ "'")
                .andCondition("casename = '" + conditionApi.getCasename()+ "'")
                .andCondition("id <> " + conditionApi.getId());
        if(conditionApiService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已存在该子条件名或者接口");
        }
        else {
            this.conditionApiService.updateTestconditionapi(conditionApi);
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
        final List<ConditionApi> list = this.conditionApiService.findtestconditionapiWithName(param);
        final PageInfo<ConditionApi> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
