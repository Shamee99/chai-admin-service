import { createRouter, createWebHashHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user-store.ts'
import { useMenuStore } from '@/stores/menu-store.ts'
import { useUIStore } from '@/stores/ui-store.ts'
import { appConfig } from '@/utils/env'

const whiteList = ['/login', '/403', '/404', '/500']

// 基础路由（不需要权限的路由）
const baseRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
      hideInMenu: true
    }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/404View.vue'),
    meta: {
      title: '页面不存在',
      requiresAuth: false,
      hideInMenu: true
    }
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/403View.vue'),
    meta: {
      title: '无权限',
      requiresAuth: false,
      hideInMenu: true
    }
  },
  {
    path: '/500',
    name: 'ServerError',
    component: () => import('@/views/error/500View.vue'),
    meta: {
      title: '服务器错误',
      requiresAuth: false,
      hideInMenu: true
    }
  }
]

// 主布局路由
const layoutRoute: RouteRecordRaw = {
  path: '/',
  name: 'Layout',
  component: () => import('@/layout/LayoutView.vue'),
  children: [
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/DashboardView.vue'),
      meta: {
        title: '首页',
        requiresAuth: true,
        hideInMenu: false
      }
    },
    {
      path: '/redirect/:path(.*)',
      name: 'Redirect',
      component: () => import('@/views/redirect/RedirectView.vue'),
      meta: {
        title: '重定向',
        requiresAuth: true,
        hideInMenu: true
      }
    },
    {
      path: '/demo/office',
      name: 'OfficeDemo',
      component: () => import('@/views/demo/OfficeDemo.vue'),
      meta: {
        title: 'Office文档预览',
        requiresAuth: true,
        hideInMenu: false
      }
    }
  ]
}

// 创建路由实例
const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [...baseRoutes, layoutRoute],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 动态路由加载状态
let isRoutesLoaded = false

/**
 * 加载动态路由
 */
export const loadDynamicRoutes = async (): Promise<void> => {
  if (isRoutesLoaded) {
    return
  }

  try {
    const menuStore = useMenuStore()

    // 加载菜单数据并生成动态路由
    await menuStore.loadMenuData()
    const dynamicRoutes = menuStore.getDynamicRoutes()

    if (dynamicRoutes.length === 0) {
      // 即使没有动态路由，也标记为已加载，避免重复尝试
      isRoutesLoaded = true
      return
    }

    // 添加动态路由到布局路由的children中
     const modules = import.meta.glob('@/views/**/*.vue')

    dynamicRoutes.forEach(route => {
      route.component = modules[`@/views${route.path}.vue`]
      router.addRoute('Layout', route)
    })

    // 添加通配符路由（必须在最后添加）
    router.addRoute({
      path: '/:pathMatch(.*)*',
      redirect: '/404'
    })

    isRoutesLoaded = true

  } catch (error) {
    // 标记为已加载，避免无限重试
    isRoutesLoaded = true
    throw error
  }
}

/**
 * 重置路由（用于登出时清理动态路由）
 */
export const resetRouter = (): void => {
  // 获取当前所有路由
  const routes = router.getRoutes()

  // 移除动态添加的路由（保留基础路由和布局路由）
  routes.forEach(route => {
    if (route.name && !['Login', 'NotFound', 'Forbidden', 'ServerError', 'Layout', 'Dashboard'].includes(route.name as string)) {
      router.removeRoute(route.name)
    }
  })

  // 移除通配符路由
  if (router.hasRoute('pathMatch')) {
    router.removeRoute('pathMatch')
  }

  isRoutesLoaded = false
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const uiStore = useUIStore()

  // 显示进度条
  uiStore.showProgress()
  
  // 模拟进度增长
  const progressTimer = setInterval(() => {
    if (uiStore.progressValue < 70) {
      uiStore.incrementProgress(Math.random() * 20)
    }
  }, 100)

  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - ${appConfig.title}`
  }

  // 白名单直接放行
  if (whiteList.includes(to.path)) {
    clearInterval(progressTimer)
    uiStore.finishProgress()
    return next()
  }

  // 检查用户是否已登录
  if (!userStore.isLoggedIn) {
    clearInterval(progressTimer)
    uiStore.finishProgress()
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
    return
  }

  // 加载动态路由
  if (!isRoutesLoaded) {
    try {
      await loadDynamicRoutes()
      const menuStore = useMenuStore()
      const dynamicRoutes = menuStore.getDynamicRoutes()

      // 动态路由为空则跳转403
      if (dynamicRoutes?.length === 0) {
        clearInterval(progressTimer)
        uiStore.finishProgress()
        next('/403')
        return
      }

      // 处理根路径重定向 - 默认跳转到dashboard
      if (to.path === '/') {
        clearInterval(progressTimer)
        uiStore.finishProgress()
        next('/dashboard')
        return
      }

      // 检查当前路径是否在动态路由中（跳过dashboard固定路由）
      if (to.path !== '/dashboard' && !menuStore.findMenuByPath(to.path)) {
        clearInterval(progressTimer)
        uiStore.finishProgress()
        next('/403')
        return
      }

      // 重新导航到当前路径以应用新路由
      clearInterval(progressTimer)
      uiStore.setProgress(90)
      next({ ...to, replace: true })
      return
    } catch (error) {
      console.error('加载动态路由失败:', error)
      clearInterval(progressTimer)
      uiStore.finishProgress()
      const menuStore = useMenuStore()
      const firstRoute = menuStore.getFirstVisibleRoute() || '/403'
      next(firstRoute.startsWith('/') ? firstRoute : `/${firstRoute}`)
      return
    }
  }

  clearInterval(progressTimer)
  uiStore.setProgress(90)
  next()
})

// 路由后置守卫
router.afterEach(() => {
  const uiStore = useUIStore()
  // 完成进度条
  uiStore.finishProgress()
})

// 路由错误处理
router.onError((error) => {
  const uiStore = useUIStore()
  uiStore.hideProgress()
  console.error('路由错误:', error)
})

export default router
