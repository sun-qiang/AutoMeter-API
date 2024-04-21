package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.EnvmachineService;
import com.zoctan.api.service.MacdepunitService;
import com.zoctan.api.service.MachineService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/04/15
 */
@RestController
@RequestMapping("/machine")
public class MachineController {
    @Resource
    private MachineService machineService;

    @Resource
    private MacdepunitService macdepunitService;

    @PostMapping
    public Result add(@RequestBody Machine machine) {
        Condition con=new Condition(Machine.class);
        con.createCriteria().andCondition("projectid = "+machine.getProjectid())
                .andCondition("machinename = '" + machine.getMachinename().replace("'","''") + "'");
        if(machineService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("服务器名已经存在");
        }
        Condition conip=new Condition(Machine.class);
        conip.createCriteria().andCondition("projectid = "+machine.getProjectid())
                .andCondition("ip = '" + machine.getIp() + "'");
        if(machineService.ifexist(conip)>0)
        {
            return ResultGenerator.genFailedResult("IP已经存在");
        } else
        {
            machineService.save(machine);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        //增加判断此服务器当前是否有环境在使用
        List<Macdepunit>macdepunitList= macdepunitService.findmachinebyid(id);
        if(macdepunitList.size()>0)
        {
            return ResultGenerator.genFailedResult("当前服务器在环境部署中还在使用，无法删除！");
        }
        else
        {
            machineService.deleteById(id);
            return ResultGenerator.genOkResult();
        }
    }

    @PutMapping("/detail")
    public Result update(@RequestBody Machine machine) {

        Condition con=new Condition(Machine.class);
        con.createCriteria().andCondition("projectid = "+machine.getProjectid())
                .andCondition("machinename = '" + machine.getMachinename().replace("'","''") + "'")
                .andCondition("id <> " + machine.getId());
        if(machineService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("服务器名已经存在");
        }
        Condition conip=new Condition(Machine.class);
        conip.createCriteria().andCondition("projectid = "+machine.getProjectid())
                .andCondition("ip = '" + machine.getIp() + "'")
                .andCondition("id <> " + machine.getId());
        if(machineService.ifexist(conip)>0)
        {
            return ResultGenerator.genFailedResult("IP已经存在");
        } else
        {
            machineService.updateMachine(machine);
            return ResultGenerator.genOkResult();
        }
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Machine machine = machineService.getById(id);
        return ResultGenerator.genOkResult(machine);
    }

    @GetMapping("/getmachinenum")
    public Result getmachinenum(@RequestParam long projectid) {
        Integer machinenum = machineService.getmachinenum(projectid);
        return ResultGenerator.genOkResult(machinenum);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Machine> list = machineService.listAll();
        PageInfo<Machine> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getmachine")
    public Result listbyenvname(@RequestParam long projectid) {
        Condition con=new Condition(Machine.class);
        con.createCriteria().andCondition("projectid = "+projectid);
        List<Machine> list = machineService.listByCondition(con);
//        List<Machine> list = machineService.listAll();
        return ResultGenerator.genOkResult(list);
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
        final List<Machine> list = this.machineService.findMachineWithName(param);
        final PageInfo<Machine> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
