<template>
  <div class="office-demo">
    <div class="demo-header">
      <h2>Office文档预览演示</h2>
      <p>支持Word、Excel、PowerPoint等文档格式的在线预览</p>
    </div>

    <div class="demo-content">
      <!-- 文件来源选择 -->
      <el-card class="source-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>文件来源选择</span>
          </div>
        </template>

        <el-radio-group v-model="fileSource" class="source-options">
          <el-radio label="local">本地示例文档</el-radio>
          <el-radio label="remote">远程文档URL</el-radio>
          <el-radio label="upload">上传文件</el-radio>
        </el-radio-group>
      </el-card>

      <!-- 远程URL输入 -->
      <el-card v-if="fileSource === 'remote'" class="url-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>远程文档URL</span>
          </div>
        </template>

        <div class="url-input-container">
          <el-input v-model="remoteUrl" placeholder="请输入文档的远程URL地址" clearable>
            <template #append>
              <el-button @click="previewRemoteFile" :disabled="!remoteUrl">预览</el-button>
            </template>
          </el-input>
        </div>
      </el-card>

      <!-- 文件上传区域 -->
      <el-card v-if="fileSource === 'upload'" class="upload-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>文件上传</span>
          </div>
        </template>

        <el-upload
          ref="uploadRef"
          class="upload-demo"
          drag
          :auto-upload="false"
          :on-change="handleFileChange"
          :show-file-list="false"
          accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
          <template #tip>
            <div class="el-upload__tip">支持 .doc/.docx/.xls/.xlsx/.ppt/.pptx/.pdf 格式文件</div>
          </template>
        </el-upload>
      </el-card>

      <!-- 示例文档 -->
      <el-card v-if="fileSource === 'local'" class="sample-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>本地示例文档</span>
          </div>
        </template>

        <div class="sample-files">
          <div
            v-for="sample in localSampleFiles"
            :key="sample.name"
            class="sample-item"
            @click="previewSample(sample)"
          >
            <el-icon class="file-icon" :style="{ color: sample.color }">
              <component :is="sample.icon" />
            </el-icon>
            <div class="file-info">
              <div class="file-name">{{ sample.name }}</div>
              <div class="file-type">{{ sample.type }}</div>
            </div>
          </div>
        </div>

        <el-alert class="sample-tip" title="提示" type="info" :closable="false" show-icon>
          <template #default>
            本地示例文档位于
            <code>/public/samples/</code>
            目录下。包含真实的Office文档格式（Word、Excel、PowerPoint）。
          </template>
        </el-alert>
      </el-card>

      <!-- 预览区域 -->
      <el-card v-if="currentFile" class="preview-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>文档预览 - {{ currentFile.name }}</span>
            <el-button @click="closePreview" size="small" type="danger" plain> 关闭预览 </el-button>
          </div>
        </template>

        <div class="preview-container">
          <!-- 尝试使用iframe预览 -->
          <iframe
            v-if="currentFile.url && ['pdf'].includes(currentFile.type)"
            :src="currentFile.url"
            width="100%"
            height="600px"
            frameborder="0"
          ></iframe>

          <!-- 对于Office文档，显示下载链接 -->
          <div
            v-else-if="currentFile.url && ['docx', 'xlsx', 'pptx'].includes(currentFile.type)"
            class="office-preview"
          >
            <div class="preview-notice">
              <el-icon class="notice-icon"><Document /></el-icon>
              <h3>Office文档预览</h3>
              <p>由于浏览器限制，Office文档无法直接预览。</p>
              <p>文件路径: {{ currentFile.url }}</p>
              <el-button type="primary" @click="downloadFile(currentFile.url, currentFile.name)">
                <el-icon><UploadFilled /></el-icon>
                下载文档
              </el-button>
            </div>
          </div>

          <!-- vue-files-preview组件（备用） -->
          <vue-files-preview
            v-else-if="currentFile.url"
            :src="currentFile.url"
            :type="currentFile.type"
            :height="600"
            @error="handlePreviewError"
          />
          <div v-else class="preview-loading">
            <el-icon class="is-loading"><loading /></el-icon>
            <span>正在加载文档...</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, Files, Grid, Loading, Present, UploadFilled } from '@element-plus/icons-vue'

interface FileItem {
  name: string
  type: string
  url?: string
  file?: File
  color: string
  icon: any
}

const uploadRef = ref()
const currentFile = ref<FileItem | null>(null)
const fileSource = ref('local') // 文件来源：local, remote, upload
const remoteUrl = ref('')

// 本地示例文件
const localSampleFiles = reactive<FileItem[]>([
  {
    name: 'sample.docx',
    type: 'docx',
    color: '#2B579A',
    icon: Document,
    url: '/samples/sample.docx',
  },
  {
    name: 'sample.xlsx',
    type: 'xlsx',
    color: '#217346',
    icon: Grid,
    url: '/samples/sample.xlsx',
  },
  {
    name: 'sample.pptx',
    type: 'pptx',
    color: '#D24726',
    icon: Present,
    url: '/samples/sample.pptx',
  },
  {
    name: 'sample.pdf',
    type: 'pdf',
    color: '#FF0000',
    icon: Files,
    url: '/samples/sample.pdf',
  },
])

