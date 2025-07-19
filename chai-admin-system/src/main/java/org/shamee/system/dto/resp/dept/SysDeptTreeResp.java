package org.shamee.system.dto.resp.dept;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;


/**
 * 部门树 vo
 * @author shamee
 * @date 2025-07-12
 *
 */
@Data
public class SysDeptTreeResp {

    private String id;
    private String parentId;
    private String deptName;

    private List<SysDeptTreeResp> children;
}
