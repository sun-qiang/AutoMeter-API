package ${basePackage}.service;

import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.core.service.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

/**
* @author ${author}
* @date ${date}
*/
public interface ${modelNameUpperCamel}Service extends Service<${modelNameUpperCamel}> {
List<${modelNameUpperCamel}> findDicWithName(final Map<String, Object> params);
int ifexist(Condition condition);
void updateDic(${modelNameUpperCamel} params);
}
