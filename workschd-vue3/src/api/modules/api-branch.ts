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
    return request.post('/branch', data);
  },

  update(id: number, data: BranchDTO): Promise<AxiosResponse> {
    return request.put(`/branch/${id}`, data);
  },

  delete(id: number): Promise<AxiosResponse> {
    return request.delete(`/branch/${id}`);
  },

  getById(id: number): Promise<AxiosResponse> {
    return request.get(`/branch/${id}`);
  },

  getList(): Promise<AxiosResponse> {
    return request.get('/branch');
  }
}; 