package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Dbvariables;
import com.zoctan.api.entity.Testvariables;
import com.zoctan.api.service.DbvariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/03/25
 */
@RestController
@RequestMapping("/dbvariables")
public class DbvariablesController {
    @Resource
    private DbvariablesService dbvariablesService;

    @PostMapping
    public Result add(@RequestBody Dbvariables dbvariables) {

        Condition con=new Condition(Dbvariables.class);
        con.createCriteria().andCondition("projectid = "+dbvariables.getProjectid())
                .andCondition("dbvariablesname = '" + dbvariables.getDbvariablesname().replace("'","''") + "'");
        if(dbvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else
        {
            dbvariablesService.save(dbvariables);
            return ResultGenerator.genOkResult();
        }

    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        dbvariablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Dbvariables dbvariables) {
        dbvariablesService.update(dbvariables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Dbvariables dbvariables = dbvariablesService.getById(id);
        return ResultGenerator.genOkResult(dbvariables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Dbvariables> list = dbvariablesService.listAll();
        PageInfo<Dbvariables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Dbvariables dic) {
        Condition con=new Condition(Dbvariables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("dbvariablesname = '" + dic.getDbvariablesname().replace("'","''") + "'").andCondition("id <> " + dic.getId());
        if(dbvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {

            this.dbvariablesService.updatedbvariables(dic);
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
        final List<Dbvariables> list = this.dbvariablesService.finddbvariablesWithName(param);
        final PageInfo<Dbvariables> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
