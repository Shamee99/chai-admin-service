import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
import VueFilesPreview from 'vue-files-preview'

import App from './App.vue'
import router from './router'
import permissionDirective from './directives/perms-directive.ts'
import permissionButtonDirective from './directives/perms-btn-directive.ts'

// 导入全局样式
import '@/assets/main.css'

// 创建应用实例
const app = createApp(App)

// 创建pinia实例并配置持久化插件
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册权限指令
app.directive('permsDirective', permissionDirective)
app.directive('permsDirective-button', permissionButtonDirective)

// 使用插件
app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})
app.use(VXETable)
app.use(VueFilesPreview)

// 挂载应用
app.mount('#app')
