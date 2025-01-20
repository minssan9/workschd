<template>
  <q-page class="q-pa-md">
    <h5>Team Registration</h5>
    <q-card class="q-mt-md">
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
    </q-card>

    <!-- Team Members Grid -->
    <q-card class="q-mt-lg">
      <q-card-section>
        <div class="text-h6">Team Members</div>
        <div class="ag-theme-alpine" style="height: 300px; width: 100%;">
          <ag-grid-vue
            :columnDefs="columnDefs"
            :rowData="teamMembers"
            :defaultColDef="defaultColDef"
            @grid-ready="onGridReady"
          />
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useQuasar } from 'quasar'
import { AgGridVue } from 'ag-grid-vue3'

const $q = useQuasar()
const emit = defineEmits(['team-registered'])

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

// AG Grid Configuration
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

const defaultColDef = {
  flex: 1,
  minWidth: 100,
  resizable: true
}

const onGridReady = async () => {
  if (!teamForm.value.name) return
  
  try {
    const response = await fetch(`/api/teams/${teamForm.value.name}/members`)
    if (response.ok) {
      teamMembers.value = await response.json()
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

    const newTeam = await response.json()
    emit('team-registered', newTeam)
    
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

onMounted(() => {
  onGridReady()
})
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}
</style>