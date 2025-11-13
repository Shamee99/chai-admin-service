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
            // 如果模块不存在，先创建模块
            if (config.getModuleName().startsWith("chai-admin-")) {
                ensureModuleExists(config.getModuleName());
            }

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

            // 生成 DTO 文件
            result.put("dtoQueryRequest", generateFile("dtoQueryRequest.ftl", dataModel, config, tableInfo, "dtoQueryRequest"));
            result.put("dtoSaveRequest", generateFile("dtoSaveRequest.ftl", dataModel, config, tableInfo, "dtoSaveRequest"));
            result.put("dtoEditRequest", generateFile("dtoEditRequest.ftl", dataModel, config, tableInfo, "dtoEditRequest"));
            result.put("dtoPageResp", generateFile("dtoPageResp.ftl", dataModel, config, tableInfo, "dtoPageResp"));

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
        dataModel.put("date", DateUtil.format(DateUtil.now(), "yyyy-MM-dd"));
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

        // 如果没有指定输出路径，则根据moduleName自动生成到对应模块的src/main/java下
        if (StrUtil.isBlank(basePath)) {
            String userDir = System.getProperty("user.dir");
            String moduleName = config.getModuleName();

            // 如果moduleName是完整模块名（如chai-admin-system），则生成到该模块下
            if (moduleName.startsWith("chai-admin-")) {
                basePath = userDir + "/" + moduleName + "/src/main/java";
            } else {
                // 兼容旧的模块名格式（如system），生成到generated目录
                basePath = userDir + "/generated";
            }
        }

        String packagePath = config.getPackageName().replace(".", "/");
        String entityName = tableInfo.getEntityName();

        return switch (type) {
            case "entity" -> basePath + "/" + packagePath + "/entity/" + entityName + ".java";
            case "mapper" -> basePath + "/" + packagePath + "/mapper/" + GeneratorUtil.getMapperName(entityName) + ".java";
            case "service" -> basePath + "/" + packagePath + "/service/" + GeneratorUtil.getServiceName(entityName) + ".java";
            case "serviceImpl" ->
                    basePath + "/" + packagePath + "/service/impl/" + GeneratorUtil.getServiceImplName(entityName) + ".java";
            case "controller" -> basePath + "/" + packagePath + "/controller/" + GeneratorUtil.getControllerName(entityName) + ".java";
            case "dtoQueryRequest" -> basePath + "/" + packagePath + "/dto/req/" + tableInfo.getEntityNameLower() + "/" + entityName + "QueryRequest.java";
            case "dtoSaveRequest" -> basePath + "/" + packagePath + "/dto/req/" + tableInfo.getEntityNameLower() + "/" + entityName + "SaveRequest.java";
            case "dtoEditRequest" -> basePath + "/" + packagePath + "/dto/req/" + tableInfo.getEntityNameLower() + "/" + entityName + "EditRequest.java";
            case "dtoPageResp" -> basePath + "/" + packagePath + "/dto/resp/" + tableInfo.getEntityNameLower() + "/" + entityName + "PageResp.java";
            default -> basePath + "/" + packagePath + "/" + entityName + ".java";
        };
    }

    /**
     * 确保模块存在，如果不存在则创建
     */
    private void ensureModuleExists(String moduleName) {
        String userDir = System.getProperty("user.dir");
        File moduleDir = new File(userDir, moduleName);

        // 如果模块的 pom.xml 已存在，说明模块已创建
        File pomFile = new File(moduleDir, "pom.xml");
        if (pomFile.exists()) {
            log.info("模块已存在: {}", moduleName);
            return;
        }

        try {
            log.info("开始创建模块: {}", moduleName);

            // 创建模块目录
            if (!moduleDir.exists()) {
                moduleDir.mkdirs();
            }

            // 创建基本目录结构
            File srcMainJava = new File(moduleDir, "src/main/java");
            File srcMainResources = new File(moduleDir, "src/main/resources");
            File srcTestJava = new File(moduleDir, "src/test/java");

            srcMainJava.mkdirs();
            srcMainResources.mkdirs();
            srcTestJava.mkdirs();

            // 创建 pom.xml
            createModulePom(moduleDir, moduleName);

            // 将模块添加到父 pom.xml
            addModuleToParentPom(moduleName);

            log.info("模块创建成功: {}", moduleName);
        } catch (Exception e) {
            log.error("创建模块失败: {}", moduleName, e);
            throw new RuntimeException("创建模块失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建模块的 pom.xml 文件
     */
    private void createModulePom(File moduleDir, String moduleName) {
        try {
            String pomContent = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0"
                             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>org.shamee</groupId>
                            <artifactId>chai-admin-service</artifactId>
                            <version>1.0.0-SNAPSHOT</version>
                        </parent>

                        <artifactId>%s</artifactId>

                        <properties>
                            <maven.compiler.source>21</maven.compiler.source>
                            <maven.compiler.target>21</maven.compiler.target>
                            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                        </properties>

                        <dependencies>
                            <!-- 内部模块依赖 -->
                            <dependency>
                                <groupId>org.shamee</groupId>
                                <artifactId>chai-admin-common</artifactId>
                                <version>${project.version}</version>
                            </dependency>

                            <dependency>
                                <groupId>jakarta.servlet</groupId>
                                <artifactId>jakarta.servlet-api</artifactId>
                            </dependency>

                            <!-- Spring Boot 基础 -->
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter</artifactId>
                            </dependency>

                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-web</artifactId>
                            </dependency>

                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-aop</artifactId>
                            </dependency>

                            <dependency>
                                <groupId>com.fasterxml.jackson.core</groupId>
                                <artifactId>jackson-annotations</artifactId>
                            </dependency>

                            <dependency>
                                <groupId>com.fasterxml.jackson.core</groupId>
                                <artifactId>jackson-databind</artifactId>
                            </dependency>

                            <!-- MyBatis Plus -->
                            <dependency>
                                <groupId>com.baomidou</groupId>
                                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                            </dependency>

                            <!-- 数据库驱动和连接池 -->
                            <dependency>
                                <groupId>org.postgresql</groupId>
                                <artifactId>postgresql</artifactId>
                                <scope>runtime</scope>
                            </dependency>
                            <dependency>
                                <groupId>com.alibaba</groupId>
                                <artifactId>druid-spring-boot-starter</artifactId>
                            </dependency>

                            <!-- Lombok -->
                            <dependency>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                                <optional>true</optional>
                                <scope>provided</scope>
                            </dependency>

                        </dependencies>

                    </project>
                    """.formatted(moduleName);

            File pomFile = new File(moduleDir, "pom.xml");
            FileUtil.writeUtf8String(pomContent, pomFile);

            log.info("pom.xml 创建成功: {}", pomFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("创建 pom.xml 失败", e);
            throw new RuntimeException("创建 pom.xml 失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将模块添加到父 pom.xml 的 modules 列表中
     */
    private void addModuleToParentPom(String moduleName) {
        try {
            String userDir = System.getProperty("user.dir");
            File parentPomFile = new File(userDir, "pom.xml");

            if (!parentPomFile.exists()) {
                log.warn("父 pom.xml 不存在，跳过添加模块");
                return;
            }

            String pomContent = FileUtil.readUtf8String(parentPomFile);

            // 检查模块是否已经存在
            if (pomContent.contains("<module>" + moduleName + "</module>")) {
                log.info("模块已在父 pom.xml 中: {}", moduleName);
                return;
            }

            // 在 </modules> 之前添加新模块
            String moduleEntry = "        <module>" + moduleName + "</module>\n    </modules>";
            pomContent = pomContent.replace("    </modules>", moduleEntry);

            FileUtil.writeUtf8String(pomContent, parentPomFile);

            log.info("已将模块添加到父 pom.xml: {}", moduleName);
        } catch (Exception e) {
            log.error("添加模块到父 pom.xml 失败", e);
            throw new RuntimeException("添加模块到父 pom.xml 失败: " + e.getMessage(), e);
        }
    }
}
