package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shamee.system.entity.SysRoleMenu;

/**
 * 角色菜单关联Mapper
 *
 * @author shamee
 * @since 2024-01-01
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    void deleteRoleMenuByRoleId(@Param("roleId") String roleId);

}