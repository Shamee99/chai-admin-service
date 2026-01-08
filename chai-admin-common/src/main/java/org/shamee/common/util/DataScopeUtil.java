package org.shamee.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.constant.enums.DataScopeEnum;
import org.shamee.common.util.context.DataScopeContext;

/**
 * 数据权限SQL拼接工具类
 * 用于根据数据权限上下文动态拼接SQL条件
 *
 * @author shamee
 * @since 2024-01-01
 */
public class DataScopeUtil {

    /**
     * 获取数据权限SQL条件
     * 使用默认的表别名：部门表别名="d", 用户表别名="u"
     * 使用默认的字段名：部门ID字段="dept_id", 用户ID字段="user_id"
     *
     * @return SQL条件对象
     */
    public static DataScopeSqlResult getDataScopeSql() {
        return getDataScopeSql("d", "u", "dept_id", "user_id");
    }

    /**
     * 获取数据权限SQL条件
     *
     * @param deptAlias   部门表别名
     * @param userAlias   用户表别名
     * @param deptColumn  部门ID字段名
     * @param userColumn  用户ID字段名
     * @return SQL条件对象
     */
    public static DataScopeSqlResult getDataScopeSql(
        String deptAlias,
        String userAlias,
        String deptColumn,
        String userColumn
    ) {
        DataScopeSqlResult result = new DataScopeSqlResult();
        List<String> sqlConditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // 检查是否为管理员
        Boolean isAdmin = DataScopeContext.getIsAdmin();
        if (isAdmin != null && isAdmin) {
            // 管理员不进行数据权限过滤
            result.setSqlConditions(Collections.emptyList());
            result.setParams(Collections.emptyList());
            return result;
        }

        // 获取数据权限范围
        Integer dataScope = DataScopeContext.getDataScope();
        if (dataScope == null) {
            // 没有设置数据权限，不进行过滤
            result.setSqlConditions(Collections.emptyList());
            result.setParams(Collections.emptyList());
            return result;
        }

        DataScopeEnum dataScopeEnum = DataScopeEnum.getByCode(dataScope);
        if (dataScopeEnum == null) {
            // 无效的数据权限范围
            result.setSqlConditions(Collections.emptyList());
            result.setParams(Collections.emptyList());
            return result;
        }

        // 根据数据权限范围构建SQL条件
        switch (dataScopeEnum) {
            case ALL:
                // 全部数据权限，不进行过滤
                result.setSqlConditions(Collections.emptyList());
                result.setParams(Collections.emptyList());
                break;
            case CUSTOM:
                // 自定义数据权限，使用配置的部门ID列表
                List<String> customDeptIds = DataScopeContext.getDeptIds();
                if (CollUtil.isNotEmpty(customDeptIds)) {
                    String inClause = buildInClause(
                        deptAlias,
                        deptColumn,
                        customDeptIds.size()
                    );
                    sqlConditions.add(inClause);
                    params.addAll(customDeptIds);
                }
                break;
            case DEPT:
                // 本部门数据权限
                String currentDeptId = DataScopeContext.getDeptId();
                if (StrUtil.isNotBlank(currentDeptId)) {
                    String column = buildColumnName(deptAlias, deptColumn);
                    sqlConditions.add(column + " = ?");
                    params.add(currentDeptId);
                }
                break;
            case DEPT_AND_CHILD:
                // 本部门及以下数据权限
                List<String> deptAndChildIds = DataScopeContext.getDeptIds();
                if (
                    CollUtil.isNotEmpty(deptAndChildIds) &&
                    !"-1".equals(deptAndChildIds.get(0))
                ) {
                    String inClause = buildInClause(
                        deptAlias,
                        deptColumn,
                        deptAndChildIds.size()
                    );
                    sqlConditions.add(inClause);
                    params.addAll(deptAndChildIds);
                }
                break;
            case SELF:
                // 仅本人数据权限
                String userId = DataScopeContext.getUserId();
                if (StrUtil.isNotBlank(userId)) {
                    String column = buildColumnName(userAlias, userColumn);
                    sqlConditions.add(column + " = ?");
                    params.add(userId);
                }
                break;
            default:
                // 未知数据权限，不进行过滤
                result.setSqlConditions(Collections.emptyList());
                result.setParams(Collections.emptyList());
                break;
        }

        result.setSqlConditions(sqlConditions);
        result.setParams(params);
        return result;
    }

    /**
     * 构建完整的SQL WHERE条件
     *
     * @param deptAlias  部门表别名
     * @param userAlias  用户表别名
     * @param deptColumn 部门ID字段名
     * @param userColumn 用户ID字段名
     * @return SQL WHERE条件字符串（不包含WHERE关键字）
     */
    public static String getDataScopeWhereSql(
        String deptAlias,
        String userAlias,
        String deptColumn,
        String userColumn
    ) {
        DataScopeSqlResult result = getDataScopeSql(
            deptAlias,
            userAlias,
            deptColumn,
            userColumn
        );
        List<String> conditions = result.getSqlConditions();

        if (CollUtil.isEmpty(conditions)) {
            return "";
        }

        return String.join(" AND ", conditions);
    }

    /**
     * 构建完整的SQL WHERE条件（使用默认别名）
     *
     * @return SQL WHERE条件字符串（不包含WHERE关键字）
     */
    public static String getDataScopeWhereSql() {
        return getDataScopeWhereSql("d", "u", "dept_id", "user_id");
    }

    /**
     * 构建列名（包含表别名）
     *
     * @param tableAlias 表别名
     * @param columnName 列名
     * @return 完整列名
     */
    private static String buildColumnName(
        String tableAlias,
        String columnName
    ) {
        if (StrUtil.isNotBlank(tableAlias)) {
            return tableAlias + "." + columnName;
        }
        return columnName;
    }

    /**
     * 构建IN子句
     *
     * @param tableAlias  表别名
     * @param columnName  列名
     * @param paramCount  参数数量
     * @return IN子句SQL
     */
    private static String buildInClause(
        String tableAlias,
        String columnName,
        int paramCount
    ) {
        String column = buildColumnName(tableAlias, columnName);
        StringBuilder inClause = new StringBuilder(column);
        inClause.append(" IN (");

        for (int i = 0; i < paramCount; i++) {
            if (i > 0) {
                inClause.append(", ");
            }
            inClause.append("?");
        }

        inClause.append(")");
        return inClause.toString();
    }

    /**
     * 数据权限SQL结果类
     * 封装SQL条件和参数列表
     */
    @Data
    public static class DataScopeSqlResult {

        /**
         * SQL条件列表
         */
        private List<String> sqlConditions;

        /**
         * SQL参数列表
         */
        private List<Object> params;

        /**
         * 检查是否需要进行数据权限过滤
         *
         * @return true-需要过滤，false-不需要过滤
         */
        public boolean needFilter() {
            return CollUtil.isNotEmpty(sqlConditions);
        }

        /**
         * 获取完整的SQL WHERE条件
         *
         * @return SQL WHERE条件字符串（不包含WHERE关键字）
         */
        public String getWhereSql() {
            if (CollUtil.isEmpty(sqlConditions)) {
                return "";
            }
            return String.join(" AND ", sqlConditions);
        }
    }
}
