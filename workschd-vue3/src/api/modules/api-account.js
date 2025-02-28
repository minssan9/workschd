import service from "@/api/axios-voyagerss.js";
 
const baesURL = `account` 
const apiAccount = {
  getUser () {
    return service.get(`${baesURL}`)
  },
  getUserById (acconutId) {
    return service.get(`${baesURL}/${acconutId}`)
  },
  putUser(account) {
    return service.put(`${baesURL}`, account)
  },
  putUserById(account) {
    return service.put(`${baesURL}/${account.accountId}`, account)
  },



  getSocialLoginUrl (socialType) {
    return `${import.meta.env.VITE_API_URL}/oauth2/authorization/${socialType}?redirect_uri=${import.meta.env.VITE_API_REDIRECT_URL}/oauth/redirect`
  },
  getSocialConnect(socialType){
    return `${import.meta.env.VITE_API_URL}/oauth2/auth-url/${socialType}`
  },
  saveAccountSns (providerType, oauth2Info) {
    return service.post(`/oauth2/save/${providerType}`, oauth2Info)
  },

  saveAccountInfo(account) {
    return service.post(`${baesURL}/info`, account)
  },
  getAccountInfo(accountId) {
    return service.get(`${baesURL}/${accountId}/info`)
  },

  saveProfileImg(accountId, profileImage) {
    return service.postFile(`${baesURL}/${accountId}/image`, profileImage)
  },

  login: (loginData) => {
    return service.post('/auth/login', loginData)
  },

  signup: (accountDTO) => {
    return service.post('/auth/signup', accountDTO)
  },
 
}

export default apiAccount
