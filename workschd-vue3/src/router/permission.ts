import { Router, RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/stores/modules/store_user'
import { Loading, QSpinnerGears } from 'quasar'

// Whitelist routes that don't require authentication
const whiteList = [
  '/',
  '/login',
  '/about',
  '/subscription',
  '/privacy-policy',
  '/terms',
  '/oauth/redirect',
  '/401',
  '/403',
  '/404'
]

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: any) => {
    // Show loading
    Loading.show({
      spinner: QSpinnerGears,
      spinnerColor: 'primary',
      message: 'Loading...',
      messageColor: 'grey-8'
    })

    const userStore = useUserStore()
    const hasToken = userStore.accessToken
    
    // Allow access to whitelisted routes
    if (whiteList.includes(to.path)) {
      next()
      return
    }

    // Check if user is authenticated
    if (!hasToken) {
      next(`/401?redirect=${to.path}`)
      return
    }

    try {
      // Check route permissions
      const userRole = userStore.role
      const requiredRoles = to.meta?.roles as string[] | undefined

      // If route requires specific roles
      if (requiredRoles && requiredRoles.length > 0) {
        if (!requiredRoles.includes(userRole)) {
          next('/403')
          return
        }
      }

      next()
    } catch (error) {
      // Handle any errors during authentication check
      console.error('Navigation guard error:', error)
      userStore.resetUserState()
      next(`/401?redirect=${to.path}`)
    }
  })

  router.afterEach(() => {
    // Hide loading when navigation is complete
    Loading.hide()
  })
} 