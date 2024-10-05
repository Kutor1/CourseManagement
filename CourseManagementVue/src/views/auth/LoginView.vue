<template>
  <el-row justify="center" class="login-container">
    <el-col :span="14" class="left-column">
      <div class="image-container">
        <img src="@/assets/3.png" alt="封面" class="responsive-image" />
      </div>
    </el-col>
    <el-col :span="10" class="right-column">
      <!-- 登录表单 -->
      <el-card class="login-card">
        <h1>登录</h1>
        <el-form
          :model="loginForm"
          ref="loginFormRef"
          :rules="rules"
          label-width="80px"
          size="large"
          style="margin-top: 30px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              clearable
              show-password
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item style="margin-top: 40px">
            <el-button type="primary" @click="onLogin" size="large"
              >登录</el-button
            >
            <el-button @click="pushToRegister" size="large">注册</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 后台管理 -->
      <div class="button-container">
        <el-button
          type="primary"
          circle
          text
          size="large"
          @click="handleControl"
        >
          <el-icon size="20"><Setting /></el-icon>
        </el-button>
      </div>

      <!-- 后台管理登录Dialog -->
      <el-dialog
        v-model="dialogVisiable"
        width="50vh"
        draggable="true"
        top="25vh"
      >
        <h1 style="margin-left: 20px">后台管理</h1>
        <el-form
          :model="controlForm"
          ref="loginFormRef"
          :rules="rules"
          label-width="80px"
          size="large"
          style="margin-top: 30px"
        >
          <el-form-item
            label="管理员"
            prop="username"
            style="margin-right: 20px; margin-left: 10px"
          >
            <el-input
              v-model="controlForm.username"
              placeholder=""
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="密码"
            prop="password"
            style="margin-right: 20px; margin-left: 10px"
          >
            <el-input
              v-model="controlForm.password"
              type="password"
              placeholder=""
              clearable
              show-password
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item style="margin-top: 40px">
            <el-button
              type="primary"
              @click="pushToContol"
              size="large"
              style="margin-left: 300px"
              >登录</el-button
            >
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { loginApi, loginControl } from '@/api/auth'
import { FormInstance, FormRules } from 'element-plus'
import { Setting } from '@element-plus/icons-vue'

// 定义登录接口
interface LoginForm {
  username: string
  password: string
}

const dialogVisiable = ref(false)

// 登录数据
const loginForm = ref<LoginForm>({
  username: '',
  password: ''
})

// 管理员登录数据
const controlForm = ref<LoginForm>({
  username: '',
  password: ''
})

// Form表单规则
const rules = ref<FormRules>({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

const loginFormRef = ref<FormInstance>()
const router = useRouter()

// 用户登录
const onLogin = async () => {
  if (loginFormRef.value) {
    loginFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          const response = await loginApi(
            loginForm.value.username,
            loginForm.value.password
          )
          if (response.code === 200) {
            ElMessage.success('登录成功')

            // 获取response中的token和role
            const role = response.data.role

            // 根据身份跳转网页
            if (role === 'TEACHER') {
              router.push('/manage')
            } else if (role === 'STUDENT') {
              router.push('/enrollment')
            }
          } else {
            ElMessage.error(response.message || '登录失败')
          }
        } catch (error) {
          // 将错误抛向控制台
          ElMessage.error('服务器错误')
          console.log(error)
        }
      } else {
        ElMessage.error('请填写完整的表单')
      }
    })
  }
}

// 转向注册页
const pushToRegister = () => {
  router.push('/register')
}

const handleControl = () => {
  dialogVisiable.value = true
}

// 管理员登录
const pushToContol = async () => {
  const response = await loginControl(
    controlForm.value.username,
    controlForm.value.password
  )
  if (response.code === 200) {
    ElMessage.success('登录成功')
    router.push('/control')
  }
}
</script>

<style scoped>
.login-container {
  height: 98vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.left-column {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  color: white;
  font-size: 2rem;
  height: 100%;
  padding-left: 10px;
}

.right-column {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  height: 100%;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 500px;
  padding: 20px;
}

/* 表单标签 */
.el-form-item--large {
  --font-size: 18px;
}

/* 封面 */
.image-container {
  width: 100%;
  height: 100%; /* 确保容器具有明确的高度 */
  display: flex; /* 可选：使容器使用 flex 布局 */
  align-items: center; /* 可选：垂直居中图片 */
  justify-content: center; /* 可选：水平居中图片 */
}

.responsive-image {
  width: 100%; /* 使图片宽度适应容器 */
  height: auto; /* 维持图片的纵横比 */
  max-height: 100%; /* 确保图片不会超出容器高度 */
  object-fit: cover; /* 确保图片按比例裁剪以适应容器 */
}

.button-container {
  position: absolute;
  bottom: 30px; /* 距离底部的距离 */
  right: 20px; /* 距离右侧的距离 */
}
</style>
