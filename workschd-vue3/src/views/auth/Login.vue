<template>
  <div class="col">
    <div class="row">
      <a id="custom-login-btn" @click="kakaoLogin()">
        <img
            src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg"
            width="190"
            height="40"
            alt="카카오 로그인 버튼"
        />
      </a>
    </div>

    <div class="row g-2" justify="center">
      <a type="button" style="width: 200px;"
          :href="getSocialLoginUrl('kakao')">
        <img :src="`${VITE_CDN_URL}/website/button/kakao-signin-korean.png`"
                type="button" width="200" height="40"
              alt="kakaoLogin"
        />
      </a>
    </div>
    <div class="row g-2" justify="center">
      <a type="button" style="width: 200px"
          :href="getSocialLoginUrl('google')">
        <img :src="`${VITE_CDN_URL}/website/button/google-signin-korean.png`"
                type="button" width="200" height="40"
          alt="googleLogin"/>
      </a>
    </div>
    <div class="row g-2" justify="center">
      <a type="button" style="width: 200px"
          :href="getSocialLoginUrl('naver')">
        <img :src="`${VITE_CDN_URL}/website/button/naver-signin-korean.png`"
                type="button" width="200" height="40"
              style="border-radius: 5px"
              alt="naverLogin"
        />
      </a>
    </div>

    <div @click="kakaoLogout()">로그아웃</div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import apiAccount from "@/api/modules/api-account"
import { removeAllCookies } from '@/utils/cookieUtils'

const VITE_CDN_URL = import.meta.env.VITE_CDN_URL

const kakaoLogin = () => {
  window.Kakao.Auth.login({
    scope: "profile_image, account_email",
    success: getKakaoAccount,
  })
}

const getKakaoAccount = () => {
  window.Kakao.API.request({
    url: "/v2/user/me",
    success: (res) => {
      const kakao_account = res.kakao_account
      const ninkname = kakao_account.profile.ninkname
      const email = kakao_account.email
      console.log("ninkname", ninkname)
      console.log("email", email)

      //로그인처리구현

      alert("로그인 성공!")
    },
    fail: (error) => {
      console.log(error)
    },
  })
}

const kakaoLogout = () => {
  window.Kakao.Auth.logout((res) => {
    console.log(res)
  })
}

const getSocialLoginUrl = (socialType) => {
  removeAllCookies()
  return apiAccount.getSocialLoginUrl(socialType, redirect.value)
}

const redirect = ref(null) // Add this if you need the redirect property

// Google callback
const callback = (response) => {
  console.log("Handle the response", response)
}
</script>