package org.shamee.system.dto.resp.user;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.shamee.common.db.typehandler.ArrayToListTypeHandler;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysUserQueryResp {

    private String id;
    private String avatar;
    private String username;
    private String nickname;
    private String deptName;
    private Integer status;
    private String email;
    private String phone;

    /**
     * 拥有角色名称
     */
    @TableField(typeHandler = ArrayToListTypeHandler.class)
    private List<String> roles;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

}
