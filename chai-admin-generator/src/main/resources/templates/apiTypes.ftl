/**
 * ${tableComment!}
 */
export interface ${entityName} {
<#list columns as column>
  /** ${column.columnComment!} */
  ${column.javaField}?: ${column.tsType}
</#list>
}

/**
 * ${tableComment!}查询参数
 */
export interface ${entityName}QueryParams {
  current?: number
  size?: number
<#list columns as column>
<#if !column.isBaseField>
  ${column.javaField}?: ${column.tsType}
</#if>
</#list>
}

