package ${packageName}.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import ${packageName}.dto.req.${entityNameLower}.${entityName}EditRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}QueryRequest;
import ${packageName}.dto.req.${entityNameLower}.${entityName}SaveRequest;
import ${packageName}.dto.resp.${entityNameLower}.${entityName}PageResp;
import ${packageName}.entity.${entityName};
import ${packageName}.service.${serviceName};
import org.springframework.web.bind.annotation.*;

/**
 * ${tableComment!}管理
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@RestController
@RequestMapping("/api/${moduleName}/${entityNameLower}")
@RequiredArgsConstructor
public class ${controllerName} {

    private final ${serviceName} ${entityNameLower}Service;

    /**
     * 分页查询${tableComment!}
     *
     * @param pageRequest 分页查询请求
     * @return 分页结果
     */
    @PostMapping("/page")
    public R<PageResult<${entityName}PageResp>> page(@RequestBody PageRequest<${entityName}QueryRequest> pageRequest) {
        return R.success(${entityNameLower}Service.get${entityName}Page(pageRequest));
    }

    /**
     * 新增${tableComment!}
     *
     * @param request 新增请求
     * @return 操作结果
     */
    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody ${entityName}SaveRequest request) {
        return R.success(${entityNameLower}Service.save${entityName}(request));
    }

    /**
     * 获取${tableComment!}详情
     *
     * @param id ${tableComment!}ID
     * @return ${tableComment!}详情
     */
    @GetMapping("/detail/{id}")
    public R<${entityName}> detail(@PathVariable String id) {
        return R.success(${entityNameLower}Service.getById(id));
    }

    /**
     * 修改${tableComment!}
     *
     * @param request 修改请求
     * @return 操作结果
     */
    @PutMapping("/edit")
    public R<Boolean> edit(@Valid @RequestBody ${entityName}EditRequest request) {
        return R.success(${entityNameLower}Service.edit${entityName}(request));
    }

    /**
     * 删除${tableComment!}
     *
     * @param id ${tableComment!}ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return R.success(${entityNameLower}Service.delete${entityName}(id));
    }
}
