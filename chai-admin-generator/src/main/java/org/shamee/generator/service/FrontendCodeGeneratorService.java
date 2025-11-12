package org.shamee.generator.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.generator.entity.GeneratorConfig;
import org.shamee.generator.entity.TableInfo;
import org.shamee.generator.util.GeneratorUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端代码生成服务
 *
 * @author shamee
 * @since 2025-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FrontendCodeGeneratorService {

    private final Configuration freemarkerConfig;
    private final DatabaseMetaService databaseMetaService;

    /**
     * 生成前端代码
     */
    public Map<String, String> generateFrontendCode(GeneratorConfig config) {
        Map<String, String> result = new HashMap<>();

        try {
            // 获取表信息
            TableInfo tableInfo = databaseMetaService.getTableInfo(config.getTableName());

            // 设置基本信息
            tableInfo.setPackageName(config.getPackageName());
            tableInfo.setModuleName(config.getModuleName());
            tableInfo.setAuthor(config.getAuthor());
            tableInfo.setEntityName(GeneratorUtil.tableNameToEntityName(
                    config.getTableName(), config.getTablePrefix()));
            tableInfo.setEntityNameLower(GeneratorUtil.getEntityNameLower(tableInfo.getEntityName()));

            // 准备模板数据
            Map<String, Object> dataModel = prepareDataModel(tableInfo);

            // 生成各个文件
            result.put("api", generateFile("api.ftl", dataModel, config, tableInfo, "api"));
            result.put("apiTypes", generateFile("apiTypes.ftl", dataModel, config, tableInfo, "apiTypes"));
            result.put("view", generateFile("view.ftl", dataModel, config, tableInfo, "view"));

            log.info("前端代码生成成功: {}", config.getTableName());
        } catch (Exception e) {
            log.error("前端代码生成失败", e);
            throw new RuntimeException("前端代码生成失败: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * 准备模板数据
     */
    private Map<String, Object> prepareDataModel(TableInfo tableInfo) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("moduleName", tableInfo.getModuleName());
        dataModel.put("tableComment", tableInfo.getTableComment());
        dataModel.put("entityName", tableInfo.getEntityName());
        dataModel.put("entityNameLower", tableInfo.getEntityNameLower());
        dataModel.put("apiFileName", GeneratorUtil.getApiFileName(tableInfo.getEntityName()));
        dataModel.put("columns", tableInfo.getColumns());

        return dataModel;
    }

    /**
     * 生成文件
     */
    private String generateFile(String templateName, Map<String, Object> dataModel,
                                 GeneratorConfig config, TableInfo tableInfo, String type) throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);

        // 确定输出路径
        String outputPath = getOutputPath(config, tableInfo, type);

        // 检查文件是否存在
        File file = new File(outputPath);
        if (file.exists() && !config.getOverwrite()) {
            log.warn("文件已存在，跳过生成: {}", outputPath);
            return outputPath;
        }

        // 确保目录存在
        FileUtil.mkParentDirs(file);

        // 生成文件
        try (Writer writer = new FileWriter(file)) {
            template.process(dataModel, writer);
        }

        log.info("生成文件: {}", outputPath);
        return outputPath;
    }

    /**
     * 获取输出路径
     */
    private String getOutputPath(GeneratorConfig config, TableInfo tableInfo, String type) {
        String basePath = config.getFrontendOutputPath();
        if (StrUtil.isBlank(basePath)) {
            basePath = System.getProperty("user.dir") + "/generated/frontend";
        }

        String moduleName = config.getModuleName();
        String entityNameLower = tableInfo.getEntityNameLower();
        String apiFileName = GeneratorUtil.getApiFileName(tableInfo.getEntityName());

        return switch (type) {
            case "api" -> basePath + "/views/" + moduleName + "/" + entityNameLower + "/api/" + apiFileName + ".ts";
            case "apiTypes" ->
                    basePath + "/views/" + moduleName + "/" + entityNameLower + "/api/" + apiFileName + ".types.ts";
            case "view" ->
                    basePath + "/views/" + moduleName + "/" + entityNameLower + "/" + tableInfo.getEntityName() + "View.vue";
            default -> basePath + "/" + entityNameLower + ".vue";
        };
    }
}

