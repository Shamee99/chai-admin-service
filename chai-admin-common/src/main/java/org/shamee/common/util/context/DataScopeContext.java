package org.shamee.common.util.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限上下文
 * 使用ThreadLocal存储当前线程的数据权限信息
 *
 * @author shamee
 * @since 2024-01-01
 */
public class DataScopeContext {

    /**
     * 使用ThreadLocal存储部门ID列表
     */
    private static final ThreadLocal<List<String>> DEPT_IDS = new ThreadLocal<>();

    /**
     * 使用ThreadLocal存储数据权限范围
     */
    private static final ThreadLocal<Integer> DATA_SCOPE = new ThreadLocal<>();

    /**
     * 使用ThreadLocal存储用户ID
     */
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    /**
     * 使用ThreadLocal存储部门ID
     */
    private static final ThreadLocal<String> DEPT_ID = new ThreadLocal<>();

    /**
     * 使用ThreadLocal存储是否为管理员
     */
    private static final ThreadLocal<Boolean> IS_ADMIN = new ThreadLocal<>();

    /**
     * 设置部门ID列表
     *
     * @param deptIds 部门ID列表
     */
    public static void setDeptIds(List<String> deptIds) {
        DEPT_IDS.set(deptIds != null ? deptIds : new ArrayList<>());
    }

    /**
     * 获取部门ID列表
     *
     * @return 部门ID列表
     */
    public static List<String> getDeptIds() {
        return DEPT_IDS.get();
    }

    /**
     * 设置数据权限范围
     *
     * @param dataScope 数据权限范围
     */
    public static void setDataScope(Integer dataScope) {
        DATA_SCOPE.set(dataScope);
    }

    /**
     * 获取数据权限范围
     *
     * @return 数据权限范围
     */
    public static Integer getDataScope() {
        return DATA_SCOPE.get();
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public static void setDeptId(String deptId) {
        DEPT_ID.set(deptId);
    }

    /**
     * 获取部门ID
     *
     * @return 部门ID
     */
    public static String getDeptId() {
        return DEPT_ID.get();
    }

    /**
     * 设置是否为管理员
     *
     * @param isAdmin 是否为管理员
     */
    public static void setIsAdmin(Boolean isAdmin) {
        IS_ADMIN.set(isAdmin != null && isAdmin);
    }

    /**
     * 获取是否为管理员
     *
     * @return 是否为管理员
     */
    public static Boolean getIsAdmin() {
        Boolean isAdmin = IS_ADMIN.get();
        return isAdmin != null && isAdmin;
    }

    /**
     * 清除所有数据权限上下文信息
     * 必须在方法执行完成后调用，防止内存泄漏
     */
    public static void clear() {
        DEPT_IDS.remove();
        DATA_SCOPE.remove();
        USER_ID.remove();
        DEPT_ID.remove();
        IS_ADMIN.remove();
    }
}
