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
          style="width: 100%; height: 400px"
          :columnDefs="columnDefs"
          :rowData="teams"
          @grid-ready="onGridReady"
          :gridOptions="gridOptions"
          class="ag-theme-alpine-dark"
      />
      
      <!-- Team Pagination -->
      <Pagination
        :total-items="teamsTotalCount"
        :initial-page="1"
        :initial-page-size="10"
        @page-change="onTeamPageChange"
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
          <q-tab name="members" :label="t('team.manage.tabs.members', 'Members')" />
          <q-tab name="workplace" :label="t('team.manage.tabs.workplace', 'Workplace')" />
          <q-tab name="schedule" :label="t('team.manage.tabs.schedule', 'Schedule Config')" />
        </q-tabs>
      </div>

      <q-separator />

      <q-tab-panels v-model="activeTab" animated>
        <q-tab-panel name="members">
          <!-- Team Members Grid -->
          <div class="q-mb-md">
            <div class="text-h6">{{ t('team.manage.teamMembers', 'Team Members') }}</div>
            <div class="ag-theme-alpine" style="height: 400px; width: 100%;">
              <GridDefault
                :columnDefs="memberColumnDefs"
                :rowData="teamMembers"
                @grid-ready="onMemberGridReady"
                class="ag-theme-alpine-dark"
              />
              
              <!-- Members Pagination -->
              <Pagination
                :total-items="membersTotalCount"
                :initial-page="1"
                :initial-page-size="10"
                @page-change="onMemberPageChange"
              />
            </div>
          </div>
        </q-tab-panel>
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
import {onMounted, ref} from 'vue'
import {useQuasar} from 'quasar'
import {useI18n} from 'vue-i18n'
import TeamApproveDialog from './dialog/TeamApproveDialog.vue'
import TeamRegistrationDialog from './dialog/TeamRegistrationDialog.vue'
import TeamScheduleConfig from './subpage/TeamManageScheduleConfig.vue'
import TeamWorkPlace from './subpage/TeamManageWorkPlace.vue'
import GridDefault from '@/components/grid/GridDefault.vue'
import Pagination from '@/components/common/Pagination.vue'
import {TeamDTO} from '@/interface/team'
import apiTeam, {PageRequest, TeamListParams, TeamMemberParams} from '@/api/modules/api-team'

// State
const $q = useQuasar()
const { t } = useI18n()
const showRegistrationDialog = ref(false)
const approvalDialog = ref(false)
const selectedTeam = ref<TeamDTO | null>(null)
const selectedTeamForDetails = ref<TeamDTO | null>(null)
const activeTab = ref('members')
const teamMembers = ref<TeamDTO[]>([])
const teams = ref<TeamDTO[]>([])

// Pagination state
const teamsTotalCount = ref(0)
const membersTotalCount = ref(0)
const teamsPageRequest = ref<PageRequest>({
  page: 0,
  size: 10,
  sort: 'id,desc'
})
const membersPageRequest = ref<PageRequest>({
  page: 0,
  size: 10,
  sort: 'id,desc'
})

// Team Members Grid Configuration
const memberColumnDefs = ref([
  { headerName: 'Name', field: 'name', sortable: true, filter: true },
  { headerName: 'Email', field: 'email', sortable: true, filter: true },
  { headerName: 'Join Date', field: 'joinDate', sortable: true, filter: true },
  { headerName: 'Status', field: 'status', sortable: true, filter: true,
    cellRenderer: (params: any) => {
      const status = params.value
      const color = status === 'Active' ? 'green' : 'orange'
      return `<span style="color: ${color}">${status}</span>`
    }
  }
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
      activeTab.value = 'members';
      // Reset members pagination when selecting a new team
      membersPageRequest.value = {
        page: 0,
        size: 10,
        sort: 'id,desc'
      };
      onMemberGridReady();
    }
  },
  { headerName: t('team.manage.grid.region', 'Region'), field: 'region' },
  { headerName: t('team.manage.grid.scheduleType', 'Schedule Type'), field: 'scheduleType' },
  { headerName: t('team.manage.grid.members', 'Members'), field: 'memberCount' },
  { headerName: t('team.manage.grid.pendingRequests', 'Pending Requests'), field: 'joinRequests', valueGetter: (params: any) => params.data.joinRequests?.length || 0 },
  { headerName: t('team.manage.grid.createdAt', 'Created At'), field: 'createdAt' },
  { headerName: t('team.manage.grid.actions', 'Actions'),
    cellRenderer: (params: any) => {
      const hasRequests = params.data.joinRequests?.length > 0
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
      if (params.data.joinRequests?.length > 0) {
        selectedTeam.value = params.data
        approvalDialog.value = true
      }
    }
  }
])

