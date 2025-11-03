// 登录日志接口
export interface LoginLog {
  id: string
  username: string
  ipAddress: string
  location: string
  browser: string
  os: string
  status: 'success' | 'failed'
  riskLevel: 'low' | 'medium' | 'high'
  loginTime: string
  logoutTime?: string
  duration: string
  userAgent: string
  sessionId?: string
  failureReason?: string
  createTime: string
}

// 登录日志查询参数
export interface LoginLogQueryParams {
  pageNo: number
  pageSize: number
  username?: string
  ipAddress?: string
  status?: string
  startTime?: string
  endTime?: string
  riskLevel?: string
}

// 登录日志分页响应
export interface LoginLogPageResponse {
  records: LoginLog[]
  total: number
  pageNo: number
  pageSize: number
}

// 异常登录统计
export interface AbnormalLoginStats {
  totalAttempts: number
  failedAttempts: number
  successRate: number
  topFailedIps: {
    ip: string
    count: number
    location: string
  }[]
  topFailedUsers: {
    username: string
    count: number
    lastAttempt: string
  }[]
  hourlyStats: {
    hour: string
    attempts: number
    failed: number
  }[]
}

// IP黑名单
export interface IpBlacklist {
  id: string
  ipAddress: string
  reason: string
  createTime: string
  createBy: string
  status: 'active' | 'inactive'
  expireTime?: string
}

// IP黑名单查询参数
export interface IpBlacklistQueryParams {
  pageNo: number
  pageSize: number
  ipAddress?: string
  status?: string
}

// IP黑名单分页响应
export interface IpBlacklistPageResponse {
  records: IpBlacklist[]
  total: number
  pageNo: number
  pageSize: number
}

// 登录趋势数据
export interface LoginTrendData {
  date: string
  totalLogins: number
  successLogins: number
  failedLogins: number
  uniqueUsers: number
}

// 地理位置统计
export interface LocationStats {
  country: string
  region: string
  city: string
  count: number
  percentage: number
}

// 浏览器统计
export interface BrowserStats {
  browser: string
  version: string
  count: number
  percentage: number
}

// 操作系统统计
export interface OsStats {
  os: string
  version: string
  count: number
  percentage: number
}

// 登录报告
export interface LoginReport {
  period: string
  totalLogins: number
  uniqueUsers: number
  successRate: number
  averageSessionDuration: string
  peakHour: string
  trends: LoginTrendData[]
  locations: LocationStats[]
  browsers: BrowserStats[]
  operatingSystems: OsStats[]
  abnormalActivities: {
    type: string
    description: string
    count: number
    severity: 'low' | 'medium' | 'high'
  }[]
}