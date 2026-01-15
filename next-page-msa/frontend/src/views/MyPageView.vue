<template>
  <div class="card fade-in" style="max-width: 900px; margin: 0 auto;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2 style="margin:0;">내 서재</h2>
      <button class="btn btn-outline" style="border-color: #ef4444; color: #ef4444; font-size: 0.85rem;"
        @click="withdraw">회원 탈퇴</button>
    </div>

    <div id="profile-section"
      style="margin-bottom: 30px; padding: 15px; background: rgba(255,255,255,0.03); border-radius: 12px;">
      <div v-if="user" id="profile-info" style="color: var(--text-muted); font-size: 0.95rem;">
        <span style="margin-right: 15px;"><strong>{{ user.userNicknm }}</strong>님</span>
        <span style="margin-right: 15px;">{{ user.userEmail }}</span>
        <span class="badge badge-writing">{{ user.userRole }}</span>
      </div>
      <div v-else>Loading...</div>
    </div>

    <div style="display: flex; gap: 20px; flex-wrap: wrap; justify-content: center; margin-bottom: 40px;">
      <div class="stats-card" style="flex: 1; min-width: 160px;">
        <span class="stat-number">{{ user?.createdBookCount || 0 }}</span>
        <span class="stat-label">내가 만든 소설</span>
      </div>
      <div class="stats-card" style="flex: 1; min-width: 160px;">
        <span class="stat-number">{{ user?.writtenSentenceCount || 0 }}</span>
        <span class="stat-label">내가 쓴 문장</span>
      </div>
      <div class="stats-card" style="flex: 1; min-width: 160px;">
        <span class="stat-number">{{ user?.writtenCommentCount || 0 }}</span>
        <span class="stat-label">내가 쓴 댓글</span>
      </div>
    </div>

    <!-- Tabs -->
    <div class="tabs"
      style="display: flex; gap: 10px; margin-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 10px;">
      <button class="btn btn-ghost" :class="{ active: currentTab === 'books' }" @click="switchTab('books')">내가 만든 소설</button>
      <button class="btn btn-ghost" :class="{ active: currentTab === 'sentences' }" @click="switchTab('sentences')">내가 쓴
        문장</button>
      <button class="btn btn-ghost" :class="{ active: currentTab === 'comments' }" @click="switchTab('comments')">내가 쓴
        댓글</button>
    </div>

    <!-- List Container -->
    <div id="list-container" style="min-height: 200px;">
      <div v-if="loadingList" class="text-center p-4">Loading...</div>
      
      <div v-else-if="items.length === 0" class="text-center p-4">
          데이터가 없습니다.
      </div>

      <div v-else>
          <!-- Books -->
          <template v-if="currentTab === 'books'">
             <div v-for="book in items" :key="book.bookId" class="card" @click="goBook(book.bookId)" 
                style="cursor: pointer; padding: 15px; margin-bottom: 10px; display: flex; align-items: center; justify-content: space-between;">
                <div>
                   <h4 style="margin: 0 0 5px 0;">{{ book.title }}</h4>
                   <span style="font-size: 0.8rem; color: var(--text-muted);">{{ new Date(book.createdAt).toLocaleDateString() }}</span>
                   <span class="badge" :class="book.status === 'WRITING' ? 'badge-writing' : 'badge-completed'" style="margin-left: 10px; font-size: 0.7rem;">{{ book.status }}</span>
                </div>
                <div style="text-align: right;">
                   <div style="font-size: 0.8rem; color: var(--text-muted);">문장 {{ book.currentSequence }} / {{ book.maxSequence }}</div>
                </div>
             </div>
          </template>

          <!-- Sentences -->
          <template v-if="currentTab === 'sentences'">
              <div v-for="sent in items" :key="sent.sentenceId" class="card" @click="goBook(sent.bookId)"
                style="padding: 15px; margin-bottom: 10px; border-left: 3px solid var(--secondary-color); cursor: pointer;">
                  <div v-if="sent.bookTitle" style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 5px;">
                      <span style="color: var(--primary-color);">[{{ sent.bookTitle }}]</span> 에 쓴 문장
                  </div>
                  <p style="margin: 0 0 5px 0; font-size: 1rem;">{{ sent.content }}</p>
                  <div style="font-size: 0.8rem; color: var(--text-muted); display: flex; justify-content: space-between;">
                      <span>{{ new Date(sent.createdAt).toLocaleString() }}</span>
                      <div>
                         <span style="margin-right: 10px;">👍 {{ sent.likeCount }}</span>
                         <span>👎 {{ sent.dislikeCount }}</span>
                      </div>
                  </div>
              </div>
          </template>

          <!-- Comments -->
          <template v-if="currentTab === 'comments'">
               <div v-for="c in items" :key="c.commentId" class="card" @click="goBook(c.bookId)"
                 style="cursor: pointer; padding: 15px; margin-bottom: 10px; border-left: 3px solid var(--accent-color);">
                  <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 5px;">
                      <span style="color: var(--primary-color);">[{{ c.bookTitle || '소설' }}]</span> 에 남긴 댓글
                  </div>
                  <p style="margin: 0 0 5px 0;">{{ c.content }}</p>
                  <div style="font-size: 0.8rem; color: var(--text-muted);">
                      {{ new Date(c.createdAt).toLocaleString() }}
                  </div>
               </div>
          </template>
      </div>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" id="pagination" style="display: flex; justify-content: center; gap: 10px; margin-top: 20px;">
        <button class="btn btn-outline" :disabled="page === 0" @click="changePage(page - 1)" style="padding: 5px 15px;">이전</button>
        <span style="align-self: center; color: var(--text-muted); font-size: 0.9rem;">{{ page + 1 }} / {{ totalPages }}</span>
        <button class="btn btn-outline" :disabled="page >= totalPages - 1" @click="changePage(page + 1)" style="padding: 5px 15px;">다음</button>
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

const user = ref(null)
const currentTab = ref('books')
const items = ref([])
const page = ref(0)
const totalPages = ref(0)
const loadingList = ref(false)

onMounted(async () => {
    if (!authStore.isAuthenticated) {
        alert('로그인이 필요합니다.')
        router.push('/')
        return
    }
    await loadUserProfile()
    loadList()
})

const loadUserProfile = async () => {
    try {
        const res = await axios.get('/members/me')
        user.value = res.data.data
    } catch(e) { console.error(e) }
}

const switchTab = (tab) => {
    currentTab.value = tab
    page.value = 0
    loadList()
}

const changePage = (newPage) => {
    page.value = newPage
    loadList()
}

const loadList = async () => {
    loadingList.value = true
    try {
        let url = ''
        let params = { page: page.value, size: 10 }
        
        if (currentTab.value === 'books') {
            url = '/books'
            params.writerId = user.value?.userId 
        } else if (currentTab.value === 'sentences') {
            url = '/books/mysentences'
        } else if (currentTab.value === 'comments') {
            url = '/reactions/mycomments'
        }

        const res = await axios.get(url, { params })
        items.value = res.data.data.content
        totalPages.value = res.data.data.totalPages
    } catch(e) {
        console.error(e)
    } finally {
        loadingList.value = false
    }
}

const goBook = (id) => {
    if (id) router.push(`/books/${id}`)
}

const withdraw = async () => {
    if (!confirm('정말로 탈퇴하시겠습니까?')) return
    try {
        await axios.delete('/auth/withdraw')
        authStore.logout()
        alert('탈퇴되었습니다.')
        router.push('/')
    } catch(e) { alert('실패') }
}
</script>

<style scoped>
.tabs .btn.active {
    border-bottom: 2px solid var(--primary-color);
}
</style>
