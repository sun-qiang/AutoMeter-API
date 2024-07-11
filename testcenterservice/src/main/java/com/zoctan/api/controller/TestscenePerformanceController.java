package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.TestscenePerformance;
import com.zoctan.api.service.TestscenePerformanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/08
*/
@RestController
@RequestMapping("/testscene/performance")
public class TestscenePerformanceController {
@Resource
private TestscenePerformanceService testscenePerformanceService;

@PostMapping
public Result add(@RequestBody TestscenePerformance testscenePerformance) {
testscenePerformanceService.save(testscenePerformance);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
testscenePerformanceService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody TestscenePerformance testscenePerformance) {
testscenePerformanceService.update(testscenePerformance);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
TestscenePerformance testscenePerformance = testscenePerformanceService.getById(id);
return ResultGenerator.genOkResult(testscenePerformance);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<TestscenePerformance> list = testscenePerformanceService.listAll();
PageInfo<TestscenePerformance> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}

@PutMapping("/detail")
public Result updateDeploy(@RequestBody final TestscenePerformance recipe) {

Condition con=new Condition(TestscenePerformance.class);
con.createCriteria().andCondition("recipename = '" + recipe.getScenename().replace("'","''") + "'")
.andCondition("id <> " + recipe.getId());;
if(testscenePerformanceService.ifexist(con)>0)
{
return ResultGenerator.genFailedResult("该处方已经存在");
}
else {
testscenePerformanceService.updateDic(recipe);
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
final List<TestscenePerformance> list = testscenePerformanceService.findDicWithName(param);
final PageInfo<TestscenePerformance> pageInfo = new PageInfo<>(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
