package org.shamee.generator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.annotation.Anonymous;
import org.shamee.common.dto.resp.R;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.generator.dto.BatchGeneratorRequest;
import org.shamee.generator.dto.GeneratorRequest;
import org.shamee.generator.entity.GeneratorConfig;
import org.shamee.generator.service.CodeGeneratorService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器控制器
 *
 * @author shamee
 * @since 2025-01-01
 */
@Slf4j
@Anonymous
@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
public class CodeGeneratorController {

    private final CodeGeneratorService codeGeneratorService;

    /**
     * 获取所有表
     */
    @GetMapping("/tables")
    public R<List<Map<String, Object>>> getAllTables() {
        return R.success(codeGeneratorService.getAllTables());
    }

    /**
     * 生成代码
     */
    @PostMapping("/generate")
    public R<Map<String, Object>> generateCode(@Valid @RequestBody GeneratorRequest request) {
        log.info("收到代码生成请求: {}", request);

        GeneratorConfig config = BeanCopierUtils.copy(request, GeneratorConfig::new);
        Map<String, Object> result = codeGeneratorService.generateCode(config);
        return R.success(result);
    }

    /**
     * 批量生成代码
     */
    @PostMapping("/batch-generate")
    public R<Map<String, Object>> batchGenerateCode(@Valid @RequestBody BatchGeneratorRequest request) {
        log.info("收到批量代码生成请求，数量: {}", request.getConfigs().size());

        List<GeneratorConfig> configs = request.getConfigs().stream()
                .map(req -> {
                    GeneratorConfig config = new GeneratorConfig();
                    BeanUtils.copyProperties(req, config);
                    return config;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = codeGeneratorService.batchGenerateCode(configs);
        return R.success(result);
    }
}

