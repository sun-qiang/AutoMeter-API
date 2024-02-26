package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.AssembleDeploy;
import com.zoctan.api.entity.EnviromentAssemble;
import com.zoctan.api.entity.Macdepunit;
import com.zoctan.api.service.EnviromentAssembleService;
import com.zoctan.api.service.MacdepunitService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/05/21
 */
@RestController
@RequestMapping("/macdepunit")
public class MacdepunitController {
    @Resource
    private MacdepunitService macdepunitService;


    @Resource
    private EnviromentAssembleService enviromentAssembleService;

    @PostMapping
    public Result add(@RequestBody Macdepunit macdepunit) {
        Condition depcon = new Condition(Macdepunit.class);
        depcon.createCriteria().andCondition("envid = " + macdepunit.getEnvid()).andCondition("depunitid = " + macdepunit.getDepunitid());
        if (macdepunitService.findmachinenumbyenvidanddeployid(macdepunit.getEnvid(),macdepunit.getDepunitid()) > 0) {
            return ResultGenerator.genFailedResult("此环境已经存在此微服务或者组件");
        } else {
            macdepunitService.save(macdepunit);
            return ResultGenerator.genOkResult();
        }
    }


    @PostMapping("/addenvassemble")
    public Result addenvassemble(@RequestBody AssembleDeploy assembleDeploy) {
        Condition asscon = new Condition(EnviromentAssemble.class);
        asscon.createCriteria().andCondition("assemblename = '" + assembleDeploy.getAssemblename() + "'").andCondition("projectid = " + assembleDeploy.getProjectid());
        if (enviromentAssembleService.ifexist(asscon) > 0) {
            return ResultGenerator.genFailedResult("当前环境已经存在此组件");
        } else {
            EnviromentAssemble enviromentAssemble = new AssembleDeploy();
            enviromentAssemble.setId(null);
            enviromentAssemble.setAssemblename(assembleDeploy.getAssemblename());
            enviromentAssemble.setAssembletype(assembleDeploy.getAssembletype());
            enviromentAssemble.setConnectstr(assembleDeploy.getConnectstr());
            enviromentAssemble.setCreator(assembleDeploy.getCreator());
            enviromentAssemble.setCreateTime(new Date());
            enviromentAssemble.setLastmodifyTime(new Date());
            enviromentAssemble.setProjectid(assembleDeploy.getProjectid());
            enviromentAssemble.setMemo(assembleDeploy.getMemo());
            enviromentAssembleService.save(enviromentAssemble);

            Long AssembleID = enviromentAssemble.getId();
            List<Macdepunit> macdepunits= macdepunitService.getmacdepbyenvidandassid(assembleDeploy.getEnvid(),AssembleID);
//            Condition depcon = new Condition(Macdepunit.class);
//            depcon.createCriteria().andCondition("envid = " + assembleDeploy.getEnvid()).andCondition("assembleid = " + AssembleID);
            if (macdepunits.size() > 0) {
                enviromentAssembleService.deleteById(AssembleID);
                return ResultGenerator.genFailedResult("当前环境已经存在此组件");
            } else {
                Macdepunit macdepunit = new Macdepunit();
                macdepunit.setAssembleid(AssembleID);
                macdepunit.setDepunitid(null);
                macdepunit.setDeployunitname(enviromentAssemble.getAssemblename());
                macdepunit.setCreator(assembleDeploy.getCreator());
                macdepunit.setEnvid(assembleDeploy.getEnvid());
                macdepunit.setEnviromentname(assembleDeploy.getEnviromentname());
                macdepunit.setDomain(assembleDeploy.getDomain());
                macdepunit.setCreateTime(new Date());
                macdepunit.setLastmodifyTime(new Date());
                macdepunit.setMachineid(assembleDeploy.getMachineid());
                macdepunit.setMachinename(assembleDeploy.getMachinename());
                macdepunit.setVisittype(assembleDeploy.getVisittype());
                macdepunit.setAssembletype("组件");
                macdepunitService.save(macdepunit);
            }
        }
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        macdepunitService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Macdepunit macdepunit) {
        macdepunitService.update(macdepunit);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Macdepunit macdepunit = macdepunitService.getById(id);
        return ResultGenerator.genOkResult(macdepunit);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Macdepunit> list = macdepunitService.listAll();
        PageInfo<Macdepunit> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final AssembleDeploy macdepunit) {
        if (macdepunitService.findmachinenumbyenvidanddeployidforup(macdepunit.getEnvid(),macdepunit.getDepunitid(),macdepunit.getId()) > 0) {
            return ResultGenerator.genFailedResult("当前环境已经存在此微服务");
        } else {
            this.macdepunitService.updateMacAndDep(macdepunit);
            return ResultGenerator.genOkResult();
        }
//        Condition assemcon = new Condition(Macdepunit.class);
//        assemcon.createCriteria().andCondition("envid = " + macdepunit.getEnvid()).andCondition("assembleid = " + macdepunit.getAssembleid())
//                .andCondition("id <> " + macdepunit.getId());
//        if (macdepunitService.ifexist(assemcon) > 0) {
//            return ResultGenerator.genFailedResult("此环境已经存在此微服务或者组件");
//        } else {
//            Condition con = new Condition(Macdepunit.class);
//            con.createCriteria().andCondition("envid = " + macdepunit.getEnvid()).andCondition("depunitid = " + macdepunit.getDepunitid())
//                    .andCondition("id <> " + macdepunit.getId());
//            if (macdepunitService.findmachinenumbyenvidanddeployidforup() > 0) {
//                return ResultGenerator.genFailedResult("此环境已经存在此微服务或者组件");
//            } else {
//                this.macdepunitService.updateMacAndDep(macdepunit);
//                return ResultGenerator.genOkResult();
//            }
//        }
    }


