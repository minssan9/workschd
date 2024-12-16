import api from '@/api/axios-voyagerss.js'

const apiSys = {
  getSysConfLike (code) {
    return api.get(`common/sys/conf/${code}`)
  },
  getSysI18n (language) {
    return api.get(`common/sys-i18n?language=` + language)
  },
}

export default apiSys
