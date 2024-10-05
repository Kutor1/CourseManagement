import axios from 'axios'

// 创建 Axios 实例，设置基础 URL
const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',

  headers: {
    'Content-Type': 'application/json'
  }
})
