package ${basePackage}.controller;

import ${basePackage}.core.response.Result;
import ${basePackage}.core.response.ResultGenerator;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author ${author}
* @date ${date}
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {
@Resource
private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

@PostMapping("/add")
public Result add(${modelNameUpperCamel} ${modelNameLowerCamel}) {
${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
return ResultGenerator.genOkResult();
}

@PostMapping("/delete")
public Result delete(@RequestParam Long id) {
${modelNameLowerCamel}Service.deleteById(id);
return ResultGenerator.genOkResult();
}

@PostMapping("/update")
public Result update(${modelNameUpperCamel} ${modelNameLowerCamel}) {
${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
return ResultGenerator.genOkResult();
}

@PostMapping("/detail")
public Result detail(@RequestParam Long id) {
${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.getById(id);
return ResultGenerator.genOkResult(${modelNameLowerCamel});
}

@PostMapping("/list")
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.listAll();
PageInfo<${modelNameUpperCamel}> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}

@PutMapping("/detail")
public Result updateDeploy(@RequestBody final ${modelNameUpperCamel} recipe) {

Condition con=new Condition(${modelNameUpperCamel}.class);
con.createCriteria().andCondition("recipename = '" + recipe.getRecipename().replace("'","''") + "'")
.andCondition("id <> " + recipe.getId());;
if(${modelNameLowerCamel}Service.ifexist(con)>0)
{
return ResultGenerator.genFailedResult("该处方已经存在");
}
else {
${modelNameLowerCamel}Service.updateDic(recipe);
return ResultGenerator.genOkResult();
}
}
/**
* 输入框查询
*/
@PostMapping("/search")
public Result search(@RequestBody final Map
<String, Object> param) {
Integer page= Integer.parseInt(param.get("page").toString());
Integer size= Integer.parseInt(param.get("size").toString());
PageHelper.startPage(page, size);
final List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findDicWithName(param);
final PageInfo<${modelNameUpperCamel}> pageInfo = new PageInfo<>(list);
return ResultGenerator.genOkResult(pageInfo);
}

}
