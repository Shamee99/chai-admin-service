<template>
  <div class="online-user-monitor container-wrapper">
    <!-- 在线用户统计卡片 -->
    <div class="overview-cards">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon online">
                <el-icon><User /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">在线用户</div>
                <div class="card-value">{{ onlineStats.total }}</div>
                <div class="card-desc">当前在线</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active">
                <el-icon><ChatDotRound /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">活跃用户</div>
                <div class="card-value">{{ onlineStats.active }}</div>
                <div class="card-desc">5分钟内活跃</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon peak">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">空闲用户</div>
                <div class="card-value">{{ onlineStats.idle }}</div>
                <div class="card-desc">5分钟内未操作</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon session">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">平均会话时长</div>
                <div class="card-value">{{ onlineStats.duration }}</div>
                <div class="card-desc">平均会话时长</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 在线用户列表 -->
    <div class="online-table__wrapper">
      <ChaiTable
        :data="tableData"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
        :showRefresh="false"
        :show-selection="true"
        :search-form="searchForm"
        @page-change="handlePageChange"
        @search="handleSearch"
        @reset="handleReset"
        @selection-change="handleSelectionChange"
      >

        <!-- 搜索表单项 -->
        <template #search-items="{ searchForm }">
          <el-form-item label="用户名">
            <el-input
              v-model="searchForm.username"
              placeholder="请输入用户名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="IP地址">
            <el-input
              v-model="searchForm.ip"
              placeholder="请输入IP地址"
              clearable
              style="width: 200px"
            />
          </el-form-item>
        </template>

        <template #toolbar-left="{ selectedRows }">
          <div class="table-toolbar">
            <div class="toolbar-left">
              <el-text type="info">共 {{ pagination.total }} 个在线用户</el-text>
            </div>
          </div>
        </template>
      </ChaiTable>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import {
  ElButton,
  ElCard,
  ElCol,
  ElForm,
  ElFormItem,
  ElIcon,
  ElInput,
  ElRow,
  ElText,
} from 'element-plus'
import {
  ChatDotRound,
  Connection,
  Refresh,
  Search,
  TrendCharts,
  User,
} from '@element-plus/icons-vue'
import ChaiTable from '@/components/common/ChaiTable.vue'
import type { OnlineUser, OnlineUserPageResponse, OnlineUserStats } from './api/online-user.types'
import {
  createOnlineUserListParams,
  getOnlineUserList, getOnlineUserListApi,
  getOnlineUserStats
} from './api/online-user'

// 响应式数据
const onlineStats = ref<OnlineUserStats>({
  total: 0,
  active: 0,
  idle: 0,
  duration: 0,
  avgSessionTime: 0,
  newUsersToday: 0,
})

const tableLoading = ref(false)
const selectedRows = ref<OnlineUserPageResponse[]>([])

// 搜索表单
const searchForm = reactive({
  username: '',
  ip: '',
})

// 分页配置
const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})

// 表格数据
const tableData = ref<OnlineUserPageResponse[]>([])

// 表格列配置
const tableColumns = [
  { prop: 'username', label: '用户名', width: 120 },
  { prop: 'realName', label: '真实姓名', width: 100 },
  { prop: 'lastLoginIp', label: 'IP地址', width: 140 },
  { prop: 'loginLocation', label: '登录地点', width: 150, showOverflowTooltip: true },
  { prop: 'browser', label: '浏览器', showOverflowTooltip: true },
  { prop: 'os', label: '操作系统', showOverflowTooltip: true },
  { prop: 'loginTime', label: '登录时间', width: 160 },
]

// 获取在线用户统计
const fetchOnlineUserStats = async () => {
  try {
    onlineStats.value = await getOnlineUserStats()
  } catch (error) {
    console.error('获取在线用户统计失败:', error)
  }
}

// 获取在线用户列表
const fetchOnlineUserList = async () => {
  tableLoading.value = true
  try {
    const params = createOnlineUserListParams(pagination.pageNo, pagination.pageSize, searchForm)

    const response = await getOnlineUserListApi(params)
    tableData.value = response.records
    pagination.total = response.total
  } finally {
    tableLoading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.pageNo = 1
  fetchOnlineUserList()
}

// 重置处理
const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    ipAddress: '',
    status: '',
  })
  pagination.pageNo = 1
  fetchOnlineUserList()
}

// 分页变化处理
const handlePageChange = (page: number, size: number) => {
  pagination.pageNo = page
  pagination.pageSize = size
  fetchOnlineUserList()
}

// 选择变化处理
const handleSelectionChange = (selection: OnlineUser[]) => {
  selectedRows.value = selection
}

// 组件挂载
onMounted(() => {
  fetchOnlineUserStats()
  fetchOnlineUserList()
})

// 组件卸载
onUnmounted(() => {})
</script>

<style scoped>
.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  height: 120px;
}

.card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
  color: white;
}

.card-icon.online {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.peak {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.session {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.card-desc {
  font-size: 12px;
  color: #c0c4cc;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-management {
  margin-bottom: 20px;
}

.search-section {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.danger {
  color: #f56c6c;
}

.danger:hover {
  color: #f78989;
}
</style>
