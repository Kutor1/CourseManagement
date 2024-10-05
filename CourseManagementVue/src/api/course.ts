import axios from 'axios'

// 定义课程接口
export interface Course {
  id?: number | undefined
  courseName: string
  description: string
  schedule: string
  teacherId?: number | undefined
}

// 创建 Axios 实例，设置基础 URL
const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// 获取当前教师的所有课程
export const getCourses = (teacher_id: string | null) => {
  return apiClient.get('/courses', {
    // 请求头
    headers: {
      teacher_id: teacher_id ? teacher_id : ''
    }
  })
}

// 根据搜索内容获取相应课程
export const getSearchCourse = (
  teacher_id: string | null,
  searchText: string
) => {
  return apiClient.get(`/courses/search/${searchText}`, {
    // 请求头
    headers: {
      teacher_id: teacher_id ? teacher_id : ''
    }
  })
}

// 创建新课程
export const createCourse = (
  course: Course,
  teacher_id: string | null,
  teachername: string | null
) => {
  return apiClient.post(`/courses/${teachername}`, course, {
    // 请求头
    headers: {
      teacher_id: teacher_id ? teacher_id : ''
    }
  })
}

// 更新现有课程
export const updateCourse = (course: Course, teacher_id: string | null) => {
  return apiClient.put(`/courses/${course.id}`, course, {
    // 请求头
    headers: {
      teacher_id: teacher_id ? teacher_id : ''
    }
  })
}

// 删除课程
export const deleteCourse = (courseId: number, teacher_id: string | null) => {
  return apiClient.delete(`/courses/${courseId}`, {
    // 请求头
    headers: {
      teacher_id: teacher_id ? teacher_id : ''
    }
  })
}
