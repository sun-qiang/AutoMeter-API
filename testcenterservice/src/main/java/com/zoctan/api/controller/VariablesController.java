package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.Variables;
import com.zoctan.api.service.VariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/02/15
 */
@RestController
@RequestMapping("/variables")
public class VariablesController {
    @Resource
    private VariablesService variablesService;

    @PostMapping
    public Result add(@RequestBody Variables dic) {

        Condition con=new Condition(Variables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("variablesname = '" + dic.getVariablesname().replace("'","''") + "'")
                .andCondition("variablestype = '" + dic.getVariablestype()+ "'");
        if(variablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {

            variablesService.save(dic);
            return ResultGenerator.genOkResult();
        }

    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        variablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Variables variables) {
        variablesService.update(variables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Variables variables = variablesService.getById(id);
        return ResultGenerator.genOkResult(variables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Variables> list = variablesService.listAll();
        PageInfo<Variables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateVariables(@RequestBody final Variables dic) {
        Condition con=new Condition(Variables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("variablesname = '" + dic.getVariablesname().replace("'","''") + "'")
                .andCondition("variablestype = '" + dic.getVariablestype()+ "'")
                .andCondition("id <> " + dic.getId());
        if(variablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {

            this.variablesService.updatevariables(dic);
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
        long accountid = Long.parseLong(param.get("accountId").toString());
        if (accountid == 1) {
            param.put("creator", null);
            param.put("projectid", null);
        }
        PageHelper.startPage(page, size);
        final List<Variables> list = this.variablesService.findvariablesWithName(param);
        final PageInfo<Variables> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
