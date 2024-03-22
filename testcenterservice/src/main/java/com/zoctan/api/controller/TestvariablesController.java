package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.TestVariablesDto;
import com.zoctan.api.entity.Deployunit;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.TestplanTestscene;
import com.zoctan.api.entity.Testvariables;
import com.zoctan.api.service.ApicasesVariablesService;
import com.zoctan.api.service.TestvariablesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2021/07/14
 */
@RestController
@RequestMapping("/testvariables")
public class TestvariablesController {
    @Resource
    private TestvariablesService testvariablesService;
    @Resource
    private ApicasesVariablesService apicasesVariablesService;

    @PostMapping
    public Result add(@RequestBody Testvariables testvariables) {

        Condition con=new Condition(Testvariables.class);
        con.createCriteria().andCondition("projectid = "+testvariables.getProjectid())
                .andCondition("testvariablesname = '" + testvariables.getTestvariablesname().replace("'","''") + "'");
        if(testvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {
            testvariablesService.save(testvariables);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apicasesVariablesService.deleteBy("variablesid",id);
        testvariablesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Testvariables testvariables) {
        testvariablesService.update(testvariables);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Testvariables testvariables = testvariablesService.getById(id);
        return ResultGenerator.genOkResult(testvariables);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Testvariables> list = testvariablesService.listAll();
        PageInfo<Testvariables> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getvariableslist")
    public Result listall() {
        List<Testvariables> list = testvariablesService.listAll();
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Testvariables dic) {
        Condition con=new Condition(Testvariables.class);
        con.createCriteria().andCondition("projectid = "+dic.getProjectid())
                .andCondition("testvariablesname = '" + dic.getTestvariablesname().replace("'","''") + "'").andCondition("id <> " + dic.getId());
        if(testvariablesService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("变量名已经存在");
        }
        else {

            this.testvariablesService.updatetestvariables(dic);
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
        final List<TestVariablesDto> list = this.testvariablesService.findtestvariablesWithName(param);
        final PageInfo<TestVariablesDto> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/findtestvariablesbycaseid")
    public Result findtestvariablesbycaseid(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<TestVariablesDto> list = this.testvariablesService.findtestvariablesWithName(param);
        final PageInfo<TestVariablesDto> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
//        final List<Testvariables> list = this.testvariablesService.findtestvariablesbycaseid(param);
//        return ResultGenerator.genOkResult(list);
    }
}
