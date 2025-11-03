// 系统状态接口
export interface SystemStatus {
  status: string
  onlineUsers: number
  cpuUsage: number
  memoryUsage: number
}

// 系统告警接口
export interface SystemAlert {
  id: string
  level: '严重' | '警告' | '信息'
  message: string
  time: string
  status: '已处理' | '未处理'
}

// 系统资源使用情况
export interface SystemResource {
  cpu: {
    usage: number
    cores: number
    processes: number
  }
  memory: {
    usage: number
    total: number
    used: number
    free: number
  }
  disk: {
    usage: number
    total: number
    used: number
    free: number
  }
  network: {
    inbound: number
    outbound: number
    connections: number
  }
}

// 图表数据点
export interface ChartDataPoint {
  time: string
  value: number
}

// 系统性能历史数据
export interface SystemPerformanceHistory {
  cpu: ChartDataPoint[]
  memory: ChartDataPoint[]
  disk: ChartDataPoint[]
  network: ChartDataPoint[]
}

// 服务状态
export interface ServiceStatus {
  name: string
  status: 'running' | 'stopped' | 'error'
  uptime: string
  port?: number
  description?: string
}

// 系统概览响应
export interface SystemOverviewResponse {
  status: SystemStatus
  resource: SystemResource
  services: ServiceStatus[]
  alerts: SystemAlert[]
  performance: SystemPerformanceHistory
}