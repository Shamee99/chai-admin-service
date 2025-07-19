package org.shamee.system.dto.resp.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;


@Data
public class SysMenuTreeResp {

    private String id;
    private String parentId;
    private String menuName;
    private Integer menuType;
    private String icon;

    private List<SysMenuTreeResp> children;
}
