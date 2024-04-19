package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.service.ApicasesDbassertService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/04/16
 */
@RestController
@RequestMapping("/apicases/dbassert")
public class ApicasesDbassertController {
    @Resource
    private ApicasesDbassertService apicasesDbassertService;

    @PostMapping
    public Result add(@RequestBody ApicasesDbassert apicasesDbassert) {
        Condition con=new Condition(ApicasesDbassert.class);
        con.createCriteria().andCondition("caseid = "+apicasesDbassert.getCaseid())
                .andCondition("enviroment = '" + apicasesDbassert.getEnviroment().replace("'","''") + "'")
                .andCondition("assemblename = '" + apicasesDbassert.getAssemblename().replace("'","''") + "'")
                .andCondition("expression = '" + apicasesDbassert.getExpression().replace("'","''") + "'");

        if(apicasesDbassertService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的数据库断言");
        }
        else {
            apicasesDbassertService.save(apicasesDbassert);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apicasesDbassertService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ApicasesDbassert apicasesDbassert) {
        apicasesDbassertService.update(apicasesDbassert);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ApicasesDbassert apicasesDbassert = apicasesDbassertService.getById(id);
        return ResultGenerator.genOkResult(apicasesDbassert);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApicasesDbassert> list = apicasesDbassertService.listAll();
        PageInfo<ApicasesDbassert> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ApicasesDbassert apicasesDbassert) {
        Condition con=new Condition(ApicasesDbassert.class);
        con.createCriteria().andCondition("caseid = "+apicasesDbassert.getCaseid())
                .andCondition("enviroment = '" + apicasesDbassert.getEnviroment().replace("'","''") + "'")
                .andCondition("assemblename = '" + apicasesDbassert.getAssemblename().replace("'","''") + "'")
                .andCondition("expression = '" + apicasesDbassert.getExpression().replace("'","''") + "'")
                .andCondition("id <> " + apicasesDbassert.getId());

        if(apicasesDbassertService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的数据库断言");
        }
        else {
            this.apicasesDbassertService.updateDbAssert(apicasesDbassert);
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
        final List<ApicasesDbassert> list = this.apicasesDbassertService.findDbAssertWithName(param);
        final PageInfo<ApicasesDbassert> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
