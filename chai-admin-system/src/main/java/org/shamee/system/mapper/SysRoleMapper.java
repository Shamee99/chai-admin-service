package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shamee.system.entity.SysRole;

import java.util.List;

/**
 * 系统角色Mapper
 *
 * @author shamee
 * @since 2024-01-01
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectByUserId(@Param("userId") String userId);

}