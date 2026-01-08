package org.shamee.system.aspect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.hutool.core.collection.CollUtil;
import org.shamee.common.annotation.DataScope;
import org.shamee.common.constant.enums.DataScopeEnum;
import org.shamee.common.dto.resp.UserPrincipal;
import org.shamee.common.util.context.DataScopeContext;
import org.shamee.common.util.context.SecurityUtils;
import org.shamee.system.entity.SysDept;
import org.shamee.system.service.SysDeptService;
import org.springframework.stereotype.Component;

/**
 * 数据权限切面
 * 处理@DataScope注解，实现数据权限过滤
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataScopeAspect {

    private final SysDeptService sysDeptService;

    /**
     * 处理@DataScope注解的方法
     *
     * @param point 切点
     * @return 方法执行结果
     * @throws Throwable 方法执行异常
     */
    @Around("@annotation(org.shamee.common.annotation.DataScope)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 1. 获取当前用户信息
        UserPrincipal userPrincipal = SecurityUtils.getUserInfo();
        if (userPrincipal == null) {
            log.warn("当前用户信息为空，跳过数据权限过滤");
            return point.proceed();
        }

        // 3. 检查是否为超级管理员
        if (userPrincipal.isSuperAdmin()) {
            log.debug("当前用户为超级管理员，跳过数据权限过滤");
            return point.proceed();
        }

        try {
            // 4. 获取用户的数据权限信息
            Integer dataScopeValue = userPrincipal.getDataScope();
            String userId = userPrincipal.getUserId();
            String deptId = userPrincipal.getDeptId();

            // 5. 设置数据权限上下文
            DataScopeContext.setDataScope(dataScopeValue);
            DataScopeContext.setUserId(userId);
            DataScopeContext.setDeptId(deptId);
            DataScopeContext.setIsAdmin(false);

            // 6. 根据数据权限范围计算部门ID列表
            List<String> deptIds = calculateDeptIds(
                dataScopeValue,
                userId,
                deptId,
                userPrincipal.getDataScopeDeptIds()
            );
            DataScopeContext.setDeptIds(deptIds);

            log.debug(
                "数据权限过滤 - 用户:{}, 数据权限范围:{}, 过滤部门:{}",
                userPrincipal.getUsername(),
                DataScopeEnum.getByCode(dataScopeValue),
                deptIds
            );

            // 7. 执行目标方法
            return point.proceed();
        } finally {
            // 8. 清理数据权限上下文，防止内存泄漏
            DataScopeContext.clear();
        }
    }

    /**
     * 根据数据权限范围计算需要过滤的部门ID列表
     *
     * @param dataScopeValue 数据权限范围值
     * @param userId 用户ID
     * @param deptId 部门ID
     * @param customDeptIds 自定义部门ID列表（自定义数据权限时使用）
     * @return 部门ID列表
     */
    private List<String> calculateDeptIds(
        Integer dataScopeValue,
        String userId,
        String deptId,
        List<String> customDeptIds
    ) {
        DataScopeEnum dataScopeEnum = DataScopeEnum.getByCode(dataScopeValue);
        if (dataScopeEnum == null) {
            log.warn("数据权限范围值无效: {}", dataScopeValue);
            return Collections.emptyList();
        }

        List<String> deptIds = new ArrayList<>();

        switch (dataScopeEnum) {
            case ALL:
                // 全部数据权限，不进行部门过滤
                deptIds = Collections.emptyList();
                break;
            case CUSTOM:
                // 自定义数据权限，使用用户配置的部门ID列表
                deptIds =
                    customDeptIds != null
                        ? new ArrayList<>(customDeptIds)
                        : Collections.emptyList();
                break;
            case DEPT:
                // 本部门数据权限
                if (deptId != null && !deptId.isEmpty()) {
                    deptIds = Collections.singletonList(deptId);
                }
                break;
            case DEPT_AND_CHILD:
                // 本部门及以下数据权限，递归查询子部门
                if (deptId != null && !deptId.isEmpty()) {
                    deptIds = getDeptAndChildIds(deptId);
                }
                break;
            case SELF:
                // 仅本人数据权限，不查询部门数据，在SQL中通过user_id过滤
                deptIds = Collections.singletonList("-1"); // 特殊标识，表示只查询本人数据
                break;
            default:
                log.warn("未知的数据权限范围: {}", dataScopeEnum);
                deptIds = Collections.emptyList();
                break;
        }

        return deptIds;
    }

    /**
     * 获取指定部门及其所有子部门的ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表
     */
    private List<String> getDeptAndChildIds(String deptId) {
        List<String> deptIds = new ArrayList<>();
        deptIds.add(deptId);

        try {
            // 递归查询所有子部门
            List<SysDept> childDepts = sysDeptService.getChildDepts(deptId);
            if (CollUtil.isNotEmpty(childDepts)) {
                List<String> childIds = childDepts
                    .stream()
                    .map(SysDept::getId)
                    .collect(Collectors.toList());
                deptIds.addAll(childIds);
            }
        } catch (Exception e) {
            log.error("查询子部门失败 - deptId: {}", deptId, e);
        }

        return deptIds;
    }
}
