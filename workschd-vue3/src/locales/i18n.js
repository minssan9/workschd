import Vue from "vue";
import VueI18n from "vue-i18n";
import useLanguageStore from '@/stores/language.js'
import axios from "axios";
import apiSys from "@/api/public-modules/api-sys";

Vue.use(VueI18n);

const languageStore = useLanguageStore()
const userLocale = navigator.languages ? navigator.languages[0] : navigator.language || navigator.userLanguage;

export const i18n = new VueI18n({
  locale: languageStore.langType,

  fallbackLocale: userLocale,
  messages: {},
});

const loadedLanguages = [];

export function loadLanaguageAsync(lang) {
  function setI18nLanguage (lang) {
    axios.defaults.headers.common['Accept-Language'] = lang
    document.querySelector('html').setAttribute('lang', lang)
    return lang
  }

  if (loadedLanguages.includes(lang)) {
    if (i18n.locale !== lang) setI18nLanguage(lang)
    return Promise.resolve()
  }

  return apiSys.getSysI18n(lang)
      .then(response => {
        let msgs = response.ko
        loadedLanguages.push(lang)
        i18n.setLocaleMessage(lang, msgs)
        setI18nLanguage(lang)
      })

  if (i18n.locale === lang) {
    return Promise.resolve(lang);
  }

  if (loadedLanguages.includes(lang)) {
    return Promise.resolve();
  }
}
