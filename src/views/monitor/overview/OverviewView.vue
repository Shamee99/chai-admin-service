<template>
  <div class="monitor-overview">
    <!-- 系统状态卡片 -->
    <div class="status-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon success">
                <el-icon><Monitor /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">系统状态</div>
                <div class="card-value">{{ systemStatus.status }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon primary">
                <el-icon><User /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">在线用户</div>
                <div class="card-value">{{ systemStatus.onlineUsers }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon warning">
                <el-icon><Cpu /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">CPU使用率</div>
                <div class="card-value">{{ systemStatus.cpuUsage }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon danger">
                <el-icon><coin /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">内存使用率</div>
                <div class="card-value">{{ systemStatus.memoryUsage }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 实时图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>CPU使用率趋势 - [模拟]</span>
                <el-button type="text" @click="refreshCpuChart">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </template>
            <div ref="cpuChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>内存使用率趋势 - [模拟]</span>
                <el-button type="text" @click="refreshMemoryChart">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </template>
            <div ref="memoryChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 告警信息面板 -->
    <div class="alerts-section">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>系统告警</span>
            <el-badge :value="alerts.length" class="alert-badge">
              <el-button type="text" @click="refreshAlerts">
                <el-icon><Bell /></el-icon>
              </el-button>
            </el-badge>
          </div>
        </template>
        <el-table :data="alerts" style="width: 100%" max-height="300">
          <el-table-column prop="level" label="级别" width="80">
            <template #default="{ row }">
              <el-tag :type="getAlertType(row.level)" size="small">
                {{ row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="message" label="告警信息" />
          <el-table-column prop="time" label="时间" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '已处理' ? 'success' : 'warning'" size="small">
                {{ row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import {
  ElBadge,
  ElButton,
  ElCard,
  ElCol,
  ElIcon,
  ElRow,
  ElTable,
  ElTableColumn,
  ElTag,
} from 'element-plus'
import { Bell, Coin, Cpu, Monitor, Refresh, User } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getSystemAlerts, getSystemStatus } from './api/overview'
import type { SystemAlert, SystemStatus } from './api/overview.types'

// 响应式数据
const systemStatus = ref<SystemStatus>({
  status: '正常',
  onlineUsers: 0,
  cpuUsage: 0,
  memoryUsage: 0,
})

const alerts = ref<SystemAlert[]>([])

// 图表引用
const cpuChartRef = ref<HTMLDivElement>()
const memoryChartRef = ref<HTMLDivElement>()
let cpuChart: echarts.ECharts | null = null
let memoryChart: echarts.ECharts | null = null
let refreshTimer: NodeJS.Timeout | null = null

// 获取告警类型
const getAlertType = (level: string) => {
  switch (level) {
    case '严重':
      return 'danger'
    case '警告':
      return 'warning'
    case '信息':
      return 'info'
    default:
      return 'primary'
  }
}

// 初始化CPU图表
const initCpuChart = () => {
  if (!cpuChartRef.value) return

  cpuChart = echarts.init(cpuChartRef.value)
  const option = {
    title: {
      show: false,
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}%',
    },
    xAxis: {
      type: 'category',
      data: [],
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100,
      axisLabel: {
        formatter: '{value}%',
      },
    },
    series: [
      {
        name: 'CPU使用率',
        type: 'line',
        smooth: true,
        data: [],
        itemStyle: {
          color: '#409EFF',
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(64, 158, 255, 0.3)',
              },
              {
                offset: 1,
                color: 'rgba(64, 158, 255, 0.1)',
              },
            ],
          },
        },
      },
    ],
  }
  cpuChart.setOption(option)
}

// 初始化内存图表
const initMemoryChart = () => {
  if (!memoryChartRef.value) return

  memoryChart = echarts.init(memoryChartRef.value)
  const option = {
    title: {
      show: false,
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}%',
    },
    xAxis: {
      type: 'category',
      data: [],
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100,
      axisLabel: {
        formatter: '{value}%',
      },
    },
    series: [
      {
        name: '内存使用率',
        type: 'line',
        smooth: true,
        data: [],
        itemStyle: {
          color: '#67C23A',
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(103, 194, 58, 0.3)',
              },
              {
                offset: 1,
                color: 'rgba(103, 194, 58, 0.1)',
              },
            ],
          },
        },
      },
    ],
  }
  memoryChart.setOption(option)
}

// 刷新系统状态
const refreshSystemStatus = async () => {
  try {
    systemStatus.value = await getSystemStatus()
  } catch (error) {
    console.error('获取系统状态失败:', error)
  }
}

// 刷新告警信息
const refreshAlerts = async () => {
  try {
    alerts.value = await getSystemAlerts()
  } catch (error) {
    console.error('获取告警信息失败:', error)
  }
}

// 刷新CPU图表
const refreshCpuChart = () => {
  if (!cpuChart) return

  // 模拟数据更新
  const now = new Date()
  const times = []
  const values = []

  for (let i = 9; i >= 0; i--) {
    const time = new Date(now.getTime() - i * 60000)
    times.push(time.toLocaleTimeString())
    values.push(Math.floor(Math.random() * 100))
  }

  cpuChart.setOption({
    xAxis: {
      data: times,
    },
    series: [
      {
        data: values,
      },
    ],
  })
}

// 刷新内存图表
const refreshMemoryChart = () => {
  if (!memoryChart) return

  // 模拟数据更新
  const now = new Date()
  const times = []
  const values = []

  for (let i = 9; i >= 0; i--) {
    const time = new Date(now.getTime() - i * 60000)
    times.push(time.toLocaleTimeString())
    values.push(Math.floor(Math.random() * 100))
  }

  memoryChart.setOption({
    xAxis: {
      data: times,
    },
    series: [
      {
        data: values,
      },
    ],
  })
}

// 启动定时刷新
const startAutoRefresh = () => {
  refreshTimer = setInterval(() => {
    refreshSystemStatus()
    refreshCpuChart()
    refreshMemoryChart()
  }, 30000) // 30秒刷新一次
}

// 停止定时刷新
const stopAutoRefresh = () => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

// 组件挂载
onMounted(async () => {
  await refreshSystemStatus()
  await refreshAlerts()

  // 初始化图表
  setTimeout(() => {
    initCpuChart()
    initMemoryChart()
    refreshCpuChart()
    refreshMemoryChart()
  }, 100)

  // 启动自动刷新
  startAutoRefresh()
})

// 组件卸载
onUnmounted(() => {
  stopAutoRefresh()
  if (cpuChart) {
    cpuChart.dispose()
  }
  if (memoryChart) {
    memoryChart.dispose()
  }
})
</script>

<style scoped>
.monitor-overview {
  padding: 20px;
}

.status-cards {
  margin-bottom: 20px;
}

.status-card {
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
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.card-icon.success {
  background-color: #67c23a;
}

.card-icon.primary {
  background-color: #409eff;
}

.card-icon.warning {
  background-color: #e6a23c;
}

.card-icon.danger {
  background-color: #f56c6c;
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
}

.charts-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.alerts-section {
  margin-bottom: 20px;
}

.alert-badge {
  margin-left: 10px;
}
</style>
