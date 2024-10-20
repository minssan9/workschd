import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/views/auth/Login.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    { path: '/',                  name: 'home',         component: () => import('@/views/HomeView.vue') },
    { path: '/login',             name: 'login',        component: () => import('@/views/auth/Login.vue') },
    { path: '/about',             name: 'about',        component: () => import('@/views/AboutView.vue') },
    { path: '/job/list',          name: 'JobList',      component: () => import('@/views/job/JobList.vue') },
    { path: '/job/today',         name: 'JobToday',     component: () => import('@/views/job/JobToday.vue') },
    { path: '/team/register',     name: 'TeamRegister', component: () => import('@/views/team/TeamRegistration.vue') },
    { path: '/team/manage',       name: 'TeamManage',   component: () => import('@/views/team/TeamManage.vue') },

    { path: '/create-team',        name: 'CreateTeam',     component: () => import('@/views/team/TeamRegistration.vue') },
    { path: '/join-team-request',  name: 'JoinTeamRequest', component: () => import('@/views/team/TeamRegistration.vue') },
    { path: '/register-workplace', name: 'RegisterWorkplace', component: () => import('@/views/team/TeamRegistration.vue') },
  ]
  /* eslint-enable */
})

export default router
