package org.shamee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.shamee.system.entity.SysRoleDept;

/**
 * 角色部门关联Mapper（数据权限）
 *
 * @author shamee
 * @since 2024-01-01
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

}