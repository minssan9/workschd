<template>
  <q-header elevated :class="$q.dark.isActive ? 'bg-secondary' : 'bg-black'">
    <q-toolbar>
      <q-btn flat @click="layoutStore.toggleLeftDrawer()" round dense icon="menu" />
      <q-toolbar-title>Header</q-toolbar-title>
      <q-btn flat @click="layoutStore.toggleRightDrawer()" round dense icon="menu" />
    </q-toolbar>
  </q-header>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar'
import { useLayoutStore } from '@/stores/modules/store_layout'

const $q = useQuasar()
const layoutStore = useLayoutStore()
</script> 