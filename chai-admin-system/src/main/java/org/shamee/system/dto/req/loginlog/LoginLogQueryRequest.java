package org.shamee.system.dto.req.loginlog;

import lombok.Data;

@Data
public class LoginLogQueryRequest {

    private String username;
    private String ipAddress;
    private String status;
    private String startTime;
    private String endTime;
}
