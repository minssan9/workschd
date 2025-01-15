import apiClient from '../apiClient';

export interface PreferredWorkDayDTO {
    id?: number;
    accountInfoId: number;
    dayOfWeek: number;
    preferredStartTime?: string;
    preferredEndTime?: string;
    preferenceType: 'PREFERRED' | 'UNAVAILABLE';
}

const apiPreferredWorkDay = {
    create: (data: PreferredWorkDayDTO) => 
        apiClient.post('/api/preferred-work-days', data),
        
    getByAccountInfoId: (accountInfoId: number) => 
        apiClient.get(`/api/preferred-work-days/account/${accountInfoId}`),
        
    update: (id: number, data: PreferredWorkDayDTO) => 
        apiClient.put(`/api/preferred-work-days/${id}`, data),
        
    delete: (id: number) => 
        apiClient.delete(`/api/preferred-work-days/${id}`)
};

export default apiPreferredWorkDay; 