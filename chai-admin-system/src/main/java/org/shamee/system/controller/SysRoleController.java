package org.shamee.system.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.system.dto.req.menu.SysMenuQueryRequest;
import org.shamee.system.dto.req.role.SysRoleEditRequest;
import org.shamee.system.dto.req.role.SysRoleQueryRequest;
import org.shamee.system.dto.req.role.SysRoleSaveRequest;
import org.shamee.system.dto.resp.menu.SysMenuPageResp;
import org.shamee.system.dto.resp.role.SysRolePageResp;
import org.shamee.system.entity.SysRole;
import org.shamee.system.service.SysMenuService;
import org.shamee.system.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色管理
 * @author shamee
 * @date 2025-07-13
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;


    @PostMapping("page")
    public R<PageResult<SysRolePageResp>> page(@RequestBody PageRequest<SysRoleQueryRequest> pageRequest) {
        return R.success(sysRoleService.getRolePage(pageRequest));
    }

    /**
     * 获取可用的角色，暂时不根据部门来判断
     * @return
     */
    @GetMapping("getEnableList")
    public R<List<SysRolePageResp>> getEnableList() {
        SysRoleQueryRequest request = new SysRoleQueryRequest();
        request.setStatus(CommonConstant.Status.ENABLED);
        return R.success(sysRoleService.getRoleList(request));
    }

    @PostMapping("add")
    public R<Boolean> add(@Valid @RequestBody SysRoleSaveRequest req) {
        return R.success(sysRoleService.saveRole(req));
    }

    @GetMapping("detail/{id}")
    public R<SysRole> detail(@PathVariable("id") String id) {
        return R.success(sysRoleService.getById(id));
    }


    @PutMapping("edit")
    public R<Boolean> edit(@Valid @RequestBody SysRoleEditRequest req) {
        return R.success(sysRoleService.editRole(req));
    }


    @PutMapping("/enable/{id}/{status}")
    public R<Boolean> enable(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        return R.success(sysRoleService.updateRoleStatus(id, status));
    }


    @DeleteMapping("delete/{id}")
    public R<Boolean> delete(@PathVariable("id") String id) {
        return R.success(sysRoleService.deleteRole(id));
    }


    @GetMapping("getPerms/{id}")
    public R<List<String>> getPerms(@PathVariable("id") String id) {
        return R.success(sysRoleService.getRolePermissions(id));
    }

    /**
     * 权限分配
     *
     * @param id
     * @param permissionIds
     * @return
     */
    @PostMapping("assignPerms/{id}")
    public R<Boolean> assignPerms(@PathVariable("id") String id, @RequestBody List<String> permissionIds) {
        return R.success(sysRoleService.assignPerms(id, permissionIds));
    }
}
