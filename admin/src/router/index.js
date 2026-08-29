import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'articles', name: 'articles', component: () => import('../views/ArticleList.vue'), meta: { title: '文章管理' } },
      { path: 'articles/edit/:id?', name: 'article-edit', component: () => import('../views/ArticleEdit.vue'), meta: { title: '编辑文章' } },
      { path: 'categories', name: 'categories', component: () => import('../views/CategoryManage.vue'), meta: { title: '分类管理' } },
      { path: 'projects', name: 'projects', component: () => import('../views/ProjectManage.vue'), meta: { title: '项目作品' } },
      { path: 'profile', name: 'profile', component: () => import('../views/ProfileManage.vue'), meta: { title: '站点设置' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory('/admin/'),
  routes
})

// 未登录跳转登录页
router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) return '/login'
  if (to.path === '/login' && token) return '/dashboard'
})

export default router
