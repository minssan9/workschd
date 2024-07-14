import './assets/main.css'
import 'primeflex/primeflex.css';

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import PrimeVue from "primevue/config";
import Aura from '@primevue/themes/aura';


const app = createApp(App)

app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      prefix: 'p',
      darkModeSelector: 'dark-mode',
      cssLayer: false
    }
  }
  // unstyled: true
})
app.use(createPinia())
app.use(router)


import vue3GoogleLogin from 'vue3-google-login'
app.use(vue3GoogleLogin, {clientId: 'YOUR_GOOGLE_CLIENT_ID'})
window.Kakao.init("d23378ff6b70109a54449af376d2c66c");

app.mount('#app')
