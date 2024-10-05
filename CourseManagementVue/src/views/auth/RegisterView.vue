<template>
  <el-row justify="center" class="register-container">
    <el-col :span="14" class="left-column">
      <div class="image-container">
        <img src="@/assets/3.png" alt="封面" class="responsive-image" />
      </div>
    </el-col>

    <!-- 注册表单 -->
    <el-col :span="10" class="right-column">
      <el-card class="register-card">
        <h1 style="margin-left: 30px">注册</h1>
        <el-form
          :model="form"
          :rules="rules"
          ref="registerForm"
          label-width="80px"
          size="large"
          style="margin-top: 30px"
        >
          <el-form-item label="用户名" prop="username" label-width="100px">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" label-width="100px">
            <el-input
              type="password"
              v-model="form.password"
              size="large"
              placeholder="请输入密码"
              show-password
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item
            label="确认密码"
            prop="confirmPassword"
            label-width="100px"
          >
            <el-input
              type="password"
              v-model="form.confirmPassword"
              size="large"
              placeholder="请确认密码"
              clearable
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="角色" prop="role" label-width="100px">
            <el-radio-group v-model="form.role">
              <el-radio label="teacher">教师</el-radio>
              <el-radio label="student">学生</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">注册</el-button>
            <el-button @click="pushToLogin">登录</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { registerApi } from '@/api/auth'

const router = useRouter()

// 定义注册表单
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  role: 'student' // 默认选项
})

// 注册表单规则
const rules = ref({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
})

const registerForm = ref()

// 提交注册
const onSubmit = async () => {
  ;(registerForm.value as any).validate(async (valid: boolean) => {
    if (valid) {
      try {
        const response = await registerApi(
          form.value.username,
          form.value.password,
          form.value.role,
          false
        )
        if (response.code === 200) {
          ElMessage.success('注册成功')
          router.push('/login') // 注册成功后跳转到登录页面
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        ElMessage.error('注册失败，请稍后重试')
      }
    }
  })
}

// 重置表单数据
const onReset = () => {
  ;(registerForm.value as any).resetFields()
}

// 转向登录页面
const pushToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
h2 {
  margin-bottom: 20px;
}

/* 容器 */
.register-container {
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

.register-card {
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
</style>
