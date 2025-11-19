package ${packageName}.dto.req.${entityNameLower};

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ${tableComment!}新增请求
 *
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${entityName}SaveRequest {

<#list columns as column>
<#if !column.isBaseField && column.columnName != 'id'>
    /**
     * ${column.columnComment!}
     */
<#if !column.isNullable>
<#if column.javaType == 'String'>
    @NotBlank(message = "${column.columnComment!}不能为空")
<#else>
    @NotNull(message = "${column.columnComment!}不能为空")
</#if>
</#if>
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}

