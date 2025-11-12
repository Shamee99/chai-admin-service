package org.shamee.generator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.generator.entity.ColumnInfo;
import org.shamee.generator.entity.TableInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库元数据服务
 *
 * @author shamee
 * @since 2025-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseMetaService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 获取所有表信息
     */
    public List<Map<String, Object>> getAllTables() {
        String sql = """
                SELECT
                    table_name,
                    obj_description(CAST(table_name AS regclass), 'pg_class') AS table_comment
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_type = 'BASE TABLE'
                ORDER BY table_name
                """;
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 获取表的详细信息
     */
    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);

        // 获取表注释
        String commentSql = """
                SELECT obj_description(CAST(? AS regclass), 'pg_class') AS table_comment
                """;
        Map<String, Object> commentMap = jdbcTemplate.queryForMap(commentSql, tableName);
        tableInfo.setTableComment((String) commentMap.get("table_comment"));

        // 获取列信息
        List<ColumnInfo> columns = getTableColumns(tableName);
        tableInfo.setColumns(columns);

        // 设置主键
        for (ColumnInfo column : columns) {
            if (column.getIsPrimaryKey()) {
                tableInfo.setPrimaryKey(column);
                break;
            }
        }

        return tableInfo;
    }

    /**
     * 获取表的列信息
     */
    private List<ColumnInfo> getTableColumns(String tableName) {
        String sql = """
                SELECT
                    c.column_name,
                    c.data_type,
                    c.character_maximum_length,
                    c.is_nullable,
                    c.column_default,
                    col_description((SELECT oid FROM pg_class WHERE relname = c.table_name), c.ordinal_position) AS column_comment,
                    CASE WHEN pk.column_name IS NOT NULL THEN true ELSE false END AS is_primary_key
                FROM information_schema.columns c
                LEFT JOIN (
                    SELECT ku.column_name
                    FROM information_schema.table_constraints tc
                    JOIN information_schema.key_column_usage ku
                        ON tc.constraint_name = ku.constraint_name
                    WHERE tc.table_name = ?
                      AND tc.constraint_type = 'PRIMARY KEY'
                ) pk ON c.column_name = pk.column_name
                WHERE c.table_name = ?
                ORDER BY c.ordinal_position
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, tableName, tableName);
        List<ColumnInfo> columns = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            ColumnInfo column = new ColumnInfo();
            String columnName = (String) row.get("column_name");
            String dataType = (String) row.get("data_type");
            String columnDefault = (String) row.get("column_default");

            column.setColumnName(columnName);
            column.setDataType(dataType);
            column.setColumnComment((String) row.get("column_comment"));
            column.setIsNullable("YES".equals(row.get("is_nullable")));
            column.setIsPrimaryKey((Boolean) row.get("is_primary_key"));
            column.setColumnLength((Integer) row.get("character_maximum_length"));
            column.setIsAutoIncrement(columnDefault != null && columnDefault.contains("nextval"));

            // 转换为Java字段名（驼峰命名）
            column.setJavaField(StrUtil.toCamelCase(columnName));

            // 转换为Java类型
            column.setJavaType(convertToJavaType(dataType));

            // 转换为TypeScript类型
            column.setTsType(convertToTsType(dataType));

            columns.add(column);
        }

        return columns;
    }

    /**
     * 将数据库类型转换为Java类型
     */
    private String convertToJavaType(String dbType) {
        return switch (dbType.toLowerCase()) {
            case "int", "int2", "int4", "integer", "smallint" -> "Integer";
            case "int8", "bigint" -> "Long";
            case "numeric", "decimal" -> "BigDecimal";
            case "float4", "real" -> "Float";
            case "float8", "double precision" -> "Double";
            case "bool", "boolean" -> "Boolean";
            case "date" -> "LocalDate";
            case "time", "time without time zone" -> "LocalTime";
            case "timestamp", "timestamp without time zone", "timestamptz", "timestamp with time zone" ->
                    "LocalDateTime";
            case "varchar", "char", "text", "character varying", "character" -> "String";
            case "bytea" -> "byte[]";
            case "json", "jsonb" -> "String";
            case "uuid" -> "String";
            default -> "String";
        };
    }

    /**
     * 将数据库类型转换为TypeScript类型
     */
    private String convertToTsType(String dbType) {
        return switch (dbType.toLowerCase()) {
            case "int", "int2", "int4", "int8", "integer", "smallint", "bigint",
                 "numeric", "decimal", "float4", "float8", "real", "double precision" -> "number";
            case "bool", "boolean" -> "boolean";
            case "date", "time", "timestamp", "time without time zone",
                 "timestamp without time zone", "timestamptz", "timestamp with time zone" -> "string";
            case "varchar", "char", "text", "character varying", "character",
                 "json", "jsonb", "uuid" -> "string";
            case "bytea" -> "Blob";
            default -> "any";
        };
    }
}

