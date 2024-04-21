package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Api;
import com.zoctan.api.entity.Deployunit;
import com.zoctan.api.service.ApiService;
import com.zoctan.api.service.DeployunitService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Zoctan
 * @date 2020/04/17
 */
@RestController
@RequestMapping("/deployunit")
public class DeployunitController {
    @Resource
    private DeployunitService deployunitService;

    @Resource
    private ApiService apiService;

    @PostMapping
    public Result add(@RequestBody Deployunit deployunit) {
        Condition con=new Condition(Deployunit.class);
        con.createCriteria().andCondition("projectid = "+deployunit.getProjectid())
                .andCondition("deployunitname = '" + deployunit.getDeployunitname().replace("'","''") + "'");
        if(deployunitService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("此微服务已经存在");
        }
        else
        {
            deployunitService.save(deployunit);
            return ResultGenerator.genOkResult();
        }

    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        List<Api> apiList= apiService.getapibydeployunitid(id);
        if(apiList.size()>0)
        {
            return ResultGenerator.genFailedResult("当前微服务还存在API，无法删除！");
        }
        else
        {
            deployunitService.deleteById(id);
            return ResultGenerator.genOkResult();
        }
    }

    @PatchMapping
    public Result update(@RequestBody Deployunit deployunit) {
        deployunitService.update(deployunit);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Deployunit deployunit = deployunitService.getById(id);
        return ResultGenerator.genOkResult(deployunit);
    }

    @GetMapping("/getdeploynum")
    public Result getdeploynum(@RequestParam long projectid) {
        Integer deployunitnum = deployunitService.getdeploynum(projectid);
        return ResultGenerator.genOkResult(deployunitnum);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Deployunit> list = deployunitService.listAll();
        PageInfo<Deployunit> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getdeplist")
    public Result listall(@RequestParam long projectid) {
        Condition con=new Condition(Deployunit.class);
        con.createCriteria().andCondition("projectid = "+projectid);
        List<Deployunit> list = deployunitService.listByCondition(con);
        //List<Deployunit> list = deployunitService.listAll();
        return ResultGenerator.genOkResult(list);
    }


    @GetMapping("/getstaticsdeploynames")
    public Result getstaticsdeploynames(@RequestParam long projectid) {
        List<String> list = deployunitService.getstaticsdeploynames(projectid);
        return ResultGenerator.genOkResult(list);
    }

    @GetMapping("/findDeployNameValueWithCode")
    public Result findDeployNameValueWithCode(@RequestParam long deployunitid) {
        Deployunit dep = deployunitService.getBy("id",deployunitid);
//        Deployunit dep = deployunitService.findDeployNameValueWithCode(deployunitid);
        return ResultGenerator.genOkResult(dep);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Deployunit deployunit) {
        Condition con=new Condition(Deployunit.class);
        con.createCriteria().andCondition("projectid = "+deployunit.getProjectid())
                .andCondition("deployunitname = '" + deployunit.getDeployunitname().replace("'","''") + "'")
                .andCondition("id <> " + deployunit.getId());
        if(deployunitService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("此微服务已经存在");
        }
        else
        {
            this.deployunitService.updateDeploy(deployunit);
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
        long accountid = Long.parseLong(param.get("accountId").toString());
        if (accountid == 1) {
            param.put("creator", null);
            param.put("projectid", null);
        }
        PageHelper.startPage(page, size);
        final List<Deployunit> list = this.deployunitService.findDeployWithName(param);
        final PageInfo<Deployunit> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
