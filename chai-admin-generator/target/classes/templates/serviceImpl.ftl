package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${packageName}.dto.req.${entityNameLower}.${entityName}EditRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}QueryRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}SaveRequest;
import ${packageName}.dto.resp.${entityNameLower}.${entityName}PageResp;
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

    @Override
    public PageResult<${entityName}PageResp> get${entityName}Page(PageRequest<${entityName}QueryRequest> pageRequest) {
        Page<${entityName}> page = new Page<>(pageRequest.getPageNo(), pageRequest.getPageSize());
        ${entityName}QueryRequest param = pageRequest.getParam();

        // TODO: 根据查询条件构建查询
        Page<${entityName}> resultPage = this.lambdaQuery()
                .page(page);

        return PageResult.of(resultPage, ${entityName}PageResp::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save${entityName}(${entityName}SaveRequest request) {
        ${entityName} entity = BeanCopierUtils.copy(request, ${entityName}::new);
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit${entityName}(${entityName}EditRequest request) {
        ${entityName} entity = getById(request.getId());
        if (entity == null) {
            throw new RuntimeException("${tableComment!}不存在");
        }

        BeanCopierUtils.copy(request, entity);
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete${entityName}(String id) {
        ${entityName} entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("${tableComment!}不存在");
        }

        return removeById(id);
    }
}
