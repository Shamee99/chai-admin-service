import { defineStore } from 'pinia'
import { ref } from 'vue'

/**
 * UI状态管理Store
 * 负责管理界面相关的状态，如侧边栏折叠、主题等
 */
export const useUIStore = defineStore('ui', () => {
  // 状态
  const sidebarCollapsed = ref<boolean>(false)
  const rememberedUsername = ref<string>('')
  const rememberedPassword = ref<string>('')
  const progressVisible = ref<boolean>(false)
  const progressValue = ref<number>(0)

  // 设置侧边栏折叠状态
  const setSidebarCollapsed = (collapsed: boolean): void => {
    sidebarCollapsed.value = collapsed
  }

  // 切换侧边栏折叠状态
  const toggleSidebar = (): void => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  // 设置记住的用户名和密码
  const setRememberedCredentials = (username: string, password: string): void => {
    rememberedUsername.value = username
    rememberedPassword.value = password
  }

  // 清除记住的用户名和密码
  const clearRememberedCredentials = (): void => {
    rememberedUsername.value = ''
    rememberedPassword.value = ''
  }

  // 显示进度条
  const showProgress = (): void => {
    progressVisible.value = true
    progressValue.value = 0
  }

  // 隐藏进度条
  const hideProgress = (): void => {
    progressVisible.value = false
    progressValue.value = 0
  }

  // 设置进度值
  const setProgress = (value: number): void => {
    progressValue.value = Math.min(Math.max(value, 0), 100)
  }

  // 增加进度值
  const incrementProgress = (increment: number = 10): void => {
    const newValue = progressValue.value + increment
    setProgress(newValue)
  }

  // 完成进度（设置为100%然后隐藏）
  const finishProgress = (): void => {
    setProgress(100)
    setTimeout(() => {
      hideProgress()
    }, 300)
  }

  return {
    // 状态
    sidebarCollapsed,
    rememberedUsername,
    rememberedPassword,
    progressVisible,
    progressValue,

    // 方法
    setSidebarCollapsed,
    toggleSidebar,
    setRememberedCredentials,
    clearRememberedCredentials,
    showProgress,
    hideProgress,
    setProgress,
    incrementProgress,
    finishProgress
  }
}, {
  persist: {
    key: 'ui-store',
    storage: localStorage,
    paths: ['sidebarCollapsed', 'rememberedUsername', 'rememberedPassword', 'progressVisible', 'progressValue']
  }
})