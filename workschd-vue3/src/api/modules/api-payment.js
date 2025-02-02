import request from '@/api/axios-voyagerss.js'

const apiPayment = {
  success (payObject) {
    return request.post('/ed/payment/success', payObject)
  },
  fail() {
    return request.get('/ed/payment/fail')
  },
  receiptUrl(orderId) {
    return request.get(`/ed/payment/receipt?orderId=${orderId}` )
  }
}

export default apiPayment
