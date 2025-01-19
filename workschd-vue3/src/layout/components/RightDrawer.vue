<template>
  <q-drawer 
    side="right" 
    v-model="drawerRight" 
    show-if-above 
    :width="200" 
    :breakpoint="700" 
    bordered
    @update:model-value="layoutStore.setRightDrawer"
  >
    <div class="drawer-header">Right Menu</div>
    <q-scroll-area class="drawer-side">
      <div class="q-pa-sm">
        <h5>{{ $t('greeting') }}</h5>
        <h6>{{ $t('farewell') }}</h6>
        <div class="language-selector">
          <q-btn flat dense @click="changeLanguage('en')">English</q-btn>
          <q-btn flat dense @click="changeLanguage('ko')">Korean</q-btn>
          <q-btn flat dense @click="changeLanguage('fr')">French</q-btn>
          <q-btn flat dense @click="changeLanguage('es')">Spanish</q-btn>
          <q-btn flat dense @click="changeLanguage('ja')">Japanese</q-btn>
        </div>
        <div class="theme-toggle q-mt-md">
          <q-btn flat dense @click="toggleDarkMode">
            <q-icon :name="$q.dark.isActive ? 'light_mode' : 'dark_mode'" />
            {{ $q.dark.isActive ? 'Light Mode' : 'Dark Mode' }}
          </q-btn>
        </div>
      </div>
    </q-scroll-area>
    <div class="drawer-footer">
      <q-btn flat dense :to="{ name: 'login' }">Login</q-btn>
      <q-btn flat dense @click="logout">Logout</q-btn>
    </div>
  </q-drawer>
</template>

<script setup lang="ts">
import { useLayoutStore } from '@/stores/modules/store_layout'
import { storeToRefs } from 'pinia'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'

const layoutStore = useLayoutStore()
const { drawerRight } = storeToRefs(layoutStore)
const $q = useQuasar()
const { locale } = useI18n()

const toggleDarkMode = () => {
  $q.dark.set(!$q.dark.isActive)
}

const changeLanguage = (lang: string) => {
  locale.value = lang
}

// TODO: Implement logout functionality
const logout = () => {
  // Add logout logic here
}
</script> 