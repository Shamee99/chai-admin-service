package org.shamee.system.dto.resp.dept;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.dromara.hutool.core.date.format.DatePattern;
import org.shamee.common.db.typehandler.ArrayToListTypeHandler;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 部门分页数据
 * @author shamee
 * @date 2025-07-12
 */
@Data
public class SysDeptPageResp {

    /**
     * 返回到前端转string，前端的number会导致精度丢失
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String id;
    private String deptName;
    private String deptCode;
    private String leader;
    private String phone;
    private String email;
    private Integer status;


    @TableField(typeHandler = ArrayToListTypeHandler.class)
    private List<String> roleNames;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
