<template>
  <!-- ${tableComment!}表单对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="handleDialogClose">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
<#list columns as column>
<#if !column.isBaseField && column.columnName != 'id'>
        <el-col :span="<#if column.javaType == 'String' && (column.columnComment!)?contains('描述')>24<#else>12</#if>">
          <el-form-item label="${column.columnComment!}" prop="${column.javaField}">
<#if column.javaType == 'Integer' || column.javaType == 'Long'>
            <el-input-number v-model="form.${column.javaField}" :min="0" placeholder="请输入${column.columnComment!}" style="width: 100%" />
<#elseif column.javaType == 'BigDecimal'>
            <el-input-number v-model="form.${column.javaField}" :min="0" :precision="2" placeholder="请输入${column.columnComment!}" style="width: 100%" />
<#elseif column.javaType == 'Boolean'>
            <el-radio-group v-model="form.${column.javaField}">
              <el-radio :label="true">是</el-radio>
              <el-radio :label="false">否</el-radio>
            </el-radio-group>
<#elseif column.javaType == 'LocalDateTime' || column.javaType == 'LocalDate'>
            <el-date-picker
              v-model="form.${column.javaField}"
              type="<#if column.javaType == 'LocalDate'>date<#else>datetime</#if>"
              placeholder="请选择${column.columnComment!}"
              style="width: 100%"
            />
<#elseif (column.columnComment!)?contains('描述') || (column.columnComment!)?contains('备注')>
            <el-input v-model="form.${column.javaField}" type="textarea" :rows="3" placeholder="请输入${column.columnComment!}" />
<#else>
            <el-input v-model="form.${column.javaField}" placeholder="请输入${column.columnComment!}" />
</#if>
          </el-form-item>
        </el-col>
</#if>
</#list>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import type { ${entityName} } from '../api/${apiFileName}.types.ts'
import { add${entityName} } from '../api/${apiFileName}.ts'

// Props 和 Emits
interface Props {
  visible?: boolean
  ${entityNameLower}Data?: ${entityName} | null
}

interface Emits {
  (e: 'update:visible', value: boolean): void
  (e: 'success'): void
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  ${entityNameLower}Data: null,
})

const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref<FormInstance>()
const dialogVisible = ref(false)
const dialogTitle = ref('新增${tableComment!}')

// 表单数据
const form = reactive<${entityName}>({
<#list columns as column>
<#if !column.isBaseField && column.columnName != 'id'>
  ${column.javaField}: <#if column.javaType == 'Integer' || column.javaType == 'Long'>0<#elseif column.javaType == 'BigDecimal'>0<#elseif column.javaType == 'Boolean'>false<#else>''</#if>,
</#if>
</#list>
})

// 表单验证规则
const rules = reactive<FormRules>({
  // 根据需要添加验证规则
<#list columns as column>
<#if !column.isBaseField && column.columnName != 'id' && !column.isNullable>
  ${column.javaField}: [{ required: true, message: '请输入${column.columnComment!}', trigger: 'blur' }],
</#if>
</#list>
})

// 监听 visible 变化
watch(
  () => props.visible,
  (val) => {
    dialogVisible.value = val
    if (val && props.${entityNameLower}Data) {
      Object.assign(form, props.${entityNameLower}Data)
    }
  },
)

// 监听 dialogVisible 变化
watch(dialogVisible, (val) => {
  emit('update:visible', val)
})

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await add${entityName}(form)
        ElMessage.success('新增成功')
        dialogVisible.value = false
        emit('success')
      } catch (error) {
        console.error('新增${tableComment!}失败:', error)
        ElMessage.error('新增失败')
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(form).forEach((key) => {
    delete form[key as keyof ${entityName}]
  })
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>

