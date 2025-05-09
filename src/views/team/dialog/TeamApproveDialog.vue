<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" maximized>
    <q-card style="width: 800px; max-width: 90vw">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ t('team.approve.title', 'Approve Join Requests') }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <q-card-section v-if="selectedTeam">
        <div class="text-subtitle1 q-mb-md">
          {{ t('team.approve.subtitle', 'Team: {teamName}', { teamName: selectedTeam.name }) }}
        </div>

        <div class="row q-col-gutter-md">
          <div class="col-12">
            <q-table
              :rows="selectedTeam.joinRequests"
              :columns="columns"
              row-key="id"
              :pagination="{ rowsPerPage: 5 }"
            >
              <template v-slot:body="props">
                <q-tr :props="props">
                  <q-td key="employeeName" :props="props">
                    {{ props.row.employeeName }}
                  </q-td>
                  <q-td key="location" :props="props">
                    {{ props.row.location }}
                  </q-td>
                  <q-td key="actions" :props="props">
                    <div class="row q-gutter-sm justify-center">
                      <q-btn
                        size="sm"
                        color="positive"
                        icon="check"
                        :label="t('team.approve.approve', 'Approve')"
                        @click="approveRequest(props.row)"
                      />
                      <q-btn
                        size="sm"
                        color="negative"
                        icon="close"
                        :label="t('team.approve.reject', 'Reject')"
                        @click="rejectRequest(props.row)"
                      />
                    </div>
                  </q-td>
                </q-tr>
              </template>
            </q-table>
          </div>
        </div>

        <div class="row q-col-gutter-md q-mt-md">
          <div class="col-12 col-md-6">
            <q-card bordered>
              <q-card-section>
                <div class="text-subtitle2">{{ t('team.approve.teamInfo', 'Team Information') }}</div>
                <q-list dense>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.location', 'Location') }}</q-item-label>
                      <q-item-label>{{ selectedTeam.location }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.memberCount', 'Current Members') }}</q-item-label>
                      <q-item-label>{{ selectedTeam.memberCount }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.createdAt', 'Created At') }}</q-item-label>
                      <q-item-label>{{ new Date(selectedTeam.createdAt).toLocaleDateString() }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-card-section>
            </q-card>
          </div>
          
          <div class="col-12 col-md-6">
            <q-card bordered>
              <q-card-section>
                <div class="text-subtitle2">{{ t('team.approve.requestStats', 'Request Statistics') }}</div>
                <q-list dense>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.pendingRequests', 'Pending Requests') }}</q-item-label>
                      <q-item-label>{{ selectedTeam.joinRequests.length }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.approvedToday', 'Approved Today') }}</q-item-label>
                      <q-item-label>{{ approvedToday }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('team.approve.rejectedToday', 'Rejected Today') }}</q-item-label>
                      <q-item-label>{{ rejectedToday }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-card-section>
            </q-card>
          </div>
        </div>
      </q-card-section>

      <q-card-section v-else>
        <div class="text-center q-pa-md">
          {{ t('team.approve.noTeamSelected', 'No team selected') }}
        </div>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn :label="t('common.close', 'Close')" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'
import { Team, JoinRequest } from '@/interface/team'

const { t } = useI18n()
const $q = useQuasar()

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  selectedTeam: {
    type: Object as () => Team | null,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'request-approved', 'request-rejected'])

// Computed property for dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Mock statistics
const approvedToday = ref(3)
const rejectedToday = ref(1)

// Table columns
const columns = [
  {
    name: 'employeeName',
    required: true,
    label: t('team.approve.columns.employeeName', 'Employee Name'),
    align: 'left',
    field: 'employeeName',
    sortable: true
  },
  {
    name: 'location',
    required: true,
    label: t('team.approve.columns.location', 'Location'),
    align: 'left',
    field: 'location',
    sortable: true
  },
  {
    name: 'actions',
    required: true,
    label: t('team.approve.columns.actions', 'Actions'),
    align: 'center',
    field: 'actions',
    sortable: false
  }
]

// Methods
const approveRequest = (request: JoinRequest) => {
  try {
    // Simulate API call
    emit('request-approved', { 
      teamId: props.selectedTeam?.id, 
      request 
    })
    
    approvedToday.value++
    
    $q.notify({
      type: 'positive',
      message: t('team.approve.approveSuccess', 'Request approved successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.approve.approveError', 'Failed to approve request')
    })
  }
}

const rejectRequest = (request: JoinRequest) => {
  try {
    // Simulate API call
    emit('request-rejected', { 
      teamId: props.selectedTeam?.id, 
      request 
    })
    
    rejectedToday.value++
    
    $q.notify({
      type: 'positive',
      message: t('team.approve.rejectSuccess', 'Request rejected successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.approve.rejectError', 'Failed to reject request')
    })
  }
}

const onHide = () => {
  // Reset any form state if needed
}
</script>

<style scoped>
/* Add any custom styles here */
</style> 