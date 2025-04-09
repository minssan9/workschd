<template>
  <q-dialog v-model="isOpen" @hide="resetForm">
    <q-card style="min-width: 800px">
      <q-card-section>
        <div class="text-h6">Team Registration</div>
      </q-card-section>

      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md"> 
          
          <!-- Team Information -->
          <div class="row">
            <div class="col-12 col-md-6">
              <q-input
                v-model="teamForm.name"
                label="Team Name"
                required
                :rules="[val => !!val || 'Team name is required']"
              />
            </div>
            <div class="col-12 col-md-6">
              <q-input
                v-model="teamForm.region"
                label="Region"
                required
                :rules="[val => !!val || 'Region is required']"
              />
            </div>
            <div class="col-12 col-md-6">
              <q-select
                v-model="teamForm.scheduleType"
                :options="scheduleTypes"
                label="Schedule Type"
                required
                :rules="[val => !!val || 'Schedule type is required']"
              />
            </div>
          </div>
          
          <!-- Invite Link Section -->
          <div v-if="inviteLink" class="q-pa-md bg-grey-2 rounded-borders">
            <div class="row items-center q-gutter-sm">
              <div class="col">
                <q-input
                  v-model="inviteLink"
                  readonly
                  dense
                  bg-color="white"
                >
                  <template v-slot:append>
                    <q-btn
                      flat
                      round
                      dense
                      icon="content_copy"
                      @click="copyInviteLink"
                    />
                  </template>
                </q-input>
              </div>
            </div>
          </div>
          
          <div class="row justify-end q-mt-md">
            <q-btn
              v-if="!inviteLink"
              label="Generate Invite Link"
              color="secondary"
              @click="generateInviteLink"
              class="q-mr-sm"
            />
            <q-btn
              type="submit"
              label="Register Team"
              color="primary"
            />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import {defineEmits, defineModel, defineProps, ref} from 'vue'
import {useQuasar} from 'quasar'
import {TeamDTO} from '@/interface/team'
import apiTeam from '@/api/modules/api-team'

// Props and emits
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'team-registered'])
const isOpen = defineModel('modelValue')

// Setup
const $q = useQuasar()

// Form State
const inviteLink = ref('')

// Schedule types
const scheduleTypes = [
  'Weekly',
  'Bi-weekly',
  'Monthly',
  'Custom'
]

// Form data
const teamForm = ref<TeamDTO>({
  name: '',
  region: '',
  scheduleType: 'Weekly',
  preferredPlaces: []
})

// Reset form function
const resetForm = () => {
  teamForm.value = {
    name: '',
    region: '',
    scheduleType: 'Weekly',
    preferredPlaces: []
  }
  inviteLink.value = ''
}

// Methods
const generateInviteLink = async () => {
  if (!teamForm.value.name || !teamForm.value.region) {
    $q.notify({ type: 'warning', message: 'Please enter team name and region first' })
    return
  }
  
  try {
    // Use the API function from api-team.ts
    const response = await apiTeam.generateInviteLink({
      teamName: teamForm.value.name,
      region: teamForm.value.region
    })

    if (response.data && response.data.inviteToken) {
      inviteLink.value = `${window.location.origin}/join-team/${response.data.inviteToken}`
      $q.notify({ type: 'positive', message: 'Invite link generated successfully' })
    } else {
      throw new Error('Invalid response format')
    }
  } catch (error) {
    console.error('Failed to generate invite link:', error)
    
    // Fallback for development/testing
    const mockToken = 'invite-' + Math.random().toString(36).substring(2, 15)
    inviteLink.value = `${window.location.origin}/join-team/${mockToken}`
    $q.notify({ type: 'warning', message: 'Using mock invite link (API error)' })
  }
}

const copyInviteLink = () => {
  navigator.clipboard.writeText(inviteLink.value)
  $q.notify({ type: 'positive', message: 'Invite link copied to clipboard' })
}

const onSubmit = async () => {
  try {
    const response = await apiTeam.registerTeam(teamForm.value)

    // Check response status
    if (response.status >= 200 && response.status < 300) {
      const newTeam = response.data
      emit('team-registered', newTeam)
      
      // Show success notification
      $q.notify({ type: 'positive', message: 'Team registered successfully' })
      
      // Close the dialog - this will trigger resetForm via @hide
      isOpen.value = false
    } else {
      throw new Error(`API error: ${response.status}`)
    }
  } catch (error) {
    console.error('Failed to register team:', error)
    $q.notify({ type: 'negative', message: 'Failed to register team' })
  }
}
</script>

<style scoped>
.q-card {
  max-width: 800px;
  margin: auto;
}
</style> 