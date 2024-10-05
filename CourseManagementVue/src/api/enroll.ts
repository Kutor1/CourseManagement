import axios from 'axios'

// 创建 Axios 实例，设置基础 URL
const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',

  headers: {
    'Content-Type': 'application/json'
  }
})

// 定义Course接口
export interface Course {
  id: number
  title: string
  description: string
  schedule: string
  teacherName?: string
}

// 获取所有可选课程列表
export const getAllCourses = (studentid: string | null) => {
  return apiClient.get(`/courses/getAllCourses/${studentid}`)
}

// 获取当前课程详情
export const getCourseDetail = (courseid: number) => {
  return apiClient.get(`/courses/${courseid}`)
}

// 搜索可选课程
export const getAvailableCourses = (
  searchText: string,
  studentid: string | null
) => {
  return apiClient.get(`/courses/getAvailableCourses/${searchText}`, {
    // 请求头
    headers: {
      studentid: studentid ? studentid : ''
    }
  })
}

// 选课
export const enrollCourse = (
  studentid: string | null,
  courseid: number,
  title: string,
  schedule: string,
  description: string,
  teachername: string | undefined,
  status: boolean
) => {
  return apiClient.post('/enrollments', {
    studentid,
    courseid,
    title,
    schedule,
    description,
    teachername,
    status
  })
}

// 获取学生已选课程列表
export const getSelectedCourses = (student_id: string | null) => {
  return apiClient.get('/enrollments', {
    // 请求头
    headers: {
      student_id: student_id ? student_id : ''
    }
  })
}

// 退课
export const withdrawCourse = (enrollId: number) => {
  return apiClient.delete(`/enrollments/${enrollId}`)
}
