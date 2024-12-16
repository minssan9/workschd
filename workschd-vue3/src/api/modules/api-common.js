import {methods} from '@/api/axios-voyagerss.js'

const apiCommon = {
  getContent () {
    return methods.get('/ed/common/content')
  },
  getCourse () {
    return methods.get('/ed/common/course')
  },
  getSysConfLike (code) {
    return methods.get(`/ed/common/sys/conf/${code}`)
  },
  getAssignByOrderId(orderId){
    let queryDto = {}
    queryDto.orderId = orderId
    return methods.get(`/ed/common/assign`, queryDto)
  },
}

export default apiCommon
