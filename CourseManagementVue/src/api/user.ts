import {
  removeToken,
  removeUserId,
  removeUserName,
  removeUserRole
} from '@/utils/auth'
import axios from 'axios'

// 创建 Axios 实例，设置基础 URL
const apiClient = axios.create({
  // baseURL: 'http://localhost:8081/api',
  baseURL: 'http://129.204.131.237:8081/api',

  headers: {
    'Content-Type': 'application/json'
  }
})

// 退出当前用户
export const logOut = () => {
  removeUserId()
  removeUserRole()
  removeToken()
  removeUserName()
}

// 获取当前用户名
export const getUserName = (userid: string | null) => {
  return apiClient.get(`/users/${userid}`)
}

// 更改用户名
export const setUserName = (newname: string, userid: string | null) => {
  return apiClient.put(`/users/${userid}&${newname}`, null, {
    // // 请求头
    // headers: {
    //   newname: newname ? newname : ''
    // }
  })
}

// 更改密码
export const setPassword = (
  currentpassword: string,
  newpassword: string,
  userid: string | null
) => {
  return apiClient.put(`/users`, null, {
    // 请求头
    headers: {
      userid: userid ? userid : '',
      currentpassword: currentpassword ? currentpassword : '',
      newpassword: newpassword ? newpassword : ''
    }
  })
}
