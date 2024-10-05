<template>
  <!-- 课程管理内容 -->
  <el-col :span="19">
    <el-row>
      <el-col :span="11">
        <h1>课程管理</h1>
      </el-col>
      <el-col
        :span="12"
        style="display: flex; align-items: center; justify-content: flex-end"
      >
        <el-button
          size="large"
          type="primary"
          @click="openCreateDialog"
          style=""
          >创建新课程</el-button
        >
      </el-col>
    </el-row>

    <!-- 搜索框 -->
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
    </el-row>

    <!-- 课程管理表单 -->
    <el-table
      :data="courses"
      size="large"
      style="width: 100%; margin-top: 20px"
      max-height="700"
    >
      <el-table-column prop="title" label="课程名称"></el-table-column>
      <el-table-column
        prop="description"
        label="描述"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="schedule" label="时间"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            type="primary"
            size="default"
            @click="editCourse(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="default"
            @click="deleteTheCourse(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-col :span="1"></el-col>

    <!-- 创建或编辑课程的弹窗 -->
    <el-dialog v-model="dialogVisible" draggable="true" top="25vh" width="65vh">
      <template #header>
        <div class="dialog-header">
          <h2 style="padding-bottom: 0px">课程信息</h2>
        </div>
      </template>
      <el-form :model="currentCourse" :rules="rules" ref="courseForm">
        <el-form-item label="课程名称" prop="title">
          <el-input
            v-model="currentCourse.title"
            placeholder="请输入课程名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="currentCourse.description"
            :autosize="{ minRows: 2, maxRows: 5 }"
            type="textarea"
            placeholder="请输入课程描述"
          />
        </el-form-item>
        <el-form-item label="时间" prop="schedule">
          <!-- <el-input v-model="currentCourse.schedule"></el-input> -->
          <el-date-picker
            v-model="currentCourse.schedule"
            type="datetime"
            placeholder="Pick a Date"
            format="YYYY/MM/DD hh:mm:ss"
            value-format="YYYY-MM-DD h:m:s a"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveCourse">保存</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </div></template
      >
    </el-dialog>
  </el-col>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getCourses,
  createCourse,
  updateCourse,
  deleteCourse,
  getSearchCourse
} from '@/api/course'

// 定义课程信息接口
interface Course {
  id?: number
  title: string
  description: string
  schedule: string
  courseName: string
  teacherId?: number
}

// 搜索框内容
const searchText = ref()

// 课程信息集合
const courses = ref<Course[]>([])

const dialogVisible = ref(false)
const isEditing = ref(false)
const teacher_id = localStorage.getItem('id')
const teachername = localStorage.getItem('username')

// 当前课程信息
const currentCourse = ref<Course>({
  title: '',
  description: '',
  schedule: '',
  courseName: ''
})

// 课程信息规则
const rules = {
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
  schedule: [{ required: true, message: '请输入时间', trigger: 'blur' }]
}

const courseForm = ref()

// 获取所有课程信息
const loadCourses = async () => {
  try {
    courses.value = []
    const response = await getCourses(teacher_id)
    if (response.data.code === 200) {
      ElMessage.success('获取课程列表成功')
      courses.value = response.data.data
    }
  } catch (error) {
    console.log('获取课程列表失败')
  }
}

// 根据搜索框内容获取信息
const searchCourses = async () => {
  const response = await getSearchCourse(teacher_id, searchText.value)
  try {
    console.log(searchText)
    if (response.data.code === 200) {
      ElMessage.success('搜索成功')
      courses.value = response.data.data
    } else if (response.data.code === 500) {
      ElMessage.error('搜索课程失败')
    }
  } catch (error) {
    console.log(response.data.message)
  }
}

// 重置搜索内容
const resetSearch = async () => {
  try {
    courses.value = []
    const response = await getCourses(teacher_id)
    if (response.data.code === 200) {
      searchText.value = ''
      ElMessage.success('重置成功')
      courses.value = response.data.data
    }
  } catch (error) {
    console.log('重置失败')
  }
}

// 打开创建课程弹窗
const openCreateDialog = () => {
  currentCourse.value = {
    title: '',
    description: '',
    schedule: '',
    courseName: ''
  }
  dialogVisible.value = true
  isEditing.value = false
}

// 编辑课程内容信息
const editCourse = (course: Course) => {
  currentCourse.value = { ...course }
  dialogVisible.value = true
  isEditing.value = true
}

// 保存课程内容信息
const saveCourse = async () => {
  ;(courseForm.value as any).validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (isEditing.value) {
          await updateCourse(currentCourse.value, teacher_id)
          ElMessage.success('课程更新成功')
        } else {
          await createCourse(currentCourse.value, teacher_id, teachername)
          ElMessage.success('课程创建成功')
        }
        loadCourses()
        dialogVisible.value = false
      } catch (error) {
        console.log(error)
        ElMessage.error('保存课程失败')
      }
    }
  })
}

// 删除课程
const deleteTheCourse = async (courseId: number) => {
  try {
    const response = await deleteCourse(courseId, teacher_id)
    if (response.data.code === 200) {
      ElMessage.success('课程删除成功')
      loadCourses()
    } else if (response.data.code === 500) {
      console.log(response.data.message)
      // 根据报错，提示已有学生选课，外键关联，无法删除课程
      ElMessage.error('已有学生选课，课程删除失败')
    }
  } catch (error) {
    ElMessage.error('删除课程失败')
  }
}

onMounted(async () => {
  courses.value = []
  await loadCourses()
})
</script>

<style scoped>
h1 {
  font-size: 50px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.dialog-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 16px;
}
</style>
