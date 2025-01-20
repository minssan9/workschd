import apiAccount from '@/api/modules/api-account'
import Cookies from 'js-cookie'
import { defineStore } from 'pinia'

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
        accountInfo: [],
        isAuthPhone: false,
    }),
    getters: {
        accountId: state => state.user.accountId,
        accessToken: state => state.user.accessToken,
        refreshToken: state => state.user.refreshToken,
        isWorker: state => state.user.accountRoles?.map(ar => ar.roleType).includes('WORKER') && 
                          state.user.accountRoles?.length === 1,
        isOwner: state => state.user.accountRoles?.map(ar => ar.roleType).includes('OWNER'), 
        isManager: state => state.user.accountRoles?.map(ar => ar.roleType).includes('MANAGER'),
    },
    actions: {
        async fetchUser() {
            if (!this.user.accountId) {
                try {
                    const res = await apiAccount.getUser()
                    Cookies.set('refreshToken', res.refreshToken, { expires: 7 })
                    Cookies.set('accountId', res.accountId, { expires: 7 })
                    Cookies.set('username', res.username, { expires: 7 })
                    Cookies.set('email', res.email, { expires: 7 })
                    Cookies.set('role', res.accountRoles, { expires: 7 })
                    this.user = res

                    const accountInfo = await apiAccount.getAccountInfo(res.accountId)
                    if (accountInfo.accountId) {
                        this.user = { ...this.user, ...accountInfo }
                    }
                    return res
                } catch (error) {
                    console.error('Error fetching user:', error)
                    throw error
                }
            }
        },
        async updateUser() {
            try {
                const res = await apiAccount.putUser(this.user)
                this.user = res
                return res
            } catch (error) {
                console.error('Error updating user:', error)
                throw error
            }
        },
        async saveAccountInfo(accountInfo) {
            try {
                const res = await apiAccount.saveAccountInfo(accountInfo)
                this.user = { ...this.user, ...res }
                return res
            } catch (error) {
                console.error('Error saving account info:', error)
                throw error
            }
        },
        async saveAccountProfileImage({ accountId, accountProfileImage }) {
            try {
                const res = await apiAccount.saveProfileImg(accountId, accountProfileImage)
                return res
            } catch (error) {
                console.error('Error saving profile image:', error)
                throw error
            }
        },
        async login(token) {
            if (token) {
                Cookies.set('accessToken', token)

                if (!this.user.accountId){
                    return apiAccount.getUser()
                      .then(res => {
                          Cookies.set('refreshToken',res.refreshToken)
                          Cookies.set('accountId', res.accountId)
                          Cookies.set('username', res.username)
                          Cookies.set('email',res.email)
                          Cookies.set('role',res.accountRoles)
                          this.user = res
                          return res
                      })
                      .then(async account => {
                          apiAccount.getAccountInfo(account.accountId)
                            .then(accountInfo => {
                                if (accountInfo.accountId) {
                                    this.user.accountInfo = accountInfo
                                    return accountInfo
                                }
                            })
                      })
                }
            } else {
                Cookies.remove('accessToken')
                Cookies.remove('refreshToken')
            }
        },
        async logout() {
            Cookies.keys().forEach(cookie => Cookies.remove(cookie))
            this.user = {
                accountId: null,
                username: null,
                email: null,
                role: null,
                accessToken: null,
                refreshToken: null,
                accountRoles: null,
                accountSnsId: null,
                accountSnsList: [],
                createdAt: null,
                englishName: null,
                expired: null,
                koreanName: null,
                password: null,
                phone: '',
                providerType: null,
                snsAccount: null,
                status: null,
                profileImageUrl: '',
                profileVideoUrl: '',
            }
            this.teacherList = []
            this.studentList = []
            this.teacher = {}
            this.student = {}
        }
    }
})
