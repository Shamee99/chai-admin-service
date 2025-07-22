package org.shamee.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.shamee.common.entity.IdEntity;
import org.shamee.system.dto.resp.auth.LoginResp;

import java.time.LocalDateTime;


/**
 * 登录记录
 * @author shamee
 * @since 2025-07-08
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_login_log")
public class SysLoginLog extends IdEntity {

    private String username;
    private String ipaddr;
    private String loginLocation;
    private String browser;
    private String os;
    private String msg;

    @TableField(value = "login_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime loginTime;
    private Integer status;

}
