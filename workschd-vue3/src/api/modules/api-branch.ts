import { AxiosInstance } from 'axios';
import apiClient from '../apiClient';

export interface BranchDTO {
    id?: number;
    name: string;
    region: string;
    address?: string;
}

const apiBranch = {
    create: (data: BranchDTO) => 
        apiClient.post('/api/branch', data),
        
    getById: (id: number) => 
        apiClient.get(`/api/branch/${id}`),
        
    update: (id: number, data: BranchDTO) => 
        apiClient.put(`/api/branch/${id}`, data),
        
    delete: (id: number) => 
        apiClient.delete(`/api/branch/${id}`),
        
    getList: () => 
        apiClient.get('/api/branch')
};

export default apiBranch; 