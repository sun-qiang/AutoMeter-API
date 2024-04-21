package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Dbvariables;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.Project;
import com.zoctan.api.service.ProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/03/27
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    @PostMapping
    public Result add(@RequestBody Project project) {

        Condition con=new Condition(Project.class);
        con.createCriteria().andCondition("projectname = '" + project.getProjectname().replace("'","''") + "'");
        if(projectService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("项目，迭代名已经存在");
        }
        else
        {
            projectService.save(project);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Project project) {
        projectService.update(project);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Project project = projectService.getById(id);
        return ResultGenerator.genOkResult(project);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Project> list = projectService.listAll();
        PageInfo<Project> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Project dic) {
        Condition con=new Condition(Project.class);
        con.createCriteria().andCondition("projectname = '" + dic.getProjectname().replace("'","''") + "'").andCondition("id <> " + dic.getId());
        if(projectService.ifexist(con)>0)
        {
            return ResultGenerator.genFailedResult("项目，迭代名已经存在");
        }
        else {

            this.projectService.updateProject(dic);
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
        final List<Project> list = this.projectService.findProjectWithName(param);
        final PageInfo<Project> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
