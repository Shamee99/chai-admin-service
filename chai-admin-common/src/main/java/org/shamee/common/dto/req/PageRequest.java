package org.shamee.common.dto.req;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询请求基类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class PageRequest<T> {

    /**
     * 当前页码
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方式 asc/desc
     */
    private String order = "desc";

    /**
     * 关键词搜索
     */
    private T param;

    public IPage<T> page() {
        return new Page<>(pageNo, pageSize);
    }
}