package org.shamee.system.dto.req.dept;


import lombok.Data;

@Data
public class SysDeptQueryRequest {

    private String parentId;
    private String deptName;
    private String deptCode;

    private Integer status;

}
