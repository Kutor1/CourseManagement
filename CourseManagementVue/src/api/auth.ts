// 创建一个 axios 实例
import axios from 'axios'
import {
  setToken,
  setUserRole,
  setUserId,
  setUserName,
  removeLog
} from '@/utils/auth'

const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',
  timeout: 10000 // 请求超时时间
})

// 登录请求
export const loginApi = async (username: string, password: string) => {
  try {
    const response = await apiClient.post('/auth/login', {
      username,
      password
    })
    if (response.data.code === 200) {
      // 将数据设置到localstorge
      setToken(response.data.data.token)
      setUserRole(response.data.data.role) // 假设后端返回 role 信息
      setUserId(response.data.data.id)
      setUserName(response.data.data.username)
      removeLog()
    }
    return response.data
  } catch (error) {
    console.error('登录请求失败', error)
    throw error
  }
}

// 注册请求
export const registerApi = async (
  username: string,
  password: string,
  role: string,
  status: boolean
) => {
  try {
    const response = await apiClient.post('/auth/register', {
      username,
      password,
      role, // 传递角色身份
      status
    })
    return response.data
  } catch (error) {
    console.error('注册请求失败', error)
    return { success: false, message: '服务器错误' }
  }
}

// 登出请求
export const logOutApi = async (userid: string | null) => {
  return apiClient.post(`/auth/logout/${userid}`)
}

// 管理员登录请求
export const loginControl = async (username: string, password: string) => {
  const response = await apiClient.post('/auth/control', {
    username,
    password
  })
  if (response.data.code === 200) {
    // 将数据设置到localstorge
    setToken(response.data.data.token)
    setUserRole(response.data.data.role) // 假设后端返回 role 信息
    setUserId(response.data.data.id)
    setUserName(response.data.data.username)
    removeLog()
  }
  return response.data
}
