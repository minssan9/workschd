{
  path: '/auth/signup',
  name: 'Signup',
  component: () => import('@/views/auth/Signup.vue'),
  meta: {
    requiresAuth: false
  }
} 