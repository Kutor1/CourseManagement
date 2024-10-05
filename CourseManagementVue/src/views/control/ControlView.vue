<template>
  <el-row class="control-view" style="height: 98vh">
    <!-- 侧边栏 -->
    <el-col :span="4">
      <el-menu router="true" :default-active="activeIndex">
        <el-menu-item
          index="/control/thank"
          style="font-size: 20px; height: 70px"
        >
          <el-icon><StarFilled /></el-icon>
          <!-- <span> 致谢 </span> -->
        </el-menu-item>
        <el-menu-item index="/control" style="height: 70px">
          <el-icon><UserFilled /></el-icon>
          <span style="font-size: 20px">用户管理</span>
        </el-menu-item>
        <el-menu-item
          index=""
          style="font-size: 20px; height: 70px"
          @click="logout"
        >
          <el-icon><Unlock /></el-icon>
          <span>退出登录</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <!-- <div class="router_container"> -->
    <el-col :span="1"></el-col>
    <router-view style="padding: 20px"></router-view>
    <!-- </div> -->
  </el-row>
</template>

<script lang="ts" setup>
import { logOutApi } from '@/api/auth'
import { logOut } from '@/api/user'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const userid = localStorage.getItem('id')

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

// 退出登录
const logout = () => {
  logOutApi(userid)
  logOut()
  // console.log(courses.value)
  router.push('/login')
}
</script>

<style scoped>
.control-view {
  padding-top: 20px;
  padding-bottom: 20px;
}

.el-menu {
  height: 95vh;
}
</style>
