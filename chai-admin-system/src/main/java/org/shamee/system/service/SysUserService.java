package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.user.SysUserEditRequest;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.req.user.SysUserSaveRequest;
import org.shamee.system.dto.resp.user.SysUserDetailResp;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.entity.SysUser;

import java.util.List;

/**
 * 系统用户服务接口
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param request 查询条件
     * @return 用户分页列表
     */
    PageResult<SysUserQueryResp> getUserPage(PageRequest<SysUserQueryRequest> request);

    /**
     * 根据用户ID查询用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUser getUserById(String userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 保存用户信息
     *
     * @param request 用户信息
     * @return 是否成功
     */
    boolean saveUser(SysUserSaveRequest request);

    /**
     * 详情
     * @param id
     * @return
     */
    SysUserDetailResp detail(String id);


    /**
     * 更新用户信息
     * @param request 用户信息
     * @return 是否成功
     */
    boolean editUser(SysUserEditRequest request);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(String userId, Integer status);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean resetUserPassword(String userId);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(String userId);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkUsernameExists(String username, String excludeId);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkEmailExists(String email, String excludeId);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone, String excludeId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getUserPermissions(String userId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param ip 登录IP
     */
    void updateLoginInfo(String userId, String ip);
}