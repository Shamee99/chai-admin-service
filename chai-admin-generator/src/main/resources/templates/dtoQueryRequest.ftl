package ${packageName}.dto.req.${entityNameLower};

import lombok.Data;

/**
 * ${tableComment!}查询请求
 *
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${entityName}QueryRequest {

<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
    /**
     * ${column.columnComment!}
     */
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}

