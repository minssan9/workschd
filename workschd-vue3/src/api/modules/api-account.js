import service from "@/api/axios-voyagerss.js";
 
const baesURL = `account`
const ELECTION_API_URL = 'http://apis.data.go.kr/9760000/PofelcddInfoInqireService/getPoelpcddRegistSttusInfoInqire'
const SERVICE_KEY = 'dKNTmNiGHpcJLrC4HIJc3nW4AaPPKOQLCvlzV7IQZDTTztv6PTuDusZxS8iC1vpBEtkLsnk97WEzKpEvf3Zqgg%3D%3D'

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

  getElectionData: (params = {}) => {
    const defaultParams = {
      serviceKey: SERVICE_KEY,
      pageNo: '1',
      numOfRows: '10',
      sgId: '20240410',
      sgTypecode: '2',
      sggName: '',
      sdName: ''
    }

    return service.get(ELECTION_API_URL, {
      params: { ...defaultParams, ...params },
      headers: {
        'Accept': 'application/json'
      }
    })
  }
}

export default apiAccount

// dKNTmNiGHpcJLrC4HIJc3nW4AaPPKOQLCvlzV7IQZDTTztv6PTuDusZxS8iC1vpBEtkLsnk97WEzKpEvf3Zqgg%3D%3D