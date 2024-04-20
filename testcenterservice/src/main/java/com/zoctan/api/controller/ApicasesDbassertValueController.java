package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.ApicasesDbassert;
import com.zoctan.api.entity.ApicasesDbassertValue;
import com.zoctan.api.service.ApicasesDbassertValueService;
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
@RequestMapping("/apicases/dbassert/value")
public class ApicasesDbassertValueController {
    @Resource
    private ApicasesDbassertValueService apicasesDbassertValueService;

    @PostMapping
    public Result add(@RequestBody ApicasesDbassertValue apicasesDbassertValue) {
        Condition con=new Condition(ApicasesDbassertValue.class);
        con.createCriteria().andCondition("dbassertid = "+apicasesDbassertValue.getDbassertid())
                .andCondition("fieldname = '" + apicasesDbassertValue.getFieldname().replace("'","''") + "'")
                .andCondition("roworder = " + apicasesDbassertValue.getRoworder());
        if(apicasesDbassertValueService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的数据库断言值");
        }
        else {
            apicasesDbassertValueService.save(apicasesDbassertValue);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apicasesDbassertValueService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ApicasesDbassertValue apicasesDbassertValue) {
        apicasesDbassertValueService.update(apicasesDbassertValue);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ApicasesDbassertValue apicasesDbassertValue = apicasesDbassertValueService.getById(id);
        return ResultGenerator.genOkResult(apicasesDbassertValue);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApicasesDbassertValue> list = apicasesDbassertValueService.listAll();
        PageInfo<ApicasesDbassertValue> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final ApicasesDbassertValue apicasesDbassertValue) {
        Condition con=new Condition(ApicasesDbassertValue.class);
        con.createCriteria().andCondition("dbassertid = "+apicasesDbassertValue.getDbassertid())
                .andCondition("fieldname = '" + apicasesDbassertValue.getFieldname().replace("'","''") + "'")
                .andCondition("roworder = " + apicasesDbassertValue.getRoworder())
                .andCondition("id <> " + apicasesDbassertValue.getId());
        if(apicasesDbassertValueService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的数据库断言值");
        }
        else {
            this.apicasesDbassertValueService.updateDbAssertValue(apicasesDbassertValue);
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
        final List<ApicasesDbassertValue> list = this.apicasesDbassertValueService.findDbAssertValueWithName(param);
        final PageInfo<ApicasesDbassertValue> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
