package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shamee.system.dto.req.dept.SysDeptQueryRequest;
import org.shamee.system.dto.resp.dept.SysDeptPageResp;
import org.shamee.system.entity.SysDept;

/**
 * 系统部门Mapper
 *
 * @author shamee
 * @since 2024-01-01
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 查询部门列表
     * @param request
     * @return
     */
    Page<SysDeptPageResp> selectDeptList(
        IPage<SysDeptQueryRequest> page,
        @Param("req") SysDeptQueryRequest request
    );

    /**
     * 根据父部门ID查询所有子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDept> findChildDepts(@Param("parentId") String parentId);

    /**
     * 获取部门及其所有子部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表
     */
    List<String> getDeptAndChildIds(@Param("deptId") String deptId);
}
