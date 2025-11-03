<template>
  <div v-if="progressVisible" class="global-progress">
    <div 
      class="progress-bar" 
      :style="{ width: `${progressValue}%` }"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useUIStore } from '@/stores/ui-store'

// Store
const uiStore = useUIStore()

// 计算属性
const progressVisible = computed(() => uiStore.progressVisible)
const progressValue = computed(() => uiStore.progressValue)
</script>

<style scoped>
.global-progress {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background-color: rgba(0, 0, 0, 0.1);
  z-index: 9999;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #409eff 0%, #79bbff 50%, #409eff 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  transition: width 0.3s ease;
  border-radius: 0 3px 3px 0;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

/* 暗色主题适配 */
:root.dark .global-progress,
[data-theme='dark'] .global-progress {
  background-color: rgba(255, 255, 255, 0.1);
}

:root.dark .progress-bar,
[data-theme='dark'] .progress-bar {
  background: linear-gradient(90deg, #409eff 0%, #79bbff 50%, #409eff 100%);
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.8);
}
</style>