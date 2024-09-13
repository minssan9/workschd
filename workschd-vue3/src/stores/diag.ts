// import api from '@/api/axios';
import { defineStore } from 'pinia'

export type Diag = {
    // grantType: string,
    // userId: string,
    // accessToken: string,
    // refreshToken: string,
    // role: string,
    // firstLoginYn: string,
    // language: string,
    status: { text: string;     value: string;   }[]
}

export const useDiagStore = defineStore('diag', {
    state: (): Diag => ({
        status: [
            { text: 'alert', value: 'alert' },
            { text: 'warning', value: 'warning' },
            { text: 'normal', value: 'normal' },
            { text: 'offline', value: 'offline' },
        ]
    }),
    getters: {
        getDiagStatus(): string {
            return this.status;
        },
    },
    actions: {
        getStatusTagType (status: string) {
            switch (status) {
                case "alert":
                    return "danger";
                case "warning":
                    return "warning";
                case "normal":
                    return "success";
                case "offline":
                    return "info";
                default:
                    return "info"; // 값이 없으면 info (no data)
            }
        },
        getStatusTagContent (status: string) {
            switch (status) {
                case "alert":
                    return "alert";
                case "warning":
                    return "warning";
                case "normal":
                    return "normal";
                case "offline":
                    return "offline";
                default:
                    return "offline"; // 값이 없으면 - (no data)
            }
        }
    }
});
