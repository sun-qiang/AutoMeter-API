package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Dbvariables;
import com.zoctan.api.entity.Scriptvariables;
import com.zoctan.api.service.ScriptvariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/04/02
 */
@RestController
@RequestMapping("/scriptvariables")
public class ScriptvariablesController {
    @Resource
    private ScriptvariablesService scriptvariablesService;

    @PostMapping
    public Result add(@RequestBody Scriptvariables scriptvariables) {

        Condition con=new Condition(Scriptvariables.class);
        con.createCriteria().andCondition("projectid = "+scriptvariables.getProjectid())
                .andCondition("scriptvariablesname = '" + scriptvariables.getScriptvariablesname().replace("'","''") + "'");
        if(scriptvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {
            scriptvariablesService.save(scriptvariables);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        scriptvariablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Scriptvariables scriptvariables) {
        scriptvariablesService.update(scriptvariables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Scriptvariables scriptvariables = scriptvariablesService.getById(id);
        return ResultGenerator.genOkResult(scriptvariables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Scriptvariables> list = scriptvariablesService.listAll();
        PageInfo<Scriptvariables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Scriptvariables dic) {
        Condition con=new Condition(Dbvariables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("scriptvariablesname = '" + dic.getScriptvariablesname().replace("'","''") + "'").andCondition("id <> " + dic.getId());
        if(scriptvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {

            this.scriptvariablesService.updatescriptvariables(dic);
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
        final List<Scriptvariables> list = this.scriptvariablesService.findscriptvariablesWithName(param);
        final PageInfo<Scriptvariables> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
