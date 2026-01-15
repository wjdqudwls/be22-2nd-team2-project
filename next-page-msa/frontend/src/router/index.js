import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/books/:id',
            name: 'book-detail',
            // Lazy loading
            component: () => import('../views/BookDetailView.vue')
        },
        {
            path: '/books/new',
            name: 'create-book',
            component: () => import('../views/CreateBookView.vue')
        },
        {
            path: '/mypage',
            name: 'mypage',
            component: () => import('../views/MyPageView.vue')
        }
    ]
})

export default router
