<template>
  <q-drawer 
    v-model="drawerLeft" 
    :width="200" 
    :breakpoint="700" 
    bordered
    @update:model-value="layoutStore.setLeftDrawer"
  >
    <div class="drawer-header">Left Menu</div>
    <q-scroll-area class="drawer-side">
      <q-list>
        <q-item v-for="route in routes" :key="route.name" :to="{ name: route.name }" clickable v-ripple>
          <q-item-section>{{ route.name }}</q-item-section>
        </q-item>
      </q-list>
    </q-scroll-area>
    <div class="drawer-footer">Left Drawer Footer</div>
  </q-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useLayoutStore } from '@/stores/modules/store_layout'
import { storeToRefs } from 'pinia'
import router from "@/router/index.js"

const layoutStore = useLayoutStore()
const { drawerLeft } = storeToRefs(layoutStore)
const routes = router.options.routes.filter(route => !route.hidden)


onMounted(() => {
  console.log(drawerLeft)
})
</script> 