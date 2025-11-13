package org.shamee.system.dto.resp.onlineuser;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnlineUserPageResp {

    private String id;
    private String username;
    private String realName;
    private String lastLoginIp;
    private String browser;
    private String os;
    private String loginLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

}
