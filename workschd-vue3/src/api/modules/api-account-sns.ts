import { AxiosResponse } from 'axios';
import request from '../axios-voyagerss';
import { EnumMaster } from '@/types/enums';

export interface AccountSnsDTO {
  accountSnsId?: number;
  userId: string;
  snsEmail?: string;
  providerType: EnumMaster.ProviderType;
  snsPhone?: string;
  expired?: Date;
  emailVerifiedYn: string;
  profileImageUrl?: string;
  profileVideoUrl?: string;
  accessToken?: string;
  refreshToken?: string;
  accountId?: number;
}

export default {
  create(data: AccountSnsDTO): Promise<AxiosResponse> {
    return request.post('/api/account-sns', data);
  },

  update(id: number, data: AccountSnsDTO): Promise<AxiosResponse> {
    return request.put(`/api/account-sns/${id}`, data);
  },

  delete(id: number): Promise<AxiosResponse> {
    return request.delete(`/api/account-sns/${id}`);
  },

  getById(id: number): Promise<AxiosResponse> {
    return request.get(`/api/account-sns/${id}`);
  },

  getByAccountId(accountId: number): Promise<AxiosResponse> {
    return request.get(`/api/account-sns/account/${accountId}`);
  }
}; 