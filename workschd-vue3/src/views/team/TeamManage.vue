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
      <GridTeam
          style="width: 100%; height: 100%"
          :columnDefs="columnDefs"
          :rowData="teams"
          @grid-ready="onGridReady"
          class="ag-theme-alpine-dark"
      />
    </div>

    <!-- Registration Dialog -->
    <TeamRegistrationDialog
      v-model="showRegistrationDialog"
      @team-registered="onTeamRegistered"
    />

    <!-- Approval Dialog -->
    <TeamApproveDialog
      v-model="approvalDialog"
      :selected-team="selectedTeam"
      @request-approved="handleRequestApproved"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useQuasar } from 'quasar'
import GridTeam from "@/components/grid/GridTeam.vue";
import TeamApproveDialog from './dialog/TeamApproveDialog.vue'
import TeamRegistrationDialog from './dialog/TeamRegistrationDialog.vue'

// Types
interface Team {
  id: number
  name: string
  location: string
  memberCount: number
  joinRequests: any[]
  createdAt: string
}

interface JoinRequest {
  id: number
  name: string
  location: string
}

// State
const $q = useQuasar()
const role = ref('manager')
const showRegistrationDialog = ref(false)
const approvalDialog = ref(false)
const selectedTeam = ref<Team | null>(null)

const teams = ref<Team[]>([
  {
    id: 1,
    name: "Team A",
    location: "Location A",
    memberCount: 12,
    joinRequests: [
      {
        name: "emp A",
        location: "KR Incheon"
      },
      {
        name: "emp B",
        location: "KR Incheon"
      }
    ],
    createdAt: new Date().toISOString(),
  },
  {
    id: 2,
    name: "Team B",
    location: "Location C",
    memberCount: 8,
    joinRequests: [
      {
        name: "emp A",
        location: "KR Incheon"
      },
      {
        name: "emp B",
        location: "KR Incheon"
      }
    ],
    createdAt: new Date().toISOString(),
  },
])


// Grid Configuration
const columnDefs = ref([
  { headerName: 'Team Name',  field: 'name' },
  { headerName: 'Location',  field: 'location' },
  { headerName: 'Members',  field: 'memberCount' },
  { headerName: 'Pending Requests',  field: 'joinRequests',  valueGetter: (params: any) => params.data.joinRequests.length },
  { headerName: 'Created At',  field: 'createdAt' },
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

// Methods
const onGridReady = () => {
  try {
    // const response = await fetch('/api/teams')
    // teams.value = await response.json()
  } catch (error) {
    $q.notify({ type: 'negative',  message: 'Failed to fetch teams' })
  }
}

const onTeamRegistered = (team: Team) => {
  teams.value.push(team)
}

const handleRequestApproved = ({ teamId, request }: { teamId: number, request: JoinRequest }) => {
  try {
    // Remove request from the list
    const teamIndex = teams.value.findIndex(t => t.id === teamId)
    if (teamIndex !== -1) {
      const requestIndex = teams.value[teamIndex].joinRequests.findIndex(
        r => r.id === request.id
      )
      if (requestIndex !== -1) {
        teams.value[teamIndex].joinRequests.splice(requestIndex, 1)
      }
    }
  } catch (error) {
    $q.notify({ type: 'negative', message: 'Failed to approve request'})
  }
}

// Lifecycle
onMounted(() => {
  // onGridReady()
})
</script>

<style scoped>
.ag-theme-alpine {
  width: 100%;
}
</style>