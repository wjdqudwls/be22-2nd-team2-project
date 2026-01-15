import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'

import App from './App.vue'
import router from './router'

import './assets/css/main.css'

const app = createApp(App)

// Axios Global Defaults
axios.defaults.baseURL = '/api' // Proxy via Vite
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('accessToken')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// Global Error Handler for 401
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            // Dispatch action to auth store via event bus or direct access if possible
            // For now, let's clear storage
            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
            window.location.href = '/' // Simple redirect
        }
        return Promise.reject(error)
    }
)

app.use(createPinia())
app.use(router)

app.mount('#app')
