import api from '@/api/axios';
import { defineStore } from 'pinia'
import { getCookie, setCookie, removeCookie } from '../utils/cookies';

export type User = {
    grantType: string,
    userId: string,
    accessToken: string,
    refreshToken: string,
    role: string,
    firstLoginYn: string,
    language: string,
}

export const useUserStore = defineStore('user', {
    state: (): User => ({
        grantType: '',
        userId: getCookie('userId'),
        accessToken: '',
        refreshToken: '',
        role: '',
        firstLoginYn: '',
        language: '',
    }),
    getters: {
        getLoggedIn(): boolean {
            // store 내 로그인 정보가 있으면 바로 TRUE 리턴
            return this.userId !== '' && this.userId !== null;
            // 새로고침 등의 이유로 store 내에는 없을 시, 쿠키 내 Token 유효성 확인 후 결과 리턴
            
        },
        getAccessToken(): string {
            return this.accessToken;
          },
        getRefreshToken(): string {
            return this.refreshToken;
        },
        getRole(): string {
            return this.role;
        },
    },
    actions: {
        async login(userId: string, password: string) {
            const responseData = await api.post('management/login', {
                userId, 
                password
            });

            if (responseData.data.success &&
                responseData.data.data.firstLoginYN === 'N' &&
                responseData.data.data.userId !== null
            ) {
                this.userId = responseData.data.data.userId;
                this.accessToken = responseData.data.data.accessToken;
                this.refreshToken = responseData.data.data.refreshToken;
                this.role = responseData.data.data.role;
                setCookie('userId', this.userId);
                setCookie('AccessToken', this.accessToken);
                setCookie('RefreshToken', this.refreshToken);
            } else {
                this.userId = ''; 
                removeCookie('userId');
                removeCookie('AccessToken');
                removeCookie('RefreshToken');
            }

            return responseData;
        },
        async logout(userId: string) {
            const responseData = await api.post('management/logout', {
                userId, 
            });
            this.userId = '';
            removeCookie('userId');
            removeCookie('AccessToken');
            removeCookie('RefreshToken');
            return responseData;
        },
    }
});
