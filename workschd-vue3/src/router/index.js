import { createRouter, createWebHistory } from 'vue-router'
import api from '@/api/axios-voyagerss'

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


router.beforeEach((to) => {
  const userStore = useUserStore();
  console.log('to: ', to, userStore.getLoggedIn, 'userId: ', userStore.userId);


  switch (to.path){
    case '/settings':
      api.get('management/user/role')
        .then(response => {
          if (response.data.data === 'admin') {
            return { path: '/settings' }
          } else {
            ElMessageBox.alert('권한이 없습니다.')
            return { path: '/' }
          }
        })
      break;
    default:

  }

  if (to.meta.requiresAuth && !userStore.getLoggedIn) {
    return {
      path: '/login',
      // 나중에 다시 올 수 있도록, 방문한 위치를 저장
      query: { redirect: to.fullPath },
    }
  } else if (to.path=='/login' && userStore.getLoggedIn) {
    return { path: '/' }
  } else {
    return;
  }
});


export default router
