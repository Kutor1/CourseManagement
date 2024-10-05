<template>
  <div class="home">
    <div class="left-section">
      <h1 style="margin-bottom: 50px">文件管理</h1>
      <div class="button-group">
        <button @click="goToPage('/file')">进入</button>
      </div>
    </div>
    <div class="right-section">
      <h1 style="margin-bottom: 50px">课程管理</h1>
      <div class="button-group">
        <button @click="goToPage('/login')">进入</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goToPage = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.home {
  display: flex;
  height: 98vh;
}

.left-section,
.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

h1 {
  font-size: 3em;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

button {
  padding: 10px 20px;
  font-size: 1em;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}
</style>
