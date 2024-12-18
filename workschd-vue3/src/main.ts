import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { Quasar } from 'quasar'
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/src/css/index.sass'
import { createI18n } from 'vue-i18n';
import axios from 'axios';

const messages = {};
const i18n = createI18n({
    locale: 'en', // 기본 언어
    messages,
});
axios.get('/api/translations').then(response => {
    const translations = response.data;

    Object.keys(translations).forEach(key => {
        Object.keys(translations[key]).forEach(lang => {
            if (!messages[lang]) {
                messages[lang] = {};
            }
            messages[lang][key] = translations[key][lang];
        });
    });
    i18n.global.setLocaleMessage(i18n.global.locale, messages[i18n.global.locale]);
});

const app = createApp(App)
app.use(i18n);
app.use(createPinia())
app.use(router)

app.use(Quasar, {
    plugins: {}, // import Quasar plugins and add here
})

declare global {
  interface Window {
    Kakao: any;
  }
}

// window.Kakao.init(import.meta.env.VITE_KAKAO_CLIENT_ID);

app.mount('#app')
