import service from "@/api/axios-voyagerss.js";

const REDIRECT_URI = `${import.meta.env.$VITE_API_REDIRECT_URL}/oauth/redirect`
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
    return `${import.meta.env.VITE_API_URL}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`
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
  }
}

export default apiAccount

