<template>
  <q-page class="q-pa-md page-container">
    <div class="row q-mb-md justify-between items-center">
      <h5 class="q-my-none">{{ t('team.manage.title', 'Team Management') }}</h5>
      <div>
        <q-btn 
          :label="t('team.manage.registerNewTeam', 'Register New Team')" 
          color="primary" 
          @click="showRegistrationDialog = true"
          class="q-mr-sm"
        />        
      </div>
    </div>

    <!-- Team Grid -->
    <div class="content-section q-pa-md q-mb-md">
      <GridDefault
          style="width: 100%; height: 100%"
          :columnDefs="columnDefs"
          :rowData="teams"
          @grid-ready="onGridReady"
          :gridOptions="gridOptions"
          class="ag-theme-alpine-dark"
      />
    </div>

    <!-- Team Details Section -->
    <div v-if="selectedTeamForDetails" class="content-section q-pa-md q-mt-md">
      <div class="row justify-between items-center q-mb-md">
        <h5 class="q-my-none">{{ t('team.manage.teamDetails', 'Team Details') }}: {{ selectedTeamForDetails.name }}</h5>
        <q-tabs
          v-model="activeTab"
          class="text-primary"
          indicator-color="primary"
          align="left"
        >
          <q-tab name="workplace" :label="t('team.manage.tabs.workplace', 'Workplace')" />
          <q-tab name="schedule" :label="t('team.manage.tabs.schedule', 'Schedule Config')" />
        </q-tabs>
      </div>

      <q-separator />

      <q-tab-panels v-model="activeTab" animated>
        <q-tab-panel name="workplace">
          <TeamWorkPlace :team-id="selectedTeamForDetails.id" />
        </q-tab-panel>
        <q-tab-panel name="schedule">
          <TeamScheduleConfig :team-id="selectedTeamForDetails.id" />
        </q-tab-panel>
      </q-tab-panels>
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
import { useI18n } from 'vue-i18n' 
import TeamApproveDialog from './dialog/TeamApproveDialog.vue'
import TeamRegistrationDialog from './dialog/TeamRegistrationDialog.vue'
import TeamScheduleConfig from './subpage/TeamManageScheduleConfig.vue'
import TeamWorkPlace from './subpage/TeamManageWorkPlace.vue'
import GridDefault from '@/components/grid/GridDefault.vue';
import { useTeamStore } from '@/stores/modules/teamStore';
import { Team, JoinRequest } from '@/interface/team';
import apiTeam from '@/api/modules/api-team'

// State
const $q = useQuasar()
const { t } = useI18n()
const showRegistrationDialog = ref(false)
const approvalDialog = ref(false)
const selectedTeam = ref<Team | null>(null)
const selectedTeamForDetails = ref<Team | null>(null)
const activeTab = ref('schedule')

const teamStore = useTeamStore();

const teams = ref<Team[]>([
  {
    id: 1,
    name: "Team A",
    location: "Location A",
    memberCount: 12,
    createdAt: new Date().toISOString(),
    joinRequests: [new JoinRequest(1, "emp A", "KR Incheon"), new JoinRequest(2, "emp B", "KR Incheon")],
  },
  {
    id: 2,
    name: "Team B",
    location: "Location C",
    memberCount: 8,
    createdAt: new Date().toISOString(),
    joinRequests: [new JoinRequest(1, "emp A", "KR Incheon"), new JoinRequest(2, "emp B", "KR Incheon")],
  },
])


// Grid Configuration
const columnDefs = ref([
  { 
    headerName: t('team.manage.grid.teamName', 'Team Name'), 
    field: 'name',
    cellRenderer: (params: any) => {
      return `<div class="clickable-cell">${params.value}</div>`;
    },
    onCellClicked: (params: any) => {
      selectedTeamForDetails.value = params.data;
      activeTab.value = 'schedule';
    }
  },
  { headerName: t('team.manage.grid.location', 'Location'), field: 'location' },
  { headerName: t('team.manage.grid.members', 'Members'), field: 'memberCount' },
  { headerName: t('team.manage.grid.pendingRequests', 'Pending Requests'), field: 'joinRequests', valueGetter: (params: any) => params.data.joinRequests.length },
  { headerName: t('team.manage.grid.createdAt', 'Created At'), field: 'createdAt' },
  { 
    headerName: t('team.manage.grid.actions', 'Actions'),
    cellRenderer: (params: any) => {
      const hasRequests = params.data.joinRequests.length > 0
      return `
        <button
          class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--rectangle text-primary q-btn--actionable q-focusable q-hoverable"
          ${!hasRequests ? 'disabled' : ''}
        >
          ${t('team.manage.grid.viewRequests', 'View Requests')}
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

const gridOptions = ref({
  onRowClicked: (params: any) => {
    console.log('Row clicked via gridOptions:', params);
    selectedTeamForDetails.value = params.data;
    activeTab.value = 'schedule';
  }
});

// Methods
const onGridReady = () => { 
  apiTeam.getTeams() 
    .then(response => {
      teams.value = response.data;
    })
    .catch(error => {
      $q.notify({ type: 'negative', message: 'Failed to fetch teams' })
    }) 
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
  onGridReady()
})
</script>

<style scoped>
.ag-theme-alpine {
  width: 100%;
}

.clickable-cell {
  cursor: pointer;
  color: var(--q-primary);
  text-decoration: underline;
}
</style>