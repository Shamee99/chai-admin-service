<template>
  <div class="${entityNameLower}-management container-wrapper">
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
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
        <el-form-item label="${column.columnComment!}">
          <el-input v-model="searchForm.${column.javaField}" placeholder="请输入${column.columnComment!}" clearable />
        </el-form-item>
</#if>
</#list>
      </template>

      <!-- 工具栏左侧 -->
      <template #toolbar-left="{ selectedRows }">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增${tableComment!}
        </el-button>
        <el-button type="success" :disabled="selectedRows.length === 0" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出选中
        </el-button>
      </template>

      <!-- 操作列 -->
      <template #actions="{ row }">
        <el-button type="text" size="small" @click="handleEdit(row)">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
        <el-button type="text" size="small" class="danger" @click="handleDelete(row)">
          <el-icon><Delete /></el-icon>
          删除
        </el-button>
      </template>
    </ChaiTable>

    <!-- 新增对话框组件 -->
    <${entityName}Add
      v-model:visible="addDialogVisible"
      @success="handleFormSuccess"
    />

    <!-- 编辑对话框组件 -->
    <${entityName}Edit
      v-model:visible="editDialogVisible"
      :${entityNameLower}-data="current${entityName}"
      :${entityNameLower}-id="current${entityName}Id"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElButton, ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Download, Edit, Plus } from '@element-plus/icons-vue'
import type { ${entityName}, ${entityName}QueryParams } from './api/${apiFileName}.types'
import {
  create${entityName}ListParams,
  delete${entityName},
  get${entityName}List,
} from './api/${apiFileName}'
import ChaiTable from '@/components/common/ChaiTable.vue'
import ${entityName}Add from './components/${entityName}Add.vue'
import ${entityName}Edit from './components/${entityName}Edit.vue'
import type { PageParams } from '@/components/common/api/page.types.ts'

const loading = ref(false)
const selectedRows = ref<${entityName}[]>([])
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const current${entityName} = ref<${entityName} | null>(null)
const current${entityName}Id = ref<string | null>(null)

// 分页配置
const paginationConfig = computed(() => ({
  pageNo: pagination.pageNo,
  pageSize: pagination.pageSize,
  total: pagination.total,
  pageSizes: [10, 20, 50, 100],
}))

// 基础表格列配置
const tableColumns = [
<#list columns as column>
<#if !column.isBaseField>
  { prop: '${column.javaField}', label: '${column.columnComment!}', width: 150<#if column.javaType == 'LocalDateTime' || column.javaType == 'LocalDate'>, sortable: true</#if> },
</#if>
</#list>
  { prop: 'actions', label: '操作', width: 180, fixed: 'right', slots: { default: 'actions' } },
]

const searchForm = reactive({
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
  ${column.javaField}: '',
</#if>
</#list>
})

const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})

// 表格数据
const tableData = ref<${entityName}[]>([])

// 新增${tableComment!}
const handleAdd = () => {
  current${entityName}.value = null
  addDialogVisible.value = true
}

// 编辑${tableComment!}
const handleEdit = (row: ${entityName}) => {
  current${entityName}.value = { ...row }
  current${entityName}Id.value = row.id || null
  editDialogVisible.value = true
}

// 表单提交成功回调
const handleFormSuccess = () => {
  loadData()
}

const handleSearch = () => {
  pagination.pageNo = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
    ${column.javaField}: '',
</#if>
</#list>
  })
  pagination.pageNo = 1
  loadData()
}

const handleDelete = async (row: ${entityName}) => {
  await ElMessageBox.confirm(`确定要删除"${r"${row."}${columns[0].javaField}${r"}"}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })

  // 调用删除API
  if (row.id) {
    await delete${entityName}(row.id)
    ElMessage.success('删除成功')
    // 重新加载数据
    await loadData()
  }
}

// 表格事件处理
const handleRefresh = () => {
  loadData()
}

const handleExport = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }
  console.log('导出数据:', selectedRows.value)
  ElMessage.success('导出成功')
}

// 选择改变
const handleSelectionChange = (selection: ${entityName}[]) => {
  selectedRows.value = selection
}

// 分页事件处理
const handlePageChange = (page: PageParams) => {
  pagination.pageNo = page.pageNo
  pagination.pageSize = page.pageSize
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    // 构建查询参数
    const queryParams: ${entityName}QueryParams = {}
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
    if (searchForm.${column.javaField}) {
      queryParams.${column.javaField} = searchForm.${column.javaField}
    }
</#if>
</#list>

    // 创建请求参数
    const params = create${entityName}ListParams(pagination.pageNo, pagination.pageSize, queryParams)

    // 调用API获取数据
    const response = await get${entityName}List(params)

    tableData.value = response.records
    pagination.total = response.total
  } catch (error) {
    console.error('获取${tableComment!}列表失败:', error)
    ElMessage.error('获取${tableComment!}列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.${entityNameLower}-management {
  padding: 0;
  height: 100%;
}

.danger {
  color: #f56c6c;
}
</style>
