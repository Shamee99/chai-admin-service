<template>
  <div class="login-log-management container-wrapper">
    <ChaiTable
      :data="tableData"
      :columns="tableColumns"
      :loading="loading"
      :pagination="paginationConfig"
      :search-form="searchForm"
      :show-selection="true"
      @page-change="handlePageChange"
      @search="handleSearch"
      @reset="handleReset"
      @refresh="handleRefresh"
      @selection-change="handleSelectionChange"
    >
      <!-- 搜索表单项 -->
      <template #search-items="{ searchForm }">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="searchForm.ipAddress" placeholder="请输入IP地址" clearable />
        </el-form-item>
        <el-form-item label="登录状态" style="width: 200px">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="成功" value="success" />
            <el-option label="失败" value="failed" />
          </el-select>
        </el-form-item>
        <el-form-item label="登录时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </template>

      <!-- 工具栏左侧 -->
      <template #toolbar-left="{ selectedRows }">
        <el-button type="success" :disabled="selectedRows.length === 0" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出选中
        </el-button>
        <el-button type="warning" @click="handleClearOldLogs">
          <el-icon><DeleteFilled /></el-icon>
          清理旧日志
        </el-button>
      </template>

      <!-- 登录状态列 -->
      <template #status="{ row }">
        <el-tag :type="row.status === 0 ? 'success' : 'danger'" size="small">
          {{ row.status === 0 ? '成功' : '失败' }}
        </el-tag>
      </template>
    </ChaiTable>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElButton, ElIcon, ElMessage, ElMessageBox, ElTag } from 'element-plus'
import { DeleteFilled, Download } from '@element-plus/icons-vue'
import type { LoginLog, LoginLogPageResponse, LoginLogQueryParams } from './api/login-log.types'
import { clearOldLogs, getLoginLogList } from './api/login-log'
import ChaiTable from '@/components/common/ChaiTable.vue'

const loading = ref(false)
const selectedRows = ref<LoginLog[]>([])
const detailDialogVisible = ref(false)
const currentLogData = ref<LoginLog | null>(null)

// 分页配置
const paginationConfig = computed(() => ({
  pageNo: pagination.pageNo,
  pageSize: pagination.pageSize,
  total: pagination.total,
  pageSizes: [10, 20, 50, 100],
}))

// 表格列配置
const tableColumns = [
  { prop: 'username', label: '用户名', width: 120, sortable: true },
  { prop: 'ipaddr', label: 'IP地址', width: 140 },
  { prop: 'loginLocation', label: '登录地点', width: 150 },
  { prop: 'browser', label: '浏览器' },
  { prop: 'os', label: '操作系统' },
  {
    prop: 'status',
    label: '登录状态',
    width: 120,
    align: 'center',
    slots: {
      default: 'status',
    },
  },
  { prop: 'loginTime', label: '登录时间', width: 180, sortable: true },
]

const searchForm = reactive({
  username: '',
  ipAddress: '',
  status: '',
  dateRange: null as [string, string] | null,
})

const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})

// 表格数据
const tableData = ref<LoginLogPageResponse[]>([])

// 获取登录日志列表
const fetchLoginLogList = async () => {
  loading.value = true
  try {
    const params: LoginLogQueryParams = {
      pageNo: pagination.pageNo,
      pageSize: pagination.pageSize,
      username: searchForm.username || undefined,
      ipAddress: searchForm.ipAddress || undefined,
      status: searchForm.status || undefined,
      startTime: searchForm.dateRange?.[0],
      endTime: searchForm.dateRange?.[1],
    }

    const response = await getLoginLogList(params)
    tableData.value = response.records
    pagination.total = response.total
  } catch (error) {
    console.error('获取登录日志列表失败:', error)
    ElMessage.error('获取登录日志列表失败')
  } finally {
    loading.value = false
  }
}

// 分页变化处理
const handlePageChange = (page: { pageNo: number; pageSize: number }) => {
  pagination.pageNo = page.pageNo
  pagination.pageSize = page.pageSize
  fetchLoginLogList()
}

// 搜索处理
const handleSearch = () => {
  pagination.pageNo = 1
  fetchLoginLogList()
}

// 重置处理
const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    ipAddress: '',
    status: '',
    dateRange: null,
  })
  pagination.pageNo = 1
  fetchLoginLogList()
}

// 刷新处理
const handleRefresh = () => {
  fetchLoginLogList()
}

// 选择变化处理
const handleSelectionChange = (selection: LoginLog[]) => {
  selectedRows.value = selection
}

// 查看详情
const handleViewDetail = (row: LoginLog) => {
  currentLogData.value = row
  detailDialogVisible.value = true
}

// 导出选中
const handleExport = () => {
  // TODO: 实现导出功能
  ElMessage.info('导出功能开发中...')
}

// 清理旧日志
const handleClearOldLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清理30天前的登录日志吗？此操作不可恢复！', '确认清理', {
      type: 'warning',
    })

    await clearOldLogs(30)
    ElMessage.success('清理完成')
    await fetchLoginLogList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理旧日志失败:', error)
      ElMessage.error('清理失败')
    }
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchLoginLogList()
})
</script>

<style scoped>
.login-log-management {
  height: 100%;
}
</style>
