<template>
  <div>
    <NavBar />

    <main class="container fade-in" style="padding: 40px 20px; min-height: 80vh;">
      <RouterView />
    </main>

    <footer>
      <div class="container text-center" style="padding: 40px 0; color: var(--text-muted); font-size: 0.9rem;">
        &copy; 2026 Team Next Page. All rights reserved.
      </div>
    </footer>

    <AuthModals />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import AuthModals from '@/components/AuthModals.vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

onMounted(() => {
  authStore.checkAutoLogin()
})
</script>
