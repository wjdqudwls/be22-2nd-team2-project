<template>
  <div style="max-width: 600px; margin: 0 auto;">
    <div class="card fade-in">
      <h2 class="text-center mb-4">새로운 이야기 시작하기</h2>
      <form @submit.prevent="createBook">
        <div class="form-group">
          <label class="form-label">제목</label>
          <input type="text" v-model="form.title" class="form-control" placeholder="소설 제목을 입력하세요 (최대 50자)" required>
        </div>

        <div class="form-group">
          <label class="form-label">장르 (카테고리)</label>
          <select v-model="form.categoryId" class="form-control">
             <option v-for="cat in categories" :key="cat.categoryId" :value="cat.categoryId">
               {{ cat.categoryName }}
             </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">최대 문장 수 (완결 기준)</label>
          <input type="number" v-model="form.maxSequence" class="form-control" min="10" max="100">
          <small style="color: var(--text-muted);">10 ~ 100 사이의 문장 수로 설정 가능합니다.</small>
        </div>

        <div class="form-group">
          <label class="form-label">첫 문장</label>
          <textarea v-model="form.firstSentence" class="form-control" rows="4" placeholder="이야기의 시작을 열어주세요... (최대 200자)"
            required></textarea>
        </div>

        <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 20px;" :disabled="loading">
          {{ loading ? '생성 중...' : '이야기 생성하기' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const router = useRouter()
const authStore = useAuthStore()

const categories = ref([])
const loading = ref(false)
const form = ref({
  title: '', categoryId: '', maxSequence: 20, firstSentence: ''
})

onMounted(async () => {
    if (!authStore.isAuthenticated) {
        alert('로그인이 필요합니다.')
        authStore.openLogin()
        router.push('/')
        return
    }
    
    try {
        const res = await axios.get('/categories')
        categories.value = res.data.data
        if (categories.value.length > 0) form.value.categoryId = categories.value[0].categoryId
    } catch(e) { console.error(e) }
})

const createBook = async () => {
    loading.value = true
    try {
        const res = await axios.post('/books', form.value)
        alert('새로운 이야기가 시작되었습니다!')
        // Response data format: check if it returns ID directly or in object
        // Monolithic CreateBookController returns ApiResponse<Long> (bookId)
        const newBookId = res.data.data
        router.push(`/books/${newBookId}`)
    } catch (e) {
        alert('생성 실패: ' + (e.response?.data?.message || '오류'))
    } finally {
        loading.value = false
    }
}
</script>
