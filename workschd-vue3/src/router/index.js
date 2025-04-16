import {createRouter, createWebHistory} from 'vue-router'
import {setupRouterGuards} from './permission'
import {Loading} from 'quasar'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    // Info (these don't need meta.requiresAuth: false since they're in whitelist)
    { path: '/',                        name: 'home',                     component: () => import('@/views/info/Home.vue'),                   meta: { icon: 'home' } },
    { path: '/about',                   name: 'about',                    component: () => import('@/views/info/About.vue'),                  meta: { icon: 'info' } },
    { path: '/subscription',            name: 'Subscription',             component: () => import('@/views/info/Subscription.vue'),           meta: { icon: 'card_membership' } },
    { path: '/privacy-policy',          name: 'PrivacyPolicy',            component: () => import('@/views/info/PrivacyPolicy.vue'),          meta: { icon: 'policy' }, hidden: true },
    { path: '/terms',                   name: 'Terms',                    component: () => import('@/views/info/Terms.vue'),                  meta: { icon: 'description' }, hidden: true },

    // Auth
    { path: '/auth/login',              name: 'login',                    component: () => import('@/views/auth/Login.vue'),                  meta: { icon: 'login' }, hidden: true },
    { path: '/auth/redirect',           name: 'redirect',                 component: () => import('@/views/auth/redirect.vue'),               meta: { icon: 'refresh' }, hidden: true },
    { path: '/auth/signup',             name: 'Signup',                   component: () => import('@/views/auth/Signup.vue'),                 meta: { icon: 'person_add', requiresAuth: false }, hidden: true },
    
    // Account
    { path: '/account/profile',         name: 'AccountProfile',           component: () => import('@/views/account/AccountProfile.vue'),      meta: { icon: 'person' }, hidden: true },
    { path: '/account/schedule',        name: 'AccountSchedule',          component: () => import('@/views/account/AccountSchedule.vue'),     meta: { icon: 'calendar_today' }, hidden: true },

    // Job
    { 
      path: '/task',                   name: 'Task',                    meta: { roles: ['WORKER','MANAGER', 'SCHEDULER'], requiresAuth: true, icon: 'event' },
      children: [
        { path: '/task/manage',          name: 'TaskManage',                component: () => import('@/views/task/TaskManage.vue'),            meta: { icon: 'list' } },
        { path: '/task/attendance',    name: 'TaskAttendance',          component: () => import('@/views/task/TaskAttendance.vue'),      meta: { icon: 'how_to_reg' } },
        { path: '/task/list-mobile',   name: 'TaskListMobile',          component: () => import('@/views/task/TaskListMobile.vue'),      meta: { icon: 'work', roles: ['WORKER'] } },
      ]
    },

    // Team
    { 
      path: '/team',                    name: 'Team',                     meta: { roles: ['MANAGER', 'SCHEDULER'], requiresAuth: true, icon: 'groups' },
      children: [
        { path: '/team/join/:token',    name: 'TeamJoin',                 component: () => import('@/views/team/TeamJoin.vue'),              meta: { roles: ['WORKER', 'MANAGER', 'SCHEDULER'], requiresAuth: true, icon: 'group_add' }, hidden: true },
        { path: '/team/manage',         name: 'TeamManage',               component: () => import('@/views/team/TeamManage.vue'),            meta: { icon: 'manage_accounts' } },
      ]
    },

    // Error pages
    { path: '/401',                     name: 'Unauthorized',             component: () => import('@/views/error/401.vue'),                   meta: { icon: 'gpp_bad' }, hidden: true },
    { path: '/403',                     name: 'Forbidden',                component: () => import('@/views/error/403.vue'),                   meta: { icon: 'block' }, hidden: true },
    { path: '/:pathMatch(.*)*',         name: 'NotFound',                 component: () => import('@/views/error/404.vue'),                   meta: { icon: 'search_off' }, hidden: true }
  ]
  /* eslint-enable */
})

// Setup navigation guards
setupRouterGuards(router)

// Handle errors that might prevent afterEach from firing
router.onError(() => {
  Loading.hide()
})

export default router
