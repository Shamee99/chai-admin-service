import request from '@/utils/request.ts'
import type {
  ${entityName},
  ${entityName}ListRequest,
  ${entityName}QueryParams,
} from '@/views/${moduleName}/${entityNameLower}/api/${apiFileName}.types'
import type { PageResult } from '@/components/common/api/page.types.ts'

/**
 * 获取${tableComment!}列表
 * @param params 分页和查询参数
 * @returns ${tableComment!}列表响应
 */
export const get${entityName}List = async (params: ${entityName}ListRequest): Promise<PageResult<${entityName}>> => {
  return request.post<PageResult<${entityName}>>('/${moduleName}/${entityNameLower}/page', params)
}

/**
 * 创建${tableComment!}列表请求参数的辅助函数
 * @param pageNo 页码
 * @param pageSize 每页大小
 * @param queryParams 查询参数
 * @param orderBy 排序字段
 * @param order 排序方式
 * @returns 完整的请求参数
 */
export const create${entityName}ListParams = (
  pageNo: number = 1,
  pageSize: number = 10,
  queryParams: ${entityName}QueryParams = {},
  orderBy?: string,
  order?: string,
): ${entityName}ListRequest => {
  return {
    pageNo,
    pageSize,
    orderBy,
    order,
    param: {
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
      ${column.javaField}: '',
</#if>
</#list>
      ...queryParams,
    },
  }
}

/**
 * 新增${tableComment!}
 * @param data ${tableComment!}数据
 * @returns 响应结果
 */
export const add${entityName} = async (data: ${entityName}): Promise<boolean> => {
  return request.post('/${moduleName}/${entityNameLower}/add', data)
}

/**
 * 更新${tableComment!}
 * @param data ${tableComment!}数据
 * @returns 响应结果
 */
export const update${entityName} = async (data: ${entityName}): Promise<boolean> => {
  return request.put('/${moduleName}/${entityNameLower}/edit', data)
}

/**
 * 删除${tableComment!}
 * @param id ${tableComment!}ID
 * @returns 响应结果
 */
export const delete${entityName} = async (id: string): Promise<boolean> => {
  return request.delete(`/${moduleName}/${entityNameLower}/delete/${'$'}{id}`)
}

/**
 * ${tableComment!}详情
 * @param id
 */
export const getDetail = async (id: string): Promise<${entityName}> => {
  return request.get<${entityName}>(`/${moduleName}/${entityNameLower}/detail/${'$'}{id}`)
}

