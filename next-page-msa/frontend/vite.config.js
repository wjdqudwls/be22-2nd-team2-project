import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 3000,
    strictPort: true, // 포트 3000번이 사용 중이면 실행 실패 (좀비 프로세스 방지)
    proxy: {
      '/api': {
        target: 'http://localhost:8000', // Gateway Server
        changeOrigin: true,
        secure: false
      },
      '/ws': {
        target: 'http://localhost:8082', // Story Service (WebSocket)
        ws: true,
        changeOrigin: true
      }
    }
  }
})
