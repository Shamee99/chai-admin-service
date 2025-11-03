<template>
  <div class="layout-container">
    <!-- 全局进度条 -->
    <GlobalProgress />
    <!-- 侧边栏 -->
    <aside class="layout-sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <img src="@/assets/logo.svg" alt="Logo" class="logo-img" />
          <span v-show="!sidebarCollapsed" class="logo-text">{{ appConfig.title }}</span>
        </div>
      </div>

      <!-- 菜单导航 -->
      <nav class="sidebar-nav">
        <MenuTree :menus="visibleMenus" :collapsed="sidebarCollapsed" />
      </nav>
    </aside>

    <!-- 主内容区 -->
    <div class="layout-main">
      <!-- 顶部导航栏 -->
      <header class="layout-header">
        <div class="header-left">
          <button
            class="sidebar-toggle"
            @click="toggleSidebar"
            :title="sidebarCollapsed ? '展开侧边栏' : '收起侧边栏'"
          >
            <el-icon><Operation v-if="!sidebarCollapsed" /><Operation v-else /></el-icon>
          </button>

          <!-- 面包屑导航 -->
          <Breadcrumb />
        </div>

        <div class="header-right">
          <!-- 全屏按钮 -->
          <el-tooltip :content="isFullscreen ? '退出全屏' : '全屏'" placement="bottom">
            <button class="header-action-btn" @click="toggleFullscreen">
              <el-icon><FullScreen v-if="!isFullscreen" /><Aim v-else /></el-icon>
            </button>
          </el-tooltip>

          <!-- GitHub链接 -->
          <el-tooltip content="GitHub" placement="bottom">
            <button class="header-action-btn" @click="openGithub">
              <svg class="github-icon" viewBox="0 0 1024 1024" width="16" height="16">
                <path d="M512 12.64c-282.752 0-512 229.216-512 512 0 226.208 146.688 418.144 350.08 485.824 25.6 4.736 35.008-11.104 35.008-24.64 0-12.192-0.48-52.544-0.704-95.328-142.464 30.976-172.512-60.416-172.512-60.416-23.296-59.168-56.832-74.912-56.832-74.912-46.464-31.776 3.52-31.136 3.52-31.136 51.392 3.616 78.464 52.768 78.464 52.768 45.664 78.272 119.776 55.648 148.992 42.56 4.576-33.088 17.856-55.68 32.512-68.48-113.728-12.928-233.28-56.864-233.28-253.024 0-55.904 19.936-101.568 52.672-137.408-5.312-12.896-22.848-64.96 4.96-135.488 0 0 42.88-13.76 140.8 52.48 40.832-11.36 84.64-17.024 128.16-17.248 43.488 0.192 87.328 5.888 128.256 17.248 97.856-66.24 140.672-52.48 140.672-52.48 27.872 70.528 10.336 122.592 5.024 135.488 32.832 35.84 52.608 81.504 52.608 137.408 0 196.64-119.776 239.936-233.792 252.64 18.368 15.904 34.72 47.04 34.72 94.816 0 68.512-0.608 123.648-0.608 140.512 0 13.632 9.216 29.6 35.168 24.576C877.44 942.624 1024 750.784 1024 524.64c0-282.784-229.248-512-512-512z" fill="currentColor"></path>
              </svg>
            </button>
          </el-tooltip>

          <!-- Gitee链接 -->
          <el-tooltip content="Gitee" placement="bottom">
            <button class="header-action-btn" @click="openGitee">
              <svg class="gitee-icon" viewBox="0 0 1024 1024" width="16" height="16">
                <path d="M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.267-25.267z" fill="#C71D23"></path>
              </svg>
            </button>
          </el-tooltip>

          <!-- 用户信息 -->
          <UserDropdown />
        </div>
      </header>

      <!-- 标签页 -->
      <TabsView />

      <!-- 内容区域 -->
      <main class="layout-content" :class="{ fullscreen: isFullscreenPage }">
        <router-view v-slot="{ Component, route }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
              <component :is="Component" :key="route.fullPath" />
            </keep-alive>
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, watch, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMenuStore } from '@/stores/menu-store.ts'
import { useUserStore } from '@/stores/user-store.ts'
import { useUIStore } from '@/stores/ui-store.ts'
import { useTabsStore } from '@/stores/tabs-store.ts'
import { appConfig } from '@/utils/env'
import MenuTree from './components/MenuTree.vue'
import Breadcrumb from './components/Breadcrumb.vue'
import UserDropdown from './components/UserDropdown.vue'
import TabsView from './components/TabsView.vue'
import GlobalProgress from '@/components/common/GlobalProgress.vue'
import { Operation, FullScreen, Aim } from '@element-plus/icons-vue'

