package org.shamee.generator.entity;

import lombok.Data;

/**
 * 代码生成配置
 *
 * @author shamee
 * @since 2025-01-01
 */
@Data
public class GeneratorConfig {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 模块名（如：system）
     */
    private String moduleName;

    /**
     * 包名（如：org.shamee.system）
     */
    private String packageName;

    /**
     * 作者
     */
    private String author = "shamee";

    /**
     * 是否生成前端代码
     */
    private Boolean generateFrontend = true;

    /**
     * 前端代码输出路径
     */
    private String frontendOutputPath;

    /**
     * 后端代码输出路径
     */
    private String backendOutputPath;

    /**
     * 表前缀（生成实体类时会去掉，如：sys_user -> User）
     */
    private String tablePrefix;

    /**
     * 是否覆盖已存在的文件
     */
    private Boolean overwrite = false;
}

