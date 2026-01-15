<template>
  <transition name="toast-fade">
    <div v-if="visible" :class="['toast-container', `toast-${type}`]">
      <div class="toast-icon">
        <span v-if="type === 'success'">✓</span>
        <span v-else-if="type === 'error'">✕</span>
        <span v-else-if="type === 'warning'">!</span>
        <span v-else>ℹ</span>
      </div>
      <div class="toast-message">{{ message }}</div>
    </div>
  </transition>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  message: String,
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
  },
  duration: {
    type: Number,
    default: 3000
  }
})

const emit = defineEmits(['close'])
const visible = ref(true)

let timer = null

const show = () => {
  visible.value = true
  if (timer) clearTimeout(timer)
  
  timer = setTimeout(() => {
    hide()
  }, props.duration)
}

const hide = () => {
  visible.value = false
  setTimeout(() => {
    emit('close')
  }, 300)
}

watch(() => props.message, () => {
  if (props.message) {
    show()
  }
}, { immediate: true })
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 24px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  z-index: 10000;
  max-width: 90%;
  min-width: 280px;
  font-family: 'Pretendard', -apple-system, sans-serif;
  font-weight: 500;
}

.toast-icon {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  flex-shrink: 0;
}

.toast-message {
  font-size: 15px;
  line-height: 1.5;
  flex: 1;
}

/* Success */
.toast-success {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.95), rgba(5, 150, 105, 0.95));
  color: white;
}

.toast-success .toast-icon {
  background: rgba(255, 255, 255, 0.25);
  color: white;
}

/* Error */
.toast-error {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.95), rgba(220, 38, 38, 0.95));
  color: white;
}

.toast-error .toast-icon {
  background: rgba(255, 255, 255, 0.25);
  color: white;
}

/* Warning */
.toast-warning {
  background: linear-gradient(135deg, rgba(251, 191, 36, 0.95), rgba(245, 158, 11, 0.95));
  color: white;
}

.toast-warning .toast-icon {
  background: rgba(255, 255, 255, 0.25);
  color: white;
}

/* Info */
.toast-info {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.95), rgba(37, 99, 235, 0.95));
  color: white;
}

.toast-info .toast-icon {
  background: rgba(255, 255, 255, 0.25);
  color: white;
}

/* Animations */
.toast-fade-enter-active {
  animation: toast-in 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.toast-fade-leave-active {
  animation: toast-out 0.3s ease-out;
}

@keyframes toast-in {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

@keyframes toast-out {
  from {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
  to {
    opacity: 0;
    transform: translateX(-50%) translateY(-20px);
  }
}

@media (max-width: 640px) {
  .toast-container {
    top: 16px;
    min-width: auto;
    max-width: calc(100% - 32px);
    padding: 14px 20px;
  }

  .toast-icon {
    width: 24px;
    height: 24px;
    font-size: 14px;
  }

  .toast-message {
    font-size: 14px;
  }
}
</style>
