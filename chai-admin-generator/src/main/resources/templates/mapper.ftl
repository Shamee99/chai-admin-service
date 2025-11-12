package ${packageName}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ${packageName}.entity.${entityName};

/**
 * ${tableComment!}Mapper
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper
public interface ${mapperName} extends BaseMapper<${entityName}> {

}

