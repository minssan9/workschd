<template>
  <q-page padding>
    <div class="row justify-center">
      <div class="col-12 col-md-6">
        <q-card class="join-team-card">
          <q-card-section>
            <div class="text-h6">Join Team</div>
            <div v-if="loading" class="text-center q-pa-md">
              <q-spinner color="primary" size="3em" />
              <div class="q-mt-sm">Verifying invitation...</div>
            </div>
            
            <div v-else-if="error" class="text-center q-pa-md">
              <q-icon name="error" color="negative" size="3em" />
              <div class="text-negative q-mt-sm">{{ error }}</div>
            </div>
            
            <div v-else-if="teamInfo" class="q-pa-md">
              <div class="text-h6">{{ teamInfo.name }}</div>
              <div class="text-subtitle2">{{ teamInfo.location }}</div>
              
              <q-list bordered class="rounded-borders q-mt-md">
                <q-item>
                  <q-item-section>
                    <q-item-label>Team Manager</q-item-label>
                    <q-item-label caption>{{ teamInfo.managerName }}</q-item-label>
                  </q-item-section>
                </q-item>
                
                <q-item>
                  <q-item-section>
                    <q-item-label>Member Count</q-item-label>
                    <q-item-label caption>{{ teamInfo.memberCount }} members</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>

              <div class="text-center q-mt-lg">
                <q-btn
                  label="Join Team"
                  color="primary"
                  :loading="joining"
                  @click="joinTeam"
                />
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useQuasar } from 'quasar'
import { useUserStore } from '@/stores/modules/store_user'

const route = useRoute()
const router = useRouter()
const $q = useQuasar()
const userStore = useUserStore()

interface TeamInfo {
  id: number
  name: string
  location: string
  managerName: string
  memberCount: number
}

const loading = ref(true)
const joining = ref(false)
const error = ref('')
const teamInfo = ref<TeamInfo | null>(null)

const fetchTeamInfo = async () => {
  const token = route.params.token as string
  
  try {
    const response = await fetch(`/api/teams/invite/${token}`)
    if (!response.ok) {
      throw new Error('Invalid or expired invitation link')
    }
    
    teamInfo.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Failed to fetch team information'
  } finally {
    loading.value = false
  }
}

const joinTeam = async () => {
  if (!teamInfo.value || !userStore.user.accountId) {
    return
  }

  joining.value = true
  const token = route.params.token as string

  try {
    const response = await fetch(`/api/teams/${teamInfo.value.id}/join`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        accountId: userStore.user.accountId,
        inviteToken: token
      })
    })

    if (!response.ok) {
      throw new Error('Failed to join team')
    }

    $q.notify({
      type: 'positive',
      message: 'Successfully joined the team!'
    })

    // Redirect to team page or dashboard
    router.push({ name: 'TeamManage' })
  } catch (err) {
    $q.notify({
      type: 'negative',
      message: err instanceof Error ? err.message : 'Failed to join team'
    })
  } finally {
    joining.value = false
  }
}

onMounted(() => {
  if (!userStore.user.accountId) {
    // Save the current URL to redirect back after login
    const returnUrl = window.location.pathname
    router.push({ 
      name: 'login', 
      query: { redirect: returnUrl }
    })
    return
  }

  fetchTeamInfo()
})
</script>

<style scoped>
.join-team-card {
  margin-top: 2rem;
}
</style> 