// Grid options
const gridOptions = ref({
  pagination: false,
  rowSelection: 'single'
})

// Methods
const onGridReady = () => { 
  fetchTeams({
    pageable: teamsPageRequest.value
  });
}

const fetchTeams = (params: TeamListParams) => {
  apiTeam.getTeams(params) 
    .then(response => {
      if (response.data && response.data.content) {
        teams.value = response.data.content;
        teamsTotalCount.value = response.data.totalElements;
      }
    })
    .catch(error => $q.notify({ type: 'negative', message: 'Failed to fetch teams' }));
}

const onTeamPageChange = (pageRequest: PageRequest) => {
  teamsPageRequest.value = pageRequest;
  fetchTeams({
    pageable: pageRequest
  });
}

const onMemberGridReady = () => {
  if (!selectedTeamForDetails.value) return;
  
  fetchTeamMembers({
    pageable: membersPageRequest.value
  });
}

const fetchTeamMembers = (params: TeamMemberParams) => {
  if (!selectedTeamForDetails.value) return;
  
  // Load team members for the selected team
  apiTeam.getTeamMembers(selectedTeamForDetails.value.name, params)
    .then(response => {
      if (response.data && response.data.content && response.data.content.length > 0) {
        teamMembers.value = response.data.content;
        membersTotalCount.value = response.data.totalElements;
      } else {
        // If no members, show sample data
        teamMembers.value = [
          { id: 1, name: 'John Doe', email: 'john@example.com', joinDate: '2023-01-15', status: 'Active' },
          { id: 2, name: 'Jane Smith', email: 'jane@example.com', joinDate: '2023-02-20', status: 'Pending' }
        ];
        membersTotalCount.value = 2;
      }
    })
    .catch(error => {
      console.error('Failed to fetch team members:', error);
      // Show sample data on error
      teamMembers.value = [
        { id: 1, name: 'John Doe', email: 'john@example.com', joinDate: '2023-01-15', status: 'Active' },
        { id: 2, name: 'Jane Smith', email: 'jane@example.com', joinDate: '2023-02-20', status: 'Pending' }
      ];
      membersTotalCount.value = 2;
    });
}

const onMemberPageChange = (pageRequest: PageRequest) => {
  membersPageRequest.value = pageRequest;
  fetchTeamMembers({
    pageable: pageRequest
  });
}

const onTeamRegistered = (team: TeamDTO) => {
  // Refresh the teams list to include the new team
  fetchTeams({
    pageable: teamsPageRequest.value
  });
  
  // Select the newly registered team and show its details
  selectedTeamForDetails.value = team;
  activeTab.value = 'members';
  
  // Reset members pagination
  membersPageRequest.value = {
    page: 0,
    size: 10,
    sort: 'id,desc'
  };
  onMemberGridReady();
}

const handleRequestApproved = ({ teamId, request }: { teamId: number, request: TeamDTO }) => {
  try {
    // Remove request from the list
    const teamIndex = teams.value.findIndex(t => t.id === teamId)
    if (teamIndex !== -1) {
      const requestIndex = teams.value[teamIndex].joinRequests?.findIndex(
        r => r.id === request.id
      ) ?? -1;
      if (requestIndex !== -1 && teams.value[teamIndex].joinRequests) {
        teams.value[teamIndex].joinRequests?.splice(requestIndex, 1)
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