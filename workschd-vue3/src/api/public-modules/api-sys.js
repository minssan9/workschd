import request from '@/api/axios-voyagerss.js'

const apiSys = {
  getSysConfLike (code) {
    return request.get(`common/sys/conf/${code}`)
  },
  getSysI18n (language) {
    return request.get(`common/sys-i18n?language=` + language)
  },
}

export default apiSys
