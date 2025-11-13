package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    Page<SysDeptPageResp> selectDeptList(IPage<SysDeptQueryRequest> page, @Param("req")SysDeptQueryRequest request);

}