<template>
  <q-page padding>
    <div class="row justify-between items-center q-mb-md">
      <h5 class="q-my-none">Job List of Today</h5>
      <q-btn color="primary" label="Add New Job" @click="showAddDialog = true" />
    </div>

    <div class="q-mt-md">
      <GridDefault
        :rowData="rowData"
        :columnDefs="columnDefs"
        :gridOptions="gridOptions"
        @grid-ready="loadGridData"
      />
    </div>

    <!-- Job Details Section -->
    <div v-if="selectedJob" class="job-details q-mt-md">
      <div class="row justify-between items-center q-mb-md">
        <h5 class="q-my-none">{{ t('events.jobDetails', 'Job Details') }}</h5>
        <div>
          <q-btn 
            v-if="isWorker"
            color="primary" 
            :label="t('events.attend', 'Attend')" 
            @click="showAttendanceForm = true"
            class="q-mr-sm"
          /> 
        </div>
      </div>

      <q-separator />

      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.basicInfo', 'Basic Information') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-md">
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.branch', 'Branch') }}</div>
                    <div class="detail-value">{{ selectedJob.branch_id }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.store', 'Store') }}</div>
                    <div class="detail-value">{{ selectedJob.store_id }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.additionalInfo', 'Additional Info') }}</div>
                    <div class="detail-value">{{ selectedJob.additional_info || 'N/A' }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        
        <div class="col-12 col-md-6">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.timeAndWage', 'Time & Wage') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-md">
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.taskDate', 'Task Date') }}</div>
                    <div class="detail-value">{{ formatDate(selectedJob.task_datetime) }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.startTime', 'Start Time') }}</div>
                    <div class="detail-value">{{ selectedJob.start_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.endTime', 'End Time') }}</div>
                    <div class="detail-value">{{ selectedJob.end_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.dailyWage', 'Daily Wage') }}</div>
                    <div class="detail-value">{{ formatCurrency(selectedJob.daily_wage) }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      
      <!-- Attendance Records Section -->
      <div class="q-mt-md">
        <EventAttendance 
          v-if="selectedJob" 
          :job-id="selectedJob.id" 
          :branch-id="selectedJob.branch_id"
          :task-id="selectedJob.id"
          :start-time="selectedJob.start_time"
          :end-time="selectedJob.end_time"
          :daily-wage="selectedJob.daily_wage"
        />
      </div>
    </div>

    <!-- Attendance Form Dialog -->
    <q-dialog v-model="showAttendanceForm">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">{{ t('events.recordAttendance', 'Record Attendance') }}</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="submitAttendance" class="q-gutter-md">
            <q-input
              v-model="attendanceForm.actualStartTime"
              :label="t('attendance.label.actualStartTime', 'Actual Start Time')"
              type="datetime-local"
              required
            />
            <q-input
              v-model="attendanceForm.actualEndTime"
              :label="t('attendance.label.actualEndTime', 'Actual End Time')"
              type="datetime-local"
              required
            />
            <q-input
              v-model.number="attendanceForm.calculatedDailyWage"
              :label="t('attendance.label.calculatedDailyWage', 'Calculated Daily Wage')"
              type="number"
              required
            />
            <div class="row justify-end q-mt-md">
              <q-btn label="Cancel" color="negative" v-close-popup class="q-mr-sm" />
              <q-btn label="Submit" type="submit" color="primary" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Add Job Dialog -->
    <q-dialog v-model="showAddDialog">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Register New Job</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="onSubmit" class="q-gutter-md">
            <q-select
              v-model="newJob.branch_id"
              label="Branch"
              :options="branches"
              option-value="id"
              option-label="name"
              required
            />
            <q-select
              v-model="newJob.store_id"
              label="Store"
              :options="stores"
              option-value="id"
              option-label="name"
              required
            />
            <q-input
              v-model="newJob.additional_info"
              label="Additional Info"
            />
            <q-input
              v-model="newJob.task_datetime"
              label="Task DateTime"
              type="datetime-local"
              required
            />
            <q-input
              v-model="newJob.start_time"
              label="Start Time"
              type="time"
              required
            />
            <q-input
              v-model="newJob.end_time"
              label="End Time"
              type="time"
              required
            />
            <q-input
              v-model.number="newJob.daily_wage"
              label="Daily Wage"
              type="number"
              required
            />
            <div class="row justify-end q-mt-md">
              <q-btn label="Cancel" color="negative" v-close-popup class="q-mr-sm" />
              <q-btn label="Reset" type="reset" color="warning" @click="handleReset" class="q-mr-sm" />
              <q-btn label="Submit" type="submit" color="primary" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Approval Dialog -->
    <q-dialog v-model="approvalDialogVisible">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Approve Join Requests</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-list dense>
            <q-item v-for="request in selectedJobRequests" :key="request.id">
              <q-item-section>
                <q-item-label>{{ request.workerName }}</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn
                  label="Approve"
                  color="positive"
                  dense
                  @click="handleApproveRequest(request)"
                />
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import GridDefault from '@/components/grid/GridDefault.vue'
import EventAttendance from './EventAttendance.vue'
import apiAttendance from '@/api/modules/api-attendance'
import { useUserStore } from '@/stores/modules/store_user'

const $q = useQuasar()
const { t } = useI18n()
const userStore = useUserStore()

// Check if user is a worker
const isWorker = computed(() => userStore.isWorker)

interface Job {
  id: number
  branch_id: number
  store_id: number
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

interface NewJob {
  branch_id: number | null
  store_id: number | null
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

interface JoinRequest {
  id: number
  workerName: string
}

interface AttendanceForm {
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
}

const jobs = ref<Job[]>([
  { 
    id: 1, 
    branch_id: 1, 
    store_id: 1, 
    additional_info: 'Regular shift', 
    task_datetime: '2023-05-15T09:00', 
    start_time: '09:00', 
    end_time: '17:00',
    daily_wage: 120
  },
  { 
    id: 2, 
    branch_id: 2, 
    store_id: 2, 
    additional_info: 'Inventory check', 
    task_datetime: '2023-05-16T10:00', 
    start_time: '10:00', 
    end_time: '18:00',
    daily_wage: 130
  },
])

const newJob = ref<NewJob>({
  branch_id: null,
  store_id: null,
  additional_info: '',
  task_datetime: '',
  start_time: '',
  end_time: '',
  daily_wage: 0
})

const attendanceForm = ref<AttendanceForm>({
  actualStartTime: '',
  actualEndTime: '',
  calculatedDailyWage: 0
})

const branches = ref([
  { id: 1, name: 'Branch 1' },
  { id: 2, name: 'Branch 2' }
])

const stores = ref([
  { id: 1, name: 'Store 1' },
  { id: 2, name: 'Store 2' }
])

const rowData = ref([])
const columnDefs = ref([
  { 
    field: 'branch_id', 
    headerName: 'Branch',
    cellRenderer: (params) => {
      return `<div class="clickable-cell">${params.value}</div>`;
    },
    onCellClicked: (params) => {
      selectedJob.value = params.data;
      activeTab.value = 'details';
      
      // Pre-fill attendance form
      if (selectedJob.value) {
        const now = new Date().toISOString().slice(0, 16);
        attendanceForm.value = {
          actualStartTime: now,
          actualEndTime: now,
          calculatedDailyWage: selectedJob.value.daily_wage
        };
      }
    }
  },
  { field: 'store_id', headerName: 'Store' },
  { field: 'additional_info', headerName: 'Additional Info' },
  { field: 'task_datetime', headerName: 'Task DateTime' },
  { field: 'start_time', headerName: 'Start Time' },
  { field: 'end_time', headerName: 'End Time' },
  { field: 'daily_wage', headerName: 'Daily Wage' }
])

const showAddDialog = ref(false)
const showAttendanceForm = ref(false)
const approvalDialogVisible = ref(false)
const selectedJobRequests = ref<JoinRequest[]>([])
const selectedJob = ref<Job | null>(null)
const activeTab = ref('details')

// Grid options with row click handler
const gridOptions = ref({
  onRowClicked: (params) => {
    console.log('Row clicked via gridOptions:', params);
    selectedJob.value = params.data;
    activeTab.value = 'details';
    
    // Pre-fill attendance form with job data
    if (selectedJob.value) {
      const now = new Date().toISOString().slice(0, 16);
      attendanceForm.value = {
        actualStartTime: now,
        actualEndTime: now,
        calculatedDailyWage: selectedJob.value.daily_wage
      };
    }
  },
  // Make rows look clickable
  rowStyle: { cursor: 'pointer' }
})

const handleCellClicked = (params: any) => {
  console.log('Cell clicked:', params)
  // Add your cell click handling logic here
}

const onSubmit = async () => {
  try {
    await fetch('/tasks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newJob.value)
    })

    await loadGridData()
    handleReset()
    showAddDialog.value = false
    
    $q.notify({
      type: 'positive',
      message: 'Job registered successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to register job'
    })
  }
}

const submitAttendance = async () => {
  if (!selectedJob.value) return;
  
  try {
    await apiAttendance.create({
      branchId: selectedJob.value.branch_id,
      taskId: selectedJob.value.id,
      actualStartTime: attendanceForm.value.actualStartTime,
      actualEndTime: attendanceForm.value.actualEndTime,
      calculatedDailyWage: attendanceForm.value.calculatedDailyWage,
      employeeId: 1, // Assuming current user ID or get from store
      attendanceDate: new Date().toISOString().split('T')[0],
      dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
      startTime: selectedJob.value.start_time,
      endTime: selectedJob.value.end_time
    });
    
    showAttendanceForm.value = false;
    activeTab.value = 'attendance';
    
    $q.notify({
      type: 'positive',
      message: t('attendance.notification.success', 'Attendance recorded successfully')
    });
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('attendance.notification.error', 'Failed to record attendance')
    });
  }
};

const handleApproveRequest = async (request: JoinRequest) => {
  try {
    await fetch(`/requests/${request.id}/approve`, {
      method: 'POST'
    })
    
    $q.notify({
      type: 'positive',
      message: 'Request approved successfully'
    })
    
    selectedJobRequests.value = selectedJobRequests.value.filter(r => r.id !== request.id)
    
    if (selectedJobRequests.value.length === 0) {
      approvalDialogVisible.value = false
    }
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to approve request'
    })
  }
}

const handleReset = () => {
  newJob.value = {
    branch_id: null,
    store_id: null,
    additional_info: '',
    task_datetime: '',
    start_time: '',
    end_time: '',
    daily_wage: 0
  }
}

const loadGridData = async () => {
  try {
    // For demo purposes, use the jobs array
    rowData.value = jobs.value;
    
    // In a real app, uncomment this:
    /*
    const response = await fetch('/tasks')
    const data = await response.json()
    rowData.value = data
    */
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to fetch tasks'
    })
  }
}

// Helper functions
const formatDate = (dateString: string) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString();
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount);
};

onMounted(() => {
  loadGridData()
})
</script>

<style scoped> 

.job-details {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 16px;
  background-color: #f9f9f9;
  margin-top: 24px;
}

.detail-card {
  height: 100%;
  transition: all 0.3s ease;
}

.detail-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.detail-item {
  margin-bottom: 12px;
}

.detail-label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 4px;
}

.detail-value {
  font-size: 1rem;
  font-weight: 500;
}

.clickable-cell {
  cursor: pointer;
  color: var(--q-primary);
  text-decoration: underline;
}
</style>


