import {methods} from '@/api/axios-voyagess.js'

const apiPayment = {
  success (payObject) {
    return methods.post('/ed/payment/success', payObject)
  },
  fail() {
    return methods.get('/ed/payment/fail')
  },
  receiptUrl(orderId) {
    return methods.get(`/ed/payment/receipt?orderId=${orderId}` )
  }
}

export default apiPayment
