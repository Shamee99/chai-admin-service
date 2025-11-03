<template>
  <div class="cache-monitor container-wrapper">
    <!-- 缓存概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon redis">
                <el-icon><Coin /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">Redis状态</div>
                <div class="card-value">
                  <el-tag :type="cacheStatus.redis.status === 'online' ? 'success' : 'danger'">
                    {{ cacheStatus.redis.status === 'online' ? '在线' : '离线' }}
                  </el-tag>
                </div>
                <div class="card-desc">{{ cacheStatus.redis.version }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon memory">
                <el-icon><Cpu /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">内存使用</div>
                <div class="card-value">{{ cacheStatus.memory.used }}MB</div>
                <div class="card-desc">总计 {{ cacheStatus.memory.total }}MB</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon keys">
                <el-icon><Key /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">缓存键数</div>
                <div class="card-value">{{ cacheStatus.keys.total.toLocaleString() }}</div>
                <div class="card-desc">过期 {{ cacheStatus.keys.expired }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon hit-rate">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">命中率</div>
                <div class="card-value">{{ cacheStatus.hitRate }}</div>
                <div class="card-desc">{{ cacheStatus.hits }} / {{ cacheStatus.requests }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 缓存管理 -->
    <div class="cache-management">
      <!-- 搜索区域 -->
      <div class="search-section">

        <!-- 缓存列表 -->
        <ChaiTable
          :data="tableData"
          :columns="tableColumns"
          :loading="tableLoading"
          :pagination="pagination"
          :search-form="searchForm"
          :show-selection="true"
          @page-change="handlePageChange"
          @search="handleSearch"
          @reset="handleReset"
          @selection-change="handleSelectionChange"
        >

          <template #search-items="{ searchForm }">
            <el-form-item label="缓存键">
              <el-input
                v-model="searchForm.key"
                placeholder="请输入缓存键名"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="数据类型">
              <el-select
                v-model="searchForm.type"
                placeholder="请选择类型"
                clearable
                style="width: 120px"
              >
                <el-option label="String" value="string" />
                <el-option label="Hash" value="hash" />
                <el-option label="List" value="list" />
                <el-option label="Set" value="set" />
                <el-option label="ZSet" value="zset" />
              </el-select>
            </el-form-item>
          </template>

          <template #toolbar-left="{ selectedRows }">
            <el-button type="primary" @click="handleRefreshAll">
              <el-icon><Refresh /></el-icon>
              刷新全部
            </el-button>
            <el-button type="warning" @click="handleClearExpired">
              <el-icon><Delete /></el-icon>
              清理过期
            </el-button>
            <el-button type="danger" @click="handleFlushAll">
              <el-icon><Warning /></el-icon>
              清空缓存
            </el-button>
          </template>
        </ChaiTable>
      </div>
    </div>

    <!-- 缓存详情弹窗 -->
    <CacheDetail
      v-model:visible="detailDialogVisible"
      :cache-data="currentCacheData"
      @refresh="handleRefresh"
    />
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
  ElMessage,
  ElMessageBox,
  ElOption,
  ElRow,
  ElSelect,
  ElTag,
} from 'element-plus'
import {
  Coin,
  Cpu,
  Delete,
  Key,
  Refresh,
  Search,
  TrendCharts,
  Warning,
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import ChaiTable from '@/components/common/ChaiTable.vue'
import CacheDetail from './components/CacheDetail.vue'
import type { CacheItem, CacheStatus } from './api/cache.types'
import {
  clearExpiredCache,
  createCacheListParams,
  deleteCache,
  flushAllCache,
  getCacheList,
  getCacheStatus,
} from './api/cache'

// 响应式数据
const cacheStatus = ref<CacheStatus>({
  redis: { status: 'online', version: 'Redis -' },
  memory: { used: 0, total: 0 },
  keys: { total: 0, expired: 0 },
  hitRate: 0.0,
  hits: 0,
  requests: 0,
})

const tableLoading = ref(false)
const selectedRows = ref<CacheItem[]>([])
const detailDialogVisible = ref(false)
const currentCacheData = ref<CacheItem | null>(null)

// 图表引用
let memoryChart: echarts.ECharts | null = null
let hitRateChart: echarts.ECharts | null = null
let refreshTimer: NodeJS.Timeout | null = null

// 搜索表单
const searchForm = reactive({
  key: '',
  type: '',
})

// 分页配置
const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})

// 表格数据
const tableData = ref<CacheItem[]>([])

// 表格列配置
const tableColumns = [
  { prop: 'key', label: '缓存键', minWidth: 150, showOverflowTooltip: true },
  { prop: 'type', label: '数据类型', width: 100 },
  { prop: 'size', label: '大小', width: 100, formatter: (row: CacheItem) => `${row.size} bytes` },
  {
    prop: 'ttl',
    label: 'TTL',
    width: 120,
    formatter: (row: CacheItem) => (row.ttl > 0 ? `${row.ttl}s` : '永久'),
  },
]

// 获取缓存状态
const fetchCacheStatus = async () => {
  try {
    const data = await getCacheStatus()
    cacheStatus.value = {
      redis: { status: data.connected ? 'online' : 'offline', version: data.version },
      memory: { used: data.usedMemoryMB, total: data.maxMemoryMB },
      keys: { total: data.keyCount, expired: data.expiredKeys },
      hitRate: data.hitRate,
      hits: data.hits,
      requests: data.totalCommands,
    }
  } catch (error) {
    console.error('获取缓存状态失败:', error)
  }
}

// 获取缓存列表
const fetchCacheList = async () => {
  tableLoading.value = true
  try {
    const params = createCacheListParams(pagination.pageNo, pagination.pageSize, searchForm)
    const response = await getCacheList(params)
    tableData.value = response.records
    pagination.total = response.total
  } finally {
    tableLoading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.pageNo = 1
  fetchCacheList()
}

// 重置处理
const handleReset = () => {
  Object.assign(searchForm, {
    key: '',
    type: '',
  })
  pagination.pageNo = 1
  fetchCacheList()
}

// 刷新处理
const handleRefresh = () => {
  fetchCacheStatus()
  fetchCacheList()
}

// 分页变化处理
const handlePageChange = (page: number, size: number) => {
  pagination.pageNo = page
  pagination.pageSize = size
  fetchCacheList()
}

// 选择变化处理
const handleSelectionChange = (selection: CacheItem[]) => {
  selectedRows.value = selection
}

// 查看缓存详情
const handleView = (row: CacheItem) => {
  currentCacheData.value = row
  detailDialogVisible.value = true
}

// 刷新TTL
const handleRefreshTTL = async (row: CacheItem) => {
  try {
    // 这里应该调用刷新TTL的API
    ElMessage.success('TTL刷新成功')
    await fetchCacheList()
  } catch (error) {
    console.error('刷新TTL失败:', error)
    ElMessage.error('刷新TTL失败')
  }
}

// 删除单个缓存
const handleDelete = async (row: CacheItem) => {
  try {
    await ElMessageBox.confirm(`确定要删除缓存键 "${row.key}" 吗？`, '确认删除', {
      type: 'warning',
    })

    await deleteCache(row.key)
    ElMessage.success('删除成功')
    await fetchCacheList()
    await fetchCacheStatus()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除缓存失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个缓存项吗？`,
      '确认批量删除',
      { type: 'warning' },
    )

    for (const row of selectedRows.value) {
      await deleteCache(row.key)
    }

    ElMessage.success('批量删除成功')
    await fetchCacheList()
    await fetchCacheStatus()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 刷新全部
const handleRefreshAll = () => {
  handleRefresh()
  refreshMemoryChart()
  refreshHitRateChart()
}

// 清理过期缓存
const handleClearExpired = async () => {
  try {
    await ElMessageBox.confirm('确定要清理所有过期的缓存项吗？', '确认清理', { type: 'warning' })

    await clearExpiredCache()
    ElMessage.success('清理过期缓存成功')
    await fetchCacheList()
    await fetchCacheStatus()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理过期缓存失败:', error)
      ElMessage.error('清理失败')
    }
  }
}

// 清空所有缓存
const handleFlushAll = async () => {
  try {
    await ElMessageBox.confirm(
      '警告：此操作将清空所有缓存数据，且不可恢复！确定要继续吗？',
      '危险操作确认',
      { type: 'error' },
    )

    await flushAllCache()
    ElMessage.success('清空缓存成功')
    await fetchCacheList()
    await fetchCacheStatus()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空缓存失败:', error)
      ElMessage.error('清空失败')
    }
  }
}

// 刷新内存图表
const refreshMemoryChart = () => {
  initMemoryChart()
}

// 刷新命中率图表
const refreshHitRateChart = () => {
  initHitRateChart()
}

// 组件挂载
onMounted(() => {
  fetchCacheStatus()
  fetchCacheList()

  // 延迟初始化图表
  setTimeout(() => {
    initMemoryChart()
    initHitRateChart()
  }, 100)

  // 设置定时刷新
  refreshTimer = setInterval(() => {
    fetchCacheStatus()
  }, 30000) // 30秒刷新一次

  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    memoryChart?.resize()
    hitRateChart?.resize()
  })
})

// 组件卸载
onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  memoryChart?.dispose()
  hitRateChart?.dispose()
  window.removeEventListener('resize', () => {
    memoryChart?.resize()
    hitRateChart?.resize()
  })
})
</script>

<style scoped>
.cache-monitor {
  padding: 20px;
}

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

.card-icon.redis {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.memory {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.keys {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.hit-rate {
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

.charts-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.cache-management {
  margin-bottom: 20px;
}

.search-section {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.danger {
  color: #f56c6c;
}

.danger:hover {
  color: #f78989;
}
</style>
