<template>
  <el-col :span="18">
    <div class="thank-you-page">
      <!-- <img src="@/assets/3.png" alt="background" /> -->

      <div class="content">
        <div class="text-content">
          <p>
            该项目使用了前端Vue3 + Element-Plus + TypeScript。后端SpringBoot +
            MybatisPlus + Lombok + Druid + Tika。
          </p>
          <!-- <p>以及，感谢我自己</p> -->
        </div>
      </div>
    </div>
  </el-col>
</template>

<script setup lang="ts"></script>

<style scoped>
.thank-you-page {
  position: relative;
  width: 100%;
  height: 90vh;
  overflow: hidden;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: white;
  text-align: center;
  padding: 20px;
}

.el-carousel__item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.carousel-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 10px;
}

.caption {
  margin-top: 20px;
}

.text-content {
  margin-top: 30px;
  font-size: 1.2em;
  font-weight: bold;
}
</style>
