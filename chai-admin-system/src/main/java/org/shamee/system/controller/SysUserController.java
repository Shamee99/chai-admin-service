package org.shamee.system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.req.user.SysUserEditRequest;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.req.user.SysUserSaveRequest;
import org.shamee.system.dto.resp.user.SysUserDetailResp;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户管理
 *
 * @author shamee
 * @since 2025-07-07
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;


    /**
     * 获取用户列表
     * @param pageRequest
     * @return
     */
    @PostMapping("/page")
    public R<PageResult<SysUserQueryResp>> page(@RequestBody PageRequest<SysUserQueryRequest> pageRequest) {
        return R.success(sysUserService.getUserPage(pageRequest));
    }


    /**
     * 新增用户信息
     * @param req
     * @return
     */
    @PostMapping("/add")
//    @PreAuthorize("@ss.hasPermi('system:user:add')")
    public R<Boolean> add(@Valid @RequestBody SysUserSaveRequest req) {
        return R.success(sysUserService.saveUser(req));
    }

    @GetMapping("detail/{id}")
    public R<SysUserDetailResp> detail(@PathVariable String id) {
        return R.success(sysUserService.detail(id));
    }

    /**
     * 更新用户
     * @param req
     * @return
     */
    @PutMapping("/edit")
//    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    public R<Boolean> edit(@Valid @RequestBody SysUserEditRequest req) {
        return R.success(sysUserService.editUser(req));
    }


    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/enable/{id}/{status}")
//    @PreAuthorize("@ss.hasPermi('system:user:status')")
    public R<Boolean> enable(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        return R.success(sysUserService.updateUserStatus(id, status));
    }


    @PutMapping("/resetPassword/{id}")
    public R<Boolean> resetPassword(@PathVariable("id") String id) {
        return R.success(sysUserService.resetUserPassword(id));
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable("id") String id) {
        return R.success(sysUserService.deleteUser(id));
    }

}
