import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { Quasar, Notify } from 'quasar'
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/src/css/index.sass'
import { i18n } from "@/locales/i18n";

const app = createApp(App)
app.use(i18n);
app.use(createPinia())
app.use(router)

app.use(Quasar, {
  plugins: { Notify }, // register Quasar plugins
  config: {
    notify: {} // default configuration will be used from vite.config.ts
  }
})

declare global {
  interface Window {
    Kakao: any;
  }
}

// window.Kakao.init(import.meta.env.VITE_KAKAO_CLIENT_ID);

app.mount('#app')
