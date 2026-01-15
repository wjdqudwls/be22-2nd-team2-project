<template>
  <div>
    <!-- Login Modal -->
    <div v-if="authStore.showLoginModal" class="modal-overlay active" @mousedown="handleOverlayMousedown" @click="handleOverlayClick(closeLogin, $event)">
      <div class="modal text-center">
        <a href="/" class="logo" style="display: block; margin-bottom: 20px; text-decoration: none;">
          <img src="/images/logo.png" alt="Next Page" style="height: 80px;">
        </a>
        <button class="btn-close" @click="closeLogin">&times;</button>
        <h2 class="mb-4">로그인</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label class="form-label" for="login-email">이메일</label>
            <input type="email" id="login-email" class="form-control" placeholder="user@example.com" required v-model="loginForm.email">
          </div>
          <div class="form-group">
            <label class="form-label" for="login-password">비밀번호</label>
            <input type="password" id="login-password" class="form-control" placeholder="비밀번호" required v-model="loginForm.password">
          </div>
          <div class="form-group" style="text-align: left; margin-top: 15px;">
            <label class="checkbox-container">
              <input type="checkbox" v-model="loginForm.autoLogin">
              <span class="checkmark"></span>
              <span style="margin-left: 8px; color: var(--text-color); font-size: 0.9rem;">자동 로그인</span>
            </label>
          </div>
          <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;" :disabled="loading">
            {{ loading ? '로그인 중...' : '로그인' }}
          </button>
        </form>
        <div class="text-center mt-4" style="font-size: 0.9rem;">
          <span style="color: var(--text-muted);">계정이 없으신가요?</span>
          <a href="#" @click.prevent="switchToSignup" style="color: var(--primary-color); margin-left: 5px;">회원가입</a>
        </div>
      </div>
    </div>

    <!-- Signup Modal -->
    <div v-if="authStore.showSignupModal" class="modal-overlay active" @mousedown="handleOverlayMousedown" @click="handleOverlayClick(closeSignup, $event)">
      <div class="modal text-center">
        <a href="/" class="logo" style="display: block; margin-bottom: 20px; text-decoration: none;">
          <img src="/images/logo.png" alt="Next Page" style="height: 80px;">
        </a>
        <button class="btn-close" @click="closeSignup">&times;</button>
        <h2 class="mb-4">회원가입</h2>
        <form @submit.prevent="handleSignup">
          <div class="form-group">
            <label class="form-label" for="signup-email">이메일</label>
            <input type="email" id="signup-email" class="form-control" placeholder="user@example.com" required v-model="signupForm.email" @input="debouncedEmailCheck">
            <span class="validation-message" :class="emailMsgClass">{{ emailMsg }}</span>
          </div>
          <div class="form-group">
            <label class="form-label" for="signup-nickname">닉네임</label>
            <input type="text" id="signup-nickname" class="form-control" placeholder="작가명 (2~10자)" required v-model="signupForm.nickname" @input="debouncedNickCheck">
            <span class="validation-message" :class="nickMsgClass">{{ nickMsg }}</span>
          </div>
          <div class="form-group">
            <label class="form-label" for="signup-password">비밀번호</label>
            <input type="password" id="signup-password" class="form-control" placeholder="영문/숫자/특수문자 포함 8자 이상" required v-model="signupForm.password">
            <span class="validation-message" :class="pwMsgClass">{{ pwMsg }}</span>
          </div>
          <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 20px;" :disabled="signupLoading || !isSignupValid">
            {{ signupLoading ? '가입 중...' : '가입하고 바로 시작하기' }}
          </button>
        </form>
        <div class="text-center mt-4" style="font-size: 0.9rem;">
            <span style="color: var(--text-muted);">이미 계정이 있으신가요?</span>
            <a href="#" @click.prevent="switchToLogin" style="color: var(--primary-color); margin-left: 5px;">로그인</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()

// Login State
const loginForm = ref({ email: '', password: '', autoLogin: false })
const loading = ref(false)

// Signup State
const signupForm = ref({ email: '', nickname: '', password: '' })
const signupLoading = ref(false)

