package org.shamee.system.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.common.util.context.SecurityUtils;
import org.shamee.system.dto.req.dept.SysDeptQueryRequest;
import org.shamee.system.dto.req.menu.SysMenuEditRequest;
import org.shamee.system.dto.req.menu.SysMenuQueryRequest;
import org.shamee.system.dto.req.menu.SysMenuSaveRequest;
import org.shamee.system.dto.resp.menu.SysMenuPageResp;
import org.shamee.system.dto.resp.menu.SysMenuTreeResp;
import org.shamee.system.dto.resp.user.SysUserDetailResp;
import org.shamee.system.entity.SysMenu;
import org.shamee.system.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 权限菜单管理
 * @author shamee
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("tree")
    public R<List<SysMenuTreeResp>> tree() {
        return R.success(sysMenuService.getMenuTree());
    }

    @PostMapping("page")
    public R<PageResult<SysMenuPageResp>> page(@RequestBody PageRequest<SysMenuQueryRequest> pageRequest) {
        return R.success(sysMenuService.getMenuPage(pageRequest));
    }

    /**
     * 获取路由，过滤按钮数据
     * @return
     */
    @GetMapping("router")
    public R<List<SysMenu>> router() {
        return R.success(sysMenuService.getUserMenuTree(SecurityUtils.getUserId()));
    }

    @PostMapping("add")
    public R<Boolean> add(@Valid @RequestBody SysMenuSaveRequest request) {
        return R.success(sysMenuService.saveMenu(request));
    }

    @PutMapping("edit")
    public R<Boolean> edit(@Valid @RequestBody SysMenuEditRequest request) {
        return R.success(sysMenuService.editMenu(request));
    }

    @GetMapping("detail/{id}")
    public R<SysMenu> detail(@PathVariable String id) {
        return R.success(sysMenuService.getMenuById(id));
    }

    @PutMapping("/enable/{id}/{status}")
    public R<Boolean> enable(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        return R.success(sysMenuService.updateMenuStatus(id, status));
    }


    @DeleteMapping("delete/{id}")
    public R<Boolean> delete(@PathVariable("id") String id) {
        return R.success(sysMenuService.deleteMenu(id));
    }

}
