import apiClient from '../apiClient';

export interface AccountInfoDTO {
    id?: number;
    accountId: number;
    employeeType: 'FULL_TIME' | 'PART_TIME' | 'TEMPORARY';
    preferredDay?: string;
    unavailableDaysOfWeek?: number[];
    preferredStartTime?: string;
    preferredEndTime?: string;
}

const apiAccountInfo = {
    create: (data: AccountInfoDTO) => 
        apiClient.post('/api/account-info', data),
        
    getById: (id: number) => 
        apiClient.get(`/api/account-info/${id}`),
        
    update: (id: number, data: AccountInfoDTO) => 
        apiClient.put(`/api/account-info/${id}`, data),
        
    delete: (id: number) => 
        apiClient.delete(`/api/account-info/${id}`)
};

export default apiAccountInfo; 