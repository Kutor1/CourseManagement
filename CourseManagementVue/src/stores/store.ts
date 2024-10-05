import { ref } from 'vue'

const token = ref<string | null>(localStorage.getItem('token'))
const isAuthenticated = ref<boolean>(!!token.value)

export const useAuthStore = () => {
  const login = (newToken: string) => {
    token.value = newToken
    isAuthenticated.value = true
    localStorage.setItem('token', newToken)
  }

  const logout = () => {
    token.value = null
    isAuthenticated.value = false
    localStorage.removeItem('token')
  }

  return {
    token,
    isAuthenticated,
    login,
    logout
  }
}
