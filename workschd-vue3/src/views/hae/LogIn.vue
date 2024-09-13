<template>
  <main class="main">
    <section class="login-wrapper">
      <div class="login-title">
        {{ layoutStore.factoryName }} - PHM SYSTEM
      </div>
      <el-form ref="refForm" label-width="0" :model="isPasswordChangeRequired ? passwordChangeForm : form" class="login-form shadow" size="large">
        <template v-if="!isPasswordChangeRequired">
          <el-form-item>
            <el-input v-model="form.userId" placeholder="ID" @keyup.enter.prevent="handleEnter">
              <template #prefix>
                <el-icon size="1.1rem">
                  <User />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" type="password" placeholder="Password" @keyup.enter.prevent="handleEnter">
              <template #prefix>
                <el-icon size="1.1rem">
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="form.rememberMe" class="login-checkbox" @change="handleRememberChange">Remember me</el-checkbox>
          </el-form-item>
          <ElButton type="primary" style="width: 100%;" size="large" @click="login">Log In
          </ElButton>
          <div class="forgot-password" @click="showForgotPasswordDialog">Forgot your password?</div>
        </template>
        <template v-else>
          <el-form-item>
            <el-input v-model="passwordChangeForm.newPassword" type="password" placeholder="New Password">
              <template #prefix>
                <el-icon size="1.1rem">
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="passwordChangeForm.confirmPassword" type="password" placeholder="Confirm New Password">
              <template #prefix>
                <el-icon size="1.1rem">
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-button type="primary" style="width: 100%;" size="large" @click="changePassword">Change Password</el-button>
        </template>
      </el-form>

      <!-- Forgot Password Dialog -->
      <el-dialog v-model="forgotPasswordDialogVisible" title="비밀번호 찾기" width="400px" @close="closeForgotPasswordDialog">
        <el-form>
          <el-form-item>
            <el-input v-model="form.userId" placeholder="아이디를 입력하세요." required>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="email" type="email" placeholder="회원가입 시 등록한 이메일을 입력하세요." required>
            </el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="closeForgotPasswordDialog">취소</el-button>
          <el-button type="primary" @click="confirmFindPassword">확인</el-button>
        </span>
      </el-dialog>

    </section>
  </main>
</template>

