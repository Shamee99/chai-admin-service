package ${packageName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import ${packageName}.dto.req.${entityNameLower}.${entityName}EditRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}QueryRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}SaveRequest;
import ${packageName}.dto.resp.${entityNameLower}.${entityName}PageResp;
import ${packageName}.entity.${entityName};

/**
 * ${tableComment!}服务接口
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${serviceName} extends IService<${entityName}> {

    /**
     * 分页查询${tableComment!}
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<${entityName}PageResp> get${entityName}Page(PageRequest<${entityName}QueryRequest> pageRequest);

    /**
     * 保存${tableComment!}
     *
     * @param request 保存请求
     * @return 是否成功
     */
    boolean save${entityName}(${entityName}SaveRequest request);

    /**
     * 修改${tableComment!}
     *
     * @param request 修改请求
     * @return 是否成功
     */
    boolean edit${entityName}(${entityName}EditRequest request);

    /**
     * 删除${tableComment!}
     *
     * @param id ${tableComment!}ID
     * @return 是否成功
     */
    boolean delete${entityName}(String id);
}
