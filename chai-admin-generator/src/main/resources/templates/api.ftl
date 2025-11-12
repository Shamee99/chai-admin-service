import request from '@/utils/request'
import type { ${entityName}, ${entityName}QueryParams } from './${apiFileName}.types'

/**
 * 分页查询${tableComment!}
 */
export function get${entityName}Page(params: ${entityName}QueryParams) {
  return request({
    url: '/api/${moduleName}/${entityNameLower}/page',
    method: 'get',
    params
  })
}

/**
 * 查询${tableComment!}列表
 */
export function get${entityName}List() {
  return request({
    url: '/api/${moduleName}/${entityNameLower}/list',
    method: 'get'
  })
}

/**
 * 根据ID查询${tableComment!}
 */
export function get${entityName}ById(id: string) {
  return request({
    url: `/api/${moduleName}/${entityNameLower}/${'$'}{id}`,
    method: 'get'
  })
}

/**
 * 新增${tableComment!}
 */
export function add${entityName}(data: ${entityName}) {
  return request({
    url: '/api/${moduleName}/${entityNameLower}',
    method: 'post',
    data
  })
}

/**
 * 修改${tableComment!}
 */
export function update${entityName}(data: ${entityName}) {
  return request({
    url: '/api/${moduleName}/${entityNameLower}',
    method: 'put',
    data
  })
}

/**
 * 删除${tableComment!}
 */
export function delete${entityName}(id: string) {
  return request({
    url: `/api/${moduleName}/${entityNameLower}/${'$'}{id}`,
    method: 'delete'
  })
}

/**
 * 批量删除${tableComment!}
 */
export function delete${entityName}Batch(ids: string[]) {
  return request({
    url: '/api/${moduleName}/${entityNameLower}/batch',
    method: 'delete',
    data: ids
  })
}

