package org.shamee.generator.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量代码生成请求
 *
 * @author shamee
 * @since 2025-01-01
 */
@Data
public class BatchGeneratorRequest {

    /**
     * 生成配置列表
     */
    @NotEmpty(message = "生成配置列表不能为空")
    @Valid
    private List<GeneratorRequest> configs;
}

