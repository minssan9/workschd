import { AxiosResponse } from 'axios';
import request from '../axios-voyagerss';

export interface BranchDTO {
  id?: number;
  name: string;
  region: string;
  address?: string;
}

export default {
  create(data: BranchDTO): Promise<AxiosResponse> {
    return request.post('/api/branch', data);
  },

  update(id: number, data: BranchDTO): Promise<AxiosResponse> {
    return request.put(`/api/branch/${id}`, data);
  },

  delete(id: number): Promise<AxiosResponse> {
    return request.delete(`/api/branch/${id}`);
  },

  getById(id: number): Promise<AxiosResponse> {
    return request.get(`/api/branch/${id}`);
  },

  getList(): Promise<AxiosResponse> {
    return request.get('/api/branch');
  }
}; 