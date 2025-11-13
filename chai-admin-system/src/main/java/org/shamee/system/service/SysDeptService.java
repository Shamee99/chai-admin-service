package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.dept.SysDeptEditRequest;
import org.shamee.system.dto.req.dept.SysDeptQueryRequest;
import org.shamee.system.dto.req.dept.SysDeptSaveRequest;
import org.shamee.system.dto.resp.dept.SysDeptPageResp;
import org.shamee.system.dto.resp.dept.SysDeptTreeResp;
import org.shamee.system.entity.SysDept;

import java.util.List;

/**
 * 系统部门服务接口
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 获取部门树形列表
     *
     * @return 部门树形列表
     */
    List<SysDeptTreeResp> getDeptTree();


    /**
     * 分页查询部门列表
     *
     * @param pageRequest  分页查询参数
     * @return 部门列表
     */
    PageResult<SysDeptPageResp> getDeptPage(PageRequest<SysDeptQueryRequest> pageRequest);


    /**
     * 根据父部门ID获取子部门列表
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDept> getChildDepts(String parentId);

    /**
     * 保存部门信息
     *
     * @param request 部门信息
     * @return 是否成功
     */
    boolean saveDept(SysDeptSaveRequest request);


    /**
     * 更新部门
     * @param request
     * @return
     */
    boolean editDept(SysDeptEditRequest request);

    /**
     * 更新部门状态
     *
     * @param deptId 部门ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateDeptStatus(String deptId, Integer status);

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     * @return 是否成功
     */
    boolean deleteDept(String deptId);

    /**
     * 检查部门名称是否存在（同一级别下）
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @param excludeId 排除的部门ID
     * @return 是否存在
     */
    boolean checkDeptNameExists(String deptName, String parentId, String excludeId);

    /**
     * 检查部门编码是否存在
     *
     * @param deptCode 部门编码
     * @param excludeId 排除的部门ID
     * @return 是否存在
     */
    boolean checkDeptCodeExists(String deptCode, String excludeId);


    /**
     * 获取部门及其所有子部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表（包含自身和所有子部门）
     */
    List<String> getDeptAndChildIds(String deptId);
}