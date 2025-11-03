import request from '@/utils/request'
import type {
  AbnormalLoginStats,
  IpBlacklistPageResponse,
  IpBlacklistQueryParams,
  LoginLogPageResponse,
  LoginLogQueryParams,
  LoginReport
} from './login-log.types'

/**
 * 获取登录日志列表
 */
export const getLoginLogList = async (params: LoginLogQueryParams): Promise<LoginLogPageResponse> => {
  return await request.post<LoginLogPageResponse>('/monitor/login-logs', { params })

}

/**
 * 删除登录日志
 */
export const deleteLoginLog = async (id: string): Promise<void> => {
  await request.delete(`/monitor/login-logs/${id}`)
}

/**
 * 批量删除登录日志
 */
export const batchDeleteLoginLogs = async (ids: string[]): Promise<void> => {
  await request.post('/monitor/login-logs/batch-delete', { ids })
}

/**
 * 清理旧日志
 */
export const clearOldLogs = async (days: number): Promise<void> => {
  await request.post('/monitor/login-logs/clear-old', { days })
}

/**
 * 获取异常登录统计
 */
export const getAbnormalLoginStats = async (days: number = 7): Promise<AbnormalLoginStats> => {
  return await request.get<AbnormalLoginStats>(`/monitor/login-logs/abnormal-stats?days=${days}`)

}

/**
 * 获取IP黑名单列表
 */
export const getIpBlacklistList = async (params: IpBlacklistQueryParams): Promise<IpBlacklistPageResponse> => {
  return await request.get<IpBlacklistPageResponse>('/monitor/ip-blacklist', { params })

}

/**
 * 添加IP到黑名单
 */
export const addToBlacklist = async (ipAddress: string, reason: string, expireTime?: string): Promise<void> => {
  await request.post('/monitor/ip-blacklist', {
    ipAddress,
    reason,
    expireTime
  })
}

/**
 * 从黑名单移除IP
 */
export const removeFromBlacklist = async (id: string): Promise<void> => {
  await request.delete(`/monitor/ip-blacklist/${id}`)
}

/**
 * 更新黑名单状态
 */
export const updateBlacklistStatus = async (id: string, status: 'active' | 'inactive'): Promise<void> => {
  await request.put(`/monitor/ip-blacklist/${id}/status`, { status })
}

/**
 * 获取登录报告
 */
export const getLoginReport = async (startDate: string, endDate: string): Promise<LoginReport> => {
  return await request.get<LoginReport>('/monitor/login-logs/report', {
    params: { startDate, endDate }
  })

}

/**
 * 导出登录日志
 */
export const exportLoginLogs = async (params: LoginLogQueryParams): Promise<Blob> => {
  return await request.get('/monitor/login-logs/export', {
    params,
    responseType: 'blob'
  })

}
