<template>
  <header>
    <div class="container">
      <nav>
        <router-link to="/" class="logo">
          <img src="/images/logo.png" alt="Next Page Icon" style="height:40px;">
          <span>Next Page</span>
        </router-link>

        <!-- Mobile Menu Toggle -->
        <button class="mobile-menu-toggle" @click="toggleMobileMenu" aria-label="메뉴">
          <span></span>
          <span></span>
          <span></span>
        </button>

        <!-- Navigation Links -->
        <ul class="nav-links" :class="{ 'mobile-open': mobileMenuOpen }">
          <template v-if="!authStore.isAuthenticated">
            <li class="nav-item">
              <button @click="handleLogin" class="btn btn-outline nav-btn">로그인</button>
            </li>
            <li class="nav-item">
              <button @click="handleSignup" class="btn btn-primary nav-btn">회원가입</button>
            </li>
          </template>
          <template v-else>
            <li class="nav-item">
              <router-link to="/books/new" @click="closeMobileMenu">소설 쓰기</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/mypage" @click="closeMobileMenu">마이페이지</router-link>
            </li>
            <li class="nav-item">
              <a href="#" @click.prevent="logout">로그아웃</a>
            </li>
          </template>
        </ul>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const mobileMenuOpen = ref(false)

const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}

const handleLogin = () => {
  authStore.openLogin()
  closeMobileMenu()
}

const handleSignup = () => {
  authStore.openSignup()
  closeMobileMenu()
}

const logout = () => {
  authStore.logout()
  router.push('/')
  closeMobileMenu()
}
</script>

<style scoped>
/* Header */
header {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 2px solid rgba(255, 211, 224, 0.5);
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 1000;
}

nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: var(--text-color);
  z-index: 1001;
}

.logo span {
  font-family: 'Gaegu', cursive;
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--primary-color);
}

/* Mobile Menu Toggle (Hamburger) */
.mobile-menu-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  z-index: 1001;
}

.mobile-menu-toggle span {
  width: 25px;
  height: 3px;
  background: var(--primary-color);
  border-radius: 2px;
  transition: all 0.3s ease;
}

/* Navigation Links */
.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  list-style: none;
}

.nav-btn {
  padding: 8px 20px !important;
  font-size: 0.9rem !important;
  white-space: nowrap;
}

.router-link-active {
  font-weight: 700;
  color: var(--primary-color) !important;
}

.nav-links a {
  text-decoration: none;
  color: var(--text-color);
  font-weight: 500;
  transition: color 0.3s;
  white-space: nowrap;
}

.nav-links a:hover {
  color: var(--primary-color);
}

/* Mobile Responsive */
@media (max-width: 768px) {
  .mobile-menu-toggle {
    display: flex;
    margin-left: auto;
  }

  .logo span {
    font-size: 1.4rem;
  }

  .logo img {
    height: 30px !important;
  }

  .nav-links {
    position: fixed;
    top: 0;
    right: -70%;
    height: 100vh;
    width: 70%;
    max-width: 250px;
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    flex-direction: column;
    justify-content: flex-start;
    padding: 80px 20px 30px;
    gap: 12px;
    box-shadow: -5px 0 20px rgba(0, 0, 0, 0.1);
    transition: right 0.3s ease;
    z-index: 1000;
  }

  .nav-links.mobile-open {
    right: 0;
  }

  .nav-item {
    width: 100%;
  }

  .nav-links a,
  .nav-btn {
    width: 100%;
    display: block;
    text-align: center;
    padding: 12px 20px;
  }

  .btn.nav-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .logo span {
    font-size: 1.2rem;
  }

  .nav-links {
    width: 75%;
    max-width: none;
    right: -75%;
  }
}
</style>
