package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.TestsceneDispatch;
import com.zoctan.api.service.TestsceneDispatchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author SeasonFan
* @date 2024/07/07
*/
@RestController
@RequestMapping("/testscene/dispatch")
public class TestsceneDispatchController {
@Resource
private TestsceneDispatchService testsceneDispatchService;

@PostMapping
public Result add(@RequestBody TestsceneDispatch testsceneDispatch) {
testsceneDispatchService.save(testsceneDispatch);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
testsceneDispatchService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody TestsceneDispatch testsceneDispatch) {
testsceneDispatchService.update(testsceneDispatch);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
TestsceneDispatch testsceneDispatch = testsceneDispatchService.getById(id);
return ResultGenerator.genOkResult(testsceneDispatch);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<TestsceneDispatch> list = testsceneDispatchService.listAll();
PageInfo<TestsceneDispatch> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}

@PutMapping("/detail")
public Result updateDeploy(@RequestBody final TestsceneDispatch recipe) {

Condition con=new Condition(TestsceneDispatch.class);
con.createCriteria().andCondition("recipename = '" + recipe.getBatchname().replace("'","''") + "'")
.andCondition("id <> " + recipe.getId());;
if(testsceneDispatchService.ifexist(con)>0)
{
return ResultGenerator.genFailedResult("该处方已经存在");
}
else {
testsceneDispatchService.updateDic(recipe);
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
final List<TestsceneDispatch> list = testsceneDispatchService.findDicWithName(param);
final PageInfo<TestsceneDispatch> pageInfo = new PageInfo<>(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
