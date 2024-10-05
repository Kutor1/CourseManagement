import axios from 'axios'

const instance = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',

  timeout: 10000
})

// 添加请求拦截器
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    // 处理错误
    return Promise.reject(error)
  }
)

export default instance
