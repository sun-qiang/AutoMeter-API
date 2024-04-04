package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ConditionApi;
import com.zoctan.api.entity.ConditionDelay;
import com.zoctan.api.entity.ConditionOrder;
import com.zoctan.api.entity.ConditionScript;
import com.zoctan.api.service.ConditionDelayService;
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
 * @date 2022/03/30
 */
@RestController
@RequestMapping("/condition/delay")
public class ConditionDelayController {
    @Resource
    private ConditionDelayService conditionDelayService;
    @Resource
    private ConditionOrderService conditionOrderService;

    @PostMapping
    public Result add(@RequestBody ConditionDelay conditionDelay) {
        Condition con=new Condition(ConditionDelay.class);
        con.createCriteria().andCondition("projectid = "+conditionDelay.getProjectid())
                .andCondition("subconditionname = '" + conditionDelay.getSubconditionname().replace("'","''")+ "'");
        if(conditionDelayService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已存在该延时子条件名");
        }
        else
        {
            conditionDelayService.save(conditionDelay);
            ConditionOrder conditionOrder=new ConditionOrder();
            conditionOrder.setId(null);
            conditionOrder.setConditiontype(conditionDelay.getConditiontype());
            conditionOrder.setConditionid(conditionDelay.getId());
            conditionOrder.setConditionname(conditionDelay.getConditionname());
            //条件来源id和name
            conditionOrder.setSubconditionid(conditionDelay.getConditionid());
            conditionOrder.setSubconditionname(conditionDelay.getSubconditionname());
            conditionOrder.setConditionorder(new Long(1));
            conditionOrder.setOrderstatus("未排序");
            conditionOrder.setSubconditiontype("前置延时条件");
            conditionOrder.setCreateTime(new Date());
            conditionOrder.setLastmodifyTime(new Date());
            conditionOrder.setCreator(conditionDelay.getCreator());
            conditionOrderService.save(conditionOrder);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        ConditionDelay conditionDelay=conditionDelayService.getBy("id",id);
        conditionOrderService.deleteconditionorderbysubconid(conditionDelay.getId(),conditionDelay.getConditiontype(),"前置延时条件");
        conditionDelayService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ConditionDelay conditionDelay) {
        conditionDelayService.update(conditionDelay);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ConditionDelay conditionDelay = conditionDelayService.getById(id);
        return ResultGenerator.genOkResult(conditionDelay);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ConditionDelay> list = conditionDelayService.listAll();
        PageInfo<ConditionDelay> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ConditionDelay conditionApi) {
        Condition con=new Condition(ConditionDelay.class);
        con.createCriteria().andCondition("projectid = "+conditionApi.getProjectid())
                .andCondition("subconditionname = '" + conditionApi.getSubconditionname().replace("'","''")+ "'")
                .andCondition("id <> " + conditionApi.getId());
        if(conditionDelayService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已存在该延时子条件名");
        }
        else {
            this.conditionDelayService.updateTestconditiondelay(conditionApi);
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
        final List<ConditionDelay> list = this.conditionDelayService.findtestconditiondelayWithName(param);
        final PageInfo<ConditionDelay> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/searchbytype")
    public Result searchbytype(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<ConditionDelay> list = this.conditionDelayService.findtestconditiondelayWithType(param);
        final PageInfo<ConditionDelay> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
