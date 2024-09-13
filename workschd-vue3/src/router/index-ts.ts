import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

import api from '@/api/axios'
import { useUserStore } from '@/stores/user';

/* eslint-disable */
const routes: Array<RouteRecordRaw> = [
    {   path: "/login",                               meta: { requiresAuth: false }, component: () => import('@/views/LogIn.vue'), },
    {   path: "/",                    name: "",       meta: { requiresAuth: true }, component: () => import('@/layout/index.vue') ,
        children: [
            { path: "",               name: "Home",   meta: { requiresAuth: true }, component: () => import(/* webpackChunkName: "Home" */ "@/views/Home.vue"), },
            { path: "/detailView",                    meta: { requiresAuth: true },
                children: [
                    { path: "CE/:id", name: "ControllerEquipment", meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/ControllerEquipment.vue"), },
                    { path: "EA/:id", name: "EquipmentAlarm",      meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/EquipmentAlarm.vue"), },
                    { path: "EV/:id", name: "EquipmentVibration",  meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/EquipmentVibration.vue"), },
                    { path: "M/:id",  name: "Motor",               meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/MotorMonitoring.vue"), },
                    { path: "PB/:id", name: "PLCBackup",           meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/PLCBackup.vue"), },
                    { path: "PW/:id", name: "PlantWide",           meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/PlantWide.vue"), },
                    { path: "RM/:id", name: "RobotMonitoring",     meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/RobotMonitoring.vue"), },
                    { path: "RV/:id", name: "RobotVibration",      meta: {}, component: () => import(/* webpackChunkName: "DetailView" */ "@/views/detailView/RobotVibration.vue"), },
                ]
            },
            { path: "/manual",        name: "Manual",         meta: { requiresAuth: true }, component: () => import("@/views/side/Manual.vue"), },
            { path: "/settings",      name: "Settings",       meta: { requiresAuth: true }, component: () => import("@/views/Settings.vue"),
                // beforeEnter: async (to, from, next) => {
                //     const response = await api.get('management/user/role')
                //     if (response.data.data === 'admin') {
                //         next()
                //     } else {
                //         ElMessageBox.alert('권한이 없습니다.')
                //         next({ name: 'Home' })
                //     }
                // }
            }
        ]
    }
];
/* eslint-enable */

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

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

export default router;
