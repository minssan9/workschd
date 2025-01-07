import service from "@/api/axios-voyagerss";

const baseURL = 'account';

export const apiAccount = {
  // Account management
  getUser() {
    return service.get(`${baseURL}`);
  },
  
  getUserById(accountId: number) {
    return service.get(`${baseURL}/${accountId}`);
  },
  
  updateUser(account: any) {
    return service.put(`${baseURL}`, account);
  },
  
  updateUserById(accountId: number, account: any) {
    return service.put(`${baseURL}/${accountId}`, account);
  },

  // Account Info
  getAccountInfo(accountId: number) {
    return service.get(`${baseURL}/${accountId}/info`);
  },
  
  saveAccountInfo(accountInfo: any) {
    return service.post(`${baseURL}/info`, accountInfo);
  },

  // OAuth operations
  getSocialLoginUrl(socialType: string) {
    return `${import.meta.env.VITE_API_URL}/oauth2/authorization/${socialType}?redirect_uri=${import.meta.env.VITE_API_REDIRECT_URL}/oauth/redirect`;
  },
  
  saveAccountSns(providerType: string, oauth2Info: any) {
    return service.post(`/oauth2/save/${providerType}`, oauth2Info);
  }
}; 