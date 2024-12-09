import apiAccount from '@/api/modules/api-account'
import apiPublicAccount from '@/api/public-modules/api-account'
import Cookies from 'js-cookie'
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        diagnostics: {},
        monitor: {},
        system: {},
        filteredAlarmLevel: [
            { text: "alert", value: "alert" },
            { text: "warning", value: "warning" },
            { text: "normal", value: "normal" },
            { text: "offline", value: "offline" },
        ],

        token: null,
        user: {
          accountId: null,
          accountRoles: null,
          accountSnsId: null,
          accountSnsList: [],
          createdAt: null,
          email: null,
          englishName: null,
          expired: null,
          koreanName: null,
          password: null,
          phone: '',
          providerType: null,
          accessToken: null,
          refreshToken: null,
          role: null,
          snsAccount: null,
          status: null,
          username: null,
          profileImageUrl: '',
          profileVideoUrl: '',
        },
        teacherList: [],
        studentList: [],
        teacher: {},
        student: {},
        isAuthPhone: false,
    }),
    getters: {
      user: state => state.user,
      accountId: state => state.user.accountId,
      accessToken: state => state.user.accessToken,

      isStudent: state => state.user.accountRoles ? state.user.accountRoles.map(ar=>ar.roleType).includes('EN9DOOR_STUDENT') : false
                        && state.user.accountRoles.length === 1,
      isOwner: state => state.user.accountRoles ? state.user.accountRoles.map(ar=>ar.roleType).includes('EN9DOOR_OWNER') : false,
      isTeacher: state => state.user.accountRoles ? state.user.accountRoles.map(ar=>ar.roleType).includes('EN9DOOR_TEACHER') : false,
      isManager: state => state.user.accountRoles ? state.user.accountRoles.map(ar=>ar.roleType).includes('EN9DOOR_MANAGER') : false
    },
    actions: {
      fetchUser ({state, commit}) {
        if (!state.user.accountId){
          return apiAccount.getUser()
            .then(res => {
              Cookies.set('refreshToken',res.refreshToken, { expires: 7 })
              Cookies.set('accountId', res.accountId, { expires: 7 })
              Cookies.set('username', res.username, { expires: 7 })
              Cookies.set('email',res.email, { expires: 7 })
              Cookies.set('role',res.accountRoles, { expires: 7 })
              commit('SET_USER', res)
              return res
            })
            .then(account => {
              apiAccount.getAccountInfo(account.accountId)
                .then(accountInfo => {
                  if (accountInfo.accountId) {
                    commit('SET_ACCOUNT_INFO', accountInfo)
                    return accountInfo
                  }
                })
            })
        }
      },
      fetchTeacherList ({commit}) {
        let fetchQuery = {}
        return apiPublicAccount.getTeacherList(fetchQuery)
                .then(res => {
                  commit('SET_TEACHER_LIST', res)
                  return res
                })
      },
      fetchTeacher ({commit}, accountId) {
        return apiPublicAccount.getTeacherById(accountId)
              .then(res => commit('SET_TEACHER', res))
      },
      updateUser ({state, commit}) {
        return new Promise((resolve, reject)=> {
          apiAccount.putUser(state.user)
            .then(res => {
              commit('SET_USER', res)
              resolve(res)
            })
              .catch(err => reject(err))
        })
      },
      saveAccountInfo ({commit}, accountInfo) {
        return new Promise((resolve, reject) => {
          apiAccount.saveAccountInfo(accountInfo)
            .then(res => {
              commit('SET_ACCOUNT_INFO', res)
              resolve(res)
            })
              .catch(err => reject(err))
        })
      },
      saveAccountProfileImage ({commit}, {accountId, accountProfileImage}) {
        return new Promise((resolve, reject) => {
          apiAccount.saveProfileImg(accountId, accountProfileImage)
            .then(res => resolve(res))
            .catch(err => reject(err))
        })
      },
      // user logout
      logout({commit}) {
        return new Promise((resolve)=> {
          Cookies.keys().forEach(cookie => Cookies.remove(cookie));
          let emptyUser = {
            accountId: '',
            username: '',
            email: '',
            role: '',
            token: ''
          }
          commit('SET_USER', emptyUser)
          commit('SET_REGISTER', [])
          commit('SET_TOKEN', '')
          resolve(undefined)
        })
      }
    }
});