    @PutMapping("/detailassemble")
    public Result updateassemble(@RequestBody final AssembleDeploy assembleDeploy) {
        Condition con=new Condition(EnviromentAssemble.class);
        con.createCriteria().andCondition("projectid = "+assembleDeploy.getProjectid())
                .andCondition("assembletype = '" + assembleDeploy.getAssembletype() + "'")
                .andCondition("assemblename = '" + assembleDeploy.getAssemblename().replace("'","''") + "'")
                .andCondition("id <> " + assembleDeploy.getAssembleid());
        if(enviromentAssembleService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("已存在相同类型的组件名");
        }
        else {
            EnviromentAssemble enviromentAssemble = enviromentAssembleService.getById(assembleDeploy.getAssembleid());
            enviromentAssemble.setAssemblename(assembleDeploy.getAssemblename());
            enviromentAssemble.setAssembletype(assembleDeploy.getAssembletype());
            enviromentAssemble.setConnectstr(assembleDeploy.getConnectstr());
            enviromentAssemble.setCreator(assembleDeploy.getCreator());
            enviromentAssemble.setLastmodifyTime(new Date());
            enviromentAssembleService.update(enviromentAssemble);
//            Condition assemcon = new Condition(Macdepunit.class);
//            assemcon.createCriteria().andCondition("envid = " + assembleDeploy.getEnvid()).andCondition("assembleid = " + assembleDeploy.getAssembleid())
//                    .andCondition("id <> " + assembleDeploy.getId());
            if (macdepunitService.findmachinenumbyenvidandassemidforup(assembleDeploy.getEnvid(),assembleDeploy.getAssembleid(),assembleDeploy.getId()) > 0) {
                return ResultGenerator.genFailedResult("此环境已经存在此组件");
            } else {
                AssembleDeploy macdepunit= macdepunitService.findassembledeploybyid(assembleDeploy.getId());
                macdepunit.setMachinename(assembleDeploy.getMachinename());
                macdepunit.setMachineid(assembleDeploy.getMachineid());
                macdepunit.setDeployunitname(assembleDeploy.getAssemblename());
                macdepunit.setDomain(assembleDeploy.getDomain());
                macdepunit.setVisittype(assembleDeploy.getVisittype());
                this.macdepunitService.updateMacAndDep(macdepunit);
                return ResultGenerator.genOkResult();
            }
        }
    }


    @PostMapping("/deleteassemble")
    public Result deleteassemble(@RequestBody AssembleDeploy assembleDeploy) {
        Long assembleid=assembleDeploy.getAssembleid();
        EnviromentAssemble enviromentAssemble= enviromentAssembleService.getById(assembleid);
        if(enviromentAssemble!=null)
        {
            enviromentAssembleService.deleteById(assembleid);
        }
        Long macassenbleid=assembleDeploy.getId();

        List<Macdepunit> macdepunits= macdepunitService.getmacdepbyenvidandassid(assembleDeploy.getEnvid(),macassenbleid);
        //Macdepunit macdepunit=macdepunitService.getById(macassenbleid);
        if(macdepunits.size()>0)
        {
            macdepunitService.deletemacdepbyenvidandassid(assembleDeploy.getEnvid(), macassenbleid);
        }
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
        final List<Macdepunit> list = this.macdepunitService.findMacAndDepWithid(param);
        final PageInfo<Macdepunit> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


    @PostMapping("/findMacAndDepWithEnv")
    public Result findMacAndDepWithEnv(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Macdepunit> list = this.macdepunitService.findMacAndDepWithEnv(param);
        final PageInfo<Macdepunit> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/findMacAndAssembleWithEnv")
    public Result findMacAndAssembleWithEnv(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<AssembleDeploy> list = this.macdepunitService.findMacAndAssembleWithEnv(param);
        final PageInfo<AssembleDeploy> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


}
