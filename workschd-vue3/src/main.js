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


import vue3GoogleLogin from 'vue3-google-login'
app.use(vue3GoogleLogin, {clientId: 'YOUR_GOOGLE_CLIENT_ID'})
window.Kakao.init("d23378ff6b70109a54449af376d2c66c");

app.mount('#app')
