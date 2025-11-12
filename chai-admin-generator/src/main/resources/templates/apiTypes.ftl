import type { PageParams } from '@/components/common/api/page.types.ts'

export interface ${entityName} {
<#list columns as column>
  ${column.javaField}?: ${column.tsType}
</#list>
}

// ${tableComment!}查询参数
export interface ${entityName}QueryParams {
<#list columns as column>
<#if !column.isBaseField>
  ${column.javaField}?: ${column.tsType}
</#if>
</#list>
}

export interface ${entityName}ListRequest extends PageParams {
  param: ${entityName}QueryParams
}
