package org.shamee.common.util.net;

import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.net.IPv6Utils;
import org.dromara.hutool.core.net.Ipv4Util;
import org.dromara.hutool.core.net.Ipv6Util;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class IpRegionUtil {
    private Searcher searcher;

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("/ip2region/ip2region.xdb");
            String dbPath = resource.getFile().getPath();

            byte[] cBuff = Searcher.loadContentFromFile(dbPath);
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            throw new RuntimeException("failed to create ip2region searcher: " + e.getMessage(), e);
        }
    }

    /**
     * 根据IP查询地区信息
     * @param ip IP地址
     * @return 地区信息字符串
     */
    public String searchRegion(String ip) {
        try {
            if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                return null;
            }
            return isInternalIp(ip) ? "本地|内网" : searcher.search(ip);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isInternalIp(String ip) {
        return ip.equalsIgnoreCase(Ipv4Util.LOCAL_IP) || Ipv6Util.localIps().contains(ip);
    }
}
