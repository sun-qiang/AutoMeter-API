package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.PlanmailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/05/19
 */
@RestController
@RequestMapping("/planmail")
public class PlanmailController {
    @Resource
    private PlanmailService planmailService;

    @PostMapping
    public Result add(@RequestBody Planmail planmail) {
        planmailService.save(planmail);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        planmailService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Planmail planmail) {
        planmailService.update(planmail);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Planmail planmail = planmailService.getById(id);
        return ResultGenerator.genOkResult(planmail);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Planmail> list = planmailService.listAll();
        PageInfo<Planmail> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Planmail> list = planmailService.findDicWithName(param);
        final PageInfo<Planmail> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/addbatchmail")
    public Result addbatchmail(@RequestBody final List<Planmail> planmailList) {
        List<Planmail> insertlist = new ArrayList<>();
        for (Planmail pl : planmailList) {
            long planid = pl.getExecuteplanid();
            long accountid = pl.getAccountid();
            Condition con = new Condition(Planmail.class);
            con.createCriteria().andCondition("executeplanid = " + planid)
                    .andCondition("accountid = " + accountid);
            if (planmailService.ifexist(con) == 0) {
                insertlist.add(pl);
            }
        }
        if(insertlist.size()>0)
        {
            planmailService.saveplanmail(insertlist);
        }
        return ResultGenerator.genOkResult();
    }
}
