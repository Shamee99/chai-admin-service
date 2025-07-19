package org.shamee.system.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.req.dept.SysDeptEditRequest;
import org.shamee.system.dto.req.dept.SysDeptQueryRequest;
import org.shamee.system.dto.req.dept.SysDeptSaveRequest;
import org.shamee.system.dto.resp.dept.SysDeptPageResp;
import org.shamee.system.dto.resp.dept.SysDeptTreeResp;
import org.shamee.system.entity.SysDept;
import org.shamee.system.service.SysDeptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 部门管理
 * @author shamee
 * @date 2025-07-11
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService sysDeptService;


    @GetMapping("tree")
    public R<List<SysDeptTreeResp>> tree() {
        return R.success(sysDeptService.getDeptTree());
    }


    @PostMapping("page")
    public R<PageResult<SysDeptPageResp>> page(@RequestBody PageRequest<SysDeptQueryRequest> pageRequest) {
        return R.success(sysDeptService.getDeptPage(pageRequest));
    }


    @PostMapping("add")
//    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    public R<Boolean> add(@Valid @RequestBody SysDeptSaveRequest req) {
        return R.success(sysDeptService.saveDept(req));
    }

    @GetMapping("detail/{id}")
    public R<SysDept> detail(@PathVariable("id") String id) {
        return R.success(sysDeptService.getById(id));
    }


    @PutMapping("edit")
    public R<Boolean> edit(@Valid @RequestBody SysDeptEditRequest req) {
        return R.success(sysDeptService.editDept(req));
    }


    @PutMapping("/enable/{id}/{status}")
    public R<Boolean> enable(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        return R.success(sysDeptService.updateDeptStatus(id, status));
    }


    @DeleteMapping("delete/{id}")
    public R<Boolean> delete(@PathVariable("id") String id) {
        return R.success(sysDeptService.deleteDept(id));
    }

}
