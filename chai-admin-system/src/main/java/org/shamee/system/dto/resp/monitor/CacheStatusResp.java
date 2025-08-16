package org.shamee.system.dto.resp.monitor;

import lombok.Data;

@Data
public class CacheStatusResp {

    private boolean connected;
    private String version;
    private Double usedMemoryMB;        // 已使用内存 (MB)
    private Double maxMemoryMB;         // 最大内存 (MB)，null 表示无限制
    private Long keyCount;              // key 数量
    private Long expiredKeys;           // 过期 key 数量
    private Long hits;                  // 命中次数
    private Long totalCommands;         // 总访问次数
    private String hitRate;             // 命中率，格式 "85.00%"

}
