import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import ShopManagement from '../views/ShopManagement.vue'

const routes = [
    { path: '/', redirect: '/dashboard' },
    { path: '/dashboard', component: Dashboard },
    { path: '/shops', component: ShopManagement },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
