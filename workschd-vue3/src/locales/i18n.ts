import { createI18n } from "vue-i18n";
import axios from "axios";
import api from "@/api/axios-voyagerss";
import { getCookie, setCookie, removeCookie } from '@/utils/cookies';

const userLocale = navigator.languages ? navigator.languages[0] : navigator.language || navigator.userLanguage;

export const i18n = createI18n ({
  // locale: layoutStore.langType,
  legacy: false,
  locale: 'en',
  fallbackLocale: userLocale,
  messages: {},
});

const loadedLanguages = [];

export function loadLanaguageAsync(lang) {
  if(!lang) lang = 'en'

  function setI18nLanguage (lang) {
    axios.defaults.headers.common['Accept-Language'] = lang
    document.querySelector('html').setAttribute('lang', lang)
    i18n.global.locale.value = lang
    return lang
  }

  if (loadedLanguages.includes(lang)) {
    if (i18n.locale !== lang) setI18nLanguage(lang)
    return Promise.resolve()
  }

  return api.get('/system/lang', { params: { lang } })
      .then(response => {
        i18n.global.setLocaleMessage(lang, response.data)
        loadedLanguages.push(lang)
        setI18nLanguage(lang)
      })

}
