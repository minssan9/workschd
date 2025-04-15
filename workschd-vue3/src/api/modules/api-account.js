import request from "@/api/axios-voyagerss.js";
 
const baesURL = `account` 
const apiAccount = {
  getUser () {
    return request.get(`${baesURL}`)
  },
  getUserById (acconutId) {
    return request.get(`${baesURL}/${acconutId}`)
  },
  putUser(account) {
    return request.put(`${baesURL}`, account)
  },
  putUserById(account) {
    return request.put(`${baesURL}/${account.accountId}`, account)
  },

  getTeamsByAccountId(accountId) {
    return request.get(`${baesURL}/${accountId}/team`)
  },

  getSocialLoginUrl (socialType) {
    return `${import.meta.env.VITE_API_URL}/oauth2/authorization/${socialType}?redirect_uri=${import.meta.env.VITE_API_REDIRECT_URL}/auth/redirect`
  },
  getSocialConnect(socialType){
    return `${import.meta.env.VITE_API_URL}/oauth2/auth-url/${socialType}`
  },
  saveAccountSns (providerType, oauth2Info) {
    return request.post(`/oauth2/save/${providerType}`, oauth2Info)
  },

  saveAccountInfo(account) {
    return request.post(`${baesURL}/info`, account)
  },
  getAccountInfo(accountId) {
    return request.get(`${baesURL}/${accountId}/info`)
  },

  saveProfileImg(accountId, profileImage) {
    return request.postFile(`${baesURL}/${accountId}/image`, profileImage)
  },

  login(data) {
    return request({
      url: '/auth/login',
      method: 'post',
      data: {
        email: data.email,
        password: data.password
      }
    })
  },

  signup(data) {
    return request({
      url: '/auth/signup',
      method: 'post',
      data: {
        email: data.email,
        username: data.username,
        password: data.password
      }
    })
  },
 
}

export default apiAccount
