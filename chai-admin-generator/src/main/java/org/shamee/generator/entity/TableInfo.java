package org.shamee.generator.entity;

import lombok.Data;

import java.util.List;

/**
 * 数据库表信息
 *
 * @author shamee
 * @since 2025-01-01
 */
@Data
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 实体类名（首字母大写的驼峰命名）
     */
    private String entityName;

    /**
     * 实体类名（首字母小写的驼峰命名）
     */
    private String entityNameLower;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 作者
     */
    private String author;

    /**
     * 表字段列表
     */
    private List<ColumnInfo> columns;

    /**
     * 主键字段
     */
    private ColumnInfo primaryKey;
}

