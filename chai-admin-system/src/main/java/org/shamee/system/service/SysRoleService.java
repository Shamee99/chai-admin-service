package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.role.SysRoleEditRequest;
import org.shamee.system.dto.req.role.SysRoleQueryRequest;
import org.shamee.system.dto.req.role.SysRoleSaveRequest;
import org.shamee.system.dto.resp.role.SysRolePageResp;
import org.shamee.system.entity.SysRole;

import java.util.List;

/**
 * 系统角色服务接口
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param request 查询条件
     * @return 角色分页列表
     */
    PageResult<SysRolePageResp> getRolePage(PageRequest<SysRoleQueryRequest> request);

    /**
     * 获取可用的角色列表
     * @return
     */
    List<SysRolePageResp> getRoleList(SysRoleQueryRequest request);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getRolesByUserId(String userId);

    /**
     * 保存角色信息
     *
     * @param request 角色信息
     * @return 是否成功
     */
    boolean saveRole(SysRoleSaveRequest request);

    /**
     * 编辑角色信息
     *
     * @param request 角色信息
     * @return 是否成功
     */
    boolean editRole(SysRoleEditRequest request);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(String roleId, Integer status);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteRole(String roleId);


    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean checkRoleCodeExists(String roleCode, String excludeId);

    /**
     * 检查角色名称是否存在
     *
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean checkRoleNameExists(String roleName, String excludeId);


    /**
     * 权限分配
     * @param roleId
     * @param permissionIds
     * @return
     */
    boolean assignPerms(String roleId, List<String> permissionIds);



    /**
     * 根据角色id获取权限标识
     * @param roleId
     * @return
     */
    List<String> getRolePermissions(String roleId);

}