package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Dispatch;
import com.zoctan.api.service.DispatchService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2020/11/21
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchController {
    @Resource
    private DispatchService dispatchService;

    @PostMapping
    public Result add(@RequestBody Dispatch dispatch) {
        dispatchService.save(dispatch);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        dispatchService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Dispatch dispatch) {
        dispatchService.update(dispatch);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Dispatch dispatch = dispatchService.getById(id);
        return ResultGenerator.genOkResult(dispatch);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Dispatch> list = dispatchService.listAll();
        PageInfo<Dispatch> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
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
        final List<Dispatch> list = this.dispatchService.findDispatchWithName(param);
        final PageInfo<Dispatch> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/getDispatchWithstatus")
    public Result getDispatchWithstatus(@RequestBody final Map<String, Object> param) {
        Integer page= Integer.parseInt(param.get("page").toString());
        Integer size= Integer.parseInt(param.get("size").toString());
        Long executeplanid = Long.parseLong(param.get("executeplanid").toString());
        Long batchid = Long.parseLong(param.get("batchid").toString());
        String batchname = param.get("batchname").toString();
        PageHelper.startPage(page, size);
        final List<Dispatch> list = this.dispatchService.getDispatchWithstatus(executeplanid,batchname,"已完成");
        final PageInfo<Dispatch> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
