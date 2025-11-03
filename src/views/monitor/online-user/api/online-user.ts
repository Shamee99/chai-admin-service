import request from '@/utils/request'
import type {
  OnlineUserPageResponse,
  OnlineUserQueryParams,
  OnlineUserStats,
} from './online-user.types'
import { OnlineUserListRequest } from './online-user.types'
import type { PageResult } from '@/components/common/api/page.types.ts'

// 获取在线用户统计
export const getOnlineUserStats = async (): Promise<OnlineUserStats> => {
  return await request.get<OnlineUserStats>('/monitor/online-user/stats')
}

// 获取在线用户列表
export const getOnlineUserListApi = async (params: OnlineUserListRequest,): Promise<PageResult<OnlineUserPageResponse>> => {
  return await request.post<PageResult<OnlineUserPageResponse>>('/monitor/online-user/list', params)
}
export const createOnlineUserListParams = (
  pageNo: number = 1,
  pageSize: number = 10,
  queryParams: OnlineUserQueryParams = {},
  orderBy?: string,
  order?: string,
): OnlineUserListRequest => {
  return {
    pageNo,
    pageSize,
    orderBy,
    order,
    param: {
      username: '',
      ip: '',
      ...queryParams,
    },
  }
}
