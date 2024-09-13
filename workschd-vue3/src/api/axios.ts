import axios from 'axios';
import { getCookie, setCookie, removeCookie } from '../utils/cookies';
import { ElMessageBox } from 'element-plus';
import router from '@/router';
import { useUserStore } from '@/stores/user';

// Prevent multiple alert boxes from appearing at the same time
let isAlertShown = false;

// Create an Axios instance
const api = axios.create({
  baseURL: import.meta.env.VITE_PHM_API,
  timeout: 30000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
  }
});

api.defaults.headers.common['Content-Type'] = 'application/json';

// axios.get(url[, config])
// axios.post(url[, data[, config]])
// axios.put(url[, data[, config]])

// `params`은 요청과 함께 전송되는 URL 파라미터입니다.
// 반드시 일반 객체나 URLSearchParams 객체여야 합니다.
// 참고: null이나 undefined는 URL에 렌더링되지 않습니다.
/*
  params: {
    ID: 12345
  },
*/
  // `data`는 요청 바디로 전송될 데이터입니다.  
  // 'PUT', 'POST', 'PATCH', 'DELETE' 메소드에서만 적용 가능합니다.
  // `transformRequest`가 설정되지 않은 경우 다음 타입 중 하나여야 합니다.
  // - string, plain object, ArrayBuffer, ArrayBufferView, URLSearchParams
  // - 브라우저 전용: FormData, File, Blob
  // - Node 전용: Stream, Buffer
/*  
  data: {
    firstName: 'Fred'
  },
*/

// Request interceptor for adding JWT token to requests
api.interceptors.request.use(
  (config) => {
     // If this is a reissue request, skip the interceptor
     /**
      * If this code is not included, the Auth is included in the header when making a request to the /reissue api. 
      * If not, the backend responds with a 401 error before going through the /reissue api.
      * Backend /reissue api is set to pass without auth, but if Auth is included in the header, it seems to verify it.
      * */ 
     if (config.url === '/management/reissue') {
      return config;
    }

    const token = getCookie('AccessToken')
    if (token) {
      config.headers["X-AUTH-TOKEN"] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답코드에 따른 처리를 위한 Response interceptor
api.interceptors.response.use(function (response) {
   // If this is a reissue response, skip the interceptor
   if (response.config.url === '/management/reissue') {
    return response;
  }

  // 2xx 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
  // 응답 데이터가 있는 작업 수행
  return response;
}, async function (error) {
   // If this is a reissue response, skip the interceptor
   if (error.config && error.config.url === '/management/reissue') {
    return Promise.reject(error);
  }

  // 2xx 외의 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
  // 응답 오류가 있는 작업 수행
  if (error.response.status === 401) {
    try {
      const reissueResponse = await api.post('/management/reissue', {
        accessToken : getCookie('AccessToken'),
        refreshToken : getCookie('RefreshToken'),
    });

      if (reissueResponse.status === 200 && reissueResponse.data.success === true) {
        // If the reissue is successful, update the tokens in the userStore and cookies
        reissueResponseSuccess(reissueResponse);

  
        // If the reissue is successful, update the token of the request to be retried
        if (getCookie('AccessToken')) {
          error.config.headers["X-AUTH-TOKEN"] = getCookie('AccessToken');
        };

        // Retry the original request (It seems that the request is not going through "api.interceptors.request.use" and "api.interceptors.response.use" at this time)
        return axios.request(error.config);
      }
      else {
        // If the reissue fails, redirect to the login page
        handleReissueFailure();
      }
    } catch (reissueError) {
      // If the reissue fails, redirect to the login page
      if (reissueError.response && reissueError.response.status === 401) {
        handleReissueFailure();
      }
    }
  }
  else if (error.response.status === 403) {
    handleForbiddenError();
  }

  return Promise.reject(error);
});

function handleReissueFailure() {
  if (!isAlertShown) {
    isAlertShown = true;

    ElMessageBox.alert("토큰 만료로 인해 재로그인이 필요합니다.", '', {
      confirmButtonText: 'OK',
      callback: async () => {
        responseError();
        await router.push('/login');
        isAlertShown = false;
      },
    });
  }
}

function handleForbiddenError() {
  if (!isAlertShown) {
    isAlertShown = true;

    ElMessageBox.alert("접근 권한이 없습니다. 로그인 페이지로 이동합니다.", '', {
      confirmButtonText: 'OK',
      callback: async () => {
        responseError();
        await router.push('/login');
        isAlertShown = false;
      },
    });
  }
}

function responseError() {
  const userStore = useUserStore();
  userStore.userId = "";
  userStore.accessToken = "";
  userStore.refreshToken = "";
  userStore.role = "";

  removeCookie("userId");
  removeCookie("AccessToken");
  removeCookie("RefreshToken");
}

function reissueResponseSuccess(reissueResponse) {
  const userStore = useUserStore();
  userStore.accessToken = reissueResponse.data.data.accessToken;
  userStore.refreshToken = reissueResponse.data.data.refreshToken;
  setCookie('userId', userStore.userId);
  setCookie('AccessToken', userStore.accessToken);
  setCookie('RefreshToken', userStore.refreshToken);
}

export default api;
