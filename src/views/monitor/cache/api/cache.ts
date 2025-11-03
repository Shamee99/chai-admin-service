import request from '@/utils/request'
import type {
  CacheBackupInfo,
  CacheClientInfo,
  CacheConfig,
  CacheDetail,
  CacheExportParams,
  CacheImportParams,
  CacheKeyAnalysis,
  CacheOperationStats,
  CachePageResponse,
  CachePerformanceMetrics,
  CacheQueryParams,
  CacheRestoreParams,
  CacheSlowLog,
  CacheStatus,
  HitRateHistoryData,
  MemoryHistoryData,
} from './cache.types'
import { CacheQueryListRequest } from './cache.types'
import type { PageResult } from '@/components/common/api/page.types.ts'

// 获取缓存状态
export const getCacheStatus = async (): Promise<CacheStatus> => {
  return request.get<CacheStatus>('/monitor/cache/status')
}

// 获取缓存列表
export const getCacheList = async (
  params: CacheQueryListRequest,
): Promise<PageResult<CachePageResponse>> => {
  return await request.post<PageResult<CachePageResponse>>('/monitor/cache/list', params)
}

export const createCacheListParams = (
  pageNo: number = 1,
  pageSize: number = 10,
  queryParams: CacheQueryParams = {},
  orderBy?: string,
  order?: string,
): CacheQueryListRequest => {
  return {
    pageNo,
    pageSize,
    orderBy,
    order,
    param: {
      key: '',
      type: '',
      ...queryParams,
    },
  }
}

// 获取缓存详情
export const getCacheDetail = async (key: string): Promise<CacheDetail> => {
  return request.get<CacheDetail>(`/monitor/cache/detail/${encodeURIComponent(key)}`)
}

// 删除缓存
export const deleteCache = async (key: string): Promise<void> => {
  await request.delete(`/monitor/cache/${encodeURIComponent(key)}`)
}

// 批量删除缓存
export const batchDeleteCache = async (keys: string[]): Promise<void> => {
  await request.post('/monitor/cache/batch-delete', { keys })
}

// 清理过期缓存
export const clearExpiredCache = async (): Promise<void> => {
  await request.post('/monitor/cache/clear-expired')
}

// 清空所有缓存
export const flushAllCache = async (): Promise<void> => {
  await request.post('/monitor/cache/flush-all')
}

// 设置缓存TTL
export const setCacheTTL = async (key: string, ttl: number): Promise<void> => {
  await request.post(`/monitor/cache/${encodeURIComponent(key)}/ttl`, { ttl })
}

// 获取内存使用历史
export const getCacheMemoryHistory = async (hours: number = 24): Promise<MemoryHistoryData[]> => {
  return request.get<MemoryHistoryData[]>('/monitor/cache/memory-history', { params: { hours } })
}

// 获取命中率历史
export const getCacheHitRateHistory = async (hours: number = 24): Promise<HitRateHistoryData[]> => {
  return request.get<HitRateHistoryData[]>('/monitor/cache/hit-rate-history', { params: { hours } })
}

// 获取操作统计
export const getCacheOperationStats = async (
  hours: number = 24,
): Promise<CacheOperationStats[]> => {
  return request.get<CacheOperationStats[]>('/monitor/cache/operation-stats', { params: { hours } })
}

// 获取Key分析
export const getCacheKeyAnalysis = async (): Promise<CacheKeyAnalysis[]> => {
  return request.get<CacheKeyAnalysis[]>('/monitor/cache/key-analysis')
}

// 获取缓存配置
export const getCacheConfig = async (): Promise<CacheConfig> => {
  return request.get<CacheConfig>('/monitor/cache/config')
}

// 更新缓存配置
export const updateCacheConfig = async (config: Partial<CacheConfig>): Promise<void> => {
  await request.put('/monitor/cache/config', config)
}

// 获取性能指标
export const getCachePerformanceMetrics = async (): Promise<CachePerformanceMetrics> => {
  return request.get<CachePerformanceMetrics>('/monitor/cache/performance-metrics')
}

// 获取慢查询日志
export const getCacheSlowLog = async (count: number = 100): Promise<CacheSlowLog[]> => {
  return request.get<CacheSlowLog[]>('/monitor/cache/slow-log', { params: { count } })
}

// 获取客户端信息
export const getCacheClientInfo = async (): Promise<CacheClientInfo[]> => {
  return request.get<CacheClientInfo[]>('/monitor/cache/client-info')
}

// 获取备份列表
export const getCacheBackupList = async (): Promise<CacheBackupInfo[]> => {
  return request.get<CacheBackupInfo[]>('/monitor/cache/backup-list')
}

// 创建备份
export const createCacheBackup = async (description?: string): Promise<void> => {
  await request.post('/monitor/cache/backup', { description })
}

// 恢复备份
export const restoreCacheBackup = async (params: CacheRestoreParams): Promise<void> => {
  await request.post('/monitor/cache/restore', params)
}

// 导出缓存数据
export const exportCacheData = async (params: CacheExportParams): Promise<Blob> => {
  return request.post('/monitor/cache/export', params, { responseType: 'blob' })
}

// 导入缓存数据
export const importCacheData = async (params: CacheImportParams): Promise<void> => {
  const formData = new FormData()
  formData.append('file', params.file)
  formData.append('format', params.format)
  formData.append('flushBefore', params.flushBefore.toString())
  formData.append('skipErrors', params.skipErrors.toString())
  if (params.database !== undefined) {
    formData.append('database', params.database.toString())
  }
  await request.post('/monitor/cache/import', formData)
}
