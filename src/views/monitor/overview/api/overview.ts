import request from '@/utils/request'
import type { SystemStatus, SystemAlert, SystemOverviewResponse, SystemPerformanceHistory } from './overview.types'

/**
 * 获取系统状态
 */
export const getSystemStatus = async (): Promise<SystemStatus> => {
  return request.get<SystemStatus>('/monitor/system/status')
}

/**
 * 获取系统告警信息
 */
export const getSystemAlerts = async (): Promise<SystemAlert[]> => {
  return request.get<SystemAlert[]>('/monitor/alerts')
}

/**
 * 获取系统概览数据
 */
export const getSystemOverview = async (): Promise<SystemOverviewResponse> => {
  return request.get<SystemOverviewResponse>('/monitor/overview')
}

/**
 * 获取系统性能历史数据
 */
export const getSystemPerformanceHistory = async (timeRange: string = '1h'): Promise<SystemPerformanceHistory> => {
  return request.get<SystemPerformanceHistory>(`/monitor/performance/history?range=${timeRange}`)
}

/**
 * 清除告警
 */
export const clearAlert = async (alertId: string): Promise<void> => {
  await request.delete(`/monitor/alerts/${alertId}`)
}

/**
 * 批量清除告警
 */
export const clearAlerts = async (alertIds: string[]): Promise<void> => {
  await request.post('/monitor/alerts/clear', { alertIds })
}

/**
 * 获取实时系统资源数据
 */
export const getRealTimeSystemResource = async () => {
  return request.get('/monitor/system/resource/realtime')

}
