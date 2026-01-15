import { createApp, h } from 'vue'
import ToastNotification from '@/components/ToastNotification.vue'

let toastQueue = []
let currentToast = null

/**
 * 토스트 알림을 표시합니다
 * @param {string} message - 표시할 메시지
 * @param {string} type - 토스트 타입 ('success', 'error', 'warning', 'info')
 * @param {number} duration - 표시 시간 (밀리초, 기본값: 3000)
 */
export function showToast(message, type = 'info', duration = 3000) {
    // 현재 토스트가 있으면 큐에 추가
    if (currentToast) {
        toastQueue.push({ message, type, duration })
        return
    }

    displayToast(message, type, duration)
}

function displayToast(message, type, duration) {
    const container = document.createElement('div')
    document.body.appendChild(container)

    const app = createApp({
        render() {
            return h(ToastNotification, {
                message,
                type,
                duration,
                onClose: () => {
                    app.unmount()
                    document.body.removeChild(container)
                    currentToast = null

                    // 큐에 다음 토스트가 있으면 표시
                    if (toastQueue.length > 0) {
                        const next = toastQueue.shift()
                        setTimeout(() => {
                            displayToast(next.message, next.type, next.duration)
                        }, 100)
                    }
                }
            })
        }
    })

    currentToast = app.mount(container)
}

// 편의 메서드들
export const toast = {
    success: (message, duration) => showToast(message, 'success', duration),
    error: (message, duration) => showToast(message, 'error', duration),
    warning: (message, duration) => showToast(message, 'warning', duration),
    info: (message, duration) => showToast(message, 'info', duration)
}
