import Vue from 'vue'
import axios from 'axios'
import store from '@/store/userStore'
import router from "@/router";
import Cookies from 'js-cookie'


// create an axios instance
const service = axios.create({
  baseURL: import.meta.env.$VITE_API_URL, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000, // 10초
  headers: {
    'Access-Control-Allow-Credentials': true,
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json;charset=utf-8'
  },
})


// request interceptor
service.interceptors.request.use(
  config => {
    let token = store.getters.token
      ? store.getters.token
      : Cookies.get('accessToken')
    let refreshToken = store.getters.refreshToken
      ? store.getters.refreshToken
      : Cookies.get('refreshToken')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      config.headers.refreshToken = `${refreshToken}`
      config.headers.$accountId = store.getters.user.accountId
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /*** If you want to get http information such as headers or status * Please return  response => response */
  response => {

    if (response.headers.rtntoken) {
      // console.log('토큰 재발급!!!!!!!!!!!!!!!!!!')
      window.sessionStorage.setItem('$accessToken', response.headers.rtntoken)
    }

    if (response.status !== 200) {
      this.$dialog.notify.error(response.statusText + ' Error', {position: 'top-right',timeout: 3000})
      if (response.status === 405) {        // to re-login

      }
      return Promise.reject(new Error(response.statusText || 'Error'))
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error) // for debug
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
  if (err.response.status === Unauthorized) {
    store.dispatch('logout')
        .catch(() => apiError.onUnauthorized(err))

    // post('/account/token/refresh', store.getters.user)
    //     .then(async res => {
    //       if (!app.config.globalProperties.$isNull(res) && !app.config.globalProperties.$isNull(res.data) && res.data.success === 'Y') {
    //
    //         const retryOption = err.config
    //         if (retryOption.headers.Accept === 'multipart/form-data') {
    //           retryOption.headers['Content-Type'] = 'multipart/form-data'
    //         }
    //         // 재호출
    //         await service(retryOption)
    //             .then((res) => {
    //               return Promise.resolve(res)
    //             })
    //             .catch((error) => {
    //               return Promise.resolve(error)
    //             })
    //       } else {
    //         return null
    //       }
    //     })
    //     .catch(err => {
    //       console.log('refreshToken error')
    //       store.commit('logOut')
    //       return Promise.reject(err)
    //     })

    return apiError.onUnauthorized(err)
        .finally(() => {
          router.replace('/')
          router.go();
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
    store.dispatch('logout')
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

export default service