const emailMsg = ref('')
const emailValid = ref(false)
const nickMsg = ref('')
const nickValid = ref(false)
const pwMsg = ref('')
const pwValid = ref(false)

const emailMsgClass = computed(() => emailValid.value ? 'valid' : 'invalid')
const nickMsgClass = computed(() => nickValid.value ? 'valid' : 'invalid')
const pwMsgClass = computed(() => pwValid.value ? 'valid' : 'invalid')

// Modal Actions
const closeLogin = () => authStore.closeLogin()
const closeSignup = () => authStore.closeSignup()

const switchToSignup = () => {
    closeLogin()
    authStore.openSignup()
}
const switchToLogin = () => {
    closeSignup()
    authStore.openLogin()
}

// Login Logic
const handleLogin = async () => {
    loading.value = true
    try {
        await authStore.login(loginForm.value.email, loginForm.value.password, loginForm.value.autoLogin)
        closeLogin()
        // Reset form
        loginForm.value = { email: '', password: '', autoLogin: false }
    } catch (e) {
        alert(e.response?.data?.message || '로그인 실패')
    } finally {
        loading.value = false
    }
}

// Validation Logic
const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)

let emailTimer = null
const debouncedEmailCheck = () => {
    const email = signupForm.value.email
    if (!email) { emailMsg.value = ''; return }
    if (!validateEmail(email)) {
        emailMsg.value = '이메일 형식이 올바르지 않습니다.'
        emailValid.value = false
        return
    }
    
    clearTimeout(emailTimer)
    emailTimer = setTimeout(async () => {
        try {
            await axios.get('/auth/check-email', { params: { email } })
            emailMsg.value = '사용 가능한 이메일입니다.'
            emailValid.value = true
        } catch (e) {
            emailMsg.value = e.response?.data?.message || '이미 사용 중인 이메일입니다.'
            emailValid.value = false
        }
    }, 500)
}

let nickTimer = null
const debouncedNickCheck = () => {
    const nick = signupForm.value.nickname
    if (!nick) { nickMsg.value = ''; return }
    if (nick.length < 2 || nick.length > 10) {
        nickMsg.value = '닉네임은 2~10자로 입력해주세요.'
        nickValid.value = false
        return
    }
    
    clearTimeout(nickTimer)
    nickTimer = setTimeout(async () => {
        try {
            await axios.get('/auth/check-nickname', { params: { nickname: nick } })
            nickMsg.value = '사용 가능한 닉네임입니다.'
            nickValid.value = true
        } catch (e) {
            nickMsg.value = e.response?.data?.message || '이미 사용 중인 닉네임입니다.'
            nickValid.value = false
        }
    }, 500)
}

watch(() => signupForm.value.password, (newVal) => {
    if (!newVal) { pwMsg.value = ''; return }
    if (newVal.length >= 8) {
        pwMsg.value = '사용 가능한 비밀번호입니다.'
        pwValid.value = true
    } else {
        pwMsg.value = '8자 이상 입력해주세요.'
        pwValid.value = false
    }
})

const isSignupValid = computed(() => emailValid.value && nickValid.value && pwValid.value)

const handleSignup = async () => {
    if (!isSignupValid.value) return
    signupLoading.value = true
    try {
        await authStore.signup(signupForm.value.email, signupForm.value.nickname, signupForm.value.password)
        closeSignup()
        alert('가입 완료! 환영합니다.')
        // Reset
        signupForm.value = { email: '', nickname: '', password: '' }
    } catch (e) {
        alert(e.response?.data?.message || '가입 실패')
    } finally {
        signupLoading.value = false
    }
}

// Modal Backdrop Click Handling
const mouseDownTarget = ref(null)

const handleOverlayMousedown = (e) => {
    mouseDownTarget.value = e.target
}

const handleOverlayClick = (closeFn, e) => {
    if (mouseDownTarget.value === e.currentTarget && e.target === e.currentTarget) {
        closeFn()
    }
    mouseDownTarget.value = null
}
</script>