// 处理文件上传
const handleFileChange = (file: any) => {
  const allowedTypes = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf']
  const fileExtension = file.name.split('.').pop()?.toLowerCase()

  if (!allowedTypes.includes(fileExtension || '')) {
    ElMessage.error('不支持的文件格式')
    return
  }

  // 创建文件URL用于预览
  const fileUrl = URL.createObjectURL(file.raw)

  currentFile.value = {
    name: file.name,
    type: fileExtension || '',
    url: fileUrl,
    file: file.raw,
    color: getFileColor(fileExtension || ''),
    icon: getFileIcon(fileExtension || ''),
  }

  ElMessage.success('文件上传成功，开始预览')
}

// 预览示例文件
const previewSample = (sample: FileItem) => {
  currentFile.value = { ...sample }
  ElMessage.success(`正在预览 ${sample.name}`)
}

// 预览远程文件
const previewRemoteFile = () => {
  if (!remoteUrl.value) {
    ElMessage.warning('请输入远程文件URL')
    return
  }

  // 从URL中提取文件名和类型
  const urlParts = remoteUrl.value.split('/')
  const fileName = urlParts[urlParts.length - 1] || '远程文档'
  const fileExtension = fileName.split('.').pop()?.toLowerCase() || 'unknown'

  currentFile.value = {
    name: fileName,
    type: fileExtension,
    url: remoteUrl.value,
    color: getFileColor(fileExtension),
    icon: getFileIcon(fileExtension),
  }

  ElMessage.success(`正在预览远程文档: ${fileName}`)
}

// 关闭预览
const closePreview = () => {
  if (currentFile.value?.url && currentFile.value.file) {
    URL.revokeObjectURL(currentFile.value.url)
  }
  currentFile.value = null
}

// 处理预览错误
const handlePreviewError = (error: any) => {
  console.error('预览错误:', error)
  ElMessage.error('文档预览失败，请检查文件格式或网络连接')
}

// 获取文件颜色
const getFileColor = (type: string): string => {
  const colorMap: Record<string, string> = {
    doc: '#2B579A',
    docx: '#2B579A',
    xls: '#217346',
    xlsx: '#217346',
    ppt: '#D24726',
    pptx: '#D24726',
    pdf: '#FF0000',
    txt: '#666666',
    html: '#E34F26',
  }
  return colorMap[type] || '#666666'
}

// 获取文件图标
const getFileIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    doc: Document,
    docx: Document,
    xls: Grid,
    xlsx: Grid,
    ppt: Present,
    pptx: Present,
    pdf: Files,
    txt: Document,
    html: Document,
  }
  return iconMap[type] || Document
}

// 下载文件
const downloadFile = (url: string, filename: string) => {
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.target = '_blank'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success(`开始下载: ${filename}`)
}
</script>

<style scoped>
.office-demo {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.demo-header {
  text-align: center;
  margin-bottom: 30px;
}

.demo-header h2 {
  color: var(--el-text-color-primary);
  margin-bottom: 10px;
  font-size: 28px;
  font-weight: 600;
}

.demo-header p {
  color: var(--el-text-color-regular);
  font-size: 16px;
}

.demo-content {
  display: grid;
  gap: 20px;
}

.source-card,
.url-card,
.upload-card,
.sample-card,
.preview-card {
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

.upload-demo {
  width: 100%;
}

.upload-demo .el-upload {
  width: 100%;
}

.upload-demo .el-upload-dragger {
  width: 100%;
  height: 180px;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  background: var(--el-fill-color-lighter);
  transition: all 0.3s;
}

.upload-demo .el-upload-dragger:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.sample-files {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.sample-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: var(--el-bg-color);
}

.sample-item:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.file-icon {
  font-size: 32px;
  margin-right: 12px;
}

.file-info {
  flex: 1;
}

.file-name {
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.file-type {
  font-size: 12px;
  color: var(--el-text-color-regular);
  text-transform: uppercase;
}

.preview-container {
  min-height: 600px;
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}

.preview-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 600px;
  color: var(--el-text-color-regular);
}

.preview-loading .el-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.source-options {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.url-input-container {
  width: 100%;
}

.sample-tip {
  margin-top: 15px;
}

.sample-tip code {
  background: var(--el-color-info-light-9);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.office-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 600px;
  background: var(--el-fill-color-lighter);
}

.preview-notice {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 400px;
}

.notice-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-bottom: 20px;
}

.preview-notice h3 {
  color: var(--el-text-color-primary);
  margin-bottom: 15px;
  font-size: 20px;
}

.preview-notice p {
  color: var(--el-text-color-regular);
  margin-bottom: 10px;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .office-demo {
    padding: 15px;
  }

  .sample-files {
    grid-template-columns: 1fr;
  }

  .demo-header h2 {
    font-size: 24px;
  }
}
</style>
