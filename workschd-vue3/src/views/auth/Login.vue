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
        <img :src="`${app.cdnUrl}/website/button/kakao-signin-korean.png`"
                type="button" width="200" height="40"
              alt="kakaoLogin"
        />
      </a>
    </div>
    <div class="row g-2" justify="center">
      <a type="button" style="width: 200px"
          :href="getSocialLoginUrl('google')">
        <img :src="`${app.cdnUrl}/website/button/google-signin-korean.png`"
                type="button" width="200" height="40"
          alt="googleLogin"/>
      </a>
    </div>
    <div class="row g-2" justify="center">
      <a type="button" style="width: 200px"
          :href="getSocialLoginUrl('naver')">
        <img :src="`${app.cdnUrl}/website/button/naver-signin-korean.png`"
                type="button" width="200" height="40"
              style="border-radius: 5px"
              alt="naverLogin"
        />
      </a>
    </div>

    <div @click="kakaoLogout()">로그아웃</div>
  </div>
</template>
<script>
import apiAccount from "@/api/public-modules/api-account";

export default {
  name: 'Login',
  components: { },
  data: () => ({
  }),
  methods: {
    kakaoLogin() {
      window.Kakao.Auth.login({
        scope: "profile_image, account_email",
        success: this.getKakaoAccount,
      });
    },
    getKakaoAccount() {
      window.Kakao.API.request({
        url: "/v2/user/me",
        success: (res) => {
          const kakao_account = res.kakao_account;
          const ninkname = kakao_account.profile.ninkname;
          const email = kakao_account.email;
          console.log("ninkname", ninkname);
          console.log("email", email);

          //로그인처리구현

          alert("로그인 성공!");
        },
        fail: (error) => {
          console.log(error);
        },
      });
    },
    kakaoLogout() {
      window.Kakao.Auth.logout((res) => {
        console.log(res);
      });
    },


    getSocialLoginUrl(socialType) {
      this.$cookies.keys().forEach(cookie => this.$cookies.remove(cookie));

      return apiAccount.getSocialLoginUrl(socialType, this.redirect)
    }
  },
};
</script>

<script setup>
const callback = (response) => {
  // This callback will be triggered when the user selects or login to
  // his Google account from the popup
  console.log("Handle the response", response)
}
</script>