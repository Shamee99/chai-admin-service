// 缓存状态接口
import { PageParams } from '@/components/common/api/page.types.ts'
import { OnlineUserQueryParams } from '@/views/monitor/online-user/api/online-user.types.ts'

export interface CacheStatus {
  connected?: boolean;
  version?: string;
  usedMemoryMB?: number;     // 单位 MB，数值类型，如 1.45
  maxMemoryMB?: number | null; // 单位 MB，null 表示无限制
  keyCount?: number;         // key 数量
  expiredKeys?: number;      // 过期 key 数量
  hits?: number;             // 命中次数
  totalCommands?: number;    // 总访问次数
  hitRate?: string;          // 命中率，格式化为 "85.00%"
}

// 缓存项接口
export interface CacheItem {
  key: string
  type: 'string' | 'hash' | 'list' | 'set' | 'zset'
  value?: any
  size: number
  ttl: number // -1表示永久，0表示已过期，>0表示剩余秒数
  createTime: string
  lastAccess: string
  accessCount?: number
  encoding?: string
  idleTime?: number
}

export interface CacheQueryListRequest extends PageParams {
  param: CacheQueryParams
}

// 缓存查询参数
export interface CacheQueryParams {
  key?: string
  type?: string
  pattern?: string
  minTtl?: number
  maxTtl?: number
  minSize?: number
  maxSize?: number
}

// 缓存分页响应
export interface CachePageResponse {
  records: CacheItem[]
  total: number
  pageNo: number
  pageSize: number
}

// 内存使用历史数据
export interface MemoryHistoryData {
  time: string
  used: number
  total: number
  peak: number
}

// 命中率历史数据
export interface HitRateHistoryData {
  time: string
  hitRate: number
  hits: number
  misses: number
  requests: number
}

// 缓存操作统计
export interface CacheOperationStats {
  time: string
  gets: number
  sets: number
  dels: number
  incrs: number
  decrs: number
}

// 缓存键分析
export interface CacheKeyAnalysis {
  pattern: string
  count: number
  totalSize: number
  avgSize: number
  types: Record<string, number>
}

// 缓存配置
export interface CacheConfig {
  maxMemory: string
  maxMemoryPolicy: string
  timeout: number
  databases: number
  save: string[]
  appendOnly: boolean
  appendFsync: string
}

// 缓存详情
export interface CacheDetail extends CacheItem {
  memoryUsage: number
  serializedLength: number
  objectRefcount: number
  objectEncoding: string
  objectIdletime: number
  expireAt?: string
  persistAt?: string
}

// 缓存性能指标
export interface CachePerformanceMetrics {
  timestamp: string
  cpu: {
    used: number
    sys: number
    user: number
    sysChildren: number
    userChildren: number
  }
  memory: {
    used: number
    peak: number
    rss: number
    overhead: number
    startup: number
    dataset: number
    lua: number
    fragmentation: number
  }
  stats: {
    connections: {
      received: number
      rejected: number
    }
    commands: {
      processed: number
      failed: number
    }
    network: {
      inputBytes: number
      outputBytes: number
    }
    keyspace: {
      hits: number
      misses: number
      expires: number
      evictions: number
    }
  }
}

// 缓存慢查询
export interface CacheSlowLog {
  id: number
  timestamp: number
  duration: number // 微秒
  command: string[]
  clientInfo: string
}

// 缓存客户端信息
export interface CacheClientInfo {
  id: string
  addr: string
  fd: number
  name: string
  age: number
  idle: number
  flags: string[]
  db: number
  sub: number
  psub: number
  multi: number
  qbuf: number
  qbufFree: number
  obl: number
  oll: number
  omem: number
  events: string
  cmd: string
}

// 缓存备份信息
export interface CacheBackupInfo {
  id: string
  filename: string
  size: number
  createTime: string
  type: 'manual' | 'auto'
  status: 'success' | 'failed' | 'running'
  description?: string
  error?: string
}

// 缓存恢复参数
export interface CacheRestoreParams {
  backupId: string
  flushBefore: boolean
  skipErrors: boolean
}

// 缓存导出参数
export interface CacheExportParams {
  keys?: string[]
  pattern?: string
  database?: number
  format: 'rdb' | 'json' | 'csv'
  compress: boolean
}

// 缓存导入参数
export interface CacheImportParams {
  file: File
  format: 'rdb' | 'json' | 'csv'
  flushBefore: boolean
  skipErrors: boolean
  database?: number
}
