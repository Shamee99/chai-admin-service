<template>
  <div class="error-page">
    <div class="error-background">
      <div class="circuit-board">
        <div class="circuit-line" v-for="i in 15" :key="i" :style="getCircuitStyle(i)"></div>
      </div>
      <div class="floating-gears">
        <div class="gear" v-for="i in 6" :key="i" :style="getGearStyle(i)">
          <div class="gear-tooth" v-for="j in 8" :key="j"></div>
        </div>
      </div>
    </div>
    <div class="error-content">
      <div class="error-illustration">
        <div class="robot-container">
          <div class="robot">
            <div class="robot-head">
              <div class="robot-antenna">
                <div class="antenna-ball"></div>
              </div>
              <div class="robot-eyes">
                <div class="eye eye-left">
                  <div class="eye-pupil"></div>
                </div>
                <div class="eye eye-right">
                  <div class="eye-pupil"></div>
                </div>
              </div>
              <div class="robot-mouth"></div>
            </div>
            <div class="robot-body">
              <div class="body-panel">
                <div class="status-light error"></div>
                <div class="status-light warning"></div>
                <div class="status-light off"></div>
              </div>
              <div class="body-screen">
                <div class="screen-text">ERROR</div>
                <div class="screen-code">500</div>
              </div>
            </div>
            <div class="robot-arms">
              <div class="arm arm-left">
                <div class="hand"></div>
              </div>
              <div class="arm arm-right">
                <div class="hand"></div>
              </div>
            </div>
          </div>
          <div class="sparks">
            <div class="spark" v-for="i in 10" :key="i" :style="getSparkStyle(i)"></div>
          </div>
        </div>
      </div>
      <div class="error-text">
        <h1 class="error-code">500</h1>
        <h2 class="error-title">服务器开小差了</h2>
        <p class="error-description">哎呀！我们的服务器机器人遇到了一些技术故障，正在紧急修复中。请稍后再试或刷新页面。</p>
        <div class="error-actions">
          <el-button type="primary" size="large" @click="goHome" class="action-btn primary-btn">
            <el-icon><House /></el-icon>
            返回首页
          </el-button>
          <el-button size="large" @click="refresh" class="action-btn secondary-btn">
            <el-icon><Refresh /></el-icon>
            刷新页面
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { House, Refresh } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/dashboard')
}

const refresh = () => {
  window.location.reload()
}

const getCircuitStyle = (index: number) => {
  const isHorizontal = index % 2 === 0
  const position = (index / 15) * 100
  const width = Math.random() * 200 + 50

  return {
    [isHorizontal ? 'top' : 'left']: `${position}%`,
    [isHorizontal ? 'width' : 'height']: `${width}px`,
    animationDelay: `${index * 0.2}s`
  }
}

const getGearStyle = (index: number) => {
  const size = Math.random() * 40 + 20
  const top = Math.random() * 100
  const left = Math.random() * 100
  const duration = Math.random() * 10 + 5

  return {
    width: `${size}px`,
    height: `${size}px`,
    top: `${top}%`,
    left: `${left}%`,
    animationDuration: `${duration}s`
  }
}

