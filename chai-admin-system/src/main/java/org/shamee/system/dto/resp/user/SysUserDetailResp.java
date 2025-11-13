package org.shamee.system.dto.resp.user;


import lombok.Data;

import java.util.List;

@Data
public class SysUserDetailResp {

    private String id;
    private String username;
    private String nickname;
    private String realName;
    private Integer gender;
    private String deptId;
    private Integer status;
    private String email;
    private String phone;
    private List<String> roleIds;

}
