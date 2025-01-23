<template>
  <q-page class="q-pa-md">
    <div class="row q-mb-md justify-between items-center">
      <h5 class="q-my-none">Team Management</h5>
      <div>
        <q-btn 
          label="Register New Team" 
          color="primary" 
          @click="showRegistrationDialog = true"
          class="q-mr-sm"
        />
        <q-btn-toggle
          v-model="role"
          :options="[
            {label: 'Worker', value: 'worker'},
            {label: 'Manager', value: 'manager'}
          ]"
        />
      </div>
    </div>

    <!-- Team Grid -->
    <div class="ag-theme-alpine" style="height: 400px">
      <ag-grid-vue
        :columnDefs="columnDefs"
        :rowData="teams"
        @grid-ready="onGridReady"
        :defaultColDef="defaultColDef"
      />
    </div>

    <!-- Registration Dialog -->
    <q-dialog v-model="showRegistrationDialog">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Team Registration</div>
        </q-card-section>
        <q-card-section>
          <TeamRegistration @team-registered="onTeamRegistered" />
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Approval Dialog -->
    <q-dialog v-model="approvalDialog">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Approve Join Requests</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-list dense>
            <q-item v-for="request in selectedTeam?.joinRequests" :key="request.id">
              <q-item-section>
                <q-item-label>{{ request.name }}</q-item-label>
                <q-item-label caption>{{ request.location }}</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn
                  label="Approve"
                  color="positive"
                  dense
                  @click="approveRequest(request)"
                />
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Close" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import { useQuasar } from 'quasar'
import TeamRegistration from './TeamRegistration.vue'

const $q = useQuasar()
const role = ref('manager')
const showRegistrationDialog = ref(false)
const approvalDialog = ref(false)
const selectedTeam = ref(null)

interface Team {
  id: number
  name: string
  location: string
  memberCount: number
  joinRequests: any[]
  createdAt: string
}

const teams = ref<Team[]>([])

const columnDefs = ref([
  { headerName: 'Team Name',  field: 'name',  sortable: true,  filter: true },
  { headerName: 'Location',  field: 'location',  sortable: true,  filter: true },
  { headerName: 'Members',   field: 'memberCount', sortable: true, filter: true  },
  { headerName: 'Pending Requests', field: 'joinRequests', valueGetter: (params: any) => params.data.joinRequests.length, sortable: true, filter: true },
  { headerName: 'Created At',  field: 'createdAt',  sortable: true,  filter: true },
  { headerName: 'Actions',
    cellRenderer: (params: any) => {
      const hasRequests = params.data.joinRequests.length > 0
      return `
        <button 
          class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--rectangle text-primary q-btn--actionable q-focusable q-hoverable"
          ${!hasRequests ? 'disabled' : ''}
        >
          View Requests
        </button>
      `
    },
    onCellClicked: (params: any) => {
      if (params.data.joinRequests.length > 0) {
        selectedTeam.value = params.data
        approvalDialog.value = true
      }
    }
  }
])

const defaultColDef = {
  flex: 1,
  minWidth: 100,
  resizable: true
}

const onGridReady = async () => {
  try {
    const response = await fetch('/api/teams')
    teams.value = await response.json()
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to fetch teams'
    })
  }
}

const onTeamRegistered = (team: Team) => {
  teams.value.push(team)
  showRegistrationDialog.value = false
  $q.notify({
    type: 'positive',
    message: 'Team registered successfully'
  })
}

const approveRequest = async (request: any) => {
  try {
    await fetch(`/api/teams/${selectedTeam.value.id}/approve`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(request)
    })

    // Remove request from the list
    const teamIndex = teams.value.findIndex(t => t.id === selectedTeam.value.id)
    if (teamIndex !== -1) {
      const requestIndex = teams.value[teamIndex].joinRequests.findIndex(
        r => r.id === request.id
      )
      if (requestIndex !== -1) {
        teams.value[teamIndex].joinRequests.splice(requestIndex, 1)
      }
    }

    $q.notify({
      type: 'positive',
      message: 'Request approved successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to approve request'
    })
  }
}

onMounted(() => {
  onGridReady()
})
</script>

<style scoped>
.ag-theme-alpine {
  width: 100%;
}
</style>