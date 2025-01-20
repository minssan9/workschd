import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    { path: '/',                  name: 'home',         component: () => import('@/views/HomeView.vue') },
    { path: '/login',             name: 'login',        component: () => import('@/views/auth/Login.vue'), hidden: true },
    { path: '/oauth/redirect',    name: 'redirect',     component: () => import('@/views/auth/redirect.vue'), hidden: true},
    { path: '/about',             name: 'about',        component: () => import('@/views/AboutView.vue') },
    // Info
    { path: '/register-workplace', name: 'RegisterWorkplace', component: () => import('@/views/team/TeamRegistration.vue') },

    // Job
    { path: '/job/list',          name: 'JobList',      component: () => import('@/views/job/JobList.vue') },
    { path: '/job/today',         name: 'JobToday',     component: () => import('@/views/job/JobToday.vue') },
    { path: '/job/attendance',    name: 'Attendance',   component: () => import('@/views/job/Attendance.vue') },
    // Account 
    { path: '/account/preferences', name: 'AccountPreferences', component: () => import('@/views/account/AccountPreferences.vue') },
    { path: '/account/schedule',      name: 'AccountSchedule',     component: () => import('@/views/account/ScheduleConfig.vue') },

    // Team
    { path: '/team/branch',       name: 'Branch',       component: () => import('@/views/team/Branch.vue') },
    { path: '/team/workplace',    name: 'WorkPlace',    component: () => import('@/views/team/WorkPlace.vue') },
    { path: '/team/employee',     name: 'Employee',     component: () => import('@/views/team/Employee.vue') },
    
    { path: '/team/register',     name: 'TeamRegister', component: () => import('@/views/team/TeamRegistration.vue') },
    { path: '/team/manage',       name: 'TeamManage',   component: () => import('@/views/team/TeamManage.vue') },
    // { path: '/team/members',      name: 'TeamMembers',  component: () => import('@/views/team/TeamMembers.vue') },
    { path: '/create-team',        name: 'CreateTeam',     component: () => import('@/views/team/TeamRegistration.vue') },    
    { path: '/join-team-request',  name: 'JoinTeamRequest', component: () => import('@/views/team/TeamRegistration.vue') },

    
  ]
  /* eslint-enable */
})

// Add navigation guard if needed
router.beforeEach((to, from, next) => {
  // Add your navigation guard logic here
  next()
})

export default router
