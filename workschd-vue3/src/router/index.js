import { createRouter, createWebHistory } from 'vue-router'
import { setupRouterGuards } from './permission'
import { Loading } from 'quasar'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    // Info (these don't need meta.requiresAuth: false since they're in whitelist)
    { path: '/',                        name: 'home',                     component: () => import('@/views/info/Home.vue') },
    { path: '/about',                   name: 'about',                    component: () => import('@/views/info/About.vue') },
    { path: '/subscription',            name: 'Subscription',             component: () => import('@/views/info/Subscription.vue') },
    { path: '/privacy-policy',          name: 'PrivacyPolicy',           component: () => import('@/views/info/PrivacyPolicy.vue'),          hidden: true  },
    { path: '/terms',                   name: 'Terms',                    component: () => import('@/views/info/Terms.vue'),                  hidden: true  },
    
    // Test routes
    { path: '/test',                    name: 'Test',
      children: [
        { path: 'assembly',             name: 'Assembly',             component: () => import('@/views/assembly/Assembly.vue') }
      ]
    },

    // Auth
    { path: '/auth/login',              name: 'login',                    component: () => import('@/views/auth/Login.vue'),                  hidden: true },
    { path: '/oauth/redirect',          name: 'redirect',                 component: () => import('@/views/auth/redirect.vue'),               hidden: true },
    { path: '/auth/signup',             name: 'Signup',                   component: () => import('@/views/auth/Signup.vue'),      meta: { requiresAuth: false }
    },
    // Account
    { path: '/account/profile',         name: 'AccountProfile',           component: () => import('@/views/account/AccountProfile.vue'),    hidden: true },
    { path: '/account/schedule',        name: 'AccountSchedule',          component: () => import('@/views/account/AccountSchedule.vue'),       hidden: true },

    // Job
    { 
      path: '/event', 
      name: 'Event',
      meta: {
        roles: ['WORKER','MANAGER', 'SCHEDULER'],
        requiresAuth: true
      },
      children: [
        { path: '/list', name: 'EventList', component: () => import('@/views/events/EventList.vue') },
        { path: '/attendance', name: 'EventAttendance', component: () => import('@/views/events/EventAttendance.vue') },
      ]
    },

    // Team
    { 
      path: '/team', 
      name: 'Team',
      meta: {
        roles: ['MANAGER', 'SCHEDULER'],
        requiresAuth: true
      },
      children: [
        { path: '/join/:token', name: 'TeamJoin', component: () => import('@/views/team/TeamJoin.vue'), hidden: true,
          meta: {
            roles: ['WORKER', 'MANAGER', 'SCHEDULER'],
            requiresAuth: true
          }
        },
        { path: '/manage', name: 'TeamManage', component: () => import('@/views/team/TeamManage.vue') },
        // { path: '/workplace', name: 'TeamWorkPlace', component: () => import('@/views/team/TeamWorkPlace.vue') },
        // { path: '/employee/schedule', name: 'TeamEmployeeSchedule', component: () => import('@/views/team/TeamScheduleConfig.vue') }        
      ]
    },

    // Error pages
    { 
      path: '/401',        
      name: 'Unauthorized',                
      component: () => import('@/views/error/401.vue'),
      hidden: true 
    },
    { 
      path: '/403',        
      name: 'Forbidden',                
      component: () => import('@/views/error/403.vue'),
      hidden: true 
    },
    { 
      path: '/:pathMatch(.*)*',        
      name: 'NotFound',                
      component: () => import('@/views/error/404.vue'),
      hidden: true 
    }
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
