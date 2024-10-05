<template>
  <el-col :span="5"></el-col>
  <el-col :span="8">
    <!-- 标题 -->
    <h1 style="margin-top: 30px">更改用户基本资料</h1>

    <!-- 更改用户名表单 -->
    <el-card class="box-card" style="margin-top: 40px">
      <h3 style="margin-bottom: 35px">更改用户名</h3>
      <el-form
        :model="usernameForm"
        :rules="updateUsernameRules"
        ref="usernameFormRef"
      >
        <el-form-item
          label="当前用户名"
          prop="currentUsername"
          style="font-size: 17px"
        >
          <el-input v-model="usernameForm.currentUsername" disabled></el-input>
        </el-form-item>
        <el-form-item
          label="新用户名"
          prop="newUsername"
          style="margin-bottom: 30px"
        >
          <el-input v-model="usernameForm.newUsername"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onUpdateUsername"
            >更改用户名</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 更改用户密码表单 -->
    <el-card class="box-card" style="margin-top: 45px">
      <h3 style="margin-bottom: 35px">更改密码</h3>
      <el-form
        :model="passwordForm"
        :rules="updatePasswordRules"
        ref="passwordFormRef"
      >
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input
            type="password"
            v-model="passwordForm.currentPassword"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            type="password"
            v-model="passwordForm.newPassword"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认新密码"
          prop="confirmPassword"
          style="margin-bottom: 30px"
        >
          <el-input
            type="password"
            v-model="passwordForm.confirmPassword"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onUpdatePassword"
            >更新密码</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </el-col>
  <el-col :span="6"></el-col>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserName, logOut, setPassword, setUserName } from '@/api/user'
import { useRouter } from 'vue-router'

// 用户名信息表单
const usernameForm = ref({
  currentUsername: '', // 当前用户名
  newUsername: '' // 新用户名
})

// 用户密码表单
const passwordForm = ref({
  currentPassword: '', // 当前密码
  newPassword: '', // 新密码
  confirmPassword: '' // 确认新密码
})

const router = useRouter()
const userid = localStorage.getItem('id')

// 加载页面时，自动获取当前用户名
onMounted(async () => {
  const response = await getUserName(userid)
  usernameForm.value.currentUsername = response.data.data.username
})

// 更新用户名规则
const updateUsernameRules = {
  newUsername: [
    { required: true, message: '请输入新的用户名', trigger: 'blur' }
  ]
}

// 更新用户密码规则
const updatePasswordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 更新用户名
const onUpdateUsername = async () => {
  if (usernameForm.value.newUsername !== '') {
    // API 调用更改用户名
    const response = await setUserName(usernameForm.value.newUsername, userid)
    if (response.data.code === 200) {
      ElMessage.success('用户名更新成功,请重新登录')
      logOut()
      router.push('/login')
    } else if (response.data.code === 500) {
      console.error(response.data.message)
      ElMessage.error(response.data.message)
    }
  } else {
    ElMessage.error('请先输入需要更改的用户名')
  }
}

// 更新用户密码
const onUpdatePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.error('新密码和确认密码不一致')
    return
  }
  // API 调用更改密码
  const response = await setPassword(
    passwordForm.value.currentPassword,
    passwordForm.value.newPassword,
    userid
  )
  if (response.data.code === 200) {
    ElMessage.success('密码更新成功,请重新登录')
    logOut()
    router.push('/login')
  } else if (response.data.code === 500) {
    console.error('更新密码失败')
    ElMessage.error(response.data.message)
  }
}
</script>

<style scoped>
h1 {
  margin-bottom: 20px;
}

/* el-card美化（更改用户名，密码表单） */
.box-card {
  padding: 20px;
}

.box-card {
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: box-shadow 0.3s;
}

.box-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

h1 {
  font-size: 30px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

h3 {
  font-size: 20px;
  color: #666;
  margin-bottom: 15px;
}

.el-form {
  --el-form-label-font-size: 16px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item label {
  font-weight: bold;
  color: #444;
}

.el-input {
  width: 100%;
}

.el-button {
  width: 100%;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.el-button:hover {
  background-color: #66b1ff;
}

.el-button:active {
  background-color: #3a8ee6;
}

.el-button:focus {
  outline: none;
}

.el-form {
  max-width: 400px;
  margin: 0 auto;
}

.el-card {
  background-color: #fff;
}

.el-card h3 {
  margin-top: 0;
}
</style>
