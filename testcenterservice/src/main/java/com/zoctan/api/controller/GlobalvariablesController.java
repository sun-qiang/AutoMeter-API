package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Globalheader;
import com.zoctan.api.entity.Globalvariables;
import com.zoctan.api.service.GlobalvariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/07/11
 */
@RestController
@RequestMapping("/globalvariables")
public class GlobalvariablesController {
    @Resource
    private GlobalvariablesService globalvariablesService;

    @PostMapping
    public Result add(@RequestBody Globalvariables globalvariables) {

        Condition con=new Condition(Globalvariables.class);
        con.createCriteria().andCondition("projectid = "+globalvariables.getProjectid())
                .andCondition("keyname = '" + globalvariables.getKeyname().replace("'","''") + "'");
        if(globalvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("全局变量名已经存在");
        }
        else {
            globalvariablesService.save(globalvariables);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        globalvariablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Globalvariables globalvariables) {
        globalvariablesService.update(globalvariables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Globalvariables globalvariables = globalvariablesService.getById(id);
        return ResultGenerator.genOkResult(globalvariables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Globalvariables> list = globalvariablesService.listAll();
        PageInfo<Globalvariables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Globalvariables dic) {
        Condition con=new Condition(Globalvariables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("keyname = '" + dic.getKeyname().replace("'","''") + "'").andCondition("id <> " + dic.getId());
        if(globalvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("全局变量名已经存在");
        }
        else {

            this.globalvariablesService.updateGlobalvariables(dic);
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
        final List<Globalvariables> list = this.globalvariablesService.findGlobalvariablesWithName(param);
        final PageInfo<Globalvariables> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
