import request from '@/api/axios-voyagerss.js'

const apiCommon = {
  getContent () {
    return request.get('/ed/common/content')
  },
  getCourse () {
    return request.get('/ed/common/course')
  },
  getSysConfLike (code) {
    return request.get(`/ed/common/sys/conf/${code}`)
  },
  getAssignByOrderId(orderId){
    let queryDto = {}
    queryDto.orderId = orderId
    return request.get(`/ed/common/assign`, queryDto)
  },
}

export default apiCommon
