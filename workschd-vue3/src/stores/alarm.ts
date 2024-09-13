import { defineStore } from 'pinia';

export const useAlarmStore = defineStore('alarm', {
    state: () => ({
        diagnostics: {},
        monitor: {},
        system: {}
    }),
    getters: {
        
    },
    actions: {
        updateAlarms(alarmType, data) {
            if (this[alarmType])
                this[alarmType] = data[alarmType];
        },
    }
});
