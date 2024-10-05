<template>
  <el-col :span="19">
    <el-row>
      <el-col :span="11">
        <h1>已选课程</h1>
      </el-col>
    </el-row>

    <!-- 已选课程表单 -->
    <el-table
      :data="selectedCourses"
      style="width: 100%; margin-top: 50px"
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
            type="danger"
            size="default"
            @click="withdrawFromCourse(scope.row)"
            >退课</el-button
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
    <!-- 尾部操作框 -->
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
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getCourseDetail,
  getSelectedCourses,
  withdrawCourse
} from '@/api/enroll'

// 定义课程信息接口
interface Course {
  id: number
  title: string
  description: string
  schedule: string
  teachername?: string
}

// 选择的课程信息
const selectedCourseDetail = ref<Course | null>(null)

// 已选课的课程集合
const selectedCourses = ref<Course[]>([])

const courseDetailVisible = ref(false)
const userid = localStorage.getItem('id')

// 加载所有已选课信息
const loadSelectedCourses = async () => {
  try {
    const response = await getSelectedCourses(userid)
    // console.log(response.data)
    if (response.data.code === 200) {
      selectedCourses.value = response.data.data
    } else {
      ElMessage.error('加载已选课程失败')
    }
  } catch (error) {
    ElMessage.error('加载已选课程失败')
  }
}

// 查看当前课程详情
const viewCourseDetail = async (course: Course) => {
  try {
    console.log(course)
    const response = await getCourseDetail(course.id)
    if (response.data.code === 200) {
      selectedCourseDetail.value = course
      courseDetailVisible.value = true
    } else {
      ElMessage.error('获取课程详情失败')
    }
  } catch (error) {
    ElMessage.error('获取课程详情失败')
  }
}

// 退课
const withdrawFromCourse = async (course: Course) => {
  try {
    const response = await withdrawCourse(course.id)
    if (response.data.code === 200) {
      ElMessage.success('退课成功')
      // loadAvailableCourses()
      loadSelectedCourses()
    } else {
      ElMessage.error(response.data.message || '退课失败')
    }
  } catch (error) {
    ElMessage.error('退课失败')
  }
}

onMounted(() => {
  loadSelectedCourses()
})
</script>

<style scoped>
h1 {
  font-size: 50px;
}

/* el-dialog弹窗样式 */
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
