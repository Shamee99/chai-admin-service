package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ${packageName}.entity.${entityName};
import ${packageName}.mapper.${mapperName};
import ${packageName}.service.${serviceName};

/**
 * ${tableComment!}服务实现
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ${serviceImplName} extends ServiceImpl<${mapperName}, ${entityName}> implements ${serviceName} {

}

