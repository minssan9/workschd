import {methods} from '@/api/axios-voyagess.js'

const REDIRECT_URI = `${process.env.VUE_APP_REDIRECT_URL}/oauth/redirect`
const baesURL = `account`

const apiAccount = {
  getUser () {
    return methods.get(`${baesURL}`)
  },
  getUserById (acconutId) {
    return methods.get(`${baesURL}/${acconutId}`)
  },
  putUser(account) {
    return methods.put(`${baesURL}`, account)
  },
  putUserById(account) {
    return methods.put(`${baesURL}/${account.accountId}`, account)
  },



  getSocialLoginUrl (socialType) {
    return `${process.env.VUE_APP_API}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`
  },
  getSocialConnect(socialType){
    return `${process.env.VUE_APP_API}/oauth2/auth-url/${socialType}`
  },
  saveAccountSns (providerType, oauth2Info) {
    return methods.post(`/oauth2/save/${providerType}`, oauth2Info)
  },

  saveAccountInfo(account) {
    return methods.post(`${baesURL}/info`, account)
  },
  getAccountInfo(accountId) {
    return methods.get(`${baesURL}/${accountId}/info`)
  },

  saveProfileImg(accountId, profileImage) {
    return methods.postFile(`${baesURL}/${accountId}/image`, profileImage)
  }
}

export default apiAccount

