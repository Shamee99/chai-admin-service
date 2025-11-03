<template>
  <div class="error-page">
    <div class="error-background">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
      </div>
    </div>
    <div class="error-content">
      <div class="error-illustration">
        <div class="astronaut">
          <div class="helmet">
            <div class="helmet-glass"></div>
            <div class="helmet-reflection"></div>
          </div>
          <div class="body"></div>
          <div class="arm arm-left"></div>
          <div class="arm arm-right"></div>
          <div class="leg leg-left"></div>
          <div class="leg leg-right"></div>
        </div>
        <div class="planet"></div>
        <div class="stars">
          <div class="star" v-for="i in 20" :key="i" :style="getStarStyle(i)"></div>
        </div>
      </div>
      <div class="error-text">
        <h1 class="error-code">404</h1>
        <h2 class="error-title">页面走丢了</h2>
        <p class="error-description">哎呀！您要找的页面好像在宇宙中迷路了，让我们帮您找到回家的路吧～</p>
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

const getStarStyle = (index: number) => {
  const size = Math.random() * 3 + 1
  const top = Math.random() * 100
  const left = Math.random() * 100
  const delay = Math.random() * 3

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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

.floating-shapes {
  position: relative;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 60px;
  height: 60px;
  top: 60%;
  right: 10%;
  animation-delay: 2s;
}

.shape-3 {
  width: 40px;
  height: 40px;
  top: 80%;
  left: 20%;
  animation-delay: 4s;
}

.shape-4 {
  width: 100px;
  height: 100px;
  top: 10%;
  right: 20%;
  animation-delay: 1s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
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

.astronaut {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: astronaut-float 4s ease-in-out infinite;
}

@keyframes astronaut-float {
  0%, 100% {
    transform: translate(-50%, -50%) rotate(-5deg);
  }
  50% {
    transform: translate(-50%, -60%) rotate(5deg);
  }
}

.helmet {
  position: relative;
  width: 80px;
  height: 80px;
  background: #f0f0f0;
  border-radius: 50%;
  margin: 0 auto 10px;
  border: 3px solid #ddd;
}

.helmet-glass {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 56px;
  height: 56px;
  background: rgba(135, 206, 250, 0.3);
  border-radius: 50%;
  border: 2px solid #87ceeb;
}

.helmet-reflection {
  position: absolute;
  top: 15px;
  left: 15px;
  width: 20px;
  height: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
}

.body {
  width: 60px;
  height: 80px;
  background: #fff;
  border-radius: 30px;
  margin: 0 auto;
  border: 3px solid #ddd;
  position: relative;
}

.body::before {
  content: '';
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 30px;
  background: #4CAF50;
  border-radius: 50%;
}

.arm {
  position: absolute;
  width: 15px;
  height: 40px;
  background: #fff;
  border: 2px solid #ddd;
  border-radius: 10px;
  top: 90px;
}

.arm-left {
  left: -20px;
  transform: rotate(-20deg);
}

.arm-right {
  right: -20px;
  transform: rotate(20deg);
}

.leg {
  position: absolute;
  width: 12px;
  height: 35px;
  background: #fff;
  border: 2px solid #ddd;
  border-radius: 8px;
  top: 160px;
}

.leg-left {
  left: 15px;
}

.leg-right {
  right: 15px;
}

.planet {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  border-radius: 50%;
  animation: planet-rotate 20s linear infinite;
}

.planet::before {
  content: '';
  position: absolute;
  top: 20px;
  left: 20px;
  width: 15px;
  height: 15px;
  background: #c44569;
  border-radius: 50%;
}

.planet::after {
  content: '';
  position: absolute;
  bottom: 30px;
  right: 25px;
  width: 10px;
  height: 10px;
  background: #c44569;
  border-radius: 50%;
}

@keyframes planet-rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.stars {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.star {
  position: absolute;
  background: #fff;
  border-radius: 50%;
  animation: twinkle 2s ease-in-out infinite alternate;
}

@keyframes twinkle {
  from {
    opacity: 0.3;
  }
  to {
    opacity: 1;
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
  background: linear-gradient(45deg, #fff, #f1c40f);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(255, 255, 255, 0.5);
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from {
    text-shadow: 0 0 20px rgba(255, 255, 255, 0.5);
  }
  to {
    text-shadow: 0 0 30px rgba(255, 255, 255, 0.8), 0 0 40px rgba(241, 196, 15, 0.5);
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
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: #fff;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.primary-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
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
