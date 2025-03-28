import { AxiosResponse } from 'axios';
import request from "@/api/axios-voyagerss.js";

export interface SubscriptionDTO {
  planType: 'premium' | 'basic' | 'starter' | 'small';
  price: number;
}

export default {
  subscribe(planType: SubscriptionDTO['planType']): Promise<AxiosResponse> {
    return request.post('/subscription', { planType });
  },

  getCurrentPlan(): Promise<AxiosResponse> {
    return request.get('/subscription/current');
  }
}; 