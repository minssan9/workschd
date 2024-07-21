import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

import { Quasar } from 'quasar'
// Import icon libraries
import '@quasar/extras/material-icons/material-icons.css'
// Import Quasar css
import 'quasar/src/css/index.sass'


const app = createApp(App)

app.use(createPinia())
app.use(router)

app.use(Quasar, {
    plugins: {}, // import Quasar plugins and add here
})


import vue3GoogleLogin from 'vue3-google-login'
app.use(vue3GoogleLogin, {clientId: 'YOUR_GOOGLE_CLIENT_ID'})
window.Kakao.init("d23378ff6b70109a54449af376d2c66c");

app.mount('#app')
