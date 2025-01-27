<template>
  <q-page class="column items-center justify-center q-pa-md">
    <!-- Social Login Container -->
    <div class="login-container q-pa-lg">
      <!-- Kakao Direct Login -->
      <div class="row justify-center q-mb-lg">
        <q-btn
          class="social-login-btn"
          flat
          @click="kakaoLogin()"
        >
          <img
            src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg"
            width="300"
            height="45"
            :alt="t('login.kakao.button.alt', '카카오 로그인 버튼')"
          />
        </q-btn>
      </div>

      <!-- Social Login Buttons -->
      <div class="column items-center q-gutter-y-md">
        <!-- Kakao -->
        <q-btn
          class="social-login-btn"
          flat
          :href="getSocialLoginUrl('kakao')"
        >
          <img 
            :src="`${VITE_CDN_URL}/website/button/kakao-signin-korean.png`"
            width="300"
            height="45"
            :alt="t('login.kakao.signin.alt', 'kakaoLogin')"
          />
        </q-btn>

        <!-- Google -->
        <q-btn
          class="social-login-btn"
          flat
          :href="getSocialLoginUrl('google')"
        >
          <img 
            :src="`${VITE_CDN_URL}/website/button/google-signin-korean.png`"
            width="300"
            height="45"
            :alt="t('login.google.signin.alt', 'googleLogin')"
          />
        </q-btn>

        <!-- Naver -->
        <q-btn
          class="social-login-btn"
          flat
          :href="getSocialLoginUrl('naver')"
        >
          <img 
            :src="`${VITE_CDN_URL}/website/button/naver-signin-korean.png`"
            width="300"
            height="45"
            style="border-radius: 8px"
            :alt="t('login.naver.signin.alt', 'naverLogin')"
          />
        </q-btn>
      </div>

      <!-- Divider -->
      <q-separator class="q-my-lg" />

      <!-- Email Login Form -->
      <div class="email-login-form q-pa-md">
        <h5 class="text-center q-mb-md">{{ t('login.form.title', '이메일로 로그인') }}</h5>
        <q-form @submit="handleLogin" class="q-gutter-md">
          <q-input
            v-model="loginForm.username"
            :label="t('login.placeholder.username', '아이디를 입력하세요')"
            outlined
            class="login-input"
            :rules="[val => !!val || t('login.validation.required', '필수 입력 항목입니다')]"
          >
            <template v-slot:prepend>
              <q-icon name="person" />
            </template>
          </q-input>

          <q-input
            v-model="loginForm.password"
            :label="t('login.placeholder.password', '비밀번호를 입력하세요')"
            outlined
            type="password"
            class="login-input"
            :rules="[val => !!val || t('login.validation.required', '필수 입력 항목입니다')]"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>

          <div class="row justify-between q-mt-md">
            <q-checkbox
              v-model="rememberMe"
              :label="t('login.form.rememberMe', '로그인 상태 유지')"
            />
            <q-btn
              flat
              color="primary"
              :label="t('login.form.forgotPassword', '비밀번호 찾기')"
              class="q-px-sm"
            />
          </div>

          <q-btn
            type="submit"
            color="primary"
            :label="t('login.button.submit', '로그인')"
            class="full-width q-py-sm q-mt-lg"
            size="lg"
          />
        </q-form>
      </div>
    </div>
  </q-page>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'
import apiAccount from "@/api/modules/api-account"
import { removeAllCookies } from '@/utils/cookieUtils'
import { useRouter } from 'vue-router'

const { t } = useI18n()
const $q = useQuasar()
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

// Add login form state
const loginForm = ref({
  username: '',
  password: ''
})
const rememberMe = ref(false)

// Add router
const router = useRouter()

// Update login handler
const handleLogin = async () => {
  try {
    const response = await apiAccount.login(loginForm.value)
    if (response.data && response.data.token) {
      // Redirect to the redirect page with token
      router.push({
        path: '/redirect',
        query: { token: response.data.token }
      })
    } else {
      alert('로그인 실패: 토큰이 없습니다.')
    }
  } catch (error) {
    console.error('Login error:', error)
    if (error.response?.status === 401) {
      alert('아이디 또는 비밀번호가 올바르지 않습니다.')
    } else {
      alert('로그인 중 오류가 발생했습니다.')
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  max-width: 450px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.social-login-btn {
  width: 300px;
  height: 45px;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-2px);
  }

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.email-login-form {
  .login-input {
    font-size: 1.1em;
  }

  :deep(.q-field__control) {
    height: 56px;
  }

  :deep(.q-field__marginal) {
    height: 56px;
  }
}

// Optional: Add a subtle background pattern
.q-page {
  background-color: #f5f5f5;
  background-image: radial-gradient(#e0e0e0 1px, transparent 1px);
  background-size: 20px 20px;
}
</style>