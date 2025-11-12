package org.shamee.generator.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.generator.entity.ColumnInfo;
import org.shamee.generator.entity.GeneratorConfig;
import org.shamee.generator.entity.TableInfo;
import org.shamee.generator.util.GeneratorUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * 后端代码生成服务
 *
 * @author shamee
 * @since 2025-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackendCodeGeneratorService {

    private final Configuration freemarkerConfig;
    private final DatabaseMetaService databaseMetaService;

    // BaseEntity中的字段，生成时需要排除
    private static final Set<String> BASE_FIELDS = Set.of(
            "id", "create_time", "update_time", "create_by", "update_by", "deleted", "version"
    );

    /**
     * 生成后端代码
     */
    public Map<String, String> generateBackendCode(GeneratorConfig config) {
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

            // 标记BaseEntity字段
            for (ColumnInfo column : tableInfo.getColumns()) {
                column.setIsBaseField(BASE_FIELDS.contains(column.getColumnName()));
            }

            // 准备模板数据
            Map<String, Object> dataModel = prepareDataModel(tableInfo);

            // 生成各个文件
            result.put("entity", generateFile("entity.ftl", dataModel, config, tableInfo, "entity"));
            result.put("mapper", generateFile("mapper.ftl", dataModel, config, tableInfo, "mapper"));
            result.put("service", generateFile("service.ftl", dataModel, config, tableInfo, "service"));
            result.put("serviceImpl", generateFile("serviceImpl.ftl", dataModel, config, tableInfo, "serviceImpl"));
            result.put("controller", generateFile("controller.ftl", dataModel, config, tableInfo, "controller"));

            log.info("后端代码生成成功: {}", config.getTableName());
        } catch (Exception e) {
            log.error("后端代码生成失败", e);
            throw new RuntimeException("后端代码生成失败: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * 准备模板数据
     */
    private Map<String, Object> prepareDataModel(TableInfo tableInfo) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", tableInfo.getPackageName());
        dataModel.put("moduleName", tableInfo.getModuleName());
        dataModel.put("author", tableInfo.getAuthor());
        dataModel.put("date", DateUtil.today());
        dataModel.put("tableName", tableInfo.getTableName());
        dataModel.put("tableComment", tableInfo.getTableComment());
        dataModel.put("entityName", tableInfo.getEntityName());
        dataModel.put("entityNameLower", tableInfo.getEntityNameLower());
        dataModel.put("mapperName", GeneratorUtil.getMapperName(tableInfo.getEntityName()));
        dataModel.put("serviceName", GeneratorUtil.getServiceName(tableInfo.getEntityName()));
        dataModel.put("serviceImplName", GeneratorUtil.getServiceImplName(tableInfo.getEntityName()));
        dataModel.put("controllerName", GeneratorUtil.getControllerName(tableInfo.getEntityName()));
        dataModel.put("columns", tableInfo.getColumns());

        // 检查是否需要导入特定类型
        boolean hasDate = false;
        boolean hasTime = false;
        boolean hasDateTime = false;
        boolean hasBigDecimal = false;

        for (ColumnInfo column : tableInfo.getColumns()) {
            if (!column.getIsBaseField()) {
                switch (column.getJavaType()) {
                    case "LocalDate" -> hasDate = true;
                    case "LocalTime" -> hasTime = true;
                    case "LocalDateTime" -> hasDateTime = true;
                    case "BigDecimal" -> hasBigDecimal = true;
                }
            }
        }

        dataModel.put("hasDate", hasDate);
        dataModel.put("hasTime", hasTime);
        dataModel.put("hasDateTime", hasDateTime);
        dataModel.put("hasBigDecimal", hasBigDecimal);

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
        String basePath = config.getBackendOutputPath();
        if (StrUtil.isBlank(basePath)) {
            basePath = System.getProperty("user.dir") + "/generated";
        }

        String packagePath = config.getPackageName().replace(".", "/");
        String entityName = tableInfo.getEntityName();

        return switch (type) {
            case "entity" -> basePath + "/entity/" + entityName + ".java";
            case "mapper" -> basePath + "/mapper/" + GeneratorUtil.getMapperName(entityName) + ".java";
            case "service" -> basePath + "/service/" + GeneratorUtil.getServiceName(entityName) + ".java";
            case "serviceImpl" ->
                    basePath + "/service/impl/" + GeneratorUtil.getServiceImplName(entityName) + ".java";
            case "controller" -> basePath + "/controller/" + GeneratorUtil.getControllerName(entityName) + ".java";
            default -> basePath + "/" + entityName + ".java";
        };
    }
}

