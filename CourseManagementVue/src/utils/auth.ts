// 本地存储用户的登录信息
// token,role,useId,username,log

export function getToken() {
  return localStorage.getItem('token')
}

export function setToken(token: string) {
  localStorage.setItem('token', token)
}

export function removeToken() {
  localStorage.removeItem('token')
}

export function getUserRole() {
  return localStorage.getItem('role')
}

export function setUserRole(role: string) {
  localStorage.setItem('role', role)
}

export function removeUserRole() {
  localStorage.removeItem('role')
}

export function getUserId() {
  return localStorage.getItem('id')
}

export function setUserId(id: string) {
  localStorage.setItem('id', id)
}

export function removeUserId() {
  localStorage.removeItem('id')
}

export function getUserName() {
  return localStorage.getItem('username')
}

export function setUserName(username: string) {
  localStorage.setItem('username', username)
}

export function removeUserName() {
  localStorage.removeItem('username')
}

export function removeLog() {
  localStorage.removeItem('log')
}
