package org.shamee.system.dto.req.onlineuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineUserQueryRequest {

    private String username;
    private String ip;

    private List<String> userIds;
}
