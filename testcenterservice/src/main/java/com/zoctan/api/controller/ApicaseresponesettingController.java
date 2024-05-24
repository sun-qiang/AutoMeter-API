package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Apicaseresponesetting;
import com.zoctan.api.service.ApicaseresponesettingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/05/24
 */
@RestController
@RequestMapping("/apicaseresponesetting")
public class ApicaseresponesettingController {
    @Resource
    private ApicaseresponesettingService apicaseresponesettingService;

    @PostMapping
    public Result add(@RequestBody Apicaseresponesetting apicaseresponesetting) {
        Apicaseresponesetting exsistapicaseresponesetting = apicaseresponesettingService.getBy("caseid", apicaseresponesetting.getCaseid());
        if (exsistapicaseresponesetting != null) {
            apicaseresponesettingService.update(apicaseresponesetting);
        } else {
            apicaseresponesettingService.save(apicaseresponesetting);
        }
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        apicaseresponesettingService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Apicaseresponesetting apicaseresponesetting) {
        apicaseresponesettingService.update(apicaseresponesetting);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Apicaseresponesetting apicaseresponesetting = apicaseresponesettingService.getById(id);
        return ResultGenerator.genOkResult(apicaseresponesetting);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Apicaseresponesetting> list = apicaseresponesettingService.listAll();
        PageInfo<Apicaseresponesetting> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Apicaseresponesetting apicaseresponesetting) {
        apicaseresponesettingService.updateDic(apicaseresponesetting);
        return ResultGenerator.genOkResult();
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Apicaseresponesetting> list = apicaseresponesettingService.findDicWithName(param);
        final PageInfo<Apicaseresponesetting> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
