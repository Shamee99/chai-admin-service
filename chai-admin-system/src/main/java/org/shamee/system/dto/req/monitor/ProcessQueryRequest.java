package org.shamee.system.dto.req.monitor;

import lombok.Data;

/**
 * 进程查询参数
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
public class ProcessQueryRequest {
    
    /**
     * 搜索关键词
     */
    private String search;
    
    /**
     * 进程状态过滤
     */
    private String status;
    
    /**
     * 用户过滤
     */
    private String user;
    
    /**
     * 排序字段：cpu、memory、pid、name
     */
    private String sortBy;
    
    /**
     * 排序方向：asc、desc
     */
    private String sortOrder;
}