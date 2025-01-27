import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  /* eslint-disable */
  routes: [
    // Info
    { path: '/',                        name: 'home',                     component: () => import('@/views/info/Home.vue') },
    { path: '/about',                   name: 'about',                    component: () => import('@/views/info/About.vue') },
    { path: '/subscription',            name: 'Subscription',             component: () => import('@/views/info/Subscription.vue') },
    { path: '/privacy-policy',          name: 'PrivacyPolicy',           component: () => import('@/views/info/PrivacyPolicy.vue'),          hidden: true  },
    { path: '/terms',                   name: 'Terms',                    component: () => import('@/views/info/Terms.vue'),                hidden: true  },

    // Auth
    { path: '/login',                   name: 'login',                    component: () => import('@/views/auth/Login.vue'),                hidden: true },
    { path: '/oauth/redirect',          name: 'redirect',                 component: () => import('@/views/auth/redirect.vue'),             hidden: true},

    // Account
    { path: '/account/profile',         name: 'AccountProfile',           component: () => import('@/views/account/AccountProfile.vue'),    hidden: true },
    { path: '/account/schedule',        name: 'AccountSchedule',          component: () => import('@/views/account/AccountSchedule.vue'),       hidden: true },

    // Job
    { path: '/event', name: 'Event',
      children: [
        { path: '/list',                name: 'EventList',                  component: () => import('@/views/events/EventList.vue') },
        // { path: '/job/today',               name: 'JobToday',                 component: () => import('@/views/job/JobToday.vue') },
        { path: '/attendance',          name: 'EventAttendance',            component: () => import('@/views/events/EventAttendance.vue') },
      ]
    },

    // Team
    { path: '/team', name: 'Team',
      children: [
        // { path: '/team/employee',           name: 'Employee',            component: () => import('@/views/team/TeamEmployee.vue') },
        // { path: '/register',           name: 'TeamRegister',             component: () => import('@/views/team/TeamRegistration.vue'),       hidden: true },
        { path: '/join/:token',        name: 'TeamJoin',                 component: () => import('@/views/team/TeamJoin.vue'),               hidden: true,
          meta: {
            roles: ['WORKER', 'MANAGER', 'SCHEDULER'],
            requiresAuth: true
          }
        },
        { path: '/manage',             name: 'TeamManage',               component: () => import('@/views/team/TeamManage.vue')  },
        { path: '/workplace',          name: 'TeamWorkPlace',            component: () => import('@/views/team/TeamWorkPlace.vue') },
        { path: '/employee/schedule',  name: 'TeamEmployeeSchedule',     component: () => import('@/views/team/TeamScheduleConfig.vue') }        
      ]
    },

    // 404 route - must be last
    { 
      path: '/:pathMatch(.*)*',        
      name: 'NotFound',                
      component: () => import('@/views/NotFound.vue'),
      hidden: true 
    }
  ]
  /* eslint-enable */
})

// Add navigation guard if needed
router.beforeEach((to, from, next) => {
  // Add your navigation guard logic here
  next()
})

export default router
