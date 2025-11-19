package org.shamee.generator.util;


import org.dromara.hutool.core.text.StrUtil;

/**
 * 代码生成工具类
 *
 * @author shamee
 * @since 2025-01-01
 */
public class GeneratorUtil {

    /**
     * 表名转换为实体类名
     * 例如：sys_user -> SysUser, user -> User
     */
    public static String tableNameToEntityName(String tableName, String tablePrefix) {
        if (StrUtil.isNotBlank(tablePrefix) && tableName.startsWith(tablePrefix)) {
            tableName = tableName.substring(tablePrefix.length());
        }
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
    }

    /**
     * 获取首字母小写的实体类名
     */
    public static String getEntityNameLower(String entityName) {
        return StrUtil.lowerFirst(entityName);
    }

    /**
     * 获取请求对象名称
     */
    public static String getRequestName(String entityName, String suffix) {
        return entityName + suffix;
    }

    /**
     * 获取响应对象名称
     */
    public static String getResponseName(String entityName, String suffix) {
        return entityName + suffix;
    }

    /**
     * 获取Service接口名
     */
    public static String getServiceName(String entityName) {
        return entityName + "Service";
    }

    /**
     * 获取Service实现类名
     */
    public static String getServiceImplName(String entityName) {
        return entityName + "ServiceImpl";
    }

    /**
     * 获取Mapper接口名
     */
    public static String getMapperName(String entityName) {
        return entityName + "Mapper";
    }

    /**
     * 获取Controller类名
     */
    public static String getControllerName(String entityName) {
        return entityName + "Controller";
    }

    /**
     * 获取前端API文件名
     */
    public static String getApiFileName(String entityName) {
        return StrUtil.toSymbolCase(entityName, '-').toLowerCase();
    }

    /**
     * 获取前端View文件名
     */
    public static String getViewFileName(String entityName) {
        return entityName + "View";
    }
}