const getSparkStyle = (index: number) => {
  const size = Math.random() * 4 + 2
  const top = Math.random() * 100
  const left = Math.random() * 100
  const delay = Math.random() * 2

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
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
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

.circuit-board {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.circuit-line {
  position: absolute;
  background: rgba(52, 152, 219, 0.2);
  animation: circuit-flow 4s ease-in-out infinite;
}

.circuit-line:nth-child(even) {
  height: 2px;
}

.circuit-line:nth-child(odd) {
  width: 2px;
}

@keyframes circuit-flow {
  0%, 100% {
    opacity: 0.2;
    box-shadow: 0 0 5px rgba(52, 152, 219, 0.2);
  }
  50% {
    opacity: 0.8;
    box-shadow: 0 0 15px rgba(52, 152, 219, 0.6);
  }
}

.floating-gears {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.gear {
  position: absolute;
  border-radius: 50%;
  background: rgba(149, 165, 166, 0.1);
  animation: gear-rotate 15s linear infinite;
  display: flex;
  align-items: center;
  justify-content: center;
}

.gear-tooth {
  position: absolute;
  width: 4px;
  height: 8px;
  background: rgba(149, 165, 166, 0.2);
  border-radius: 2px;
}

.gear-tooth:nth-child(1) { top: -4px; left: 50%; transform: translateX(-50%); }
.gear-tooth:nth-child(2) { top: 50%; right: -4px; transform: translateY(-50%) rotate(90deg); }
.gear-tooth:nth-child(3) { bottom: -4px; left: 50%; transform: translateX(-50%); }
.gear-tooth:nth-child(4) { top: 50%; left: -4px; transform: translateY(-50%) rotate(90deg); }
.gear-tooth:nth-child(5) { top: 15%; right: 15%; transform: rotate(45deg); }
.gear-tooth:nth-child(6) { bottom: 15%; right: 15%; transform: rotate(135deg); }
.gear-tooth:nth-child(7) { bottom: 15%; left: 15%; transform: rotate(225deg); }
.gear-tooth:nth-child(8) { top: 15%; left: 15%; transform: rotate(315deg); }

@keyframes gear-rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
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

.robot-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.robot {
  position: relative;
  animation: robot-shake 2s ease-in-out infinite;
}

@keyframes robot-shake {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-2px);
  }
  75% {
    transform: translateX(2px);
  }
}

.robot-head {
  width: 100px;
  height: 80px;
  background: linear-gradient(145deg, #ecf0f1, #bdc3c7);
  border-radius: 20px;
  position: relative;
  margin: 0 auto 10px;
  border: 3px solid #95a5a6;
}

.robot-antenna {
  position: absolute;
  top: -20px;
  left: 50%;
  transform: translateX(-50%);
  width: 3px;
  height: 15px;
  background: #95a5a6;
}

.antenna-ball {
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 8px;
  height: 8px;
  background: #e74c3c;
  border-radius: 50%;
  animation: antenna-blink 1s ease-in-out infinite alternate;
}

@keyframes antenna-blink {
  from {
    opacity: 1;
    box-shadow: 0 0 10px #e74c3c;
  }
  to {
    opacity: 0.3;
    box-shadow: 0 0 5px #e74c3c;
  }
}

.robot-eyes {
  display: flex;
  justify-content: space-between;
  padding: 20px 25px 10px;
}

.eye {
  width: 20px;
  height: 20px;
  background: #2c3e50;
  border-radius: 50%;
  position: relative;
  animation: eye-blink 3s ease-in-out infinite;
}

.eye-pupil {
  width: 8px;
  height: 8px;
  background: #e74c3c;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: pupil-glow 1.5s ease-in-out infinite alternate;
}

@keyframes eye-blink {
  0%, 90%, 100% {
    transform: scaleY(1);
  }
  95% {
    transform: scaleY(0.1);
  }
}

@keyframes pupil-glow {
  from {
    box-shadow: 0 0 5px #e74c3c;
  }
  to {
    box-shadow: 0 0 15px #e74c3c, 0 0 25px #e74c3c;
  }
}

.robot-mouth {
  position: absolute;
  bottom: 15px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 3px;
  background: #e74c3c;
  border-radius: 2px;
}

.robot-body {
  width: 120px;
  height: 140px;
  background: linear-gradient(145deg, #ecf0f1, #bdc3c7);
  border-radius: 15px;
  margin: 0 auto;
  border: 3px solid #95a5a6;
  position: relative;
  padding: 20px;
}

.body-panel {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.status-light {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  animation: status-blink 2s ease-in-out infinite;
}

.status-light.error {
  background: #e74c3c;
  box-shadow: 0 0 10px #e74c3c;
}

.status-light.warning {
  background: #f39c12;
  box-shadow: 0 0 10px #f39c12;
  animation-delay: 0.5s;
}

.status-light.off {
  background: #7f8c8d;
  opacity: 0.3;
}

@keyframes status-blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.3;
  }
}

.body-screen {
  width: 100%;
  height: 60px;
  background: #2c3e50;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px solid #34495e;
}

.screen-text {
  color: #e74c3c;
  font-size: 12px;
  font-weight: bold;
  margin-bottom: 5px;
  animation: screen-flicker 1s ease-in-out infinite;
}

.screen-code {
  color: #e74c3c;
  font-size: 18px;
  font-weight: bold;
  animation: screen-flicker 1s ease-in-out infinite;
}

@keyframes screen-flicker {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.robot-arms {
  display: flex;
  justify-content: space-between;
  position: absolute;
  top: 120px;
  left: -30px;
  right: -30px;
}

.arm {
  width: 15px;
  height: 60px;
  background: linear-gradient(145deg, #ecf0f1, #bdc3c7);
  border-radius: 8px;
  border: 2px solid #95a5a6;
  position: relative;
}

.arm-left {
  transform: rotate(-20deg);
  animation: arm-wave-left 3s ease-in-out infinite;
}

.arm-right {
  transform: rotate(20deg);
  animation: arm-wave-right 3s ease-in-out infinite;
}

@keyframes arm-wave-left {
  0%, 100% {
    transform: rotate(-20deg);
  }
  50% {
    transform: rotate(-40deg);
  }
}

@keyframes arm-wave-right {
  0%, 100% {
    transform: rotate(20deg);
  }
  50% {
    transform: rotate(40deg);
  }
}

.hand {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 12px;
  background: linear-gradient(145deg, #ecf0f1, #bdc3c7);
  border-radius: 50%;
  border: 2px solid #95a5a6;
}

.sparks {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.spark {
  position: absolute;
  background: #f39c12;
  border-radius: 50%;
  animation: spark-fly 2s ease-out infinite;
}

@keyframes spark-fly {
  0% {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
  100% {
    opacity: 0;
    transform: scale(0.5) translateY(-50px);
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
  background: linear-gradient(45deg, #3498db, #2980b9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(52, 152, 219, 0.5);
  animation: code-pulse 2s ease-in-out infinite alternate;
}

@keyframes code-pulse {
  from {
    text-shadow: 0 0 20px rgba(52, 152, 219, 0.5);
  }
  to {
    text-shadow: 0 0 30px rgba(52, 152, 219, 0.8), 0 0 40px rgba(41, 128, 185, 0.5);
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
  background: linear-gradient(45deg, #3498db, #2980b9);
  color: #fff;
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
}

.primary-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 152, 219, 0.6);
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

  .robot-head {
    width: 80px;
    height: 64px;
  }

  .robot-body {
    width: 96px;
    height: 112px;
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
