// 在线用户接口
import { PageParams } from '@/components/common/api/page.types.ts'

export interface OnlineUser {
  id: string
  username: string
  realName: string
  lastLoginIp?: string
  browser?: string
  os: string
  loginLocation?: string
  loginTime?: Date
}

// 在线用户统计
export interface OnlineUserStats {
  total: number
  active: number
  idle: number
  duration: number
  avgSessionTime: number
  newUsersToday: number
  usersByDevice?: {
    desktop: number
    mobile: number
    tablet: number
  }
  usersByBrowser?: {
    chrome: number
    firefox: number
    safari: number
    edge: number
    other: number
  }
  usersByLocation?: {
    [key: string]: number
  }
}

export interface OnlineUserListRequest extends PageParams {
  param: OnlineUserQueryParams
}
// 在线用户查询参数
export interface OnlineUserQueryParams {
  username?: string
  ip?: string
}

// 在线用户分页响应
export interface OnlineUserPageResponse {
  records: OnlineUser[]
  total: number
  pageNo: number
  pageSize: number
}

