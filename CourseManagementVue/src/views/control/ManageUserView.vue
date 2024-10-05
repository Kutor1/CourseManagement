<template>
  <!-- 用户管理表单 -->
  <el-col :span="18">
    <el-table :data="users" size="large" style="width: 100%; margin-top: 100px">
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="password" label="用户密码"></el-table-column>
      <el-table-column prop="role" label="身份"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" size="default" @click="editUser(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="default"
            @click="deleteUser(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-col>
  <el-col :span="1"></el-col>

  <!-- 创建或编辑用户信息的弹窗 -->
  <el-dialog v-model="dialogVisible" draggable="true" top="25vh" width="65vh">
    <template #header>
      <div class="dialog-header">
        <h2 style="padding-bottom: 0px">用户信息</h2>
      </div>
    </template>
    <el-form :model="currentUser" ref="courseForm">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="currentUser.username"
          placeholder="请输入用户名"
        ></el-input>
      </el-form-item>
      <el-form-item label="用户密码" prop="password">
        <el-input v-model="currentUser.password" placeholder="请输入用户密码" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select size="large" v-model="currentUser.status">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="saveUser">保存</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </div></template
    >
  </el-dialog>
</template>

<script lang="ts" setup>
import {
  deleteAllUsersApi,
  editAllUsersApi,
  getAllUsersApi
} from '@/api/control'
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

// 定义用户信息接口
interface User {
  id?: number
  username: string
  password: string
  role: string
  status: boolean
}

// 当前用户信息表单
const currentUser = ref<User>({
  username: '',
  password: '',
  role: '',
  status: false
})

// 用户状态选择栏
const statusOptions = [
  {
    value: 'false',
    label: '登出'
  },
  {
    value: 'true',
    label: '登录中'
  }
]

const users = ref<User[]>([])
const dialogVisible = ref(false)

// 获取所有用户信息
const loadUsers = async () => {
  try {
    users.value = []
    const response = await getAllUsersApi()
    if (response.data.code === 200) {
      ElMessage.success('成功获取所有用户信息')
      console.log(response.data)
      users.value = response.data.data
    }
  } catch (error) {
    console.log(error)
  }
}

// 编辑用户信息
const editUser = async (user: User) => {
  currentUser.value = { ...user }
  dialogVisible.value = true
}

const saveUser = async () => {
  try {
    // console.log(currentUser.value)
    const response = await editAllUsersApi(currentUser.value)
    if (response.data.code === 200) {
      loadUsers()
      ElMessage.success('编辑成功')
      dialogVisible.value = false
    } else if (response.data.code == 500) {
      ElMessage.error('编辑失败')
    }
  } catch (error) {
    console.log(error)
  }
}

// 删除用户
const deleteUser = async (userid: number) => {
  try {
    const response = await deleteAllUsersApi(userid)
    if (response.data.code === 200) {
      loadUsers()
      ElMessage.success('删除成功')
    } else if (response.data.code == 500) {
      console.log(response.data.message)
      ElMessage.error('该用户有关联课程，删除失败')
    }
  } catch (error) {
    console.log(error)
  }
}
onMounted(() => {
  loadUsers()
})
</script>
