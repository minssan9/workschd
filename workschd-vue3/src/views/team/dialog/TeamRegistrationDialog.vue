<template>
  <q-dialog v-model="isOpen">
    <q-card style="min-width: 800px">
      <q-card-section>
        <div class="text-h6">Team Registration</div>
      </q-card-section>

      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <q-input
            v-model="teamForm.name"
            label="Team Name"
            required
            :rules="[val => !!val || 'Team name is required']"
          />
          <q-input
            v-model="teamForm.location"
            label="Location"
            required
            :rules="[val => !!val || 'Location is required']"
          />
          <q-select
            v-model="teamForm.preferredPlaces"
            label="Preferred Places (1-3)"
            multiple
            :options="placeOptions"
            use-chips
            stack-label
            :rules="[
              val => val.length > 0 || 'Select at least one place',
              val => val.length <= 3 || 'Select no more than 3 places'
            ]"
          />
          
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
              <div class="col-auto">
                <q-btn
                  label="Generate New Link"
                  color="secondary"
                  flat
                  @click="generateInviteLink"
                />
              </div>
            </div>
          </div>

          <div>
            <q-btn label="Register" type="submit" color="primary" class="q-mr-sm" />
            <q-btn
              v-if="!inviteLink"
              label="Generate Invite Link"
              color="secondary"
              @click="generateInviteLink"
            />
          </div>
        </q-form>
      </q-card-section>

      <!-- Team Members Grid -->
      <q-card-section>
        <div class="text-h6">Team Members</div>
        <div class="ag-theme-alpine" style="height: 300px; width: 100%;">
          <GridDefault
            :columnDefs="columnDefs"
            :rowData="teamMembers"
            @grid-ready="onGridReady"
            class="ag-theme-alpine-dark"
          />
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useQuasar } from 'quasar' 
import GridDefault from '@/components/grid/GridDefault.vue'

const $q = useQuasar()
const emit = defineEmits(['team-registered'])
const isOpen = defineModel('modelValue')

interface TeamForm {
  name: string
  location: string
  preferredPlaces: string[]
}

interface TeamMember {
  id: number
  name: string
  email: string
  joinDate: string
  status: string
}

interface Team {
  id: number
  name: string
  location: string
  memberCount: number
  joinRequests: any[]
  createdAt: string
}

const props = defineProps<{
  modelValue: boolean
}>()

// Form State
const teamForm = ref<TeamForm>({
  name: '',
  location: '',
  preferredPlaces: []
})

const placeOptions = [
  'Office A', 'Office B', 'Office C',
  'Remote', 'Hybrid', 'Flexible'
]

const inviteLink = ref('')
const teamMembers = ref<TeamMember[]>([])

// Grid Configuration
const columnDefs = ref([
  { 
    headerName: 'Name', 
    field: 'name',
    sortable: true,
    filter: true 
  },
  { 
    headerName: 'Email', 
    field: 'email',
    sortable: true,
    filter: true 
  },
  { 
    headerName: 'Join Date', 
    field: 'joinDate',
    sortable: true,
    filter: true 
  },
  { 
    headerName: 'Status', 
    field: 'status',
    sortable: true,
    filter: true,
    cellRenderer: (params: any) => {
      const status = params.value
      const color = status === 'Active' ? 'green' : 'orange'
      return `<span style="color: ${color}">${status}</span>`
    }
  }
])

// Methods
const onGridReady = async () => {
  if (!teamForm.value.name) return
  
  try {
    const response = await fetch(`/api/teams/${teamForm.value.name}/members`)
    if (response.ok) {
      const data = await response.json()
      teamMembers.value = data
    }
  } catch (error) {
    console.error('Failed to fetch team members:', error)
  }
}

const generateInviteLink = async () => {
  try {
    const response = await fetch('/api/teams/generate-invite', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        teamName: teamForm.value.name,
        location: teamForm.value.location
      })
    })

    if (!response.ok) {
      throw new Error('Failed to generate invite link')
    }

    const { inviteToken } = await response.json()
    inviteLink.value = `${window.location.origin}/join-team/${inviteToken}`

    $q.notify({
      type: 'positive',
      message: 'Invite link generated successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to generate invite link'
    })
  }
}

const copyInviteLink = () => {
  navigator.clipboard.writeText(inviteLink.value)
  $q.notify({
    type: 'positive',
    message: 'Invite link copied to clipboard'
  })
}

const onSubmit = async () => {
  try {
    const response = await fetch('/api/teams', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...teamForm.value,
        inviteToken: inviteLink.value ? inviteLink.value.split('/').pop() : null
      })
    })

    if (!response.ok) {
      throw new Error('Failed to register team')
    }

    const newTeam = await response.json()
    emit('team-registered', newTeam)
    isOpen.value = false
    
    // Reset form
    teamForm.value = {
      name: '',
      location: '',
      preferredPlaces: []
    }
    inviteLink.value = ''

    $q.notify({
      type: 'positive',
      message: 'Team registered successfully'
    })

    // Refresh member grid
    onGridReady()
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to register team'
    })
  }
}
</script>

<style scoped>
.q-card {
  max-width: 800px;
  margin: auto;
}

.ag-theme-alpine {
  width: 100%;
}
</style> 