package org.shamee.generator.entity;

import lombok.Data;

/**
 * 数据库列信息
 *
 * @author shamee
 * @since 2025-01-01
 */
@Data
public class ColumnInfo {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列注释
     */
    private String columnComment;

    /**
     * 数据库类型
     */
    private String dataType;

    /**
     * Java类型
     */
    private String javaType;

    /**
     * Java字段名（驼峰命名）
     */
    private String javaField;

    /**
     * TypeScript类型
     */
    private String tsType;

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;

    /**
     * 是否可为空
     */
    private Boolean isNullable;

    /**
     * 列长度
     */
    private Integer columnLength;

    /**
     * 是否自增
     */
    private Boolean isAutoIncrement;

    /**
     * 是否为BaseEntity中的字段
     */
    private Boolean isBaseField = false;
}

