package org.shamee.system.dto.resp.onlineuser;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserStatisticResp {

    private Long total;
    private Long active;
    private Long idle;
    private Long duration;

}
