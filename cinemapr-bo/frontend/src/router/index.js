import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from '@/views/MainView.vue'
import NotFound from '@/views/NotFound'

Vue.use(VueRouter)

const routes = [
  {
    path: '/loginForm',
    name: 'LoginForm',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/auth/LoginForm.vue')
  },
  {
    path: '/',
    name: 'MainView',
    component: MainView,
    children: [
      { path: 'grid', component: () => import(/* webpackChunkName: "about" */ '../views/sample/GirdSample.vue') },
      { path: 'menuForm', component: () => import(/* webpackChunkName: "about" */ '../views/menu/MenuView.vue') },
      { path: 'rolesList', component: () => import(/* webpackChunkName: "about" */ '../views/roles/RolesList') },
      { path: 'adminList', component: () => import(/* webpackChunkName: "about" */ '../views/auth/AdminList.vue') },
      { path: 'adminManagement/:id', component: () => import(/* webpackChunkName: "about" */ '../views/auth/AdminManagement.vue') },
      { path: 'adminForm/:id', component: () => import('@/views/auth/AdminForm') }
    ]
  },
  {
    path: '*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
