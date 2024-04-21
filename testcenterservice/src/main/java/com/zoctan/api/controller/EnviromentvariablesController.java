package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Enviromentvariables;
import com.zoctan.api.entity.Globalvariables;
import com.zoctan.api.service.EnviromentvariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/04/14
 */
@RestController
@RequestMapping("/enviromentvariables")
public class EnviromentvariablesController {
    @Resource
    private EnviromentvariablesService enviromentvariablesService;

    @PostMapping
    public Result add(@RequestBody Enviromentvariables enviromentvariables) {
        Condition con=new Condition(Enviromentvariables.class);
        con.createCriteria().andCondition("projectid = "+enviromentvariables.getProjectid())
                .andCondition("variablesname = '" + enviromentvariables.getVariablesname().replace("'","''") + "'")
                .andCondition("envid = " + enviromentvariables.getEnvid());

        if(enviromentvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("当前环境下已经存在此环境变量名");
        }
        else {
            enviromentvariablesService.save(enviromentvariables);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        enviromentvariablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Enviromentvariables enviromentvariables) {
        enviromentvariablesService.update(enviromentvariables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Enviromentvariables enviromentvariables = enviromentvariablesService.getById(id);
        return ResultGenerator.genOkResult(enviromentvariables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Enviromentvariables> list = enviromentvariablesService.listAll();
        PageInfo<Enviromentvariables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Enviromentvariables dic) {
        Condition con=new Condition(Enviromentvariables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("variablesname = '" + dic.getVariablesname().replace("'","''") + "'")
                .andCondition("envid = " + dic.getEnvid()).andCondition("id <> " + dic.getId());
        if(enviromentvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("当前环境下已经存在此环境变量名");
        }
        else {

            this.enviromentvariablesService.updateEnviromentvariables(dic);
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
        final List<Enviromentvariables> list = this.enviromentvariablesService.findEnviromentvariablesWithName(param);
        final PageInfo<Enviromentvariables> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
