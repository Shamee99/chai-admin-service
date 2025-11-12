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
            result.put("componentAdd", generateFile("componentAdd.ftl", dataModel, config, tableInfo, "componentAdd"));
            result.put("componentEdit", generateFile("componentEdit.ftl", dataModel, config, tableInfo, "componentEdit"));

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

        // 从moduleName中提取实际的模块名（去掉chai-admin-前缀）
        String actualModuleName = tableInfo.getModuleName();
        if (actualModuleName.startsWith("chai-admin-")) {
            actualModuleName = actualModuleName.substring("chai-admin-".length());
        }

        dataModel.put("moduleName", actualModuleName);
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

        // 如果没有指定输出路径，则根据moduleName自动生成到前端项目的src/views下
        if (StrUtil.isBlank(basePath)) {
            String userDir = System.getProperty("user.dir");
            String moduleName = config.getModuleName();

            // 如果moduleName是完整模块名（如chai-admin-system），则生成到前端项目
            if (moduleName.startsWith("chai-admin-")) {
                // 假设前端项目在同级目录的chai-vue3-element下
                basePath = userDir.replace("chai-admin-service", "chai-vue3-element") + "/src";
            } else {
                // 兼容旧的模块名格式，生成到generated目录
                basePath = userDir + "/generated/frontend";
            }
        }

        // 从moduleName中提取实际的模块名（去掉chai-admin-前缀）
        String actualModuleName = config.getModuleName();
        if (actualModuleName.startsWith("chai-admin-")) {
            actualModuleName = actualModuleName.substring("chai-admin-".length());
        }

        String entityNameLower = tableInfo.getEntityNameLower();
        String apiFileName = GeneratorUtil.getApiFileName(tableInfo.getEntityName());

        return switch (type) {
            case "api" -> basePath + "/views/" + actualModuleName + "/" + entityNameLower + "/api/" + apiFileName + ".ts";
            case "apiTypes" ->
                    basePath + "/views/" + actualModuleName + "/" + entityNameLower + "/api/" + apiFileName + ".types.ts";
            case "view" ->
                    basePath + "/views/" + actualModuleName + "/" + entityNameLower + "/" + tableInfo.getEntityName() + "View.vue";
            case "componentAdd" ->
                    basePath + "/views/" + actualModuleName + "/" + entityNameLower + "/components/" + tableInfo.getEntityName() + "Add.vue";
            case "componentEdit" ->
                    basePath + "/views/" + actualModuleName + "/" + entityNameLower + "/components/" + tableInfo.getEntityName() + "Edit.vue";
            default -> basePath + "/" + entityNameLower + ".vue";
        };
    }
}

