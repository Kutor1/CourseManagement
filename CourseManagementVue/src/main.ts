import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'

// 导入全局样式
import '@/styles/global.css'
// import { logOut } from './api/user'
// import { logOutApi } from './api/auth'

// window.addEventListener('beforeunload', () => {
//   localStorage.setItem('log', '登出')
//   logOutApi(localStorage.getItem('id'))
//   logOut()
// })

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus, {
  locale: zhCn
})
app.use(createPinia())
app.use(router)

app.mount('#app')
