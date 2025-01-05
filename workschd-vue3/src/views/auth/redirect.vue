<template>
  <div></div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/modules/store_user'
import Cookies from 'js-cookie'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const token = route.query.token

const redirect = async () => {
  await userStore.fetchUser()
  if (token) {
    userStore.login(token)
    try {
      const redirectPath = Cookies.get('redirect') || null
      
      if (redirectPath) {
        router.replace(redirectPath)
      } else {
        if (userStore.isManager) {
          router.replace('/')
        } else {
          router.replace('/')
        }
      }
    } catch (error) {
      console.error('Error during redirect:', error)
      router.replace('/login')
    }
  }
}

onMounted(() => {
  redirect()
})
</script>
