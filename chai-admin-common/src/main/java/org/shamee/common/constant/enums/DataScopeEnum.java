package org.shamee.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限范围枚举
 *
 * @author shamee
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /**
     * 全部数据权限
     */
    ALL(1, "全部数据"),

    /**
     * 自定义数据权限
     */
    CUSTOM(5, "自定义数据"),

    /**
     * 本部门数据权限
     */
    DEPT(2, "本部门数据"),

    /**
     * 本部门及以下数据权限
     */
    DEPT_AND_CHILD(3, "本部门及以下数据"),

    /**
     * 仅本人数据权限
     */
    SELF(4, "仅本人数据");

    /**
     * 数据权限范围值
     */
    private final Integer code;

    /**
     * 数据权限范围描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 代码
     * @return 枚举
     */
    public static DataScopeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DataScopeEnum dataScope : values()) {
            if (dataScope.getCode().equals(code)) {
                return dataScope;
            }
        }
        return null;
    }

    /**
     * 校验是否需要部门ID列表
     *
     * @return true-需要，false-不需要
     */
    public boolean needDeptIds() {
        return this == CUSTOM;
    }

    /**
     * 校验是否需要查询子部门
     *
     * @return true-需要，false-不需要
     */
    public boolean needChildDept() {
        return this == DEPT_AND_CHILD;
    }

    /**
     * 校验是否需要查询本部门
     *
     * @return true-需要，false-不需要
     */
    public boolean needCurrentDept() {
        return this == DEPT || this == DEPT_AND_CHILD;
    }
}
