import request from '@/api/axios-voyagerss.js'

// let fetchQuery = {role: '', username: '', size: 0, page: 1, sort: 'createdAt'}
const baseUrl = `ed/common/review`

const apiReview = {
  getByOrderId(orderId){
    let queryDto = {}
    queryDto.orderId = orderId
    return request.get(`${baseUrl}`, queryDto)
  },
  saveReview(review){
    return request.post(`${baseUrl}`, review)
  },
  getReviews() {
    return request.get(`${baseUrl}/list`)
  },
  getReviewPage(searchQuery) {
    return request.get(`${baseUrl}/page`, searchQuery)
  }
}

export default apiReview
