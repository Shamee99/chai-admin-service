package org.shamee.generator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.generator.dto.resp.TableObjsResp;
import org.shamee.generator.entity.GeneratorConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器主服务
 *
 * @author shamee
 * @since 2025-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final DatabaseMetaService databaseMetaService;
    private final BackendCodeGeneratorService backendCodeGeneratorService;
    private final FrontendCodeGeneratorService frontendCodeGeneratorService;

    /**
     * 获取所有表
     */
    public List<TableObjsResp> getAllTables() {
        return databaseMetaService.getAllTables();
    }

    /**
     * 生成代码
     */
    public Map<String, Object> generateCode(GeneratorConfig config) {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("开始生成代码，表名: {}, 模块: {}", config.getTableName(), config.getModuleName());

            // 生成后端代码
            Map<String, String> backendFiles = backendCodeGeneratorService.generateBackendCode(config);
            result.put("backend", backendFiles);

            // 如果需要生成前端代码
            if (config.getGenerateFrontend()) {
                Map<String, String> frontendFiles = frontendCodeGeneratorService.generateFrontendCode(config);
                result.put("frontend", frontendFiles);
            }

            result.put("success", true);
            result.put("message", "代码生成成功");

            log.info("代码生成完成，表名: {}", config.getTableName());
        } catch (Exception e) {
            log.error("代码生成失败", e);
            result.put("success", false);
            result.put("message", "代码生成失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 批量生成代码
     */
    public Map<String, Object> batchGenerateCode(List<GeneratorConfig> configs) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        for (GeneratorConfig config : configs) {
            try {
                generateCode(config);
                successCount++;
            } catch (Exception e) {
                log.error("批量生成代码失败，表名: {}", config.getTableName(), e);
                failCount++;
            }
        }

        result.put("total", configs.size());
        result.put("success", successCount);
        result.put("fail", failCount);
        result.put("message", String.format("批量生成完成，成功: %d, 失败: %d", successCount, failCount));

        return result;
    }
}

