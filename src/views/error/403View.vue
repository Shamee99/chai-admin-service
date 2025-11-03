<template>
  <div class="error-page">
    <div class="error-background">
      <div class="security-grid">
        <div class="grid-line" v-for="i in 20" :key="i" :style="getGridLineStyle(i)"></div>
      </div>
      <div class="floating-locks">
        <div class="lock-icon" v-for="i in 8" :key="i" :style="getLockStyle(i)">
          <div class="lock-body"></div>
          <div class="lock-shackle"></div>
        </div>
      </div>
    </div>
    <div class="error-content">
      <div class="error-illustration">
        <div class="vault-container">
          <div class="vault">
            <div class="vault-door">
              <div class="vault-handle"></div>
              <div class="vault-lock">
                <div class="lock-circle">
                  <div class="lock-keyhole"></div>
                </div>
              </div>
              <div class="vault-bolts">
                <div class="bolt" v-for="i in 8" :key="i"></div>
              </div>
            </div>
          </div>
          <div class="warning-signs">
            <div class="warning-sign" v-for="i in 3" :key="i">
              <div class="warning-triangle">
                <div class="warning-exclamation">!</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="error-text">
        <h1 class="error-code">403</h1>
        <h2 class="error-title">访问被拒绝</h2>
        <p class="error-description">抱歉，这个区域需要特殊权限才能进入。请联系管理员获取访问权限，或返回安全区域。</p>
        <div class="error-actions">
          <el-button type="primary" size="large" @click="goHome" class="action-btn primary-btn">
            <el-icon><House /></el-icon>
            返回首页
          </el-button>
          <el-button size="large" @click="goBack" class="action-btn secondary-btn">
            <el-icon><ArrowLeft /></el-icon>
            返回上页
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { House, ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/dashboard')
}

const goBack = () => {
  router.go(-1)
}

const getGridLineStyle = (index: number) => {
  const isVertical = index % 2 === 0
  const position = (index / 20) * 100

  return {
    [isVertical ? 'left' : 'top']: `${position}%`,
    animationDelay: `${index * 0.1}s`
  }
}

const getLockStyle = (index: number) => {
  const size = Math.random() * 20 + 10
  const top = Math.random() * 100
  const left = Math.random() * 100
  const delay = Math.random() * 4

  return {
    width: `${size}px`,
    height: `${size}px`,
    top: `${top}%`,
    left: `${left}%`,
    animationDelay: `${delay}s`
  }
}
</script>

<style scoped>
.error-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  overflow: hidden;
}

.error-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.security-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.grid-line {
  position: absolute;
  background: rgba(231, 76, 60, 0.1);
  animation: security-scan 3s ease-in-out infinite;
}

.grid-line:nth-child(even) {
  width: 1px;
  height: 100%;
}

.grid-line:nth-child(odd) {
  width: 100%;
  height: 1px;
}

@keyframes security-scan {
  0%, 100% {
    opacity: 0.1;
  }
  50% {
    opacity: 0.3;
    background: rgba(231, 76, 60, 0.3);
  }
}

.floating-locks {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.lock-icon {
  position: absolute;
  animation: float-lock 4s ease-in-out infinite;
}

.lock-body {
  width: 60%;
  height: 70%;
  background: rgba(231, 76, 60, 0.2);
  border-radius: 2px;
  position: relative;
  top: 30%;
}

.lock-shackle {
  width: 40%;
  height: 40%;
  border: 2px solid rgba(231, 76, 60, 0.2);
  border-bottom: none;
  border-radius: 50% 50% 0 0;
  position: absolute;
  top: 10%;
  left: 30%;
}

@keyframes float-lock {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    opacity: 0.3;
  }
  50% {
    transform: translateY(-10px) rotate(180deg);
    opacity: 0.6;
  }
}

.error-content {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4rem;
  max-width: 1200px;
  padding: 2rem;
}

.error-illustration {
  position: relative;
  width: 400px;
  height: 400px;
  flex-shrink: 0;
}

.vault-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.vault {
  width: 250px;
  height: 250px;
  background: linear-gradient(145deg, #34495e, #2c3e50);
  border-radius: 20px;
  position: relative;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.3),
    inset 0 2px 4px rgba(255, 255, 255, 0.1);
  animation: vault-pulse 3s ease-in-out infinite;
}

@keyframes vault-pulse {
  0%, 100% {
    box-shadow:
      0 20px 40px rgba(0, 0, 0, 0.3),
      inset 0 2px 4px rgba(255, 255, 255, 0.1);
  }
  50% {
    box-shadow:
      0 25px 50px rgba(0, 0, 0, 0.4),
      inset 0 2px 4px rgba(255, 255, 255, 0.2),
      0 0 30px rgba(231, 76, 60, 0.3);
  }
}

