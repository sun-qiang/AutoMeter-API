package ${basePackage}.service.impl;

import ${basePackage}.mapper.${modelNameUpperCamel}Mapper;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import tk.mybatis.mapper.entity.Condition;
import java.util.List;
import java.util.Map;

/**
* @author ${author}
* @date ${date}
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {
@Resource
private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

@Override
public List<${modelNameUpperCamel}> findDicWithName(Map<String, Object> params) {
return ${modelNameLowerCamel}Mapper.findDicWithName(params);
}

@Override
public int ifexist(Condition con) {
return countByCondition(con);
}


@Override
public void updateDic(${modelNameUpperCamel} params) {
${modelNameLowerCamel}Mapper.updateDic(params);
}

}
