import {methods} from '@/api/axios-voyagerss.js'

// let fetchQuery = {role: '', username: '', size: 0, page: 1, sort: 'createdAt'}
const baseUrl = `ed/common/review`

const apiReview = {
  getByOrderId(orderId){
    let queryDto = {}
    queryDto.orderId = orderId
    return methods.get(`${baseUrl}`, queryDto)
  },
  saveReview(review){
    return methods.post(`${baseUrl}`, review)
  },
  getReviews() {
    return methods.get(`${baseUrl}/list`)
  },
  getReviewPage(searchQuery) {
    return methods.get(`${baseUrl}/page`, searchQuery)
  }
}

export default apiReview
