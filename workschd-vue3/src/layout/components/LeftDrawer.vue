<template>
  <q-drawer 
    v-model="drawerLeft" 
    :width="200" 
    :breakpoint="700" 
    bordered
    @update:model-value="layoutStore.setLeftDrawer"
  >
    <div class="drawer-header">Menus</div>
    <q-scroll-area class="drawer-side">
      <q-list>
        <template v-for="route in filteredRoutes" :key="route.name">
          <!-- Parent route with children -->
          <template v-if="route.children">
            <q-expansion-item
              :label="route.name"
              :default-opened="isRouteExpanded(route)"
            >
              <q-list>
                <q-item
                  v-for="child in filterHiddenRoutes(route.children)"
                  :key="child.name"
                  :to="{ name: child.name }"
                  clickable
                  v-ripple
                >
                  <q-item-section>{{ formatRouteName(child.name) }}</q-item-section>
                </q-item>
              </q-list>
            </q-expansion-item>
          </template>
          
          <!-- Routes without children -->
          <q-item
            v-else
            :to="{ name: route.name }"
            clickable
            v-ripple
          >
            <q-item-section>{{ formatRouteName(route.name) }}</q-item-section>
          </q-item>
        </template>
      </q-list>
    </q-scroll-area>
    <!-- <div class="drawer-footer">Left Drawer Footer</div> -->
  </q-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useLayoutStore } from '@/stores/modules/store_layout'
import { useUserStore } from '@/stores/modules/store_user'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'

const layoutStore = useLayoutStore()
const userStore = useUserStore()
const { drawerLeft } = storeToRefs(layoutStore)
const route = useRoute()
const router = useRouter()

// Filter out hidden routes
const filteredRoutes = computed(() => {
  return filterHiddenRoutes(router.options.routes)
})

// Helper function to check if user has required role
function hasRequiredRole(routeMeta) {
  if (!routeMeta?.roles) return true
  return routeMeta.roles.includes(userStore.role)
}

// Updated helper function to filter hidden and role-based routes
function filterHiddenRoutes(routes) {
  return routes.filter(route => {
    // Filter out hidden routes
    if (route.hidden) return false
    
    // Check parent route roles
    if (!hasRequiredRole(route.meta)) return false
    
    // If route has children, check if at least one child is accessible
    if (route.children) {
      const accessibleChildren = route.children.filter(child => 
        !child.hidden && hasRequiredRole(child.meta)
      )
      return accessibleChildren.length > 0
    }
    
    return true
  })
}

// Check if route should be expanded based on current route
function isRouteExpanded(parentRoute) {
  return route.path.startsWith(parentRoute.path)
}

// Format route name for display (e.g., "TeamWorkPlace" -> "Team Work Place")
function formatRouteName(name: string) {
  return name
    .replace(/([A-Z])/g, ' $1')
    .trim()
    .replace(/^./, str => str.toUpperCase())
}

onMounted(() => {
  console.log(drawerLeft)
})
</script>

<style scoped>
.q-item {
  padding-left: 1rem;
}

.q-expansion-item :deep(.q-list) {
  padding-left: 2rem;
}
</style> 