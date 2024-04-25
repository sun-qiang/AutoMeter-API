package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.Machine;
import com.zoctan.api.entity.Plannmessage;
import com.zoctan.api.service.PlannmessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2024/04/25
 */
@RestController
@RequestMapping("/plannmessage")
public class PlannmessageController {
    @Resource
    private PlannmessageService plannmessageService;

    @PostMapping
    public Result add(@RequestBody Plannmessage dic) {
        Condition con=new Condition(Plannmessage.class);
        con.createCriteria().andCondition("executeplanid = "+dic.getExecuteplanid())
                .andCondition("messagetype = '" + dic.getMessagetype()+"'")
                .andCondition("hookcontent = '" + dic.getHookcontent().replace("'","''") + "'");
        if(plannmessageService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的通知");
        }else
        {
            plannmessageService.save(dic);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        plannmessageService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Plannmessage plannmessage) {
        plannmessageService.update(plannmessage);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Plannmessage plannmessage = plannmessageService.getById(id);
        return ResultGenerator.genOkResult(plannmessage);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Plannmessage> list = plannmessageService.listAll();
        PageInfo<Plannmessage> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Plannmessage dic) {
        Condition con=new Condition(Plannmessage.class);
        con.createCriteria().andCondition("executeplanid = "+dic.getExecuteplanid())
                .andCondition("messagetype = '" + dic.getMessagetype()+"'")
                .andCondition("hookcontent = '" + dic.getHookcontent().replace("'","''") + "'")
                .andCondition("id <> " + dic.getId());
        if(plannmessageService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已经存在相同的通知");
        }
        else {
            this.plannmessageService.updatePlannmessage(dic);
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
        final List<Plannmessage> list = this.plannmessageService.findPlannmessageWithName(param);
        final PageInfo<Plannmessage> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
