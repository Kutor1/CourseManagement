<template>
  <!-- 学生选课内容 -->
  <el-col :span="19">
    <el-row>
      <el-col :span="11">
        <h1>可选课程</h1>
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
        @click="searchAvailableCourses()"
        >搜索</el-button
      >
      <el-button style="margin-left: 10px" @click="resetSearch" size="large"
        >重置</el-button
      >
    </el-row>

    <!-- 选课表单 -->
    <el-table
      :data="availableCourses"
      style="width: 100%; margin-top: 20px"
      size="large"
      max-height="700"
    >
      <el-table-column prop="title" label="课程名称"></el-table-column>
      <el-table-column prop="teachername" label="任教老师"></el-table-column>
      <el-table-column prop="schedule" label="上课时间"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            type="primary"
            size="default"
            @click="viewCourseDetail(scope.row)"
            >详情</el-button
          >
          <el-button
            type="success"
            size="default"
            @click="enrollInCourse(scope.row)"
            >选课</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-col>
  <el-col :span="1"></el-col>

  <!-- 课程详情弹窗 -->
  <el-dialog
    v-model="courseDetailVisible"
    draggable="true"
    top="25vh"
    width="65vh"
  >
    <template #header>
      <div class="dialog-header">
        <h2 style="padding-bottom: 0px">课程详情</h2>
      </div>
    </template>
    <el-descriptions :border="true" :column="2" size="large">
      <el-descriptions-item label="课程名称">
        {{ selectedCourseDetail?.title }}</el-descriptions-item
      >
      <el-descriptions-item label="上课时间">{{
        selectedCourseDetail?.schedule
      }}</el-descriptions-item>
      <el-descriptions-item label="任教老师">{{
        selectedCourseDetail?.teachername
      }}</el-descriptions-item>
    </el-descriptions>
    <!-- 课程描述el-card -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-row>
        <el-col :span="5" style="background-color: rgb(245, 247, 250)">
          <div
            style="
              padding-top: 12px;
              padding-left: 15px;
              padding-bottom: 12px;
              font-weight: bold;
              color: #606266;
            "
          >
            课程描述
          </div>
        </el-col>
        <el-col :span="19"
          ><div
            style="
              padding-left: 15px;
              padding-top: 12px;
              padding-bottom: 12px;
              padding-right: 15px;
            "
          >
            {{ selectedCourseDetail?.description }}
          </div>
        </el-col>
      </el-row>
    </el-card>
    <template #footer>
      <div class="dialog-footer">
        <el-button
          @click="courseDetailVisible = false"
          style="margin-left: 10px"
          >关闭</el-button
        >
      </div></template
    >
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  enrollCourse,
  getAllCourses,
  getAvailableCourses,
  getCourseDetail
} from '@/api/enroll'

// 定义课程信息接口
interface Course {
  id: number
  title: string
  description: string
  schedule: string
  teachername?: string
}

// 可选课程信息集合
const availableCourses = ref<Course[]>([])

// 当前选择的课程信息
const selectedCourseDetail = ref<Course | null>(null)

// 搜索框内容
const searchText = ref('')

const student_id = localStorage.getItem('id')
const courseDetailVisible = ref(false)

// 加载所有可选课程信息
const loadAvailableCourses = async () => {
  try {
    const response = await getAllCourses(student_id)
    if (response.data.code === 200) {
      availableCourses.value = response.data.data.map((course: any) => ({
        id: course.id,
        title: course.title,
        description: course.description,
        schedule: course.schedule,
        teachername: course.teachername
      }))
    } else {
      ElMessage.error('加载可选课程失败')
    }
  } catch (error) {
    ElMessage.error('加载可选课程失败')
  }
}

// 选课
const enrollInCourse = async (course: Course) => {
  try {
    const response = await enrollCourse(
      student_id,
      course.id,
      course.title,
      course.schedule,
      course.description,
      course.teachername,
      true
    )
    if (response.data.code === 200) {
      console.log(response.data)
      ElMessage.success('选课成功')
      loadAvailableCourses()
      // loadSelectedCourses()
    } else if (response.data.code === 500) {
      ElMessage.error(response.data.message || '选课失败')
    }
  } catch (error) {
    ElMessage.error('选课失败')
  }
}

// 获取当前选择课程的课程详情
const viewCourseDetail = async (course: Course) => {
  try {
    const response = await getCourseDetail(course.id)
    if (response.data.code === 200) {
      console.log(response.data)
      selectedCourseDetail.value = response.data.data
      courseDetailVisible.value = true
    } else {
      ElMessage.error('获取课程详情失败')
    }
  } catch (error) {
    ElMessage.error('获取课程详情失败')
  }
}

// 根据搜索框内容搜索
const searchAvailableCourses = async () => {
  try {
    const response = await getAvailableCourses(searchText.value, student_id)
    console.log(response.data)
    if (response.data.code === 200) {
      availableCourses.value = response.data.data
      ElMessage.success('搜索成功')
      // searchText.value = ''
    } else if (response.data.code === 500) {
      ElMessage.error('搜索课程失败')
    }
  } catch (error) {
    console.log(error)
  }
}

// 重置搜索内容
const resetSearch = async () => {
  try {
    const response = await getAllCourses(student_id)
    if (response.data.code === 200) {
      availableCourses.value = response.data.data.map((course: any) => ({
        id: course.id,
        title: course.title,
        description: course.description,
        schedule: course.schedule,
        teachername: course.teachername
      }))
      searchText.value = ''
      ElMessage.success('重置成功')
    } else {
      ElMessage.error('重置失败')
    }
  } catch (error) {
    ElMessage.error('重置失败')
  }
}

onMounted(() => {
  loadAvailableCourses()
})
</script>

<style scoped>
h1 {
  font-size: 50px;
}

/* el-dialog样式 */
.custom-dialog .el-dialog {
  max-width: 100%;
}

.description-content {
  margin-bottom: 20px;
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

.el-card {
  --el-card-padding: 0px;
}
</style>
