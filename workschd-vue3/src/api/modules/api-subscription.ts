import { AxiosResponse } from 'axios';
import request from '../axios-voyagerss';

export interface SubscriptionDTO {
  planType: 'premium' | 'basic' | 'starter' | 'small';
  price: number;
}

export default {
  subscribe(planType: SubscriptionDTO['planType']): Promise<AxiosResponse> {
    return request.post('/api/subscription', { planType });
  },

  getCurrentPlan(): Promise<AxiosResponse> {
    return request.get('/api/subscription/current');
  }
}; 