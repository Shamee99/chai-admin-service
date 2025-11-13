package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.system.dto.req.dept.SysDeptEditRequest;
import org.shamee.system.dto.req.dept.SysDeptQueryRequest;
import org.shamee.system.dto.req.dept.SysDeptSaveRequest;
import org.shamee.system.dto.resp.dept.SysDeptPageResp;
import org.shamee.system.dto.resp.dept.SysDeptTreeResp;
import org.shamee.system.entity.SysDept;
import org.shamee.system.entity.SysRoleDept;
import org.shamee.system.mapper.SysDeptMapper;
import org.shamee.system.service.SysDeptService;
import org.shamee.system.service.SysRoleDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统部门服务实现类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysRoleDeptService sysRoleDeptService;

    @Override
    public List<SysDeptTreeResp> getDeptTree() {
        List<SysDept> listed = this.lambdaQuery()
                .select(SysDept::getId, SysDept::getDeptName, SysDept::getParentId)
                .eq(SysDept::getStatus, CommonConstant.Status.ENABLED).list();
        if(CollUtil.isEmpty(listed)) {
            return List.of();
        }
        return buildDeptTreeResult(listed);
    }

    @Override
    public PageResult<SysDeptPageResp> getDeptPage(PageRequest<SysDeptQueryRequest> pageRequest) {
        Page<SysDeptPageResp> deptPageResps = baseMapper.selectDeptList(pageRequest.page(), pageRequest.getParam());
        return PageResult.of(deptPageResps, SysDeptPageResp::new);
    }


    @Override
    public List<SysDept> getChildDepts(String parentId) {
        return this.lambdaQuery().eq(SysDept::getParentId, parentId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDept(SysDeptSaveRequest request) {
        SysDept dept = BeanCopierUtils.copy(request, SysDept::new);

        // 检查部门名称是否重复
        if (checkDeptNameExists(request.getDeptName(), request.getParentId(), null)) {
            throw new RuntimeException("同级部门名称已存在");
        }

        // 检查部门编码是否重复
        if (request.getDeptCode() != null &&
                checkDeptCodeExists(request.getDeptCode(), null)) {
            throw new RuntimeException("部门编码已存在");
        }

        return save(dept);
    }

    @Override
    public boolean editDept(SysDeptEditRequest request) {
        SysDept existDept = getById(request.getId());
        if (existDept == null) {
            throw new RuntimeException("部门不存在");
        }

        // 检查部门名称是否重复
        if (checkDeptNameExists(request.getDeptName(), request.getParentId(), request.getId())) {
            throw new RuntimeException("同级部门名称已存在");
        }

        // 检查部门编码是否重复
        if (request.getDeptCode() != null &&
            checkDeptCodeExists(request.getDeptCode(), request.getId())) {
            throw new RuntimeException("部门编码已存在");
        }
        return updateById(BeanCopierUtils.copy(request, SysDept::new));
    }

    @Override
    public boolean updateDeptStatus(String deptId, Integer status) {
        SysDept dept = new SysDept();
        dept.setId(deptId);
        dept.setStatus(status);
        return updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(String deptId) {
        // 检查是否有子部门
        if (this.lambdaQuery().eq(SysDept::getParentId, deptId).count() > 0) {
            throw new RuntimeException("存在子部门，无法删除");
        }

        // 检查是否有角色关联此部门
        if (sysRoleDeptService.count(Wrappers.lambdaQuery(SysRoleDept.class).eq(SysRoleDept::getDeptId, deptId)) > 0) {
            throw new RuntimeException("该部门已分配给角色，无法删除");
        }
        
        // 删除部门
        this.removeById(deptId);
        // 删除部门相关角色
        sysRoleDeptService.remove(Wrappers.lambdaQuery(SysRoleDept.class).eq(SysRoleDept::getDeptId, deptId));
        return true;
    }

    @Override
    public boolean checkDeptNameExists(String deptName, String parentId, String excludeId) {
        return this.lambdaQuery()
                .eq(SysDept::getParentId, parentId)
                .eq(SysDept::getDeptName, deptName)
                .ne(excludeId != null, SysDept::getId, excludeId).count() > 0;
    }

    @Override
    public boolean checkDeptCodeExists(String deptCode, String excludeId) {
        return this.lambdaQuery()
                .eq(SysDept::getDeptCode, deptCode)
                .ne(excludeId != null, SysDept::getId, excludeId).count() > 0;
    }

    private List<SysDeptTreeResp> buildDeptTreeResult(List<SysDept> depts) {
        if (CollectionUtils.isEmpty(depts)) {
            return List.of();
        }

        List<SysDeptTreeResp> rootDepts = new ArrayList<>();

        for (SysDept dept : depts) {
            if (dept.getParentId() == null || dept.getParentId().equalsIgnoreCase("0")) {
                // 根部门
                dept.setChildren(getChildDepts(dept.getId(), depts));
                rootDepts.add(BeanCopierUtils.copy(dept, SysDeptTreeResp::new));
            }
        }

        return rootDepts;
    }
    
    /**
     * 递归获取子部门
     */
    private List<SysDept> getChildDepts(String parentId, List<SysDept> allDepts) {
        List<SysDept> childDepts = allDepts.stream()
            .filter(dept -> parentId.equals(dept.getParentId()))
            .collect(Collectors.toList());
        
        for (SysDept childDept : childDepts) {
            childDept.setChildren(getChildDepts(childDept.getId(), allDepts));
        }
        
        return childDepts;
    }


    @Override
    public List<String> getDeptAndChildIds(String deptId) {
        List<String> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        
        // 递归获取所有子部门ID
        List<SysDept> childDepts = getChildDepts(deptId);
        if (!CollectionUtils.isEmpty(childDepts)) {
            for (SysDept childDept : childDepts) {
                deptIds.addAll(getDeptAndChildIds(childDept.getId()));
            }
        }
        
        return deptIds;
    }
}