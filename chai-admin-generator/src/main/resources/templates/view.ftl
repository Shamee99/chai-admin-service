<template>
  <div class="${entityNameLower}-container">
    <el-card>
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryParams" class="search-form">
<#list columns as column>
<#if !column.isBaseField && column.javaType == 'String'>
        <el-form-item label="${column.columnComment!}">
          <el-input v-model="queryParams.${column.javaField}" placeholder="请输入${column.columnComment!}" clearable />
        </el-form-item>
</#if>
</#list>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb-3">
        <el-col :span="1.5">
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleDeleteBatch">批量删除</el-button>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
<#list columns as column>
<#if !column.isBaseField>
        <el-table-column prop="${column.javaField}" label="${column.columnComment!}" />
</#if>
</#list>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
<#list columns as column>
<#if !column.isBaseField && column.columnName != 'id'>
        <el-form-item label="${column.columnComment!}" prop="${column.javaField}">
<#if column.javaType == 'Integer' || column.javaType == 'Long'>
          <el-input-number v-model="formData.${column.javaField}" :min="0" />
<#elseif column.javaType == 'Boolean'>
          <el-switch v-model="formData.${column.javaField}" />
<#else>
          <el-input v-model="formData.${column.javaField}" placeholder="请输入${column.columnComment!}" />
</#if>
        </el-form-item>
</#if>
</#list>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  get${entityName}Page,
  add${entityName},
  update${entityName},
  delete${entityName},
  delete${entityName}Batch
} from './api/${apiFileName}'
import type { ${entityName}, ${entityName}QueryParams } from './api/${apiFileName}.types'

// 查询参数
const queryParams = reactive<${entityName}QueryParams>({
  current: 1,
  size: 10
})

// 表格数据
const tableData = ref<${entityName}[]>([])
const total = ref(0)
const loading = ref(false)
const selectedIds = ref<string[]>([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const formData = reactive<${entityName}>({})
const rules = reactive({
  // 添加表单验证规则
})

// 查询数据
const handleQuery = async () => {
  loading.value = true
  try {
    const { data } = await get${entityName}Page(queryParams)
    tableData.value = data.records
    total.value = data.total
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const handleReset = () => {
  queryParams.current = 1
  queryParams.size = 10
  handleQuery()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增${tableComment!}'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: ${entityName}) => {
  dialogTitle.value = '编辑${tableComment!}'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: ${entityName}) => {
  try {
    await ElMessageBox.confirm('确认删除该记录吗？', '提示', {
      type: 'warning'
    })
    await delete${entityName}(row.id!)
    ElMessage.success('删除成功')
    handleQuery()
  } catch (error) {
    // 用户取消删除
  }
}

// 批量删除
const handleDeleteBatch = async () => {
  try {
    await ElMessageBox.confirm('确认删除选中的记录吗？', '提示', {
      type: 'warning'
    })
    await delete${entityName}Batch(selectedIds.value)
    ElMessage.success('删除成功')
    handleQuery()
  } catch (error) {
    // 用户取消删除
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (formData.id) {
          await update${entityName}(formData)
          ElMessage.success('修改成功')
        } else {
          await add${entityName}(formData)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        handleQuery()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(formData).forEach(key => {
    delete formData[key as keyof ${entityName}]
  })
}

// 选择变化
const handleSelectionChange = (selection: ${entityName}[]) => {
  selectedIds.value = selection.map(item => item.id!)
}

// 初始化
onMounted(() => {
  handleQuery()
})
</script>

<style scoped lang="scss">
.${entityNameLower}-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }

  .mb-3 {
    margin-bottom: 15px;
  }
}
</style>

