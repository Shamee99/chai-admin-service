package org.shamee.system.dto.resp.monitor;

import lombok.Data;

@Data
public class CachePageResp {

    private String key;
    private String type;        // 数据类型：string, hash, list, set, zset
    private Long ttl;           // 剩余过期时间（秒），-1 表示永不过期，-2 表示已过期或不存在
    private Long size;          // 大小（根据类型估算）

}
