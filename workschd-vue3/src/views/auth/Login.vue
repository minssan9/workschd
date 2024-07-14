<template>
  <div class="col">
    <div class="row">
      <a id="custom-login-btn" @click="kakaoLogin()">
        <img
            src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg"
            width="222"
            alt="카카오 로그인 버튼"
        />
      </a>
      <div @click="kakaoLogout()">로그아웃</div>
    </div>
    <div class="row" >
      <GoogleLogin :callback="callback" prompt auto-login/>
    </div>
  </div>
</template>
<script>
import {GoogleLogin} from "vue3-google-login";

export default {
  name: 'Login',
  components: { GoogleLogin },
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