.vault-door {
  position: absolute;
  top: 20px;
  left: 20px;
  right: 20px;
  bottom: 20px;
  background: linear-gradient(145deg, #3d566e, #2c3e50);
  border-radius: 15px;
  border: 3px solid #1a252f;
}

.vault-handle {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  width: 15px;
  height: 60px;
  background: linear-gradient(145deg, #95a5a6, #7f8c8d);
  border-radius: 8px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
}

.vault-lock {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 80px;
}

.lock-circle {
  width: 100%;
  height: 100%;
  background: linear-gradient(145deg, #e74c3c, #c0392b);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    0 4px 8px rgba(0, 0, 0, 0.3),
    inset 0 2px 4px rgba(255, 255, 255, 0.2);
  animation: lock-glow 2s ease-in-out infinite alternate;
}

@keyframes lock-glow {
  from {
    box-shadow:
      0 4px 8px rgba(0, 0, 0, 0.3),
      inset 0 2px 4px rgba(255, 255, 255, 0.2);
  }
  to {
    box-shadow:
      0 4px 8px rgba(0, 0, 0, 0.3),
      inset 0 2px 4px rgba(255, 255, 255, 0.2),
      0 0 20px rgba(231, 76, 60, 0.6);
  }
}

.lock-keyhole {
  width: 12px;
  height: 12px;
  background: #1a252f;
  border-radius: 50%;
  position: relative;
}

.lock-keyhole::after {
  content: '';
  position: absolute;
  top: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 3px;
  height: 8px;
  background: #1a252f;
}

.vault-bolts {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.bolt {
  position: absolute;
  width: 8px;
  height: 8px;
  background: linear-gradient(145deg, #95a5a6, #7f8c8d);
  border-radius: 50%;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.3);
}

.bolt:nth-child(1) { top: 15px; left: 15px; }
.bolt:nth-child(2) { top: 15px; right: 15px; }
.bolt:nth-child(3) { bottom: 15px; left: 15px; }
.bolt:nth-child(4) { bottom: 15px; right: 15px; }
.bolt:nth-child(5) { top: 50%; left: 15px; transform: translateY(-50%); }
.bolt:nth-child(6) { top: 50%; right: 15px; transform: translateY(-50%); }
.bolt:nth-child(7) { top: 15px; left: 50%; transform: translateX(-50%); }
.bolt:nth-child(8) { bottom: 15px; left: 50%; transform: translateX(-50%); }

.warning-signs {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.warning-sign {
  position: absolute;
  animation: warning-blink 1.5s ease-in-out infinite;
}

.warning-sign:nth-child(1) {
  top: 20px;
  left: 20px;
}

.warning-sign:nth-child(2) {
  top: 20px;
  right: 20px;
}

.warning-sign:nth-child(3) {
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
}

.warning-triangle {
  width: 30px;
  height: 30px;
  background: linear-gradient(145deg, #f39c12, #e67e22);
  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.warning-exclamation {
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  position: absolute;
  top: 8px;
  left: 50%;
  transform: translateX(-50%);
}

@keyframes warning-blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.3;
  }
}

.error-text {
  text-align: center;
  color: #fff;
}

.error-code {
  font-size: 8rem;
  font-weight: 900;
  margin: 0;
  background: linear-gradient(45deg, #e74c3c, #f39c12);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(231, 76, 60, 0.5);
  animation: danger-glow 2s ease-in-out infinite alternate;
}

@keyframes danger-glow {
  from {
    text-shadow: 0 0 20px rgba(231, 76, 60, 0.5);
  }
  to {
    text-shadow: 0 0 30px rgba(231, 76, 60, 0.8), 0 0 40px rgba(243, 156, 18, 0.5);
  }
}

.error-title {
  font-size: 2.5rem;
  margin: 1rem 0;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.error-description {
  font-size: 1.2rem;
  margin-bottom: 3rem;
  line-height: 1.6;
  opacity: 0.9;
  max-width: 400px;
}

.error-actions {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  flex-wrap: wrap;
}

.action-btn {
  padding: 12px 24px;
  border-radius: 25px;
  font-weight: 600;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.primary-btn {
  background: linear-gradient(45deg, #e74c3c, #c0392b);
  color: #fff;
  box-shadow: 0 4px 15px rgba(231, 76, 60, 0.4);
}

.primary-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(231, 76, 60, 0.6);
}

.secondary-btn {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: 2px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
}

.secondary-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .error-content {
    flex-direction: column;
    gap: 2rem;
    text-align: center;
  }

  .error-illustration {
    width: 300px;
    height: 300px;
  }

  .vault {
    width: 200px;
    height: 200px;
  }

  .error-code {
    font-size: 6rem;
  }

  .error-title {
    font-size: 2rem;
  }

  .error-description {
    font-size: 1rem;
  }

  .error-actions {
    flex-direction: column;
    align-items: center;
  }

  .action-btn {
    width: 200px;
    justify-content: center;
  }
}
</style>
