<template>
  <el-container class="file-uploader">
    <el-header>
      <h1>文件上传</h1>
    </el-header>

    <!-- 文件上传表单 -->
    <el-main>
      <el-upload
        class="upload-demo"
        drag
        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        :auto-upload="false"
        :file-list="fileList"
        :on-change="onFileChange"
        :on-remove="handleRemove"
        :limit="1"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">文件大小限制在10MB内</div>
        </template>
      </el-upload>

      <el-button
        type="primary"
        @click="uploadFile"
        :disabled="!selectedFile"
        style="margin-top: 20px"
      >
        上传
      </el-button>
    </el-main>

    <el-header>
      <h1>文件列表</h1>
    </el-header>

    <!-- 搜索框(Experiment)
      <el-row style="padding: 10px">
        <el-input
          v-model="searchText"
          placeholder="输入关键词搜索课程"
          style="width: 300px"
          clearable
          size="large"
        />
        <el-button
          style="margin-left: 10px"
          size="large"
          type="primary"
          @click="searchCourses()"
          >搜索</el-button
        >
        <el-button style="margin-left: 10px" @click="resetSearch" size="large"
          >重置</el-button
        >
      </el-row> -->

    <!-- 文件列表 -->
    <el-main>
      <el-table :data="files" style="width: 100%" max-height="500px">
        <el-table-column prop="filename" label="文件名"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="text" @click="downloadFile(scope.row.id)">
              下载
            </el-button>
            <el-button type="text" @click="deleteFile(scope.row.filepath)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 定义文件信息的类型
interface FileInfo {
  id: number
  filename: string
  filepath: string
}

// 使用ref声明响应式变量
const selectedFile = ref<File | null>(null)
const fileList = ref<any[]>([])
const files = ref<FileInfo[]>([])
// const searchText = ref('')

// 文件选择事件处理
const onFileChange = (file: File) => {
  selectedFile.value = file.raw
  fileList.value = [file]
}

// 从上传处移除文件
const handleRemove = () => {
  selectedFile.value = null
  fileList.value = []
}

// 文件上传
const uploadFile = async () => {
  if (!selectedFile.value) {
    ElMessage.error('请选择一个文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    // const response = await axios.post('http://localhost:8081/files', formData, {
    const response = await axios.post(
      'http://129.204.131.237:8081/files',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )
    if (response.data.code === 200) {
      ElMessage.success('文件上传成功')
      await fetchFiles() // 上传成功后刷新文件列表
      handleRemove() // 清除已上传的文件
    } else if (response.data.code === 500) {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('文件上传失败')
  }
}

// 获取文件列表
const fetchFiles = async () => {
  try {
    // const response = await axios.get('http://localhost:8081/files')
    const response = await axios.get('http://129.204.131.237:8081/files')
    files.value = response.data.data
    ElMessage.success('获取文件列表成功')
  } catch (error) {
    console.error('获取文件列表失败:', error)
    ElMessage.error('获取文件列表失败')
  }
}

// 文件下载
const downloadFile = async (fileId: number) => {
  try {
    // const response = await axios.get(`http://localhost:8081/files/${fileId}`, {
    const response = await axios.get(
      `http://129.204.131.237:8081/files/${fileId}`,
      {
        responseType: 'blob' // 确保处理为 Blob 类型
      }
    )

    // 获取文件名
    const contentDisposition = response.headers['content-disposition']
    let fileName = 'downloaded-file'
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(
        /filename[^;=\n]*=['"]?([^'";\n]*)/
      )
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = fileNameMatch[1]
      }
    }

    // 创建 Blob 对象和链接
    const url = window.URL.createObjectURL(
      new Blob([response.data], { type: response.headers['content-type'] })
    )
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName) // 设置下载的文件名
    document.body.appendChild(link)
    link.click()

    // 清除链接和 Blob 对象
    window.URL.revokeObjectURL(url)
    document.body.removeChild(link)
  } catch (error) {
    console.error('下载失败:', error)
    ElMessage.error('文件下载失败')
  }
}

// 文件删除
const deleteFile = async (filePath: string) => {
  try {
    // 发送DELETE请求到后端API
    // const response = await axios.delete('http://localhost:8081/files', {
    const response = await axios.delete('http://129.204.131.237:8081/files', {
      params: {
        filepath: filePath // 传递文件路径作为参数
      }
    })
    ElMessage.success(response.data.data)
    fetchFiles()
  } catch (error) {
    // 处理错误并设置错误消息
    if (axios.isAxiosError(error) && error.response) {
      console.log(error)
    } else {
      console.log(error)
    }
  }
}

// 组件挂载时获取文件列表
onMounted(() => {
  fetchFiles()
})
</script>

<style scoped>
h1 {
  font-size: 35px;
}

.file-uploader {
  margin: 20px;
}

.upload-demo {
  margin-bottom: 20px;
}

.el-upload__text {
  font-size: 16px;
}

.el-upload__text em {
  color: #409eff;
  cursor: pointer;
}
</style>
