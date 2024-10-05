import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/auth/LoginView.vue'
import UserView from '@/views/user/UserView.vue'
import RegisterView from '@/views/auth/RegisterView.vue'
import EnrollmentView from '@/views/enrollment/EnrollmentView.vue'
import { getToken, getUserId, getUserName, getUserRole } from '@/utils/auth'
import CourseView from '@/views/course/CourseView.vue'
import ManageView from '@/views/course/ManageView.vue'
import ListView from '@/views/enrollment/ListView.vue'
import CheckView from '@/views/enrollment/CheckView.vue'
import TestView from '@/views/TestView.vue'
import ControlView from '@/views/control/ControlView.vue'
import ManageUserView from '@/views/control/ManageUserView.vue'
import FileView from '@/views/file/FileView.vue'
import HomeView from '@/views/HomeView.vue'
import ThanksView from '@/views/control/ThanksView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/user',
      name: 'user',
      component: UserView
    },
    {
      path: '/register',
      component: RegisterView
    },
    {
      path: '/enrollment',
      component: EnrollmentView,
      meta: { requiresAuth: true, role: 'STUDENT' },
      children: [
        { path: '/enrollment', component: ListView },
        { path: '/enrollment/selected', component: CheckView },
        { path: '/enrollment/user', component: UserView }
      ]
    },
    {
      path: '/manage',
      component: CourseView,
      meta: { requiresAuth: true, role: 'TEACHER' },
      children: [
        { path: '/manage/user', component: UserView },
        { path: '/manage', component: ManageView }
      ]
    },
    {
      path: '/control',
      component: ControlView,
      meta: { requiresAuth: true, role: 'CONTROL' },
      children: [
        { path: '/control', component: ManageUserView },
        { path: '/control/thank', component: ThanksView }
      ]
    },
    {
      path: '/file',
      component: FileView
    },
    {
      path: '/test',
      component: TestView
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = getToken()
  const userRole = getUserRole()
  const id = getUserId()
  const username = getUserName()

  // 如果访问的是主页或登录或注册或文件或测试页面，直接放行
  if (
    to.path === '/login' ||
    to.path === '/register' ||
    to.path === '/test' ||
    to.path === '/' ||
    to.path === '/file'
  ) {
    // 有 token 时访问登录或注册页面，直接重定向到首页或其他页面
    // 根据role角色访问对应界面
    if (token && id && username && userRole) {
      if (userRole === 'STUDENT') {
        next({ path: '/enrollment' })
      } else if (userRole === 'TEACHER') {
        next({ path: '/manage' })
      } else if (userRole === 'CONTROL') {
        next({ path: '/control' })
      } else {
        next({ path: '/' }) // 角色不明确时重定向到首页或其他页面
      }
    } else {
      next()
    }
  } else if (!token || !id || !username || !userRole) {
    // 如果没有 token、id 或 username，重定向到登录页面
    next({ path: '/login' })
  } else if (to.matched.some((record) => record.meta.requiresAuth)) {
    // 如果目标路由需要权限检查
    if (to.meta.role && to.meta.role !== userRole) {
      // 角色不匹配，重定向到登录页面
      next({ path: '/login' })
    } else {
      // 角色匹配或不要求角色，放行
      next()
    }
  } else {
    // 如果不需要权限验证，直接放行
    next()
  }
})
export default router
