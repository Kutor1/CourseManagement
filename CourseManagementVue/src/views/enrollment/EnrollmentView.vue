<template>
  <el-row class="enrollment-view" style="height: 98vh">
    <!-- 侧边栏 -->
    <el-col :span="3">
      <el-menu :default-active="activeIndex" router="true">
        <el-menu-item index="/enrollment" style="height: 70px">
          <el-icon><Management /></el-icon>
          <span style="font-size: 20px">课程选择</span>
        </el-menu-item>
        <el-menu-item index="/enrollment/selected" style="height: 70px">
          <el-icon><Checked /></el-icon>
          <span style="font-size: 20px">已选课程</span>
        </el-menu-item>
        <el-sub-menu index="2">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span style="font-size: 20px">个人中心</span>
          </template>
          <el-menu-item index="/enrollment/user" style="height: 60px">
            <el-icon><User /></el-icon>
            <span style="font-size: 17px">基本资料</span></el-menu-item
          >
          <el-menu-item @click="logout" style="height: 60px">
            <el-icon><Unlock /></el-icon>
            <span style="font-size: 17px">退出登录</span></el-menu-item
          >
        </el-sub-menu>
      </el-menu>
    </el-col>

    <el-col :span="1"></el-col>
    <router-view></router-view>
  </el-row>
</template>

<script setup lang="ts">
import { logOutApi } from '@/api/auth'
import { logOut } from '@/api/user'
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const router = useRouter()
const route = useRoute()
const studentid = localStorage.getItem('id')

// 侧边栏高亮标签
const activeIndex = ref('/')

// 监测当前路径
watch(
  () => route.path,
  (newPath) => {
    activeIndex.value = newPath
  },
  { immediate: true }
)

// 用户登出
const logout = () => {
  logOutApi(studentid)
  logOut()
  router.push('/login')
}
</script>

<style scoped>
.enrollment-view {
  padding-top: 20px;
  padding-bottom: 20px;
}

.el-menu {
  height: 95vh;
}
</style>