// 路由
const route = useRoute()
const router = useRouter()

// Store
const menuStore = useMenuStore()
const userStore = useUserStore()
const uiStore = useUIStore()
const tabsStore = useTabsStore()

// 侧边栏状态
const sidebarCollapsed = computed(() => uiStore.sidebarCollapsed)

// 全屏状态
const isFullscreen = ref(false)

// 计算属性
const visibleMenus = computed(() => menuStore.completeMenuTree)

// 检测是否为全屏页面（如dashboard）
const isFullscreenPage = computed(() => {
  const fullscreenPages = ['/']
  return fullscreenPages.includes(route.path)
})

const cachedViews = computed(() => {
  // 使用标签页store的缓存列表
  return tabsStore.cachedTabs
})

// 方法
const toggleSidebar = () => {
  uiStore.toggleSidebar()
}

// 全屏切换
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    isFullscreen.value = true
  } else {
    document.exitFullscreen()
    isFullscreen.value = false
  }
}

// 打开GitHub
const openGithub = () => {
  window.open('https://github.com/Shamee99', '_blank')
}

// 打开Gitee
const openGitee = () => {
  window.open('https://gitee.com/shamee', '_blank')
}

// 路由监听
watch(
  () => route.fullPath,
  () => {
    // 排除不需要添加标签页的路由
    const excludePaths = ['/login', '/404', '/403', '/500']
    const isRedirectPath = route.path.startsWith('/redirect')
    if (!excludePaths.includes(route.path) && !isRedirectPath && route.meta?.title) {
      tabsStore.addTab(route)
    }
  },
  { immediate: false }, // 改为false，避免在初始化时重复添加
)

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

// 初始化
onMounted(() => {
  // 初始化用户信息
  userStore.initUserInfo()

  // 初始化固定标签页 - 总是添加dashboard
  const dashboardRoute = {
    path: '/dashboard',
    fullPath: '/dashboard',
    name: 'Dashboard',
    meta: {
      title: '首页',
      icon: 'Odometer',
      affix: true
    },
    query: {},
    params: {}
  } as any
  tabsStore.addTab(dashboardRoute)

  // 手动触发一次路由监听，确保当前路由被添加到标签页
  const excludePaths = ['/login', '/404', '/403', '/500']
  const isRedirectPath = route.path.startsWith('/redirect')
  if (!excludePaths.includes(route.path) && !isRedirectPath && route.meta?.title) {
    tabsStore.addTab(route)
  }

  // 监听全屏状态变化
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

// 组件卸载时清理事件监听器
onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  background-color: #f0f2f5;
}

.layout-sidebar {
  width: 240px;
  background: #001529;
  transition: width 0.3s ease;
  overflow: hidden;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.layout-sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  border-bottom: 1px solid #1f1f1f;
}

.logo {
  display: flex;
  align-items: center;
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.logo-text {
  white-space: nowrap;
  overflow: hidden;
}

.sidebar-nav {
  flex: 1;
  overflow: hidden;
  padding: 16px 0;
}

.layout-sidebar.collapsed .sidebar-nav {
  overflow: hidden;
}

.layout-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.layout-header {
  height: 64px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.sidebar-toggle {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.sidebar-toggle:hover {
  background-color: #f5f5f5;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-action-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
  color: #666;
}

.header-action-btn:hover {
  background-color: #f5f5f5;
  color: #409eff;
}

.github-icon {
  transition: color 0.3s;
}

.header-action-btn:hover .github-icon {
  color: #333;
}

.gitee-icon {
  transition: color 0.3s;
}

.header-action-btn:hover .gitee-icon {
  color: #C71D23;
}

.layout-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f0f2f5;
}

.layout-content.fullscreen {
  padding: 0;
  background: #ffffff;
}

/* 路由过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s ease;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>
