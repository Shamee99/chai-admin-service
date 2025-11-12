package ${packageName}.dto.resp.${entityNameLower};

import lombok.Data;
<#assign hasDateField = false>
<#assign hasBigDecimalField = false>
<#list columns as column>
<#if column.javaType == 'LocalDateTime' || column.javaType == 'LocalDate'>
<#assign hasDateField = true>
</#if>
<#if column.javaType == 'BigDecimal'>
<#assign hasBigDecimalField = true>
</#if>
</#list>
<#if hasDateField>
import java.time.LocalDateTime;
import java.time.LocalDate;
</#if>
<#if hasBigDecimalField>
import java.math.BigDecimal;
</#if>

/**
 * ${tableComment!}分页响应
 *
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${entityName}PageResp {

<#list columns as column>
    /**
     * ${column.columnComment!}
     */
    private ${column.javaType} ${column.javaField};

</#list>
}

