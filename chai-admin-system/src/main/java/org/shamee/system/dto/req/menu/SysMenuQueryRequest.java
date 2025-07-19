package org.shamee.system.dto.req.menu;


import lombok.Data;

@Data
public class SysMenuQueryRequest {

    private String parentId;
    private String menuName;

    private Integer status;

}
