package org.shamee.system.entity;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.shamee.common.entity.IdEntity;
import org.shamee.system.entity.enums.BusinessType;

import java.time.LocalDateTime;


/**
 * 系统操作日志
 * @author Shamee
 * @date 2023-07-05
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_operation_log")
public class SysOperationLog extends IdEntity {

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型：0-其它，1-新增，2-修改，3-删除，4-授权，5-导出，6-导入，7-强退，8-生成代码，9-清空数据
     */
    @EnumValue
    private BusinessType businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式（GET、POST 等）
     */
    private String requestMethod;

    /**
     * 操作类别：0-其它，1-后台用户，2-手机端用户
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String userName;
    private String userId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址（IP）
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数（JSON 结果）
     */
    private String jsonResult;

    /**
     * 操作状态：0-正常，1-异常
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operTime;

    /**
     * 消耗时间（毫秒）
     */
    private Long costTime;
}
