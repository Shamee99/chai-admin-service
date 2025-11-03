<template>
  <el-dialog
    v-model="dialogVisible"
    title="缓存详情"
    width="800px"
    :before-close="handleClose"
  >
    <div v-if="cacheData" class="cache-detail">
      <!-- 基本信息 -->
      <div class="detail-section">
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="缓存键">
            <el-text class="cache-key">{{ cacheData.key }}</el-text>
            <el-button type="text" size="small" @click="copyToClipboard(cacheData.key)">
              <el-icon><CopyDocument /></el-icon>
            </el-button>
          </el-descriptions-item>
          <el-descriptions-item label="数据类型">
            <el-tag :type="getTypeTagType(cacheData.type)">{{ cacheData.type.toUpperCase() }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数据大小">
            {{ formatBytes(cacheData.size) }}
          </el-descriptions-item>
          <el-descriptions-item label="TTL">
            <span v-if="cacheData.ttl === -1" class="ttl-permanent">永久</span>
            <span v-else-if="cacheData.ttl === 0" class="ttl-expired">已过期</span>
            <span v-else class="ttl-active">{{ formatTTL(cacheData.ttl) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ cacheData.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="最后访问">
            {{ cacheData.lastAccess }}
          </el-descriptions-item>
          <el-descriptions-item label="访问次数">
            {{ cacheData.accessCount || 0 }} 次
          </el-descriptions-item>
          <el-descriptions-item label="编码方式">
            {{ cacheData.encoding || 'N/A' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 详细信息 -->
      <div v-if="detailData" class="detail-section">
        <el-descriptions title="详细信息" :column="2" border>
          <el-descriptions-item label="内存使用">
            {{ formatBytes(detailData.memoryUsage) }}
          </el-descriptions-item>
          <el-descriptions-item label="序列化长度">
            {{ formatBytes(detailData.serializedLength) }}
          </el-descriptions-item>
          <el-descriptions-item label="引用计数">
            {{ detailData.objectRefcount }}
          </el-descriptions-item>
          <el-descriptions-item label="对象编码">
            {{ detailData.objectEncoding }}
          </el-descriptions-item>
          <el-descriptions-item label="空闲时间">
            {{ formatSeconds(detailData.objectIdletime) }}
          </el-descriptions-item>
          <el-descriptions-item label="过期时间">
            {{ detailData.expireAt || 'N/A' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 缓存值 -->
      <div class="detail-section">
        <el-divider content-position="left">
          <span>缓存值</span>
          <el-button-group style="margin-left: 16px">
            <el-button
              size="small"
              :type="viewMode === 'formatted' ? 'primary' : 'default'"
              @click="viewMode = 'formatted'"
            >
              格式化
            </el-button>
            <el-button
              size="small"
              :type="viewMode === 'raw' ? 'primary' : 'default'"
              @click="viewMode = 'raw'"
            >
              原始
            </el-button>
            <el-button
              size="small"
              :type="viewMode === 'hex' ? 'primary' : 'default'"
              @click="viewMode = 'hex'"
            >
              十六进制
            </el-button>
          </el-button-group>
        </el-divider>
        
        <div class="cache-value-container">
          <div class="value-toolbar">
            <div class="toolbar-left">
              <el-text size="small" type="info">
                长度: {{ getValueLength() }} | 类型: {{ typeof displayValue }}
              </el-text>
            </div>
            <div class="toolbar-right">
              <el-button type="text" size="small" @click="copyValue">
                <el-icon><CopyDocument /></el-icon>
                复制
              </el-button>
              <el-button type="text" size="small" @click="downloadValue">
                <el-icon><Download /></el-icon>
                下载
              </el-button>
            </div>
          </div>
          
          <el-input
            v-model="displayValue"
            type="textarea"
            :rows="12"
            readonly
            class="cache-value-input"
          />
        </div>
      </div>

      <!-- 操作历史 -->
      <div class="detail-section">
        <el-divider content-position="left">操作历史</el-divider>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in operationHistory"
            :key="index"
            :timestamp="item.timestamp"
            :type="getOperationTypeColor(item.type)"
          >
            <div class="operation-item">
              <div class="operation-title">
                <el-icon><component :is="getOperationIcon(item.type)" /></el-icon>
                {{ item.operation }}
              </div>
              <div class="operation-desc">{{ item.description }}</div>
              <div v-if="item.details" class="operation-details">
                <el-text size="small" type="info">{{ item.details }}</el-text>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="footer-left">
          <el-button type="warning" @click="handleRefreshTTL">
            <el-icon><Timer /></el-icon>
            刷新TTL
          </el-button>
          <el-button type="danger" @click="handleDelete">
            <el-icon><Delete /></el-icon>
            删除缓存
          </el-button>
        </div>
        <div class="footer-right">
          <el-button @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
          <el-button @click="handleClose">关闭</el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import {
  ElDialog,
  ElDescriptions,
  ElDescriptionsItem,
  ElText,
  ElTag,
  ElButton,
  ElButtonGroup,
  ElIcon,
  ElDivider,
  ElInput,
  ElTimeline,
  ElTimelineItem,
  ElMessage,
  ElMessageBox
} from 'element-plus'
import {
  CopyDocument,
  Download,
  Timer,
  Delete,
  Refresh,
  View,
  Edit,
  Plus,
  Minus
} from '@element-plus/icons-vue'
import type { CacheItem, CacheDetail } from '../api/cache.types'
import { getCacheDetail, deleteCache, setCacheTTL } from '../api/cache'

interface Props {
  visible: boolean
  cacheData?: CacheItem | null
}

interface Emits {
  (e: 'update:visible', value: boolean): void
  (e: 'refresh'): void
}

interface OperationHistoryItem {
  timestamp: string
  type: 'create' | 'read' | 'update' | 'delete' | 'expire'
  operation: string
  description: string
  details?: string
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const detailData = ref<CacheDetail | null>(null)
const viewMode = ref<'formatted' | 'raw' | 'hex'>('formatted')
const loading = ref(false)

// 模拟操作历史数据
const operationHistory = ref<OperationHistoryItem[]>([
  {
    timestamp: '2024-01-15 14:25:30',
    type: 'read',
    operation: '读取操作',
    description: '缓存被应用程序读取',
    details: 'GET命令执行，耗时: 0.5ms'
  },
  {
    timestamp: '2024-01-15 12:15:20',
    type: 'update',
    operation: '更新操作',
    description: '缓存值被更新',
    details: 'SET命令执行，新值大小: 1024 bytes'
  },
  {
    timestamp: '2024-01-15 10:30:00',
    type: 'create',
    operation: '创建操作',
    description: '缓存项被创建',
    details: '初始TTL: 3600秒'
  }
])

// 显示的缓存值
const displayValue = computed(() => {
  if (!props.cacheData?.value) return ''
  
  const value = props.cacheData.value
  
  switch (viewMode.value) {
    case 'formatted':
      if (typeof value === 'object') {
        return JSON.stringify(value, null, 2)
      }
      return String(value)
    case 'raw':
      return String(value)
    case 'hex':
      return stringToHex(String(value))
    default:
      return String(value)
  }
})

// 获取数据类型标签颜色
const getTypeTagType = (type: string) => {
  const typeMap: Record<string, string> = {
    string: 'primary',
    hash: 'success',
    list: 'warning',
    set: 'info',
    zset: 'danger'
  }
  return typeMap[type] || 'default'
}

// 格式化字节数
const formatBytes = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化TTL
const formatTTL = (seconds: number) => {
  if (seconds < 60) return `${seconds}秒`
  if (seconds < 3600) return `${Math.floor(seconds / 60)}分${seconds % 60}秒`
  if (seconds < 86400) return `${Math.floor(seconds / 3600)}小时${Math.floor((seconds % 3600) / 60)}分`
  return `${Math.floor(seconds / 86400)}天${Math.floor((seconds % 86400) / 3600)}小时`
}

// 格式化秒数
const formatSeconds = (seconds: number) => {
  if (seconds < 60) return `${seconds}秒`
  if (seconds < 3600) return `${Math.floor(seconds / 60)}分钟`
  if (seconds < 86400) return `${Math.floor(seconds / 3600)}小时`
  return `${Math.floor(seconds / 86400)}天`
}

// 字符串转十六进制
const stringToHex = (str: string) => {
  return str.split('').map(char => 
    char.charCodeAt(0).toString(16).padStart(2, '0')
  ).join(' ')
}

// 获取值长度
const getValueLength = () => {
  if (!props.cacheData?.value) return 0
  return String(props.cacheData.value).length
}

// 获取操作类型颜色
const getOperationTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    create: 'success',
    read: 'primary',
    update: 'warning',
    delete: 'danger',
    expire: 'info'
  }
  return colorMap[type] || 'primary'
}

// 获取操作图标
const getOperationIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    create: Plus,
    read: View,
    update: Edit,
    delete: Minus,
    expire: Timer
  }
  return iconMap[type] || View
}

// 复制到剪贴板
const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败')
  }
}

// 复制缓存值
const copyValue = () => {
  copyToClipboard(displayValue.value)
}

// 下载缓存值
const downloadValue = () => {
  const blob = new Blob([displayValue.value], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `cache_${props.cacheData?.key?.replace(/[^a-zA-Z0-9]/g, '_')}.txt`
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

// 刷新TTL
const handleRefreshTTL = async () => {
  if (!props.cacheData) return
  
  try {
    const { value } = await ElMessageBox.prompt(
      '请输入新的TTL值（秒）：',
      '刷新TTL',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+$/,
        inputErrorMessage: '请输入有效的数字'
      }
    )
    
    const ttl = parseInt(value)
    await setCacheTTL(props.cacheData.key, ttl)
    ElMessage.success('TTL刷新成功')
    await fetchCacheDetail()
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('刷新TTL失败:', error)
      ElMessage.error('刷新TTL失败')
    }
  }
}

// 删除缓存
const handleDelete = async () => {
  if (!props.cacheData) return
  
  try {
    await ElMessageBox.confirm(
      `确定要删除缓存键 "${props.cacheData.key}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )
    
    await deleteCache(props.cacheData.key)
    ElMessage.success('删除成功')
    emit('refresh')
    handleClose()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除缓存失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 刷新详情
const handleRefresh = () => {
  fetchCacheDetail()
}

// 获取缓存详情
const fetchCacheDetail = async () => {
  if (!props.cacheData) return
  
  loading.value = true
  try {
    detailData.value = await getCacheDetail(props.cacheData.key)
  } catch (error) {
    console.error('获取缓存详情失败:', error)
    ElMessage.error('获取缓存详情失败')
  } finally {
    loading.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  detailData.value = null
  viewMode.value = 'formatted'
}

// 监听缓存数据变化
watch(
  () => props.cacheData,
  (newData) => {
    if (newData && props.visible) {
      fetchCacheDetail()
    }
  },
  { immediate: true }
)

// 组件挂载
onMounted(() => {
  if (props.cacheData && props.visible) {
    fetchCacheDetail()
  }
})
</script>

<style scoped>
.cache-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 24px;
}

.cache-key {
  font-family: 'Courier New', monospace;
  font-weight: bold;
  margin-right: 8px;
}

.ttl-permanent {
  color: #67c23a;
  font-weight: bold;
}

.ttl-expired {
  color: #f56c6c;
  font-weight: bold;
}

.ttl-active {
  color: #409eff;
  font-weight: bold;
}

.cache-value-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.value-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.toolbar-right {
  display: flex;
  gap: 8px;
}

.cache-value-input {
  border: none;
}

.cache-value-input :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
}

.operation-item {
  padding: 8px 0;
}

.operation-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  margin-bottom: 4px;
}

.operation-desc {
  color: #606266;
  margin-bottom: 4px;
}

.operation-details {
  font-size: 12px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left,
.footer-right {
  display: flex;
  gap: 8px;
}
</style>