<script setup lang="ts">
import { Ref, ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import router from '@/router';
// import { useRoute } from 'vue-router';
import {ElMessageBox, ElNotification} from 'element-plus'
import api from '@/api/axios';
import {useLayoutStore} from "@/stores/layout";

type LoginForm = {
  userId: string,
  password: string,
  rememberMe: boolean,
};

type PasswordChangeForm = {
  newPassword: string,
  confirmPassword: string,
};

const form : Ref<LoginForm> = ref({
  userId: '',
  password: '',
  rememberMe: localStorage.getItem("rememberedUserId") !== null,
})

const passwordChangeForm = ref<PasswordChangeForm>({
  newPassword: '',
  confirmPassword: '',
});

const isPasswordChangeRequired = ref(false);
const email = ref('');
const forgotPasswordDialogVisible = ref(false);
const userStore = useUserStore();
const layoutStore = useLayoutStore();
let factoryName = ref('');



onMounted(() => {
  layoutStore.fetchFactoryName()

  const savedUserId = localStorage.getItem('rememberedUserId');
  if (savedUserId !== null && form.value.rememberMe) {
    form.value.userId = savedUserId;
    form.value.rememberMe = true;
  }
});

function rememberUserId() {
    if (form.value.rememberMe && form.value.userId) {
      localStorage.setItem('rememberedUserId', form.value.userId);
    } else {
      localStorage.removeItem('rememberedUserId');
    }
}

function handleRememberChange() {
    if (!form.value.rememberMe) {
      localStorage.removeItem('rememberedUserId');
    }
}

function login() {
    if (!form.value.userId) {
        ElMessageBox.alert('아이디를 입력해 주세요.');
        return;
    }
    else if (!form.value.password) {
        ElMessageBox.alert('비밀번호를 입력해 주세요.');
        return;
    }

    rememberUserId();

    userStore.login(form.value.userId, form.value.password).then(async (res) => {
      if (res.data.success) {
        if (res.data.data.firstLoginYN === 'Y') {
          isPasswordChangeRequired.value = true;
        } else {
          const redirectTo = router.currentRoute.value.query.redirect;

          if (redirectTo) {
            await router.push(router.currentRoute.value.query.redirect);
          } else {
            await router.push('/');
          }
        }
      } 
      else {
        if (res.data.data.accountLocked === true) {
          ElMessageBox.alert('로그인을 5회 이상 연속 실패하여 계정이 10분간 잠깁니다. 잠시 후 다시 시도해 주세요.');
        }
        else if (res.data.data.failedLoginAttempts != null && res.data.data.failedLoginAttempts >= 2 && res.data.data.failedLoginAttempts < 5) {
          ElMessageBox.alert(`로그인을 ${res.data.data.failedLoginAttempts}회 연속 실패하였습니다. 5회 실패 시 계정이 10분간 잠깁니다.`);
        }
        else {
          ElMessageBox.alert('아이디 또는 비밀번호를 잘못 입력했습니다.');
        }
      }
      return res;
    }).then(res => {
      if (res.data.data.lastLoginTime !== null && res.data.data.logoutTime === null) {
        ElNotification({
          // title: data.status,
          message: "다른 기기에서 접속 중입니다. 기존 로그인은 자동으로 종료됩니다.",
          type: 'info',
          duration: 3000
        })
      }
    }).catch(err => {
      ElMessageBox.alert(err);
    })
}

// Enter key event handler : If MessageBox is not visible, login function is called.
const handleEnter = (event: KeyboardEvent) => {
  const activeModals = document.getElementsByClassName('el-message-box')
  if (activeModals.length === 0) {
    login()
  }
}

const showForgotPasswordDialog = () => {
  forgotPasswordDialogVisible.value = true;
};

const closeForgotPasswordDialog = () => {
  forgotPasswordDialogVisible.value = false;
};

const confirmFindPassword = () => {
  const requestData = {
    userId: form.value.userId,
    email: email.value,
  };

  api.post('/management/resetPassword', requestData)
  .then((response) => {
    if (response.data.success) {
      ElMessageBox.alert("해당 이메일로 임시 비밀번호를 발송했습니다.").then(() => {
        forgotPasswordDialogVisible.value = false;
      });
    } else {
      ElMessageBox.alert("해당 정보와 일치하는 사용자가 없습니다.<br>가입 시 등록한 이메일 주소를 다시 확인해주세요.", {
        dangerouslyUseHTMLString: true,
      });
    }
  })
  .catch((error) => {
    console.error("Error confirming find password:", error);
  });
};

const changePassword = () => {
  if (passwordChangeForm.value.newPassword !== passwordChangeForm.value.confirmPassword) {
    ElMessageBox.alert("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    return;
  }

  if (!validatePassword(passwordChangeForm.value.newPassword)) {
    ElMessageBox.alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.");
    return;
  }

  if (passwordChangeForm.value.newPassword === form.value.password) {
    ElMessageBox.alert("기존 비밀번호와 새 비밀번호가 동일합니다.<br>다른 비밀번호를 입력해 주세요.", {
      dangerouslyUseHTMLString: true,
    });
    return;
  }

  const requestData = {
    userId: form.value.userId,
    currentPassword: form.value.password,
    newPassword: passwordChangeForm.value.newPassword
  };

  api.put('/management/user/password', requestData)
  .then(response => {
    if (response.data.success) {
      ElMessageBox.alert("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해 주세요.").then(() => {
        isPasswordChangeRequired.value = false;
        form.value.password = '';
        passwordChangeForm.value.newPassword = '';
        passwordChangeForm.value.confirmPassword = '';
      });
    } else {
      ElMessageBox.alert("비밀번호 변경에 실패했습니다. 다시 시도해 주세요.");
    }
  }).catch(error => {
    console.error("Error changing password:", error);
    ElMessageBox.alert("서버와의 통신 중 문제가 발생했습니다.");
  });
};

const validatePassword = (password: string): boolean => {
  const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
  return regex.test(password);
};

</script>

<style lang="scss">
.main {
  background-image: url('@/assets/loginbg.jpg');
  background-size: cover;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  & .login-wrapper {
    width: 600px;
    margin-top: -15rem;
    padding: 48px;
    border-radius: .5rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    background-color:rgba(128, 145, 158, 0.61);
    
    & .login-title {
      font-size: 32px;
      font-weight: 700;
      color: white;
      margin-bottom: .5rem;
    }

    & .login-form {
      background-color: var(--white);
      padding: 2rem 1.5rem;
      width: 25rem;
      border-radius: .5rem;

      & .wrapper-remember {
        margin-bottom: 1rem;
      }

      & .login-checkbox {
        color: #002C5F;;
      }

      & .forgot-password {
        color: #d1dde7b6;
        cursor: pointer;
        text-align: center;
        position: relative;
        top: 22px;
      }
    }

    & .dialog-footer {
      display: flex;
      justify-content: flex-end;
      margin-top: 25px;
    }
  }
}
</style>