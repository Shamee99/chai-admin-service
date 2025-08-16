package org.shamee.system.dto.resp.loginlog;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.dromara.hutool.core.date.DateFormatPool;

import java.time.LocalDateTime;

@Data
public class LoginLogPageResp {

    private String username;
    private String ipaddr;
    private String loginLocation;
    private String browser;
    private String os;
    private String msg;

    @JsonFormat(pattern = DateFormatPool.NORM_DATETIME_PATTERN)
    private LocalDateTime loginTime;
    private Integer status;

}
