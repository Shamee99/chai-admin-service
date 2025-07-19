package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.entity.SysUser;

/**
 * 系统用户Mapper
 *
 * @author shamee
 * @since 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 获取用户列表
     * @param page
     * @param request
     * @return
     */
    Page<SysUserQueryResp> selectUserList(IPage<SysUserQueryRequest> page, @Param("req")SysUserQueryRequest request);

}