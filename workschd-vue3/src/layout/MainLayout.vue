<template>
  <div>
    <div class="background-pattern"></div>
    <div>
      <ul>
        <li v-for="(notification, index) in notifications" :key="index">
          {{ notification }}
        </li>
      </ul>
    </div>
    <q-layout view="hHh LpR fFf" container
              style="height: 100vh"
              class="shadow-2 rounded-borders">
      <q-header elevated :class="$q.dark.isActive ? 'bg-secondary' : 'bg-black'">
        <q-toolbar>
          <q-btn flat @click="layoutStore.toggleLeftDrawer()" round dense icon="menu" />
          <q-toolbar-title>Header</q-toolbar-title>
          <q-btn flat @click="layoutStore.toggleRightDrawer()" round dense icon="menu" />
        </q-toolbar>
      </q-header>
      
      <q-drawer 
        v-model="drawerLeft" 
        show-if-above 
        :width="200" 
        :breakpoint="700" 
        bordered
        :model-value="drawerLeft"
        @update:model-value="layoutStore.setLeftDrawer"
      >
        <div class="drawer-header">Left Menu</div>
        <q-scroll-area class="fit">
          <q-list>
            <q-item v-for="route in routes" :key="route.name" :to="{ name: route.name }" clickable v-ripple>
              <q-item-section>{{ route.name }}</q-item-section>
            </q-item>
          </q-list>
        </q-scroll-area>
        <div class="drawer-footer">Left Drawer Footer</div>
      </q-drawer>

      <q-drawer 
        side="right" 
        v-model="drawerRight" 
        show-if-above 
        :width="200" 
        :breakpoint="700" 
        bordered
        :model-value="drawerRight"
        @update:model-value="layoutStore.setRightDrawer"
      >
        <div class="drawer-header">Right Menu</div>
        <q-scroll-area style="height: calc(100% - 150px); margin-top: 40px; border-right: 1px solid #ddd">
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
          </div>
        </q-scroll-area>
        <div class="drawer-footer">
          <q-btn flat dense :to="{ name: 'login' }">Login</q-btn>
          <q-btn flat dense @click="logout">Logout</q-btn>
        </div>
      </q-drawer>

      <q-page-container>
        <q-page padding>
          <slot></slot>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n';
import { useQuasar } from 'quasar'
import router from "@/router/index.js"
import { useLayoutStore } from '@/stores/modules/store_layout'
import { useCounterStore } from "@/stores/modules/counter.js"
import { storeToRefs } from 'pinia'
import * as ChannelService from '@channel.io/channel-web-sdk-loader'

// Store setup
const layoutStore = useLayoutStore()
const counterStore = useCounterStore()
const { drawerLeft, drawerRight } = storeToRefs(layoutStore)

// Composables setup
const $q = useQuasar()
const { t } = useI18n();
const { locale } = useI18n()

// State
const notifications = ref([])
const routes = router.options.routes.filter(route => route.name !== 'login')

// Methods
const toggleDarkMode = () => {
  alert(`current mode : ${$q.dark.isActive} ->  ${!$q.dark.isActive}`)
  $q.dark.set(!$q.dark.isActive)
}

const changeLanguage = (lang: string) => {
  locale.value = lang
}

// Lifecycle hooks
onMounted(() => {
  layoutStore.resetDrawers()
  ChannelService.loadScript()
  ChannelService.boot({
    "pluginKey": import.meta.env.VITE_CHANNEL_TALK_PLUGIN_KEY
  })
})
</script>

<style lang="scss">
@import "@/scss/layout.scss";
</style>
