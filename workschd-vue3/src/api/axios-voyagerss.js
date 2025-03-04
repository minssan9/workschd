import { useUserStore } from '@/stores/modules/store_user'
import axios from 'axios'
import router from "@/router"
import Cookies from 'js-cookie'

// create an axios instance
const service = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000,
  headers: {
    'Access-Control-Allow-Credentials': true,
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json;charset=utf-8'
  },
})

// request interceptor
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    let token = userStore.accessToken ?? Cookies.get('accessToken')
    let refreshToken = userStore.refreshToken ?? Cookies.get('refreshToken')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      config.headers.refreshToken = `${refreshToken}`
      config.headers.$accountId = userStore.accountId
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    if (response.headers.rtntoken) {
      window.sessionStorage.setItem('$accessToken', response.headers.rtntoken)
    }

    if (response.status !== 200) {
      // Use Vue 3 notification system or your preferred toast/notification library
      return Promise.reject(new Error(response.statusText || 'Error'))
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error)
    return Promise.reject(error)
  }
)

const BadRequest = 400
const Unauthorized = 401
const Forbidden = 403
const NotFound = 404
const MethodNotAllowed = 405
const ServerError = 500

export const requestFile = (method, url, data) => {
  return service({
      method,
      url: url,
      data,
      processData: false,
      contentType: 'Content-Type: Multipart-file'
    })
    .then(result => result)
    .catch(err => errLogic(err))
    .catch(err => this.$dialog.notify.error(err.message, {position: 'top-right', timeout: 1500}))
}

function errLogic(err) {
  const userStore = useUserStore()
  
  if (err.response.status === Unauthorized) {
    userStore.logout()
      .catch(() => apiError.onUnauthorized(err))
      .finally(() => {
        router.replace('/')
        router.go()
      })
  }
  else if (err.response.status === Forbidden || err.response.status === MethodNotAllowed) return apiError.onForbidden(err)
  else if (err.response.status === BadRequest) return apiError.onBadRequest(err)
  else if (err.response.status === NotFound) return apiError.onNotFound(err)
  else if (err.response.status === ServerError) return apiError.onServerError(err)

  this.$dialog.notify.error(err.message, {position: 'top-right', timeout: 1500})
  return Promise.reject(err)
}

const apiError = {
  onUnauthorized(err) {
    const userStore = useUserStore()
    userStore.logout()
    err.message = err.response.data.message ? err.response.data.message : '' + `\n 인증되지 않았습니다. \n   `
    return Promise.reject(err)
  },
  onMethodNotAllowed(err) {
    err.message = err.response.data.message ? err.response.data.message : '' + `\n 권한이 없습니다. \n   `
    return Promise.reject(err)
  },
  onForbidden(err) {
    err.message = err.response.data.message ? err.response.data.message : '' + `\n 권한이 없습니다. \n   `
    return Promise.reject(err)
  },
  onBadRequest(err) {
    err.message = err.response.data.message ? err.response.data.message : '' +`\n 잘못된 요청입니다. \n   `
    return Promise.reject(err)
  },
  onNotFound(err) {
    err.message = err.response.data.message ? err.response.data.message : '' + `\n 잘못된 접근입니다. \n`
    return Promise.reject(err)
  },
  onServerError(err) {
    err.message = err.response.data.message ? err.response.data.message : '' + `\n 서버 문제입니다. 관리자에게 문의 부탁드립니다. \n`
    return Promise.reject(err)
  },
}

// Make sure to export service
export { service }  // Named export
// or
export default service  // Default export
