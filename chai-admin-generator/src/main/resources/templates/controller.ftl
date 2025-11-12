package ${packageName}.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.resp.R;
import org.springframework.web.bind.annotation.*;
import ${packageName}.entity.${entityName};
import ${packageName}.service.${serviceName};

import java.util.List;

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
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page<${entityName}>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<${entityName}> page = new Page<>(current, size);
        return R.success(${entityNameLower}Service.page(page));
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public R<List<${entityName}>> list() {
        return R.success(${entityNameLower}Service.list());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public R<${entityName}> getById(@PathVariable String id) {
        return R.success(${entityNameLower}Service.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    public R<Boolean> save(@RequestBody ${entityName} ${entityNameLower}) {
        return R.success(${entityNameLower}Service.save(${entityNameLower}));
    }

    /**
     * 修改
     */
    @PutMapping
    public R<Boolean> update(@RequestBody ${entityName} ${entityNameLower}) {
        return R.success(${entityNameLower}Service.updateById(${entityNameLower}));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return R.success(${entityNameLower}Service.removeById(id));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestBody List<String> ids) {
        return R.success(${entityNameLower}Service.removeByIds(ids));
    }
}

