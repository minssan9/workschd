<template>
  <div>
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
      <q-drawer
          v-model="drawerLeft"
          show-if-above
          :width="200"
          :breakpoint="700"
          elevated
          class="bg-primary text-white"
      >
        <q-scroll-area class="fit">
          <div class="q-pa-sm">
            <div v-for="n in 30" :key="n">Drawer {{ n }} / 50</div>
          </div>
        </q-scroll-area>
      </q-drawer>
      <q-drawer
          side="right"
          v-model="drawerRight"
          show-if-above
          elevated
          :width="200"
          :breakpoint="700"
          :class="$q.dark.isActive ? 'bg-grey-3' : 'bg-grey-9'"
      >
<!--        <q-btn flat @click="toggleDarkMode" round dense icon="menu" />-->

        <div class="q-pa-sm">

          <h1>{{ $t('greeting') }}</h1>
          <h2>{{ $t('farewell') }}</h2>
          <q-btn @click="changeLanguage('en')">English</q-btn>
          <q-btn @click="changeLanguage('ko')">Korean</q-btn>
          <q-btn @click="changeLanguage('fr')">French</q-btn>
          <q-btn @click="changeLanguage('es')">Spanish</q-btn>
          <q-btn @click="changeLanguage('ja')">Japanese</q-btn>
        </div>
        abc
      </q-drawer>

      <q-page-container>
        <q-page padding>
          <slot></slot>
        </q-page>
      </q-page-container>
<!--      <div style="margin-top: 50px">-->
<!--        <slot></slot>-->
<!--      </div>-->
    </q-layout>
<!--    <Menubar :model="items" />-->

<!--    <Toolbar>-->
<!--      <template #start>-->
<!--        <Button icon="pi pi-plus" class="mr-2" severity="secondary" text />-->
<!--        <Button icon="pi pi-print" class="mr-2" severity="secondary" text />-->
<!--        <Button icon="pi pi-upload" severity="secondary" text />-->
<!--      </template>-->


<!--&lt;!&ndash;      <template #center>&ndash;&gt;-->
<!--&lt;!&ndash;        <IconField>&ndash;&gt;-->
<!--&lt;!&ndash;          <InputIcon>&ndash;&gt;-->
<!--&lt;!&ndash;            <i class="pi pi-search" />&ndash;&gt;-->
<!--&lt;!&ndash;          </InputIcon>&ndash;&gt;-->
<!--&lt;!&ndash;          <InputText placeholder="Search" />&ndash;&gt;-->
<!--&lt;!&ndash;        </IconField>&ndash;&gt;-->
<!--&lt;!&ndash;      </template>&ndash;&gt;-->

<!--&lt;!&ndash;      <template #end> <SplitButton label="Save" :model="items"></SplitButton></template>&ndash;&gt;-->
<!--    </Toolbar>-->
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

    return {
      drawerLeft: ref(false),
      drawerRight: ref(false),
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