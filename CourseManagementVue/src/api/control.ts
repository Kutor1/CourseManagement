import axios from 'axios'

// 创建 Axios 实例，设置基础 URL
const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',

  headers: {
    'Content-Type': 'application/json'
  }
})

// 定义User接口
interface User {
  id?: number | undefined
  username: string
  password: string
  role: string
  status: boolean
}

// 获取所有用户信息
export const getAllUsersApi = () => {
  return apiClient.get('/users')
}

// 管理编辑用户信息
export const editAllUsersApi = (user: User) => {
  return apiClient.post('/users', user)
}

// 管理删除用户
export const deleteAllUsersApi = (userid: number) => {
  return apiClient.delete(`/users/${userid}`)
}
