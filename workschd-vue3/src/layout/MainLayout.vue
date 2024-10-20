<template>
  <div>
    <div class="background-pattern"></div>
    <q-layout view="hHh LpR fFf" container
              style="height: 100vh"
              class="shadow-2 rounded-borders">
      <q-header elevated :class="$q.dark.isActive ? 'bg-secondary' : 'bg-black'">
        <q-toolbar>
          <q-btn flat @click="drawerLeft = !drawerLeft" round dense icon="menu" />
          <q-toolbar-title>Header</q-toolbar-title>
          <q-btn flat @click="drawerRight = !drawerRight" round dense icon="menu" />
        </q-toolbar>
      </q-header>
      
      <q-drawer v-model="drawerLeft" show-if-above :width="200" :breakpoint="700" bordered >
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

      <q-drawer side="right" v-model="drawerRight" show-if-above :width="200" :breakpoint="700" bordered >
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
          <q-btn flat dense @click="logout()">Logout</q-btn>
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

<script setup>
</script>

<script>
import { ref } from 'vue'
import router from "@/router/index.js";
import {useCounterStore} from "@/stores/modules/counter.js";
import * as ChannelService from '@channel.io/channel-web-sdk-loader';


export default {
  setup() {
    ChannelService.loadScript()
    const counterStore = useCounterStore();

    const routes = router.options.routes.filter(route => route.name !== 'login');

    return {
      drawerLeft: ref(false),
      drawerRight: ref(false),
      routes,
      count: counterStore.count,
      increment: counterStore.increment,
      decrement: counterStore.doubleCount
    }
  },
  mounted() {
    ChannelService.boot({
      // "pluginKey": import.meta.env.VITE_CHANNEL_TALK_PLUGIN_KEY, // fill your plugin key
      "pluginKey": import.meta.env.VITE_CHANNEL_TALK_PLUGIN_KEY
    });

    // if (this.user.id) {
    //   ChannelService.boot({
    //     "pluginKey": "YOUR_PLUGIN_KEY", // fill your plugin key
    //     "memberId": "USER_MEMBER_ID", // fill user's member id
    //     "profile": { // fill user's profile
    //       "name": "USER_NAME", // fill user's name
    //       "mobileNumber": "USER_MOBILE_NUMBER", // fill user's mobile number
    //       "landlineNumber": "USER_LANDLINE_NUMBER", // fill user's landline number
    //       "customField1": "VALUE_1", // custom property
    //       "customField2": "VALUE_2" // custom property
    //     }
    //   })
    // }
  },
  methods: {
    toggleDarkMode() {
      alert(`current mode : ${this.$q.dark.isActive} ->  ${!this.$q.dark.isActive}`)
      this.$q.dark.set(!this.$q.dark.isActive);
    },

    changeLanguage(lang) {
      this.$i18n.locale = lang;
    }
  }
}
</script>

<style lang="scss">
@import "@/scss/layout.scss";
</style>
