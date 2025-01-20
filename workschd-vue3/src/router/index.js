import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    { path: '/',                        name: 'home',         component: () => import('@/views/HomeView.vue') },
    { path: '/login',                   name: 'login',        component: () => import('@/views/auth/Login.vue'),          hidden: true },
    { path: '/oauth/redirect',          name: 'redirect',     component: () => import('@/views/auth/redirect.vue'),       hidden: true},
    { path: '/about',                   name: 'about',        component: () => import('@/views/AboutView.vue') },
    // Info
    { path: '/info/branch',             name: 'Branch',       component: () => import('@/views/info/Branch.vue') },
    { path: '/info/workplace',          name: 'WorkPlace',    component: () => import('@/views/info/WorkPlace.vue') },

    // Job
    { path: '/job/list',                name: 'JobList',      component: () => import('@/views/job/JobList.vue') },
    { path: '/job/today',               name: 'JobToday',     component: () => import('@/views/job/JobToday.vue') },
    { path: '/job/attendance',          name: 'Attendance',   component: () => import('@/views/job/Attendance.vue') },
    // Account 
    { path: '/account/preferences',     name: 'AccountPreferences', component: () => import('@/views/account/AccountPreferences.vue'),    hidden: true },
    { path: '/account/schedule',        name: 'AccountSchedule',    component: () => import('@/views/account/AccountSchedule.vue'),       hidden: true },

    // Team
    { path: '/team/employee',           name: 'Employee',            component: () => import('@/views/team/TeamEmployee.vue') },
    { path: '/team/employee/schedule',  name: 'AccountSchedule',     component: () => import('@/views/team/TeamScheduleConfig.vue') },
    
    { path: '/team/register',           name: 'TeamRegister', component: () => import('@/views/team/TeamRegistration.vue')  },
    // { path: '/team/manage',             name: 'TeamManage',   component: () => import('@/views/team/TeamManage.vue'),       hidden: true },    
    { path: '/join-team/:token',        name: 'JoinTeam',     component: () => import('@/views/team/JoinTeam.vue'),       hidden: true , 
      meta: { 
        roles: ['WORKER', 'MANAGER', 'SCHEDULER'],
        requiresAuth: true 
      }
    },
  ]
  /* eslint-enable */
})

// Add navigation guard if needed
router.beforeEach((to, from, next) => {
  // Add your navigation guard logic here
  next()
})

export default router
